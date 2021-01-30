package com.sanket.chess.service.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class Piece {

    private String name;
    private boolean killed = false;
    private boolean white;

    Piece(String name, boolean white) {
        this.name = name;
        this.white = white;
    }

    public boolean canMove(Board board, Spot start, Spot end) {
        return false;
    }
}
