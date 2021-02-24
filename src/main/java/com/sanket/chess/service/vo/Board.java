package com.sanket.chess.service.vo;

import com.sanket.chess.service.vo.Pieces.*;
import lombok.Data;

import java.util.List;

@Data
public class Board {
    private Spot[][] boxes;

    public Board() {
        boxes = new Spot[8][8];
        initPieces(true);
        initPieces(false);

        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                boxes[i][j] = new Spot(null, i, j);
            }
        }
    }

    public Spot getBox(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7) {
            throw new IndexOutOfBoundsException("Invalid spot");
        }

        return boxes[x][y];
    }

    private void initPieces(boolean white) {
        int pos = white ? 0 : 7;
        int pawnPos = white ? 1 : 6;

        for (int i = 0; i < 8; i++) {
            boxes[pawnPos][i] = new Spot(new Pawn(white), pawnPos, i);
        }

        boxes[pos][0] = new Spot(new Rook(white), pos, 0);
        boxes[pos][1] = new Spot(new Knight(white), pos, 1);
        boxes[pos][2] = new Spot(new Bishop(white), pos, 2);
        boxes[pos][3] = new Spot(new Queen(white), pos, 3);
        boxes[pos][4] = new Spot(new King(white), pos, 4);
        boxes[pos][5] = new Spot(new Bishop(white), pos, 5);
        boxes[pos][6] = new Spot(new Knight(white), pos, 6);
        boxes[pos][7] = new Spot(new Rook(white), pos, 7);
    }
}
