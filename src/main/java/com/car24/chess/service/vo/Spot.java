package com.car24.chess.service.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Spot {
    private Piece piece;
    private int x;
    private int y;

    public Spot(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
