package com.sanket.chess.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanket.chess.mongodb.game.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(id = "listener", topics = { "chess" })
public class Consumer {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaHandler
    public void chess(String game) throws JsonProcessingException {
        Game g = mapper.readValue(game, Game.class);
        simpMessagingTemplate.convertAndSend("/topic/game/" + g.getId(), g);
    }
}
