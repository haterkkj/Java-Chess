package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Knight extends ChessPiece {
    public Knight(Board board, Color color) {
        super(board, color);
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] possibleMoves = new boolean[getBoard().getRows()][getBoard().getCols()];

        Position p = new Position(0, 0);

        p.setValue(position.getRow() - 2, position.getCol() - 1);
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
        }

        p.setValue(position.getRow() - 2, position.getCol() + 1);
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
        }

        p.setValue(position.getRow() - 1, position.getCol() - 2);
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
        }

        p.setValue(position.getRow() - 1, position.getCol() + 2);
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
        }

        p.setValue(position.getRow() + 2, position.getCol() - 1);
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
        }

        p.setValue(position.getRow() + 2, position.getCol() + 1);
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
        }

        p.setValue(position.getRow() + 1, position.getCol() - 2);
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
        }

        p.setValue(position.getRow() + 1, position.getCol() + 2);
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
        }

        return possibleMoves;
    }

    @Override
    public String toString() {
        return "K";
    }
}
