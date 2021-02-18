package com.sanket.chess.service.vo.Pieces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sanket.chess.service.vo.Board;
import com.sanket.chess.service.vo.Spot;
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
    public boolean canMove(Board board, Spot start, Spot end) {
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

    public boolean isValidCastling(Board board, Spot end) {
        int x = end.getX();
        int y = end.getY();

        if (!castlingDone && !moved) {
            if (y == 6 || y == 1) {
                int rookPos = y == 6 ? 7 : 0;
                int step = y == 6 ? 1 : -1;
                Piece rook = board.getBox(x, rookPos).getPiece();
                if (rook instanceof Rook && !((Rook) rook).isMoved()) {
                    for (int i = 4 + step; i != rookPos; i += step) {
                        if (board.getBox(x, i).getPiece() != null) {
                            return false;
                        }
                    }
                    board.getBox(x, y == 6 ? 5 : 2).setPiece(rook);
                    board.getBox(x, rookPos).setPiece(null);
                    castlingDone = true;
                    moved = true;
                    return true;
                }
            }
        }
        return false;
    }


}

