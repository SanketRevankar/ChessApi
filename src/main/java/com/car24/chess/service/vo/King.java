package com.car24.chess.service.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class King extends Piece {
    @JsonIgnore private boolean castlingDone = false;
    @JsonIgnore private boolean moved = false;

    public King(boolean white, int x, int y) {
        super("King", white, x, y);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        return x <= 1 && y <= 1;
    }

    // TODO: Castling
    private boolean isValidCastling(Board board, Spot start, Spot end) {
        if (!castlingDone || !moved) {
            if (board.getBox(end.getX(), start.getY()) == null) {
                castlingDone = true;
                return true;
            }
        }
        return false;
    }

}

