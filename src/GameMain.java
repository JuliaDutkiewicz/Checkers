import auxiliary.WrongBoardSizeExeption;
import controller.Controller;
import model.Board;
import view.Viewer;

public class GameMain {
    public static void main(String[] args) {

        Board board;//= new model.Board();
        try {
            board = Controller.makeBoard(Viewer.gameStart());//logic
        } catch (Exception | WrongBoardSizeExeption e) {
            System.out.println("Wrong size!!! You cant play checkers if you cant enter right size! I will give you last try. The size will be 8 :P");
            board = new Board(8);
        }

        Viewer viewer = new Viewer(board.getBoard()); //viever
        Controller controller = new Controller(board); //controller
        viewer.play(controller);
    }
}
