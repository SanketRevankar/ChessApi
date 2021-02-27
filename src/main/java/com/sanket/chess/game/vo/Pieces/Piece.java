package com.sanket.chess.game.vo.Pieces;

import com.sanket.chess.mongodb.game.Game;
import com.sanket.chess.game.vo.Board;
import com.sanket.chess.game.vo.Spot;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public
class Piece {

    private String name;
    private boolean killed = false;
    private boolean white;
    private List<Spot> possibleMoves = new ArrayList<>();

    public Piece(String name, boolean white) {
        this.name = name;
        this.white = white;
    }

    public boolean canMove(Board board, Spot start, Spot end) {
        return false;
    }

    public void loadPossibleMoves(Game game, int x, int y) {
        getPossibleMoves().clear();
    }

}
