package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {
    private int turn;
    private Color currentPlayer;
    private final Board board;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassentVulnerable;
    private ChessPiece promoted;

    private List<ChessPiece> piecesOnTheBoard;
    private List<ChessPiece> capturedPieces;

    public ChessMatch() {
        this.board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        piecesOnTheBoard = new ArrayList<>();
        capturedPieces = new ArrayList<>();
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

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
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
        Piece movedPiece = board.piece(target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("Caution: you can't put yourself in check");
        }

        if (movedPiece instanceof Pawn && ((Pawn) movedPiece).getMoveCount() == 1) {
            int casesAdvanced = ((Pawn) movedPiece).getColor() == Color.WHITE ? -2 : 2;
            if(source.getRow() + casesAdvanced == target.getRow()) {
                enPassentVulnerable = (ChessPiece) movedPiece;
            } else {
                enPassentVulnerable = null;
            }
        }

        promoted = null;
        if (movedPiece instanceof Pawn) {
            int lastRow = ((Pawn) movedPiece).getColor() == Color.WHITE ? 0 : 7;
            if(movedPiece.getPosition().getRow() == lastRow) {
                promoted = (ChessPiece) movedPiece;
                promoted = replacePromotedPiece("Q");
            }
        }

        check = testCheck(oponnent(currentPlayer));

        if (testCheckMate(oponnent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        return (ChessPiece) capturedPiece;
    }

    public ChessPiece replacePromotedPiece(String type) {
        if(promoted == null) {
            throw new IllegalStateException("Error: There is no piece to be promoted");
        }

        if(!type.equals("B") && !type.equals("R") && !type.equals("Q") && !type.equals("N")) {
            return promoted;
        }

        Position pos = promoted.getPosition();
        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnTheBoard.add(newPiece);

        return newPiece;
    }

    private ChessPiece newPiece(String type, Color color) {
        return switch (type) {
            case "B" -> new Bishop(board, color);
            case "R" -> new Rook(board, color);
            case "Q" -> new Queen(board, color);
            default -> new Knight(board, color);
        };
    }

    private void validateSourcePosition(Position source) {
        if (!board.thereIsAPiece(source)) {
            throw new ChessException("Error moving piece: there is no pieces in the specified position");
        }
        if (!board.piece(source).isThereAnyPossibleMove()) {
            throw new ChessException("Error moving piece: there is no possible move for this piece");
        }
        if (currentPlayer != ((ChessPiece) board.piece(source)).getColor()) {
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
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove((ChessPiece) capturedPiece);
            capturedPieces.add((ChessPiece) capturedPiece);
        }

        // sm kingside rook
        if(p instanceof King && target.getCol() == source.getCol() + 2) {
            Position sourceT = new Position(source.getRow(), source.getCol() + 3);
            Position targetT = new Position(source.getRow(), source.getCol() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // sm queenside rook
        if(p instanceof King && target.getCol() == source.getCol() - 2) {
            Position sourceT = new Position(source.getRow(), source.getCol() - 4);
            Position targetT = new Position(source.getRow(), source.getCol() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // sm en passant
        if(p instanceof Pawn) {
            if (source.getCol() != target.getCol() && capturedPiece == null) {
                Position capturedPiecePosition = p.getColor() == Color.WHITE ? new Position (target.getRow() + 1, target.getCol()) : new Position(target.getRow() - 1, target.getCol());
                capturedPiece = board.removePiece(capturedPiecePosition);
                capturedPieces.add((ChessPiece) capturedPiece);
                piecesOnTheBoard.remove((ChessPiece) capturedPiece);
            }
        }

        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        // sm en passant
        if(p instanceof Pawn) {
            if (source.getCol() != target.getCol() && capturedPiece == enPassentVulnerable) {
                ChessPiece pawn = (ChessPiece) board.removePiece(target);
                Position capturedPawnPos = oponnent(pawn.getColor()) == Color.WHITE ? new Position(3, target.getCol()) : new Position(4, source.getCol());

                // putting the pawn captured by en passant in its old pos
                board.placePiece(capturedPiece, capturedPawnPos);
                // putting the pawn who did en passant in its old pos
                board.placePiece(pawn, source);

                capturedPieces.remove((ChessPiece) capturedPiece);
                piecesOnTheBoard.add((ChessPiece) capturedPiece);
            }
            return;
        }

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            piecesOnTheBoard.add((ChessPiece) capturedPiece);
            capturedPieces.remove((ChessPiece) capturedPiece);
        }

        // sm kingside rook
        if(p instanceof King && target.getCol() == source.getCol() + 2) {
            Position sourceT = new Position(source.getRow(), source.getCol() + 3);
            Position targetT = new Position(source.getRow(), source.getCol() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        // sm queenside rook
        if(p instanceof King && target.getCol() == source.getCol() - 2) {
            Position sourceT = new Position(source.getRow(), source.getCol() - 4);
            Position targetT = new Position(source.getRow(), source.getCol() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }
    }

    private Color oponnent(Color color) {
        return color == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color) {
        List<Piece> pieces = piecesOnTheBoard.stream().filter(x -> x.getColor() == color).collect(Collectors.toList());
        for (Piece piece : pieces) {
            if (piece instanceof King) {
                return (ChessPiece) piece;
            }
        }
        throw new IllegalStateException("Error: there is no " + color + " king on the board");
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> oponnentPieces = piecesOnTheBoard.stream().filter(x -> x.getColor() == oponnent(color)).collect(Collectors.toList());
        for (Piece piece : oponnentPieces) {
            boolean[][] possibleMoves = piece.possibleMoves();
            if (possibleMoves[kingPosition.getRow()][kingPosition.getCol()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }
        List<Piece> pieces = piecesOnTheBoard.stream().filter(x -> x.getColor() == color).collect(Collectors.toList());
        for (Piece piece : pieces) {
            boolean[][] possibleMoves = piece.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getCols(); j++) {
                    if (possibleMoves[i][j]) {
                        Position source = piece.getPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void placeNewPiece(ChessPiece piece, char col, int row) {
        board.placePiece(piece, new ChessPosition(col, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void initialSetup() {
        placeNewPiece(new Rook(board, Color.WHITE), 'a', 1);
        placeNewPiece(new Knight(board, Color.WHITE), 'b', 1);
        placeNewPiece(new Bishop(board, Color.WHITE), 'c', 1);
        placeNewPiece(new Queen(board, Color.WHITE), 'd', 1);
        placeNewPiece(new King(board, Color.WHITE, this), 'e', 1);
        placeNewPiece(new Bishop(board, Color.WHITE), 'f', 1);
        placeNewPiece(new Knight(board, Color.WHITE), 'g', 1);
        placeNewPiece(new Rook(board, Color.WHITE), 'h', 1);
        placeNewPiece(new Pawn(board, Color.WHITE, this), 'a', 2);
        placeNewPiece(new Pawn(board, Color.WHITE, this), 'b', 2);
        placeNewPiece(new Pawn(board, Color.WHITE, this), 'c', 2);
        placeNewPiece(new Pawn(board, Color.WHITE, this), 'd', 2);
        placeNewPiece(new Pawn(board, Color.WHITE, this), 'e', 2);
        placeNewPiece(new Pawn(board, Color.WHITE, this), 'f', 2);
        placeNewPiece(new Pawn(board, Color.WHITE, this), 'g', 2);
        placeNewPiece(new Pawn(board, Color.WHITE, this), 'h', 2);

        placeNewPiece(new Rook(board, Color.BLACK), 'a', 8);
        placeNewPiece(new Knight(board, Color.BLACK), 'b', 8);
        placeNewPiece(new Bishop(board, Color.BLACK), 'c', 8);
        placeNewPiece(new Queen(board, Color.BLACK), 'd', 8);
        placeNewPiece(new King(board, Color.BLACK, this), 'e', 8);
        placeNewPiece(new Bishop(board, Color.BLACK), 'f', 8);
        placeNewPiece(new Knight(board, Color.BLACK), 'g', 8);
        placeNewPiece(new Rook(board, Color.BLACK), 'h', 8);
        placeNewPiece(new Pawn(board, Color.BLACK, this), 'a', 7);
        placeNewPiece(new Pawn(board, Color.BLACK, this), 'b', 7);
        placeNewPiece(new Pawn(board, Color.BLACK, this), 'c', 7);
        placeNewPiece(new Pawn(board, Color.BLACK, this), 'd', 7);
        placeNewPiece(new Pawn(board, Color.BLACK, this), 'e', 7);
        placeNewPiece(new Pawn(board, Color.BLACK, this), 'f', 7);
        placeNewPiece(new Pawn(board, Color.BLACK, this), 'g', 7);
        placeNewPiece(new Pawn(board, Color.BLACK, this), 'h', 7);
    }

    public ChessPiece getPromoted() {
        return promoted;
    }

    public ChessPiece getEnPassentVulnerable() {
        return enPassentVulnerable;
    }

    public boolean isCheckMate() {
        return checkMate;
    }

    public boolean isCheck() {
        return check;
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public List<ChessPiece> getCapturedPieces() {
        return capturedPieces;
    }

    public List<ChessPiece> getPiecesOnTheBoard() {
        return piecesOnTheBoard;
    }
}
