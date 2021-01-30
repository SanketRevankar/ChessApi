package com.sanket.chess.api;

import com.sanket.chess.service.ChessService;
import com.sanket.chess.service.vo.Move;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public Map<String, Object> chess(@RequestBody String move) throws JsonProcessingException {
        return chessService.play(move);
    }

    @GetMapping(value = "/api/start", produces = APPLICATION_JSON)
    public Map<String, Object> newGame() {
        return chessService.newGame();
    }

    @GetMapping(value = "/api/game/{gameId}", produces = APPLICATION_JSON)
    public Map<String, Object> show(@PathVariable String gameId) {
        return chessService.show(gameId);
    }

    @GetMapping(value = "/api/moves", produces = APPLICATION_JSON)
    public List<Move> moves(@RequestParam String gameId) throws JsonProcessingException {
        return chessService.moves(gameId);
    }
}
