package com.sanket.chess.service.vo.Pieces;

import com.sanket.chess.service.vo.Board;
import com.sanket.chess.service.vo.Spot;

public class Knight extends Piece {
    public Knight(boolean white) {
        super("Knight", white);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        return x * y == 2;
    }
}
