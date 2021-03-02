package com.sanket.chess.game.vo;

import com.sanket.chess.game.vo.Pieces.Piece;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public
class Spot extends Box {
    private Piece piece;

    public Spot(Piece piece, int i, int j) {
        super(i, j);
        this.piece = piece;
    }
}
