package com.car24.chess.service.vo;

public class Knight extends Piece {
    public Knight(boolean white, int x, int y) {
        super("Knight", white, x, y);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        return x * y == 2;
    }
}
