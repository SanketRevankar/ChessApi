package com.sanket.chess.game.vo;

import com.sanket.chess.game.vo.Pieces.Piece;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public
class Spot {
    private Piece piece;
    private int x;
    private int y;

}
