package model;

import auxiliary.Converter;

//board must have even numbers of squares in the row and it has to have >=8 squares in a row
//It is not necessary to colour the squares but if somebody would want to change the viewer then it will be helpful
public class Board {
    private int size;
    private Square[][] board;

    public Board(int n) {
        this.size = n;
        this.board = new Square[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = new Square();
            }
        }
        for (int i = 0; i < size; i = i + 2) {
            board[i][0].setPiece(new Piece(Colour.Black));
            board[i][2].setPiece(new Piece(Colour.Black));
            board[i][n - 2].setPiece(new Piece(Colour.White));
        }
        for (int i = 1; i < size; i = i + 2) {
            board[i][1].setPiece(new Piece(Colour.Black));
            board[i][n - 1].setPiece(new Piece(Colour.White));
            board[i][n - 3].setPiece(new Piece(Colour.White));
        }
    }

    public Piece[][] getBoard() {
        Piece[][] p = new Piece[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (board[i][j].getPiece() != null)
                    p[i][j] = board[i][j].getPiece();
            }
        return p;
    }

    public Board(Board board) { // copy constructor
        this.size = board.getSize();
        this.board = new Square[this.size][this.size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                this.board[i][j] = new Square();
                if (board.getPiece(i, j) != null)
                    this.board[i][j].setPiece(board.getPiece(i, j));
            }
    }

    public Piece getPiece(int i, int j) {
        return board[i][j].getPiece();
    }

    public Piece getPiece(Coordinates c) {
        int i = Converter.LettersToNumbers(c.getX());
        int j = c.getY();
        return getPiece(i - 1, j - 1);
    }

    public void setSquare(Coordinates c, Piece p) {
        int i = Converter.LettersToNumbers(c.getX()) - 1;
        int j = c.getY() - 1;
        board[i][j] = new Square(p);
    }

    public void makeKing(Coordinates c) {
        int i = Converter.LettersToNumbers(c.getX()) - 1;
        int j = c.getY() - 1;
        board[i][j].makeKing();
    }

    public void setSquare(int i, int j, Piece p) {
        board[i][j] = new Square(p);
    }

    public int getSize() {
        return this.size;
    }
}
