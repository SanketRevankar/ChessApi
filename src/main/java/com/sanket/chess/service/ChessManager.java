package com.sanket.chess.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanket.chess.mongodb.game.Game;
import com.sanket.chess.mongodb.user.User;
import com.sanket.chess.service.exception.InvalidMoveException;
import com.sanket.chess.service.vo.*;
import com.sanket.chess.service.vo.Pieces.King;
import com.sanket.chess.service.vo.Pieces.Pawn;
import com.sanket.chess.service.vo.Pieces.Piece;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;

@Data
public class ChessManager {
    private final ObjectMapper mapper = new ObjectMapper();

    private Game game;

    public ChessManager() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        game = new Game();
        game.getPlayers()[0] = new Player(user.getId(), user.getFullName(), false);
        game.getPlayers()[1] = new Player("", "", false);
        game.setBoard(new Board());
        game.setCurrentMoveNumber(0);
        game.setStatus(GameStatus.CREATED);
        game.setMovesPlayed(new HashMap<>());
        game.setCurrentTurn(game.getPlayers()[0].isWhiteSide() ? game.getPlayers()[0] : game.getPlayers()[1]);
    }

    public ChessManager(Game game) {
        this.game = game;
    }

    public boolean isEnd() {
        return game.getStatus() != GameStatus.ACTIVE;
    }

    public void makeMove(Move move) throws InvalidMoveException, JsonProcessingException {
        if (isEnd()) {
            throw new InvalidMoveException(game.getStatus().toString());
        }

        Player player = move.getPlayer();
        Spot start = game.getBoard().getBox(move.getStart().getX(), move.getStart().getY());
        Spot end = game.getBoard().getBox(move.getEnd().getX(), move.getEnd().getY());

        move.setStart(start);
        move.setEnd(end);

        Piece sourcePiece = start.getPiece();
        Piece destPiece = end.getPiece();

        validateMove(player, sourcePiece, destPiece);
        destPiece = validateMove(move, sourcePiece, destPiece);

        if (destPiece != null) {
            destPiece.setKilled(true);
            move.setPieceKilled(destPiece);
        }

        game.setCurrentMoveNumber(game.getCurrentMoveNumber() + 1);
        move.setPieceMoved(sourcePiece);
        move.setMoveId(game.getCurrentMoveNumber());
        game.getMovesPlayed().put(game.getCurrentMoveNumber(),
                mapper.readValue(mapper.writeValueAsString(move), Move.class));

        end.setPiece(move.getStart().getPiece());
        start.setPiece(null);

        if (destPiece instanceof King) {
            game.setStatus(player.isWhiteSide() ? GameStatus.WHITE_WIN : GameStatus.BLACK_WIN);
        }

        Player[] players = game.getPlayers();
        game.setCurrentTurn(game.getCurrentTurn().getId().equals(players[0].getId()) ? players[1] : players[0]);
    }

    private Piece validateMove(Move move, Piece sourcePiece, Piece destPiece) throws InvalidMoveException {
        Spot start = move.getStart();
        Spot end = move.getEnd();
        if (!sourcePiece.canMove(game.getBoard(), start, end)) {
            if (sourcePiece instanceof King && ((King) sourcePiece).isValidCastling(game.getBoard(), end)) {
                move.setCastlingMove(true);
            } else if (sourcePiece instanceof Pawn) {
                if (((Pawn) sourcePiece).isPassedPawn(game.getBoard(), start, end)) {
                    move.setEnPassant(end);
                } else {
                    Move lastMove = game.getMovesPlayed().get(game.getCurrentMoveNumber());
                    Spot enPassant;
                    if (lastMove != null) {
                        enPassant = lastMove.getEnPassant();
                    } else {
                        throw new InvalidMoveException("Invalid move");
                    }
                    if (((Pawn) sourcePiece).isEnPassant(start.getX(), end.getY(), enPassant)) {
                        Spot enPassantSpot = game.getBoard().getBox(enPassant.getX(), enPassant.getY());
                        destPiece = enPassantSpot.getPiece();
                        enPassantSpot.setPiece(null);
                        move.setEnPassantMove(true);
                    } else {
                        throw new InvalidMoveException("Invalid move");
                    }
                }
            } else {
                throw new InvalidMoveException("Invalid move");
            }
        }
        return destPiece;
    }

    private void validateMove(Player player, Piece sourcePiece, Piece destPiece) throws InvalidMoveException {
        if (sourcePiece == null) {
            throw new InvalidMoveException("No piece at start position");
        }

        if (!player.getId().equals(game.getCurrentTurn().getId())) {
            throw new InvalidMoveException("Not players turn");
        }

        if (sourcePiece.isWhite() != player.isWhiteSide()) {
            throw new InvalidMoveException("Not your piece");
        }

        if (destPiece != null && sourcePiece.isWhite() == destPiece.isWhite()) {
            throw new InvalidMoveException("Can't kill your piece");
        }
    }

}

