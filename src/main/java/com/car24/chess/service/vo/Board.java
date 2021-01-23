package com.car24.chess.service.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Data
public class Board {
    private Spot[][] boxes;
    private HashMap<Character, List<Piece>> whitePieces;
    private HashMap<Character, List<Piece>> blackPieces;

    public Board() {
        this.initBoard();
    }

    public Spot getBox(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7) {
            throw new IndexOutOfBoundsException("Invalid spot");
        }

        return boxes[x][y];
    }

    public void initBoard() {
        boxes = new Spot[8][8];
        whitePieces = initPieces(true);
        blackPieces = initPieces(false);

        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                boxes[i][j] = new Spot(null, i, j);
            }
        }
    }

    private HashMap<Character, List<Piece>> initPieces(boolean white) {
        int pos = white ? 0 : 7;
        int pawnPos = white ? 1 : 6;

        HashMap<Character, List<Piece>> pieces = new HashMap<>();
        ArrayList<Piece> pawns = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Pawn pawn = new Pawn(white, pawnPos, i);
            boxes[pawnPos][i] = new Spot(pawn, pawnPos, i);
            pawns.add(pawn);
        }
        pieces.put('P', pawns);

        ArrayList<Piece> whiteRooks = new ArrayList<>();
        Rook rook1 = new Rook(white, pos, 0);
        Rook rook2 = new Rook(white, pos, 7);
        whiteRooks.add(rook1);
        whiteRooks.add(rook2);
        pieces.put('R', whiteRooks);

        ArrayList<Piece> whiteKnights = new ArrayList<>();
        Knight knight1 = new Knight(white, pos, 1);
        Knight knight2 = new Knight(white, pos, 6);
        whiteKnights.add(knight1);
        whiteKnights.add(knight2);
        pieces.put('N', whiteKnights);

        ArrayList<Piece> whiteBishops = new ArrayList<>();
        Bishop bishop1 = new Bishop(white, pos, 2);
        Bishop bishop2 = new Bishop(white, pos, 5);
        whiteBishops.add(bishop1);
        whiteBishops.add(bishop2);
        pieces.put('B', whiteBishops);

        Queen queen = new Queen(white, pos, 3);
        pieces.put('Q', Collections.singletonList(queen));

        King king = new King(white, pos, 4);
        pieces.put('K', Collections.singletonList(king));

        boxes[pos][0] = new Spot(rook1, pos, 0);
        boxes[pos][1] = new Spot(knight1, pos, 1);
        boxes[pos][2] = new Spot(bishop1, pos, 2);
        boxes[pos][3] = new Spot(queen, pos, 3);
        boxes[pos][4] = new Spot(king, pos, 4);
        boxes[pos][5] = new Spot(bishop2, pos, 5);
        boxes[pos][6] = new Spot(knight2, pos, 6);
        boxes[pos][7] = new Spot(rook2, pos, 7);

        return pieces;
    }
}
