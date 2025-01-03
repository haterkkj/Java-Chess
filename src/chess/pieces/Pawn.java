package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {
    private final ChessMatch chessMatch;

    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
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

        // sm en passant
        int rowForEnPassant = getColor() == Color.WHITE ? 3 : 4;
        if (getPosition().getRow() == rowForEnPassant) {
            Position left = new Position(position.getRow(), position.getCol() - 1);
            if (getBoard().positionExists(left)) {
                if (isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassentVulnerable()) {
                    possibleMoves[left.getRow() - 1][left.getCol()] = true;
                }
            }

            Position right = new Position(position.getRow(), position.getCol() + 1);
            if (getBoard().positionExists(right)) {
                if (isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassentVulnerable()) {
                    possibleMoves[right.getRow() - 1][right.getCol()] = true;
                }
            }
        }

        return possibleMoves;
    }

    @Override
    public String toString() {
        return "P";
    }
}
