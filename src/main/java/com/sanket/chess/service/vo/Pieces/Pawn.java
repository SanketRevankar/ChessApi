package com.sanket.chess.service.vo.Pieces;

import com.sanket.chess.mongodb.game.Game;
import com.sanket.chess.service.vo.Board;
import com.sanket.chess.service.vo.Move;
import com.sanket.chess.service.vo.Spot;

import java.util.ArrayList;

public class Pawn extends Piece {

    public Pawn(boolean white) {
        super("Pawn", white);
    }

    @Override
    public void loadPossibleMoves(Game game, int x, int y) {
        super.loadPossibleMoves(game, x, y);
        Board board = game.getBoard();
        int change = isWhite() ? 1 : -1;
        int start = isWhite() ? 1 : 6;
        addPossibleMove(board, x + change, y, false);
        if (x == start) {
            addPossibleMove(board, x + 2 * change, y, false);
        }
        addPossibleMove(board, x + change, y + 1, true);
        addPossibleMove(board, x + change, y - 1, true);
        if (game.getCurrentMoveNumber() > 0) {
            Move move = game.getMovesPlayed().get(game.getCurrentMoveNumber());
            Spot enPassant = move.getEnPassant();
            if (enPassant != null && x == enPassant.getX() && Math.abs(y - enPassant.getY()) == 1) {
                addPossibleMove(board, x + change, enPassant.getY(), false);
            }
        }
    }

    private void addPossibleMove(Board board, int x, int y, boolean enemy) {
        try {
            Spot box = board.getBox(x, y);
            if (enemy) {
                if (box.getPiece() != null && box.getPiece().isWhite() != isWhite()) {
                    getPossibleMoves().add(box);
                }
            } else if (box.getPiece() == null) {
                getPossibleMoves().add(box);
            }
        } catch (IndexOutOfBoundsException ignored) {}
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
