package boardgame;

public abstract class Piece {
    protected Position position;
    private final Board board;

    public Piece(Board board) {
        this.board = board;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    protected Board getBoard() {
        return board;
    }

    public abstract boolean[][] possibleMoves();

    public boolean possibleMove(Position position) {
        return possibleMoves()[position.getRow()][position.getCol()];
    }

    public boolean isThereAnyPossibleMove() {
        boolean[][] matrix = possibleMoves();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }
}
