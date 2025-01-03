package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
    private final ChessMatch chessMatch;

    public King(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
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

        // specialmove castling
        if (getMoveCount() == 0 && !chessMatch.isCheck()) {
            Position auxPos = new Position(0, 0);

            // kingside rook
            auxPos.setValue(position.getRow(), position.getCol() + 3);
            if (testRookCastling(auxPos)) {
                Position auxPos1 = new Position(position.getRow(), position.getCol() + 1);
                Position auxPos2 = new Position(position.getRow(), position.getCol() + 2);
                if (!getBoard().thereIsAPiece(auxPos1) && !getBoard().thereIsAPiece(auxPos2)) {
                    possibleMoves[auxPos1.getRow()][auxPos2.getCol()] = true;
                }
            }

            // queenside rook
            auxPos.setValue(position.getRow(), position.getCol() - 4);
            if (testRookCastling(auxPos)) {
                Position auxPos1 = new Position(position.getRow(), position.getCol() + 1);
                Position auxPos2 = new Position(position.getRow(), position.getCol() + 2);
                Position auxPos3 = new Position(position.getRow(), position.getCol() + 3);
                if (!getBoard().thereIsAPiece(auxPos1) && !getBoard().thereIsAPiece(auxPos2) && !getBoard().thereIsAPiece(auxPos3)) {
                    possibleMoves[auxPos2.getRow()][auxPos2.getCol()] = true;
                }
            }
        }

        return possibleMoves;
    }

    private boolean testRookCastling(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p instanceof Rook && p.getMoveCount() == 0 && p.getColor() == getColor();
    }

    @Override
    public String toString() {
        return "K";
    }
}
