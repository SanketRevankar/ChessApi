package com.sanket.chess.game.vo.Pieces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sanket.chess.game.vo.Box;
import com.sanket.chess.mongodb.game.Game;
import com.sanket.chess.game.vo.Board;
import com.sanket.chess.game.vo.Spot;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class King extends Piece {
    @JsonIgnore
    private boolean castlingDone = false;
    @JsonIgnore
    private boolean moved = false;

    public King(boolean white) {
        super("King", white);
    }

    @Override
    public boolean canMove(Board board, Box start, Box end) {
        int startX = start.getX();
        int endX = end.getX();
        int startY = start.getY();
        int endY = end.getY();

        int x = Math.abs(startX - endX);
        int y = Math.abs(startY - endY);

        if (x <= 1 && y <= 1) {
            moved = true;
            return true;
        }

        return false;
    }

    public boolean isValidCastling(Board board, int x, int y, boolean check) {
        if (!castlingDone && !moved) {
            if (y == 6 || y == 2) {
                int rookPos = y == 6 ? 7 : 0;
                int step = y == 6 ? 1 : -1;
                Piece rook = board.getBox(x, rookPos).getPiece();
                if (rook instanceof Rook && !((Rook) rook).isMoved()) {
                    for (int i = 4 + step; i != rookPos; i += step) {
                        if (board.getBox(x, i).getPiece() != null) {
                            return false;
                        }
                    }
                    if (!check) {
                        board.getBox(x, y == 6 ? 5 : 3).setPiece(rook);
                        board.getBox(x, rookPos).setPiece(null);
                        castlingDone = true;
                        moved = true;
                    }
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void loadPossibleMoves(Game game, int x, int y) {
        super.loadPossibleMoves(game, x, y);
        Board board = game.getBoard();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                addPossibleMove(board, x + i, y + j);
            }
        }
        addPossibleCastlingMove(board, x, 2);
        addPossibleCastlingMove(board, x, 6);
    }

    private void addPossibleCastlingMove(Board board, int x, int y) {
        if (isValidCastling(board, x, y, true)) {
            getPossibleMoves().add(board.getBox(x, y));
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

