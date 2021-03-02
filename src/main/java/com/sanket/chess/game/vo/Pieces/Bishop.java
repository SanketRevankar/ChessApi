package com.sanket.chess.game.vo.Pieces;

import com.sanket.chess.game.vo.Box;
import com.sanket.chess.mongodb.game.Game;
import com.sanket.chess.game.vo.Board;
import com.sanket.chess.game.vo.Spot;

public class Bishop extends Piece {
    public Bishop(boolean white) {
        super("Bishop", white);
    }

    @Override
    public boolean canMove(Board board, Box start, Box end) {
        int startX = start.getX();
        int endX = end.getX();
        int startY = start.getY();
        int endY = end.getY();

        int x = Math.abs(startX - endX);
        int y = Math.abs(startY - endY);

        if (x != y) {
            return false;
        }

        int stepX = endX > startX ? 1 : -1;
        int stepY = endY > startY ? 1 : -1;

        for (int i = startX + stepX, j = startY + stepY; i != endX && j != endY; i += stepX, j += stepY) {
            if (board.getBox(i, j).getPiece() != null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void loadPossibleMoves(Game game, int x, int y) {
        super.loadPossibleMoves(game, x, y);
        Board board = game.getBoard();
        int[] choices = new int[]{-1, 1};
        for (int choice1: choices) {
            for (int choice2: choices) {
                for (int i = 1; i < 8; i++) {
                    if (!addPossibleMove(board, x + choice1 * i, y + choice2 * i)) {
                        break;
                    }
                }
            }
        }
    }

    private boolean addPossibleMove(Board board, int x, int y) {
        try {
            Spot box = board.getBox(x, y);
            if (box.getPiece() == null) {
                getPossibleMoves().add(box);
                return true;
            } else if (box.getPiece().isWhite() != isWhite()) {
                getPossibleMoves().add(box);
            }
        } catch (IndexOutOfBoundsException ignored) {}
        return false;
    }
}
