package edu.uob;
import java.util.ArrayList;
import java.util.Map;

public class ActionParser {
    public GameAction parserAction(String[] commandTokens,String command){
        ArrayList<GameAction> action;
        boolean flag;
        try{
            action=parseActionTrigger(commandTokens,command);
            if(DispatchCenter.basicCommand.containsKey(action.get(0).trigger)){
                return checkBasicCommand(action.get(0),commandTokens)?action.get(0):null;
            }else{
                for (GameAction gameAction : action) {
                    flag = parseActionSubjects(commandTokens, gameAction);
                    if (flag) {
                        return gameAction;
                    }
                }
            }
        }catch(Exception e){
            return null;
        }
        return null;
    }


    public ArrayList<GameAction> parseActionTrigger(String[] commandTokens,String command)throws Exception{
        Map<String, ArrayList<GameAction>> actions = DispatchCenter.actions;
        ArrayList<GameAction> actionEntity=null;
        //check for single work trigger
        for (String commandToken : commandTokens) {
            if (actionEntity != null && actions.containsKey(commandToken)) {
                GameAction actionTemp1 = actions.get(commandToken).get(0);
                //no more than one pre defined trigger
                GameAction actionTemp2 = actionEntity.get(0);
                if (actionTemp1.narration.equals(actionTemp2.narration) && actionTemp2.isPreDefined) {
                    throw new Exception("No more than two pre defined trigger");
                }
                //no more two different meaning triggers
                if (!actionTemp2.narration.equals(actionTemp1.narration)) {
                    throw new Exception("No more than two different meaning triggers");
                }
            }

            if (actionEntity == null && actions.containsKey(commandToken)) {
                actionEntity = actions.get(commandToken);
            }
        }
        //check for trigger that more than one word!
        for(Map.Entry<String,ArrayList<GameAction>> action:actions.entrySet()){
            String key = action.getKey();
            GameAction value = action.getValue().get(0);
            if(!key.contains(" ")){
                continue;
            }
            if(match(command,key)==-1){
                continue;
            }
            //字符串匹配算法如果成功
            if(actionEntity!=null){
                if(!actionEntity.get(0).narration.equals(value.narration)){
                    throw new Exception("No more than two different meaning triggers");
                }
            }
            if(actionEntity==null){
                actionEntity=action.getValue();
            }
        }
        if(actionEntity==null){
            throw new Exception("No trigger match");
        }
        return actionEntity;
    }

    public boolean parseActionSubjects(String[] commandTokens,GameAction action){
        String narration = action.narration;
        Map<String, Integer> subjects = action.subjects;
        Map<String, ArrayList<GameAction>> actions = DispatchCenter.actions;
        for (String commandToken : commandTokens) {
            for (Map.Entry<String, ArrayList<GameAction>> actionTemp1 : actions.entrySet()) {
                ArrayList<GameAction> value = actionTemp1.getValue();
                for (GameAction gameAction : value) {
                    if (!gameAction.narration.equals(narration)) {
                        if (!action.subjects.containsKey(commandToken) && gameAction.subjects.containsKey(commandToken)) {
                            return false;
                        }
                    }
                }
            }
        }
        boolean flag=false;
        for (String commandToken : commandTokens) {
            for (Map.Entry<String, Integer> subject : subjects.entrySet()) {
                if (subject.getKey().equals(commandToken)) {
                    flag = true;
                    break;
                }
            }
        }
        return action.isPreDefined || flag;
    }


    public int match(String command, String trigger) {
        for(int i=0;i<=command.length()-trigger.length();i++){
            boolean flag=true;
            for(int j=0;j<trigger.length();j++){
                if(command.charAt(i+j)!=trigger.charAt(j)){
                    flag=false;
                    break;
                }
            }
            if(flag){
                if(i+trigger.length()==command.length()){
                    return i;
                }
                if(command.charAt(i+trigger.length())==' '){
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean checkBasicCommand(GameAction action,String[] commandTokens){
        if(action.trigger.equals("inv")||action.trigger.equals("inventory")||action.trigger.equals("look")){
            return commandTokens.length==1;
        }
        if(!commandTokens[0].equals("get")&&!commandTokens[0].equals("drop")&&!commandTokens[0].equals("goto")){
            return false;
        }
        if(commandTokens.length!=2){
            return false;
        }
        action.basicCommandInfo=commandTokens[1];
        return true;

    }
}
