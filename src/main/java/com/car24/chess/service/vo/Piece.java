package com.car24.chess.service.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Piece {

    private String name;
    private boolean killed = false;
    private boolean white;
    private int x;
    private int y;

    public Piece(String name, boolean white, int x, int y) {
        this.name = name;
        this.white = white;
        this.x = x;
        this.y = y;
    }

    public boolean canMove(Board board, Spot start, Spot end) {
        return false;
    }
}
