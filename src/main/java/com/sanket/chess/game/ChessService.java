package com.sanket.chess.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanket.chess.kafka.Producer;
import com.sanket.chess.mongodb.game.Game;
import com.sanket.chess.mongodb.game.GameService;
import com.sanket.chess.mongodb.user.User;
import com.sanket.chess.game.exception.InvalidMoveException;
import com.sanket.chess.game.vo.GameStatus;
import com.sanket.chess.game.vo.Move;
import com.sanket.chess.game.vo.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ChessService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper mapper = new ObjectMapper();

    private final Producer producer;
    private final GameService gameService;

    public ChessService(@Autowired Producer producer,
                        @Autowired GameService gameService) {
        this.producer = producer;
        this.gameService = gameService;
    }

    public void play(Move move) throws JsonProcessingException {
        ChessManager chessManager = new ChessManager(gameService.getGame(move.getGameId()));
        try {
            chessManager.makeMove(move);
        } catch (InvalidMoveException e) {
            return;
        }
        chessManager.getPossibleMoves();
        gameService.saveGame(chessManager.getGame());
        producer.sendGame(mapper.writeValueAsString(chessManager.getGame()));
    }

    public Game show(String gameId) {
        return gameService.getGame(gameId);
    }

    public Map<String, String> newGame() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ChessManager chessManager = new ChessManager();
        gameService.saveGame(chessManager.getGame());

        String gameId = chessManager.getGame().getId();
        logger.info("New Game [" + gameId + "] started by " + user.getFullName() + " [" + user.getId() + "]");

        return getResponseMap(gameId);
    }

    private Map<String, String> getResponseMap(String gameId) {
        Map<String, String> response = new HashMap<>();
        response.put("gameId", gameId);
        return response;
    }

    public Map<String, String> joinGame(String gameId) throws JsonProcessingException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Game game = gameService.getGame(gameId);

        if (game.getPlayers()[0].getId().equals(user.getId()) ||
                Objects.equals(game.getPlayers()[1].getId(), user.getId())) {
            return getResponseMap(gameId);
        }

        if (game.getStatus() == GameStatus.CREATED) {
            boolean whiteSide = Math.random() < 0.5;
            game.getPlayers()[1] = new Player(user.getId(), user.getFullName(), whiteSide);
            game.getPlayers()[0].setWhiteSide(!whiteSide);
            game.setStatus(GameStatus.ACTIVE);
            game.setCurrentTurn(game.getPlayers()[0].isWhiteSide() ? game.getPlayers()[0] : game.getPlayers()[1]);
            ChessManager chessManager = new ChessManager(game);
            chessManager.getPossibleMoves();
            game = chessManager.getGame();
            gameService.saveGame(game);
            producer.sendGame(mapper.writeValueAsString(game));
            logger.info("Game [" + gameId + "] joined by " + user.getFullName() + " [" + user.getId() + "]");
            return getResponseMap(gameId);
        }

        throw new HttpServerErrorException(HttpStatus.BAD_REQUEST);
    }

}
