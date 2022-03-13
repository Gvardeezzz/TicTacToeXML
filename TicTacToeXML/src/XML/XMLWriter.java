package XML;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;

public class XMLWriter {
    public static Document createDocument () {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = builder.newDocument();
        return document;
    }

    public static Element createPlayer(Document doc, int id, String name, String symbol) {
        Element player = doc.createElement("Player");
        player.setAttribute("id", String.valueOf(id));
        player.setAttribute("name", name);
        player.setAttribute("symbol", symbol);
        return player;
    }

    public static Element makeStep(Document doc, int num, int playerId, int x, int y){
        Element step = doc.createElement("Step");
        step.setAttribute("num", String.valueOf(num));
        step.setAttribute("playerId", String.valueOf(playerId));
        String cell = x + " " + y;
        step.setTextContent(cell);
        return step;
    }

    public static void saveAsFile(Document document, String fileName){
        try{
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            FileOutputStream fos = new FileOutputStream(fileName);
            StreamResult result = new StreamResult(fos);
            tr.transform(source,result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Element makeGameResult(Document doc){
        Element gameResult = doc.createElement("GameResult");
        gameResult.setTextContent("Draw!");
        return gameResult;
    }

    public static Element makeGameResult(Document doc, Element winner){
        Element gameResult = doc.createElement("GameResult");
        gameResult.appendChild(winner);
        return gameResult;
    }
}
