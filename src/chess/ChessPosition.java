package chess;

import boardgame.Position;

public class ChessPosition {
    private final int row;
    private final char col;

    public ChessPosition(char col, int row) {
        if (row < 1 || row > 8 || col < 'a' || col > 'h') {
            throw new ChessException("Error instantiating ChessPosition: valid values are from a1 to h8");
        }
        this.col = col;
        this.row = row;
    }

    protected Position toPosition() {
        return new Position(8 - row, col - 'a');
    }

    protected static ChessPosition fromPosition(Position position) {
        return new ChessPosition((char) ('a' + position.getCol()), 8 - position.getRow());
    }

    public int getRow() {
        return row;
    }

    public char getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "" + col + row;
    }
}
