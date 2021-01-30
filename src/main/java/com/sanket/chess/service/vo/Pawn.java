package com.sanket.chess.service.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pawn extends Piece {

    Pawn(boolean white) {
        super("Pawn", white);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        int startX = start.getX();
        int endX = end.getX();
        int startY = start.getY();
        int endY = end.getY();

        int x = isWhite() ? (endX - startX) : (startX - endX);
        int y = Math.abs(startY - endY);

        if (x == 1) {
            switch (y) {
                case 0: return board.getBox(endX, endY).getPiece() == null;
                case 1: return board.getBox(endX, endY).getPiece() != null;
            }
        } if (x == 2 && y == 0 && startX == (isWhite() ? 1 : 6)) {
            return board.getBox(startX + (isWhite() ? 1 : -1), endY).getPiece() == null &&
                    board.getBox(endX, endY).getPiece() == null;
        }

        return false;
    }

    Spot isEnPassant(Board board, Spot start, Spot end) {
        int startX = start.getX();
        int endX = end.getX();
        int startY = start.getY();
        int endY = end.getY();

        int x = Math.abs(startX - endX);
        int y = Math.abs(startY - endY);

        if (y == 1 && x == 1 && board.getBox(endX, endY).getPiece() == null) {
            Spot piece = board.getBox(startX, endY);
            if (piece.getPiece() != null) {
                return piece;
            }
        }

        return null;
    }
}
