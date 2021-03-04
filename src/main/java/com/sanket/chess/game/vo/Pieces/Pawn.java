package com.sanket.chess.game.vo.Pieces;

import com.sanket.chess.game.vo.Box;
import com.sanket.chess.mongodb.game.Game;
import com.sanket.chess.game.vo.Board;
import com.sanket.chess.game.vo.Move;
import com.sanket.chess.game.vo.Spot;

import java.util.ArrayList;

public class Pawn extends Piece {

    public Pawn(boolean white) {
        super("Pawn", white);
    }

    @Override
    public boolean canMove(Board board, Box start, Box end) {
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

    @Override
    public ArrayList<Box> fetchPossibleMoves(Game game, int x, int y) {
        ArrayList<Box> possibleMoves = new ArrayList<>();
        Board board = game.getBoard();
        int change = isWhite() ? 1 : -1;
        addPossibleMove(board, x + change, y, false, possibleMoves);
        if (x == (isWhite() ? 1 : 6) && board.getBox(x + change, y).getPiece() == null) {
            addPossibleMove(board, x + 2 * change, y, false, possibleMoves);
        }
        addPossibleMove(board, x + change, y + 1, true, possibleMoves);
        addPossibleMove(board, x + change, y - 1, true, possibleMoves);
        addPossibleEnPassant(game, x, y, board, change, possibleMoves);
        return possibleMoves;
    }

    private void addPossibleEnPassant(Game game, int x, int y, Board board, int change, ArrayList<Box> possibleMoves) {
        if (game.getCurrentMoveNumber() > 0) {
            Move move = game.getMovesPlayed().get(game.getCurrentMoveNumber() - 1);
            Box enPassant = move.getEnPassant();
            if (enPassant != null && x == enPassant.getX() && Math.abs(y - enPassant.getY()) == 1) {
                addPossibleMove(board, x + change, enPassant.getY(), false, possibleMoves);
            }
        }
    }

    private void addPossibleMove(Board board, int x, int y, boolean enemy, ArrayList<Box> possibleMoves) {
        try {
            Spot box = board.getBox(x, y);
            if (enemy) {
                if (box.getPiece() != null && box.getPiece().isWhite() != isWhite()) {
                    possibleMoves.add(new Box(x, y));
                }
            } else if (box.getPiece() == null) {
                possibleMoves.add(new Box(x, y));
            }
        } catch (IndexOutOfBoundsException ignored) {}
    }

    public boolean isPassedPawn(Board board, Box start, Box end) {
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

    public boolean isEnPassant(int x, int y, Box enPassant) {
        if (enPassant == null) {
            return false;
        }

        int passantX = enPassant.getX();
        int passantY = enPassant.getY();

        return x == passantX && y == passantY;
    }

}
