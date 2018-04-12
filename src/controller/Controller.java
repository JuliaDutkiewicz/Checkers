package controller;

import auxiliary.Converter;
import auxiliary.WrongBoardSizeExeption;
import auxiliary.Pair;
import model.*;

import static java.lang.Math.abs;

public class Controller {
    private Board board;
    private Board boardCopy;

    public Controller(Board board) {
        this.board = board;
    }

    public static Board makeBoard(int n) throws WrongBoardSizeExeption {
        if (n == 8 || n == 10 || n == 12)
            return new Board(n);
        else
            throw new WrongBoardSizeExeption();
    }

    public Piece[][] getBoard() {
        return board.getBoard();
    }

    public void makeMove(Coordinates start, Coordinates end, Player player, boolean sequence, boolean first, boolean last) {
        // checking if start square if not null, color of the piece is the color of player and the end square i free
        if (board.getPiece(start) == null || board.getPiece(start).getColour() != player.getColour() || board.getPiece(end) != null) {
            throw new IllegalArgumentException();
        }
        boolean isKing = board.getPiece(start).isKing();
        Colour colour = board.getPiece(start).getColour();
        //Simple model.Piece
        if (!isKing) {
            //simple move
            if (!sequence) {
                //no check
                int mul = (colour == Colour.White) ? 1 : -1;
                //checking if move is ok(1.if piece is going forward,2. if it is moving one square to side
                if (mul * (start.getY() - end.getY()) == 1 && Math.abs(Converter.LettersToNumbers(start.getX()) - Converter.LettersToNumbers(end.getX())) == 1) {
                    board.setSquare(end, board.getPiece(start));//moving the piece
                    board.setSquare(start, null);
                    if (end.getY() == board.getSize() || end.getY() - 1 == 0)//checking if piece is transforming into King
                        board.makeKing(end);
                    return;
                }
                //check
                else
                    SimplePieceCheck(start, end, colour);
                //sequence
            } else {
                if (first)//if it is a first move then I make a security copy
                    boardCopy = new Board(board);
                try {
                    SimplePieceCheck(start, end, colour);
                } catch (IllegalArgumentException e) {
                    board = new Board(boardCopy);//restoring the right board
                    throw new IllegalArgumentException();
                }
                if ((colour == Colour.Black && end.getY() == board.getSize()) || (colour == Colour.White && end.getY() - 1 == 0) && last)//checking if piece is transforming int King only if it is the last move
                    board.makeKing(end);
            }
        } else {
            //King move
            if (!sequence) {
                Coordinates lostPiece = KingMove(start, end, colour);//the function that checks if the move is ok and returns null or coordinates of the lost piece
                board.setSquare(end, board.getPiece(start));//moving the piece
                board.setSquare(start, null);
                if (lostPiece != null)
                    board.setSquare(lostPiece, null);//removing the lost piece

            } else {
                if (first)//if it is a first move then I make a security copy
                    boardCopy = new Board(board);
                Coordinates lostPiece = KingMove(start, end, colour);
                if (lostPiece != null) {
                    board.setSquare(end, board.getPiece(start));//moving the piece
                    board.setSquare(start, null);
                    board.setSquare(lostPiece, null);//removing the lost piece
                } else {
                    board = new Board(boardCopy); //restoring the right board
                    throw new IllegalArgumentException();

                }

            }
        }


    }

    private void SimplePieceCheck(Coordinates start, Coordinates end, Colour colour) {
        //checking if move is ok(1,2.if piece is going 2 squres forward/backward/to side,
        if (!(Math.abs(start.getY() - end.getY()) == 2 && Math.abs(Converter.LettersToNumbers(start.getX()) - Converter.LettersToNumbers(end.getX())) == 2))
            throw new IllegalArgumentException();
        int y = (start.getY() + end.getY()) / 2 - 1; //getting the Coordinates -1 of the lost piece
        int x = (Converter.LettersToNumbers(start.getX()) + Converter.LettersToNumbers(end.getX())) / 2 - 1;
        if (board.getPiece(x, y).getColour() == colour) //checking if thert is a piece to be lost and if it has a different colour then the player's colour
            throw new IllegalArgumentException();
        board.setSquare(end, board.getPiece(start));//moving the piece
        board.setSquare(start, null);
        board.setSquare(x, y, null);//removing the lost piece
    }

