package com.car24.chess.service.vo;

public class Bishop extends Piece {
    public Bishop(boolean white, int x, int y) {
        super("Bishop", white, x, y);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        int startX = start.getX();
        int endX = end.getX();
        int x = Math.abs(startX - endX);
        int startY = start.getY();
        int endY = end.getY();
        int y = Math.abs(startY - endY);

        if (x != y) {
            return false;
        }

        for (int i = startX + 1, j = startY + 1; i < endX && j < endY;
             i += endX > startX ? 1 : -1, j += endY > startY ? 1 : -1) {
            if (board.getBox(i, j) != null) {
                return false;
            }
        }

        return true;
    }
}
