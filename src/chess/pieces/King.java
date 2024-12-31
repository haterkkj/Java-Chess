package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] possibleMoves = new boolean[getBoard().getRows()][getBoard().getCols()]   ;

        Position p = new Position(0,0);

        // above
        p.setValue(position.getRow() - 1, position.getCol());
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
        }

        // below
        p.setValue(position.getRow() + 1, position.getCol());
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
        }

        // left
        p.setValue(position.getRow(), position.getCol() - 1);
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
        }

        // right
        p.setValue(position.getRow(), position.getCol() + 1);
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
        }

        // nw
        p.setValue(position.getRow() + 1, position.getCol() + 1);
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
        }

        // ne
        p.setValue(position.getRow() + 1, position.getCol() - 1);
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
        }

        // sw
        p.setValue(position.getRow() - 1, position.getCol() + 1);
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
        }

        // se
        p.setValue(position.getRow() - 1, position.getCol() - 1);
        if (getBoard().positionExists(p)) {
            possibleMoves[p.getRow()][p.getCol()] = !getBoard().thereIsAPiece(p) || isThereOpponentPiece(p);
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
