package edu.uob;

import java.util.HashMap;
import java.util.Map;

public class GameAction {
    boolean isPreDefined=false;
    String trigger=null;
    String narration=null;
    String basicCommandInfo=null;
    Map<String,Integer> subjects=new HashMap<>();
    Map<String,Integer> consumed=new HashMap<>();
    Map<String,Integer> produced=new HashMap<>();
}
