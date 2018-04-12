package model;

//Key:
//WhitePlain= *
//WhiteKing = O
//BlackPlain= x
//BlackKing = %
public class  Piece {
    private boolean isKing;
    private Colour colour;

    public Piece (Colour colour){
        this.colour=colour;

        this.isKing=false;
    }
    public void makeKing(){
        this.isKing=true;
    }

    public boolean isKing() {
        return isKing;
    }

    public Colour getColour() {
        return colour;
    }
    @Override
    public String toString() {
        if(colour== Colour.White){
         if(!isKing)
             return "*";
         else
             return "O";
        }
        else if (!isKing)
            return "x";
        else
            return "%";
    }
}