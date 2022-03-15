import XML.XMLWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class Controller {
    private final Model model;
    private final View view;

    String playerOneName;
    String playerTwoName;
    PlayerMark playerMark1;
    PlayerMark playerMark2;

    int gameCount =1;


    public Controller(Model model, View view){
        this.model = model;
        this.view = view;
    }
    public void mainLoop(){

        Document document = XMLWriter.createDocument();
        Element gamePlay = document.createElement("Gameplay");
        document.appendChild(gamePlay);

        ConsoleHelper.printMessage("Enter 1st player name");
        playerOneName = model.createPlayer();
        Element playerOne = XMLWriter.createPlayer(document,1,playerOneName,"X");

        ConsoleHelper.printMessage("Enter 2nd player name");
        playerTwoName = model.createPlayer();
        Element playerTwo = XMLWriter.createPlayer(document,2,playerTwoName,"0");

        gamePlay.appendChild(playerOne);
        gamePlay.appendChild(playerTwo);

        Element game = document.createElement("Game");
        gamePlay.appendChild(game);

        model.init();

        playerMark1 = PlayerMark.CROSS;
        playerMark2 = PlayerMark.ZERO;

        String currentPlayer = playerOneName;
        PlayerMark currentMark = playerMark1;

        boolean isWinnerFound = false;
        int stepNumber =1;

        while (model.hasMotion(model.getGameField())){

            model.putMark(model.getGameField(), currentMark);
            Element step = XMLWriter.makeStep(document,stepNumber,model.PlayerId,model.Xcoordinate,model.Ycoordinate);
            game.appendChild(step);
            stepNumber++;
            view.refresh(model.getGameField());

            if(model.isWin(model.getGameField(), currentMark)){
                Element winner;
                ConsoleHelper.printMessage(String.format("Winner is %s!",currentPlayer));
                int ID = currentMark == PlayerMark.CROSS ? 1 : 2;
                if(ID == 1)  winner = XMLWriter.createPlayer(document,1,playerOneName,"X");
                else winner = XMLWriter.createPlayer(document,2,playerTwoName,"0");

                Element gameResult =  XMLWriter.makeGameResult(document,winner);
                gamePlay.appendChild(gameResult);

                Integer []s = model.getGameStat().get(currentPlayer);
                s[0]++;
                model.getGameStat().put(currentPlayer,s);

                s = model.getGameStat().get(changeName(currentPlayer));
                s[2]++;
                model.getGameStat().put(changeName(currentPlayer),s);
                isWinnerFound = true;
                break;
            }
            currentMark = changeMark(currentMark);
            currentPlayer = changeName(currentPlayer);
        }
        if (!isWinnerFound){
            ConsoleHelper.printMessage("Draw");
            gamePlay.appendChild(XMLWriter.makeGameResult(document));
            Integer []s = model.getGameStat().get(playerOneName);
            s[1]++;
            model.getGameStat().put(playerOneName,s);
            s = model.getGameStat().get(playerTwoName);
            s[1]++;
            model.getGameStat().put(playerTwoName,s);
        }
        model.sendStatisticsToFile(model.makeOutputData(model.getGameStat()));
        String fileName = String.format("%s_vs_%s_%s.xml", playerOneName, playerTwoName, new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
        XMLWriter.saveAsFile(document,fileName);
        gameCount++;
        ConsoleHelper.printMessage("Do you want to play again?");
        ConsoleHelper.printMessage("If no, enter word \"exit\". For continue playing enter any symbol");
        String wantToPlay = ConsoleHelper.readMessage();
        if(!wantToPlay.equalsIgnoreCase("exit")) mainLoop();
    }

    public String changeName(String name){
        if(name.equals(playerOneName)) return playerTwoName;
        return playerOneName;
    }

    public PlayerMark changeMark(PlayerMark mark){
        if(mark == playerMark1) return playerMark2;
        return playerMark1;
    }
}