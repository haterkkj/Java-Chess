package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {
    private int turn;
    private Color currentPlayer;
    private final Board board;
    private boolean check;

    private List<ChessPiece> piecesOnTheBoard;
    private List<ChessPiece> capturedPieces;

    public ChessMatch() {
        this.board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        piecesOnTheBoard = new ArrayList<ChessPiece>();
        capturedPieces = new ArrayList<ChessPiece>();
        initialSetup();
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] matrix = new ChessPiece[board.getRows()][board.getCols()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                matrix[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return matrix;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition){
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);

        Piece capturedPiece = makeMove(source, target);

        if(testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("Caution: you can't put yourself in check");
        }

        check = testCheck(oponnent(currentPlayer));

        nextTurn();
        return (ChessPiece) capturedPiece;
    }

    private void validateSourcePosition(Position source) {
        if (!board.thereIsAPiece(source)) {
            throw new ChessException("Error moving piece: there is no pieces in the specified position");
        }
        if (!board.piece(source).isThereAnyPossibleMove()) {
            throw new ChessException("Error moving piece: there is no possible move for this piece");
        }
        if(currentPlayer != ((ChessPiece) board.piece(source)).getColor()) {
            throw new ChessException("Error moving piece: you can't chose a piece that its not yours");
        }
    }

    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("Error moving piece: the chosen piece can't move to the specified target");
        }
    }

    private void nextTurn() {
        turn++;
        currentPlayer = currentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    private Piece makeMove(Position source, Position target) {
        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove((ChessPiece) capturedPiece);
            capturedPieces.add((ChessPiece) capturedPiece);
        }

        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        Piece p = board.removePiece(target);
        board.placePiece(p, source);

        if(capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            piecesOnTheBoard.add((ChessPiece) capturedPiece);
            capturedPieces.remove((ChessPiece) capturedPiece);
        }
    }

    private Color oponnent(Color color) {
        return color == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> x.getColor() == color).collect(Collectors.toList());
        for(Piece piece : list) {
            if(piece instanceof King) {
                return (ChessPiece) piece;
            }
        }
        throw new IllegalStateException("Error: there is no " + color + " king on the board");
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> oponnentPieces = piecesOnTheBoard.stream().filter(x -> x.getColor() == oponnent(color)).collect(Collectors.toList());
        for(Piece piece : oponnentPieces) {
            boolean[][] possibleMoves = piece.possibleMoves();
            if(possibleMoves[kingPosition.getRow()][kingPosition.getCol()]) {
                return true;
            }
        }
        return false;
    }

    private void placeNewPiece(ChessPiece piece, char col, int row) {
        board.placePiece(piece, new ChessPosition(col, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void initialSetup() {
        placeNewPiece(new Rook(board, Color.WHITE), 'c', 1);
        placeNewPiece(new Rook(board, Color.WHITE), 'c', 2);
        placeNewPiece(new Rook(board, Color.WHITE), 'd', 2);
        placeNewPiece(new Rook(board, Color.WHITE), 'e', 2);
        placeNewPiece(new Rook(board, Color.WHITE), 'e', 1);
        placeNewPiece(new King(board, Color.WHITE), 'd', 1);

        placeNewPiece(new Rook(board, Color.BLACK), 'c', 7);
        placeNewPiece(new Rook(board, Color.BLACK), 'c', 8);
        placeNewPiece(new Rook(board, Color.BLACK), 'd', 7);
        placeNewPiece(new Rook(board, Color.BLACK), 'e', 7);
        placeNewPiece(new Rook(board, Color.BLACK), 'e', 8);
        placeNewPiece(new King(board, Color.BLACK), 'd', 8);
    }

    public boolean getCheck() {
        return check;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Color currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<ChessPiece> getCapturedPieces() {
        return capturedPieces;
    }

    public List<ChessPiece> getPiecesOnTheBoard() {
        return piecesOnTheBoard;
    }
}
