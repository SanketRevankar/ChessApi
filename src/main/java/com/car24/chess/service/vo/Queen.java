package com.car24.chess.service.vo;

public class Queen extends Piece {
    public Queen(boolean white, int x, int y) {
        super("Queen", white, x, y);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        int startX = start.getX();
        int endX = end.getX();
        int startY = start.getY();
        int endY = end.getY();

        int x = Math.abs(startX - endX);
        int y = Math.abs(startY - endY);

        if (x == 0) {
            for (int i = 1; i <= y; i++) {
                if (board.getBox(startX, startY + (endY > startY ? i : -1 * i)) != null) {
                    return false;
                }
            }
            return true;
        }

        if (y == 0) {
            for (int i = 1; i <= x; i++) {
                if (board.getBox(startX + (endX > startX ? i : -1 * i), startY) != null) {
                    return false;
                }
            }
            return true;
        }

        if (x == y) {
            for (int i = startX + 1, j = startY + 1; i < endX && j < endY;
                 i += endX > startX ? 1 : -1, j += endY > startY ? 1 : -1) {
                if (board.getBox(i, j) != null) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }
}
