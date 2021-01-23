package com.car24.chess.service.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public void makeMove(Move move, Map<String, Object> response) throws JsonProcessingException {
        if (status != GameStatus.ACTIVE) {
            response.put("error", status);
            return;
        }

        Player player = move.getPlayer();
        move.setStart(board.getBox(move.getStart().getX(), move.getStart().getY()));
        move.setEnd(board.getBox(move.getEnd().getX(), move.getEnd().getY()));

        Piece sourcePiece = move.getStart().getPiece();
        Piece destPiece = move.getEnd().getPiece();
        logger.info(String.valueOf(sourcePiece));
        logger.info(String.valueOf(destPiece));

        if (sourcePiece == null) {
            response.put("error", "No piece at start position");
            response.put("moved", false);
            return;
        }

        if (player.getId() != currentTurn.getId()) {
            response.put("error", "Not players turn");
            response.put("moved", false);
            return;
        }

        if (sourcePiece.isWhite() != player.isWhiteSide()) {
            response.put("error", "Not your piece");
            response.put("moved", false);
            return;
        }

        if (destPiece != null && sourcePiece.isWhite() == destPiece.isWhite()) {
            response.put("error", "Can't kill your piece");
            response.put("moved", false);
            return;
        }

        if (!sourcePiece.canMove(board, move.getStart(), move.getEnd())) {
            response.put("error", "Invalid move");
            response.put("moved", false);
            return;
        }

        if (destPiece != null) {
            destPiece.setKilled(true);
            move.setPieceKilled(destPiece);
        }

//        if (sourcePiece instanceof King && move.isCastlingMove()) {
//            move.setCastlingMove(true);
//        }
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

