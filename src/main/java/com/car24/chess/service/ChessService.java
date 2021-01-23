package com.car24.chess.service;

import com.car24.chess.service.vo.Game;
import com.car24.chess.service.vo.Move;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChessService {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    ObjectMapper mapper = new ObjectMapper();

    private final Map<String, Game> games;

    public ChessService() {
        this.games = new HashMap<>();
    }

    public Map<String, Object> play(String m) throws JsonProcessingException {
        Move move = mapper.readValue(m, Move.class);

        Map<String, Object> response = new HashMap<>();
        games.get(move.getGameId()).makeMove(move, response);
        response.putAll(show(move.getGameId()));

        return response;
    }

    public Map<String, Object> show(String gameId) {
        Map<String, Object> response = new HashMap<>();
        response.put("board", games.get(gameId).getBoard().getBoxes());
        response.put("status", games.get(gameId).getStatus());
        response.put("currentTurn", games.get(gameId).getCurrentTurn().getId());
        return response;
    }

    public Map<String, Object> newGame() {
        Game game = new Game();
        String gameId = game.getGameId();
        logger.info("[" + gameId + "]" + " Started Game");

        games.put(gameId, game);

        Map<String, Object> response = new HashMap<>();
        response.put("gameId", gameId);

        return response;
    }

    public List<Move> moves(String gameId) throws JsonProcessingException {
        List<Move> moves = new ArrayList<>();
        for (String move: games.get(gameId).getMovesPlayed()) {
            moves.add(mapper.readValue(move, Move.class));
        }
        return moves;
    }
}
