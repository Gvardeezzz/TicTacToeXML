import XML.XMLReader;
import XML.XMLWriter;

public class Game {
    public static void main(String[] args) throws InterruptedException {
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller(model, view);

        String choise = "";
        while (!(choise.equals("1") || choise.equals("2"))){
            ConsoleHelper.printMessage("If you want to watch replay enter \'1\'");
            ConsoleHelper.printMessage("If you want to play TicTacToe enter \'2\'");
            choise = ConsoleHelper.readMessage();
        }

        switch (choise){
            case "1" -> XMLReader.playSavedGame();
            case "2" -> controller.mainLoop();
        }
    }
}
