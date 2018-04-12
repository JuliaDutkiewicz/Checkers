package model;

public class Square {
    private Piece p;

    public Square(){
        this.p=null;
    }

    public Square(Piece p){
        this.p=p;
    }

    public void setPiece (Piece p){
        this.p=p;
    }

    public Piece getPiece() {
        return p;
    }

    public void makeKing(){
        p.makeKing();
    }
}
