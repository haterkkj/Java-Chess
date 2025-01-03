package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {
    public Pawn(Board board, Color color) {
        super(board, color);
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] possibleMoves = new boolean[getBoard().getRows()][getBoard().getCols()];

        Position p = new Position(0, 0);

        int step = getColor() == Color.WHITE ? -1 : 1;

        p.setValue(position.getRow() + step, position.getCol());
        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            possibleMoves[p.getRow()][p.getCol()] = true;
            if (getMoveCount() == 0) {
                p.setValue(position.getRow() + 2 * step, position.getCol());
                possibleMoves[p.getRow()][p.getCol()] = true;
            }
        }

        p.setValue(position.getRow() + step, position.getCol() - 1);
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = isThereOpponentPiece(p);
        }

        p.setValue(position.getRow() + step, position.getCol() + 1);
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = isThereOpponentPiece(p);
        }

        // sm

        return possibleMoves;
    }

    @Override
    public String toString() {
        return "P";
    }
}
