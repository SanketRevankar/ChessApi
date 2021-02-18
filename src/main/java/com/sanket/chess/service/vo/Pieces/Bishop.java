package com.sanket.chess.service.vo.Pieces;

import com.sanket.chess.service.vo.Board;
import com.sanket.chess.service.vo.Spot;

public class Bishop extends Piece {
    public Bishop(boolean white) {
        super("Bishop", white);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
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
}
