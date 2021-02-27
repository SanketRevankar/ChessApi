package com.sanket.chess.game.vo;

import com.sanket.chess.game.vo.Pieces.Piece;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Move {
    private String gameId;
    private int moveId;
    private Player player;
    private Spot start;
    private Spot end;
    private Piece pieceMoved;
    private Piece pieceKilled;
    private boolean castlingMove;
    private Spot enPassant;
    private boolean enPassantMove;

}
