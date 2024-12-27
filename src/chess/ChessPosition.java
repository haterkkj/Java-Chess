package chess;

import boardgame.Position;

public class ChessPosition {
    private int row;
    private char col;

    protected Position toPosition() {
        return new Position(row, col);
    }
}
