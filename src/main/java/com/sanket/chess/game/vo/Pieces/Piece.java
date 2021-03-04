package com.sanket.chess.game.vo.Pieces;

import com.sanket.chess.game.vo.Box;
import com.sanket.chess.mongodb.game.Game;
import com.sanket.chess.game.vo.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public
class Piece {

    private String name;
    private boolean killed = false;
    private boolean white;
    private ArrayList<Box> possibleMoves = new ArrayList<>();

    public Piece(String name, boolean white) {
        this.name = name;
        this.white = white;
    }

    public boolean canMove(Board board, Box start, Box end) {
        return false;
    }

    public ArrayList<Box> fetchPossibleMoves(Game game, int x, int y) {
        return possibleMoves;
    }

}
