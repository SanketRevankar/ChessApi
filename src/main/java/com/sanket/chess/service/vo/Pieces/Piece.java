package com.sanket.chess.service.vo.Pieces;

import com.sanket.chess.service.vo.Board;
import com.sanket.chess.service.vo.Spot;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public
class Piece {

    private String name;
    private boolean killed = false;
    private boolean white;

    public Piece(String name, boolean white) {
        this.name = name;
        this.white = white;
    }

    public boolean canMove(Board board, Spot start, Spot end) {
        return false;
    }
}
