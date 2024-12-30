package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.*;

public class ChessMatch {
    private Board board;

    public ChessMatch() {
        this.board = new Board(8, 8);
        initialSetup();
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] matrix = new ChessPiece[board.getRows()][board.getCols()];
        for(int i = 0; i < board.getRows(); i++) {
            for(int j = 0; j < board.getCols(); j++) {
                matrix[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return matrix;
    }

    private void initialSetup() {
        board.placePiece(new Rook(board, Color.WHITE), new Position(2, 2));
        board.placePiece(new King(board, Color.BLACK), new Position(7, 3));
        board.placePiece(new King(board, Color.BLACK), new Position(1, 4));
    }
}
