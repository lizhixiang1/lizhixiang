package edu.uob;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DispatchCenter {
    static Map<String,PlayerEntity> players;

    static Map<String,LocationEntity> locations;

    static Map<String,FurnitureEntity> furniture;
    static Map<String,ArtefactEntity> artefacts;
    static Map<String,CharacterEntity> characters;

    static Map<String,Integer> basicCommand;

    static Map<String, ArrayList<GameAction>> actions;

    static LocationEntity bornLocation;

    public ActionParser actionParser;

    DispatchCenter(File entityFile, File actionFile){
        actionParser=new ActionParser();
        players=new HashMap<>();
        locations=new HashMap<>();
        furniture=new HashMap<>();
        artefacts=new HashMap<>();
        characters=new HashMap<>();
        actions=new HashMap<>();
        basicCommand=new HashMap<>();
        bornLocation=null;
        initialBasicCommand();
        GameActionLoaded gameActionParser = new GameActionLoaded();
        GameEntityLoad gameEntityParser = new GameEntityLoad();
        gameEntityParser.load(entityFile);
        gameActionParser.load(actionFile);
    }

    private void initialBasicCommand(){
        new HashMap<>();
        basicCommand.put("get",1);
        basicCommand.put("drop",1);
        basicCommand.put("goto",1);
        basicCommand.put("look",1);
        basicCommand.put("inv",1);
        basicCommand.put("inventory",1);
    }


}
