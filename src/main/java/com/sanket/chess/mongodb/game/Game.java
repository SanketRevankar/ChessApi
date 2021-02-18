package com.sanket.chess.mongodb.game;

import com.sanket.chess.service.vo.Board;
import com.sanket.chess.service.vo.GameStatus;
import com.sanket.chess.service.vo.Move;
import com.sanket.chess.service.vo.Player;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Data
@NoArgsConstructor
public class Game {

    @Id
    private String id;
    private Player[] players = new Player[2];
    private Board board;
    private Player currentTurn;
    private GameStatus status;
    private int currentMoveNumber;
    private Map<Integer, Move> movesPlayed;

}
