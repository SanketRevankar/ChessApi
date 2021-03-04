package com.sanket.chess.game.vo.Pieces;

import com.sanket.chess.game.vo.Box;
import com.sanket.chess.mongodb.game.Game;
import com.sanket.chess.game.vo.Board;
import com.sanket.chess.game.vo.Spot;

import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(boolean white) {
        super("Knight", white);
    }

    @Override
    public boolean canMove(Board board, Box start, Box end) {
        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        return x * y == 2;
    }

    @Override
    public ArrayList<Box> fetchPossibleMoves(Game game, int x, int y) {
        ArrayList<Box> possibleMoves = new ArrayList<>();
        Board board = game.getBoard();
        int[][] choices = new int[][]{{1, 2}, {1, -2}, {-1, 2}, {-1, -2}, {-2, 1}, {-2, -1}, {2, 1}, {2, -1}};
        for (int[] choice: choices) {
            addPossibleMove(board, x + choice[0], y + choice[1], possibleMoves);
        }
        return possibleMoves;
    }

    private void addPossibleMove(Board board, int x, int y, ArrayList<Box> possibleMoves) {
        try {
            Spot box = board.getBox(x, y);
            if (box.getPiece() == null || box.getPiece().isWhite() != isWhite()) {
                possibleMoves.add(new Box(x, y));
            }
        } catch (IndexOutOfBoundsException ignored) {}
    }
}
