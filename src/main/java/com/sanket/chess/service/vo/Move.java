package com.sanket.chess.service.vo;

import com.sanket.chess.service.vo.Pieces.Piece;
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
