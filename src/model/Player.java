package model;

import model.Colour;

public class Player {
    private String name;
    private Colour colour;

    public Player (String n, Colour colour){
        this.name= n;
        this.colour=colour;
    }

    public String getName() {
        return name;
    }

    public Colour getColour() {
        return colour;
    }
}