    //this function returns lost piece coordinates or null(if there is no lost piece), if the move is incorrect it throws illegal argument exception
    private Coordinates KingMove(Coordinates start, Coordinates end, Colour colour) {
        if (!(Math.abs(start.getY() - end.getY()) == Math.abs(Converter.LettersToNumbers(start.getX()) - Converter.LettersToNumbers(end.getX()))))//1.is it moving diagonally
            throw new IllegalArgumentException();
        if (start.getX() == end.getX())//if the start is the same as end
            throw new IllegalArgumentException();
        //is the move checking one piece
        int numberOfPiecesChecked = 0;
        Coordinates lostPiece = null;
        int shiftX = (Converter.LettersToNumbers(start.getX()) < Converter.LettersToNumbers(end.getX())) ? 1 : (-1);//is it going left or right
        int shiftY = (start.getY() < end.getY()) ? 1 : (-1);//is it going up or down
        for (int i = Converter.LettersToNumbers(start.getX()) - 1 + shiftX, j = start.getY() - 1 + shiftY; j != end.getY() - 1; i = i + shiftX, j = j + shiftY) {
            if (board.getPiece(i, j) != null) {
                numberOfPiecesChecked++;
                lostPiece = new Coordinates(Converter.NumbersToLetters(i + 1), j + 1);
            }
        }
        if (numberOfPiecesChecked > 1)
            throw new IllegalArgumentException();
        //is it checking the other player piece
        if (numberOfPiecesChecked == 1 && board.getPiece(lostPiece).getColour() == colour)
            throw new IllegalArgumentException();
        if (numberOfPiecesChecked == 1)
            return lostPiece;
        else
            return null;
    }

    public boolean checkIfTie() {
        for (int i = 0; i < board.getSize(); i++) {//checking all the squares on the board
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getPiece(i, j) == null)//if the square is empty then we go to the next one
                    continue;
                if (canPieceMove(i, j))//if the piece can move then it id not a tie
                    return false;
            }
        }
        return true;
    }

    public boolean checkIfWins(Player player) {
        //checking if there are any opponent's pieces that can move
        for (int i = 0; i < board.getSize(); i++)
            for (int j = 0; j < board.getSize(); j++)
                if (board.getPiece(i, j) != null && board.getPiece(i, j).getColour() != player.getColour() && canPieceMove(i, j))//if the opponent's piece can move then hve doesn't move
                    return false;
        return true;
    }

    private boolean canPieceMove(int i, int j) {
        Piece tmp = board.getPiece(i, j);
        Pair[] possibleMoves = new Pair[4];
        possibleMoves[0] = new Pair(1, 1);
        possibleMoves[1] = new Pair(1, -1);
        possibleMoves[2] = new Pair(-1, 1);
        possibleMoves[3] = new Pair(-1, -1);
        //checking if piece can check (it is the same for King and Simple piece)
        for (int k = 0; k < 4; k++) {
            int x1 = possibleMoves[k].getX() + i;//the position that must have a piecce on it
            int y1 = possibleMoves[k].getY() + j;
            int x2 = possibleMoves[k].getX() + x1;//the position that must be free
            int y2 = possibleMoves[k].getY() + y1;
            if (x2 >= 0 && x2 < board.getSize() && y2 >= 0 && y2 < board.getSize() &&
                    board.getPiece(x1, y1) != null &&
                        board.getPiece(x1, y1).getColour() != tmp.getColour() && board.getPiece(x2, y2) == null)
                return true;
        }
        if (!tmp.isKing()) {//the piece is Simple
            //checking if simple move is possible
            int simpleMoveShiftY = (tmp.getColour() == Colour.Black) ? 1 : (-1);
            if (j + simpleMoveShiftY >= 0 && j + simpleMoveShiftY < board.getSize() &&
                    ((i - 1 >= 0 && board.getPiece(i - 1, j + simpleMoveShiftY) == null) ||
                            (i + 1 < board.getSize() && board.getPiece(i + 1, j + simpleMoveShiftY) == null)))
                return true;
        }
        if (tmp.isKing()) {
            //checking if simple move is possible
            for (int k = 0; k < 4; k++) {
                int x1 = possibleMoves[k].getX() + i;//the position that must have a piece on it
                int y1 = possibleMoves[k].getY() + j;
                if (x1 >= 0 && x1 < board.getSize() && y1 >= 0 && y1 < board.getSize() && board.getPiece(x1, y1) == null)
                    return true;
            }
        }
        return false;
    }
}
