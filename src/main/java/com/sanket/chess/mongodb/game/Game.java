package com.sanket.chess.mongodb.game;

import com.sanket.chess.game.vo.Board;
import com.sanket.chess.game.vo.GameStatus;
import com.sanket.chess.game.vo.Move;
import com.sanket.chess.game.vo.Player;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

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
    private List<Move> movesPlayed;
    private boolean check;
}
