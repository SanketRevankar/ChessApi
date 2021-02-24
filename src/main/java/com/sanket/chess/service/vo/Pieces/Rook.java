package com.sanket.chess.service.vo.Pieces;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.sanket.chess.mongodb.game.Game;
import com.sanket.chess.service.vo.Board;
import com.sanket.chess.service.vo.Spot;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Rook extends Piece {
    @JsonIgnore
    private boolean moved = false;

    public Rook(boolean white) {
        super("Rook", white);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        int startX = start.getX();
        int endX = end.getX();
        int startY = start.getY();
        int endY = end.getY();

        int x = Math.abs(startX - endX);
        int y = Math.abs(startY - endY);

        if (x != 0 && y != 0) {
            return false;
        }

        int stepX = x == 0 ? 0 : endX > startX ? 1 : -1;
        int stepY = y == 0 ? 0 : endY > startY ? 1 : -1;

        for (int i = startX + stepX, j = startY + stepY; i != endX || j != endY; i += stepX, j += stepY) {
            if (board.getBox(i, j).getPiece() != null) {
                return false;
            }
        }

        moved = true;
        return true;
    }

    @Override
    public void loadPossibleMoves(Game game, int x, int y) {
        super.loadPossibleMoves(game, x, y);
        Board board = game.getBoard();
        int[][] choices = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] choice: choices) {
            for (int k = 1; k < 8; k++) {
                if (!addPossibleMove(board, x + choice[0] * k, y + choice[1] * k)) {
                    break;
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
