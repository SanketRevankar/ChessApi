package com.sanket.chess.game.vo.Pieces;

import com.sanket.chess.game.vo.Box;
import com.sanket.chess.mongodb.game.Game;
import com.sanket.chess.game.vo.Board;
import com.sanket.chess.game.vo.Spot;

public class Queen extends Piece {
    public Queen(boolean white) {
        super("Queen", white);
    }

    @Override
    public boolean canMove(Board board, Box start, Box end) {
        int startX = start.getX();
        int endX = end.getX();
        int startY = start.getY();
        int endY = end.getY();

        int x = Math.abs(startX - endX);
        int y = Math.abs(startY - endY);

        if ((x != y) && (x != 0 && y != 0)) {
            return false;
        }

        int stepX = x == 0 ? 0 : endX > startX ? 1 : -1;
        int stepY = y == 0 ? 0 : endY > startY ? 1 : -1;

        for (int i = startX + stepX, j = startY + stepY; i != endX || j != endY; i += stepX, j += stepY) {
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
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = 1; k < 8; k++) {
                    if (!addPossibleMove(board, x + i * k, y + j * k)) {
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
