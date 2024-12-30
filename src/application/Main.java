package application;

import boardgame.Board;
import boardgame.Piece;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Main {
    public static void main(String[] args) {

        ChessMatch chessMatch = new ChessMatch();

        UI.printBoard(chessMatch.getPieces());
    }
}
