package com.sanket.chess.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanket.chess.game.vo.Pieces.*;
import com.sanket.chess.mongodb.game.Game;
import com.sanket.chess.mongodb.user.User;
import com.sanket.chess.game.exception.InvalidMoveException;
import com.sanket.chess.game.vo.*;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;

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
        game.setMovesPlayed(new ArrayList<>());
        game.setCurrentTurn(game.getPlayers()[0].isWhiteSide() ? game.getPlayers()[0] : game.getPlayers()[1]);
    }

    public ChessManager(Game game) {
        this.game = game;
    }

    public boolean isEnd() {
        return game.getStatus() != GameStatus.ACTIVE;
    }

    public void getPossibleMoves() {
        Board gameBoard = game.getBoard();
        Spot[][] board = gameBoard.getBoxes();
        boolean whiteSide = game.getCurrentTurn().isWhiteSide();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j].getPiece();
                if (piece != null) {
                    if (piece.isWhite() == whiteSide) {
                        piece.loadPossibleMoves(game, i, j);
                    } else {
                        piece.setPossibleMoves(new ArrayList<>());
                    }
                }
            }
        }
    }

    public void makeMove(Move move) throws InvalidMoveException, JsonProcessingException {
        if (isEnd()) {
            throw new InvalidMoveException(game.getStatus().toString());
        }

        Player player = move.getPlayer();
        Spot start = game.getBoard().getBox(move.getStart().getX(), move.getStart().getY());
        Spot end = game.getBoard().getBox(move.getEnd().getX(), move.getEnd().getY());

        Piece sourcePiece = start.getPiece();
        Piece destPiece = end.getPiece();

        validateMove(player, sourcePiece, destPiece);
        destPiece = validateMove(move, sourcePiece, destPiece);

        if (destPiece != null) {
            destPiece.setKilled(true);
            move.setPieceKilled(destPiece.getName());
        }

        game.setCurrentMoveNumber(game.getCurrentMoveNumber() + 1);
        move.setMoveId(game.getCurrentMoveNumber());
        move.setPieceMoved(sourcePiece.getName());
        game.getMovesPlayed().add(mapper.readValue(mapper.writeValueAsString(move), Move.class));

        if (sourcePiece instanceof Pawn && end.getX() == (start.getPiece().isWhite() ? 7 : 0)) {
            switch (move.getPawnPromotion()) {
                case "queen": end.setPiece(new Queen(player.isWhiteSide())); break;
                case "rook": end.setPiece(new Rook(player.isWhiteSide())); break;
                case "bishop": end.setPiece(new Bishop(player.isWhiteSide())); break;
                case "knight": end.setPiece(new Knight(player.isWhiteSide())); break;
            }
        } else {
            end.setPiece(sourcePiece);
        }
        start.setPiece(null);

        if (destPiece instanceof King) {
            game.setStatus(player.isWhiteSide() ? GameStatus.WHITE_WIN : GameStatus.BLACK_WIN);
        } else {
            Player[] players = game.getPlayers();
            game.setCurrentTurn(game.getCurrentTurn().getId().equals(players[0].getId()) ? players[1] : players[0]);
        }
    }

    private Piece validateMove(Move move, Piece sourcePiece, Piece destPiece) throws InvalidMoveException {
        Box start = move.getStart();
        Box end = move.getEnd();
        if (!sourcePiece.canMove(game.getBoard(), start, end)) {
            if (sourcePiece instanceof King &&
                    ((King) sourcePiece).isValidCastling(game.getBoard(), end.getX(), end.getY(), false)) {
                move.setCastlingMove(true);
            } else if (sourcePiece instanceof Pawn) {
                if (((Pawn) sourcePiece).isPassedPawn(game.getBoard(), start, end)) {
                    move.setEnPassant(end);
                } else {
                    Move lastMove = game.getMovesPlayed().get(game.getCurrentMoveNumber() - 1);
                    Box enPassant;
                    if (lastMove != null) {
                        enPassant = lastMove.getEnPassant();
                    } else {
                        throw new InvalidMoveException("Invalid move");
                    }
                    if (((Pawn) sourcePiece).isEnPassant(start.getX(), end.getY(), enPassant)) {
                        Spot enPassantSpot = game.getBoard().getBox(enPassant.getX(), enPassant.getY());
                        destPiece = enPassantSpot.getPiece();
                        enPassantSpot.setPiece(null);
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

