package edu.uob;

public class DeleteParser{

    public  DeleteParser(String[] tokens)throws Exception{
        check(tokens);
        DeleteCommand deleteCommand = new DeleteCommand();
        deleteCommand.executeDelete(tokens);
    }
    public void check(String[] tokens) throws Exception {
        if(tokens.length<8){
            TopLevelParse.resustInfo="[ERROR] Delete command as least has 8 tokens.";
            throw new Exception();
        }
        if(!tokens[1].equalsIgnoreCase("FROM")){
            TopLevelParse.resustInfo="[ERROR] Delete command followed by key word from.";
            throw new Exception();
        }
        if(!TopLevelParse.tools.databaseNameCheck(tokens[2])){
            TopLevelParse.resustInfo="[ERROR] From key word followed by a valid table name.";
            throw new Exception();
        }
        if(!tokens[3].equalsIgnoreCase("WHERE")){
            TopLevelParse.resustInfo="[ERROR] Table name followed by key word from.";
            throw new Exception();
        }
        if(!TopLevelParse.tools.condition(tokens,4)){
            TopLevelParse.resustInfo="[ERROR] Condition wrong.";
            throw new Exception();
        }
    }
}
