package com.sanket.chess.service.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanket.chess.service.exception.InvalidMoveException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Data
public class Game {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private String gameId;
    private Player[] players;
    private Board board;
    private Player currentTurn;
    private GameStatus status;
    private List<String> movesPlayed;
    private ObjectMapper objectMapper;

    public Game() {
        objectMapper = new ObjectMapper();
        gameId = UUID.randomUUID().toString();
        board = new Board();
        status = GameStatus.ACTIVE;

        players = new Player[2];
        players[0] = new Player(1, true);
        players[1] = new Player(2, false);

        this.currentTurn = players[0].isWhiteSide() ? players[0] : players[1];

        movesPlayed = new ArrayList<>();
    }

    public boolean isEnd() {
        return this.getStatus() != GameStatus.ACTIVE;
    }

    public void makeMove(Move move, Map<String, Object> response) throws JsonProcessingException, InvalidMoveException {
        if (status != GameStatus.ACTIVE) {
            throw new InvalidMoveException(status.toString());
        }

        Player player = move.getPlayer();
        move.setStart(board.getBox(move.getStart().getX(), move.getStart().getY()));
        move.setEnd(board.getBox(move.getEnd().getX(), move.getEnd().getY()));

        Piece sourcePiece = move.getStart().getPiece();
        Piece destPiece = move.getEnd().getPiece();

        if (sourcePiece == null) {
            throw new InvalidMoveException("No piece at start position");
        }

        if (player.getId() != currentTurn.getId()) {
            throw new InvalidMoveException("Not players turn");
        }

        if (sourcePiece.isWhite() != player.isWhiteSide()) {
            throw new InvalidMoveException("Not your piece");
        }

        if (destPiece != null && sourcePiece.isWhite() == destPiece.isWhite()) {
            throw new InvalidMoveException("Can't kill your piece");
        }

        if (!sourcePiece.canMove(board, move.getStart(), move.getEnd())) {
            if (sourcePiece instanceof King && ((King) sourcePiece).isValidCastling(board, move.getEnd())) {
                move.setCastlingMove(true);
            } else if (sourcePiece instanceof Pawn) {
                Spot enPassantSpot = ((Pawn) sourcePiece).isEnPassant(board, move.getStart(), move.getEnd());
                if (enPassantSpot != null) {
                    destPiece = enPassantSpot.getPiece();
                    enPassantSpot.setPiece(null);
                    move.setEnPassantMove(true);
                }
            } else {
                throw new InvalidMoveException("Invalid move");
            }
        }

        if (destPiece != null) {
            destPiece.setKilled(true);
            move.setPieceKilled(destPiece);
        }

        move.setPieceMoved(sourcePiece);
        movesPlayed.add(objectMapper.writeValueAsString(move));

        move.getEnd().setPiece(move.getStart().getPiece());
        move.getStart().setPiece(null);

        if (destPiece instanceof King) {
            status = player.isWhiteSide() ? GameStatus.WHITE_WIN : GameStatus.BLACK_WIN;
        }

        this.currentTurn = this.currentTurn == players[0] ? players[1] : players[0];
        response.put("moved", true);
    }

}

