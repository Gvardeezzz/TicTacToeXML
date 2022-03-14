package XML;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.util.ArrayList;

public class XMLReader {
    private static String[][] gameField = {{"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", "9"}};
    private static ArrayList<Step> steps = new ArrayList<>();
    private static ArrayList<Player> players = new ArrayList<>();
    private static String win = "Draw";

    public static int[] inputDataAdapter(String step) {
        int [] coordinates = new int[2];
        int len = step.length();
        switch (len) {
            case 1 -> {
                int num = Integer.parseInt(step) - 1;
                coordinates[0] = num / 3;
                coordinates[1] = num % 3;
            }
            case 2 ->{
                char[] s = step.toCharArray();
                coordinates[0] = Integer.parseInt(String.valueOf(s[0]));
                coordinates[1] = Integer.parseInt(String.valueOf(s[1]));
            }

            case 3 -> {
                String []s = step.split(" ");
                coordinates[0] = Integer.parseInt(s[0]);
                coordinates[1] = Integer.parseInt(s[1]);
            }
        }
        return coordinates;
    }

    public static void readXML(String fileName){
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try{
            XMLEventReader reader = factory.createXMLEventReader(new FileInputStream(fileName));
            while (reader.hasNext()){
                Player player = new Player();
                Step step = new Step();
                XMLEvent event = reader.nextEvent();
                if(event.isStartElement()){
                    StartElement startElement = event.asStartElement();
                    if(startElement.getName().getLocalPart().equals("Player")){
                        Attribute attribute = startElement.getAttributeByName(new QName("id"));
                        player.setId(Integer.parseInt(attribute.getValue()));
                        attribute = startElement.getAttributeByName(new QName("name"));
                        player.setName(attribute.getValue());
                        attribute = startElement.getAttributeByName(new QName("symbol"));
                        player.setSymbol(attribute.getValue());
                        players.add(player);
                    }
                    else if(startElement.getName().getLocalPart().equals("Step")){
                        event = reader.nextEvent();
                        String stringStep = event.asCharacters().getData();
                        int [] mas = inputDataAdapter(stringStep);
                        step.setX(mas[0]);
                        step.setY(mas[1]);
                        steps.add(step);
                    }

                  /* else if(startElement.getName().getLocalPart().equals("GameResult")){
                        event = reader.nextEvent();
                        win = event.asCharacters().getData();
                    }*/
                }

                if(event.isEndElement()){

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playSavedGame() throws InterruptedException {
        readXML("C:\\Users\\Igor Ivanovich\\Downloads\\savedgame-1.xml");
        int stepCount = 1;
        for (int i = 0; i < steps.size(); i++) {
            if(stepCount%2 == 1){
                gameField[steps.get(i).getX()][steps.get(i).getY()] = "X";
            }
            else  gameField[steps.get(i).getX()][steps.get(i).getY()] = "0";
            print(gameField);
            System.out.println();
            stepCount++;
        }
        if(players.size() == 3){
            Player winner = players.get(2);
            System.out.printf("Player %d -> %s is winner as \'%s\'!", winner.getId(),winner.getName(),winner.getSymbol());
        }
        else System.out.println(win);
    }

    public static void print(String gamefield[][]) throws InterruptedException {
        StringBuilder resultString;
        for (int i = 0; i < 3; i++) {
            resultString = new StringBuilder("|");
            for (int j = 0; j < 3; j++) {
                resultString.append(gamefield[i][j] + "|");
            }
            System.out.println(resultString);
        }
        Thread.sleep(1000);
    }
}
