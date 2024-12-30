package chess;

import boardgame.Board;
import boardgame.Piece;
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

    private void placeNewPiece(Piece piece, char col, int row) {
        board.placePiece(piece, new ChessPosition(col, row).toPosition());
    }

    private void initialSetup() {
        placeNewPiece(new Rook(board, Color.WHITE), 'c', 8);
        placeNewPiece(new King(board, Color.BLACK), 'd',1);
        placeNewPiece(new King(board, Color.WHITE), 'e', 8);
    }
}
