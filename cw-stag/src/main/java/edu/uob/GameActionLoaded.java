package edu.uob;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

public class GameActionLoaded {

    public void load(File actionFile){
        parserBasic();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(actionFile);
            Element root = document.getDocumentElement();
            NodeList actions = root.getChildNodes();
            for(int i=1;i<actions.getLength();i+=2){
                Element element = (Element)actions.item(i);
                Element triggers=(Element)element.getElementsByTagName("triggers").item(0);
                NodeList keyphrases = triggers.getElementsByTagName("keyphrase");
                for(int j=0;j<keyphrases.getLength();j++){
                    GameAction gameAction = new GameAction();
                    help(gameAction,keyphrases,j,element);
                    Element narration = (Element)element.getElementsByTagName("narration").item(0);
                    gameAction.narration=narration.getTextContent();
                    if(DispatchCenter.actions.getOrDefault(gameAction.trigger,null)==null){
                        ArrayList<GameAction> gameActionList = new ArrayList<>();
                        gameActionList.add(gameAction);
                        DispatchCenter.actions.put(gameAction.trigger,gameActionList);
                    }else{
                        ArrayList<GameAction> gameActionList = DispatchCenter.actions.get(gameAction.trigger);
                        gameActionList.add(gameAction);
                    }
                }
            }
        } catch(Exception pce) {
        }
    }

    public void help(GameAction gameAction,NodeList keyphrases,int j,Element element){
        gameAction.trigger=keyphrases.item(j).getTextContent();
        Element subjects = (Element)element.getElementsByTagName("subjects").item(0);
        NodeList entites = subjects.getElementsByTagName("entity");
        for(int k=0;k<entites.getLength();k++){
            gameAction.subjects.put(entites.item(k).getTextContent(),1);
        }

        Element consumed = (Element)element.getElementsByTagName("consumed").item(0);
        entites = consumed.getElementsByTagName("entity");
        for(int k=0;k<entites.getLength();k++){
            gameAction.consumed.put(entites.item(k).getTextContent(),1);
        }

        Element produced = (Element)element.getElementsByTagName("produced").item(0);
        entites = produced.getElementsByTagName("entity");
        for(int k=0;k<entites.getLength();k++){
            gameAction.produced.put(entites.item(k).getTextContent(),1);
        }

    }


    private void parserBasic(){
        String[] kewWords=new String[]{"inventory","get","drop","goto","look","inv"};
        String[] descriptions=new String[]{"lists all of the artefacts currently being carried by the player",
        "picks up a specified artefact from the current location and adds it into player's inventory",
        "puts down an artefact from player's inventory and places it into the current location",
        "moves the player to the specified location (if there is a path to that location)",
        "prints names and descriptions of entities in the current location and lists paths to other locations",
                "lists all of the artefacts currently being carried by the player"};
        for(int i=0;i<kewWords.length;i++){
            GameAction action = new GameAction();
            action.trigger=kewWords[i];
            action.narration=descriptions[i];
            action.isPreDefined=true;
            ArrayList<GameAction> gameActions = new ArrayList<>();
            gameActions.add(action);
            DispatchCenter.actions.put(action.trigger,gameActions);
        }

    }


}
