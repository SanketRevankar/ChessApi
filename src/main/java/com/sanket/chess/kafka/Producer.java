package com.sanket.chess.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {
    private final KafkaTemplate<Object, Object> kafkaTemplate;

    Producer(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendGame(String what) {
        this.kafkaTemplate.send("chess", what);
    }

}