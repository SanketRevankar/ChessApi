package com.sanket.chess.service.vo.Pieces;

import com.sanket.chess.service.vo.Board;
import com.sanket.chess.service.vo.Spot;

public class Pawn extends Piece {

    public Pawn(boolean white) {
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
        }

        return false;
    }

    public boolean isPassedPawn(Board board, Spot start, Spot end) {
        int startX = start.getX();
        int endX = end.getX();
        int startY = start.getY();
        int endY = end.getY();

        int x = isWhite() ? (endX - startX) : (startX - endX);
        int y = Math.abs(startY - endY);

        if (x == 2 && y == 0 && startX == (isWhite() ? 1 : 6)) {
            return board.getBox(startX + (isWhite() ? 1 : -1), endY).getPiece() == null &&
                    board.getBox(endX, endY).getPiece() == null;
        }

        return false;
    }

    public boolean isEnPassant(int x, int y, Spot enPassant) {
        if (enPassant == null) {
            return false;
        }

        int passantX = enPassant.getX();
        int passantY = enPassant.getY();

        return x == passantX && y == passantY;
    }

}
