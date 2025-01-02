package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Bishop extends ChessPiece {
    public Bishop(Board board, Color color) {
        super(board, color);
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] possibleMoves = new boolean[getBoard().getRows()][getBoard().getCols()];

        Position p = new Position(0, 0);

        p.setValue(position.getRow() - 1, position.getCol() - 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            possibleMoves[p.getRow()][p.getCol()] = true;
            p.setValue(p.getRow() - 1, p.getCol() - 1);
        }
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = isThereOpponentPiece(p);
        }

        p.setValue(position.getRow() - 1, position.getCol() + 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            possibleMoves[p.getRow()][p.getCol()] = true;
            p.setValue(p.getRow() - 1, p.getCol() + 1);
        }
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = isThereOpponentPiece(p);
        }

        p.setValue(position.getRow() + 1, position.getCol() - 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            possibleMoves[p.getRow()][p.getCol()] = true;
            p.setValue(p.getRow() + 1, p.getCol() - 1);
        }
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = isThereOpponentPiece(p);
        }

        p.setValue(position.getRow() + 1, position.getCol() + 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            possibleMoves[p.getRow()][p.getCol()] = true;
            p.setValue(p.getRow() + 1, p.getCol() + 1);
        }
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = isThereOpponentPiece(p);
        }

        return possibleMoves;
    }

    @Override
    public String toString() {
        return "B";
    }
}
