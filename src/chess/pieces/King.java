package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] possibleMoves = new boolean[getBoard().getRows()][getBoard().getCols()];

        Position p = new Position(0, 0);

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (j == 0 && i == 0) {
                    continue;
                }

                p.setValue(position.getRow() - i, position.getCol() - j);
                if (getBoard().positionExists(p)) {
                    possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
                }
            }
        }

        return possibleMoves;
    }

    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }


    @Override
    public String toString() {
        return "K";
    }
}
