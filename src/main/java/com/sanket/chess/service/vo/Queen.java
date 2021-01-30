package com.sanket.chess.service.vo;

public class Queen extends Piece {
    public Queen(boolean white) {
        super("Queen", white);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
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
}
