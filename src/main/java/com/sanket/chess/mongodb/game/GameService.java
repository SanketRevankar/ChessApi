package com.sanket.chess.mongodb.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(@Autowired GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game getGame(String gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        if (game.isPresent()) {
            return game.get();
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    public void saveGame(Game game) {
        gameRepository.save(game);
    }

}
