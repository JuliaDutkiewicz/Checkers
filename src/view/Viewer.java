package view;
//Key:
//WhitePlain= *
//WhiteKing = O
//BlackPlain= x
//BlackKing = %

import auxiliary.Converter;
import controller.Controller;
import model.Colour;
import model.Coordinates;
import model.Piece;
import model.Player;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Viewer {
    private Piece[][] board;

    public Viewer(Piece[][] p) {
        this.board = p;
    }

    private void printBoard() {
        int n = board[0].length;
        if (n == 8)
            System.out.println("   a  b  c  d  e  f  g  h");
        if (n == 10)
            System.out.println("   a  b  c  d  e  f  g  h  i  j");
        if (n == 12)
            System.out.println("   a  b  c  d  e  f  g  h  i  j  k  l");
        for (int j = n - 1; j >= 0; j--) {
            if (j + 1 > 9)
                System.out.print(j + 1);
            else
                System.out.print(" " + (j + 1));
            for (int i = 0; i < n; i++) {
                if (board[i][j] != null)
                    System.out.print("[" + board[i][j].toString() + "]");
                else
                    System.out.print("[ ]");
            }
            if (j + 1 > 9)
                System.out.print(j + 1);
            else
                System.out.print(" " + (j + 1));
            System.out.println();
        }
        if (n == 8)
            System.out.println("   a  b  c  d  e  f  g  h");
        if (n == 10)
            System.out.println("   a  b  c  d  e  f  g  h  i  j");
        if (n == 12)
            System.out.println("   a  b  c  d  e  f  g  h  i  j  k  l");
    }

    public static int gameStart() {
        System.out.println("Hello!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the size of board (8,10 or 12):");
        int size = scanner.nextInt();
        return size;
    }

    public void play(Controller controller) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter first model.Player's name(white pieces '*'):");
        String name1 = scanner.nextLine();
        System.out.println("Enter second model.Player's name(black pieces 'x'): ");
        String name2 = scanner.nextLine();
        Player player1 = new Player(name1, Colour.White);
        Player player2 = new Player(name2, Colour.Black);

        boolean firstMove = true;
        boolean playerFlag = true; //true player1, false player2
        while (true) {
            if (playerFlag)
                System.out.println(name1 + "'s move: ");
            else
                System.out.println(name2 + "'s move: ");
            boolean wrongMove = false;
            do {
                try {
                    wrongMove = false;
                    char startChar = 'a';
                    int startInt = 0;
                    boolean exception;
                    do {                 //making a loop that make the player enter start position that is on the board
                        exception = false;
                        if (firstMove) {
                            printBoard();
                            firstMove = false;
                        }
                        System.out.println("Enter start position: ");
                        try {
                            String input = scanner.next();
                            if (input.length() > 1)
                                throw new InputMismatchException();
                            startChar = input.charAt(0);
                            startInt = scanner.nextInt();
                            if (startInt < 1 || startInt > board[0].length || Converter.LettersToNumbers(startChar) > board[0].length)//checking if position is on the board char cant be <1 because of letter to number function
                                throw new InputMismatchException();
                        } catch (InputMismatchException e) {
                            exception = true;
                            scanner.skip(".*\n");
                            System.out.println("This position is not on the board!");
                        }
                    } while (exception);

                    int numberOfMoves = 1;
                    do
                    {                 //making a loop that make the player enter the right number of moves in the path

                        exception = false;
                        System.out.println("Enter number of moves in the path: ");
                        try {
                            numberOfMoves = scanner.nextInt();
                            if (numberOfMoves < 1)
                                throw new InputMismatchException();
                        } catch (InputMismatchException e) {
                            exception = true;
                            if (scanner.hasNext()) {
                                String o = scanner.nextLine();
                            }
                            System.out.println("Wrong number of moves in the path!");
                        }
                    } while (exception);
                    for (int i = 0; i < numberOfMoves; i++) {
                        char nextChar = 'a';
                        int nextInt = 1;
                        do {
                            try {
                                exception = false;
                                System.out.println("Enter next position:");
                                String input = scanner.next();
                                if (input.length() > 1)
                                    throw new InputMismatchException();
                                nextChar = input.charAt(0);
                                nextInt = scanner.nextInt();
                                //checking if position is on the board, char cant be <1 because of letter to number function
                                if (nextInt < 1 || nextInt > board[0].length || Converter.LettersToNumbers(nextChar) > board[0].length)
                                    throw new InputMismatchException();
                            } catch (InputMismatchException e) {
                                exception = true;
                                scanner.skip(".*\n");
                                System.out.println("This position is not on the board! Try again:");
                                printBoard();
                            }
                        } while (exception);
                        controller.makeMove(new Coordinates(startChar, startInt), new Coordinates(nextChar, nextInt), playerFlag ? player1 : player2, (numberOfMoves != 1), (i == 0), i == (numberOfMoves - 1));
                        update(controller);
                        printBoard();
                        startChar = nextChar;
                        startInt = nextInt;
                    }
                } catch (IllegalArgumentException e) {
                    wrongMove = true;
                    update(controller);
                    System.out.println("Wrong move try again!");
                }

            } while (wrongMove);
            if (controller.checkIfTie()) {
                System.out.println("It is a tie. GAME OVER!");
                break;
            }
            if (controller.checkIfWins((playerFlag) ? player1 : player2)) {
                if (playerFlag)
                    System.out.println(player1.getName() + " wins! Congrats!!!");
                else
                    System.out.println(player2.getName() + " wins! Congrats!!!");
                break;
            }
            playerFlag = !playerFlag;
        }

    }

    private void update(Controller c) {
        this.board = c.getBoard();
    }
}
