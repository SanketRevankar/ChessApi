package com.sanket.chess.api;

import com.sanket.chess.mongodb.game.Game;
import com.sanket.chess.service.ChessService;
import com.sanket.chess.service.exception.InvalidMoveException;
import com.sanket.chess.service.vo.Move;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
class ChessApi {
    private static final String APPLICATION_JSON = "application/json";

    @Autowired
    private final ChessService chessService;

    public ChessApi(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping(value = "/api/play", produces = APPLICATION_JSON)
    @ResponseStatus(HttpStatus.CREATED)
    public void chess(@RequestBody Move move) throws JsonProcessingException {
        chessService.play(move);
    }

    @GetMapping(value = "/api/start", produces = APPLICATION_JSON)
    public Map<String, String> newGame() {
        return chessService.newGame();
    }

    @PostMapping(value = "/api/join/{gameId}", produces = APPLICATION_JSON)
    public Map<String, String> joinGame(@PathVariable String gameId) throws JsonProcessingException {
        return chessService.joinGame(gameId);
    }

    @GetMapping(value = "/api/game/{gameId}", produces = APPLICATION_JSON)
    public Game show(@PathVariable String gameId) {
        return chessService.show(gameId);
    }

    @GetMapping(value = "/api/moves", produces = APPLICATION_JSON)
    public Map<Integer, Move> moves(@RequestParam String gameId) {
        return chessService.moves(gameId);
    }
}
