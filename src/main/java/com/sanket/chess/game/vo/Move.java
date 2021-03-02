package com.sanket.chess.game.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Move {
    private String gameId;
    private int moveId;
    private Player player;
    private Box start;
    private Box end;
    private String pieceMoved;
    private String pieceKilled;
    private boolean castlingMove;
    private Box enPassant;
    private String pawnPromotion;

}
