package com.sanket.chess.service.vo.Pieces;

import com.sanket.chess.mongodb.game.Game;
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

    @Override
    public void loadPossibleMoves(Game game, int x, int y) {
        super.loadPossibleMoves(game, x, y);
        Board board = game.getBoard();
        int[][] choices = new int[][]{{1, 2}, {1, -2}, {-1, 2}, {-1, -2}, {-2, 1}, {-2, -1}, {2, 1}, {2, -1}};
        for (int[] choice: choices) {
            addPossibleMove(board, x + choice[0], y + choice[1]);
        }
    }

    private void addPossibleMove(Board board, int x, int y) {
        try {
            Spot box = board.getBox(x, y);
            if (box.getPiece() == null || box.getPiece().isWhite() != isWhite()) {
                getPossibleMoves().add(box);
            }
        } catch (IndexOutOfBoundsException ignored) {}
    }
}
