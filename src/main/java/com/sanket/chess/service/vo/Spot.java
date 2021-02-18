package com.sanket.chess.service.vo;

import com.sanket.chess.service.vo.Pieces.Piece;
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
