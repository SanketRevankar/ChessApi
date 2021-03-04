package com.sanket.chess.game.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Box {
    private int x;
    private int y;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Box) {
            Box b = (Box) obj;
            return this.x == b.x && this.y == b.y;
        }
        return false;
    }
}
