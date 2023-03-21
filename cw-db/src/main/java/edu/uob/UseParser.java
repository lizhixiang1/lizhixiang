package edu.uob;

public class UseParser extends GeneralParse{
    public UseParser(String[] tokens)throws Exception{
        check(tokens);
        UseCommand useCommand = new UseCommand();
        useCommand.useDatabase(tokens[1]);
    }

    public void check(String[] tokens)throws Exception{
        checkLength(tokens);
        if(!TopLevelParse.tools.databaseNameCheck(tokens[1])){
            TopLevelParse.resustInfo="[ERROR] Database name must be plain text.";
            throw new Exception();
        }

    }

    public void checkLength(String[] tokens)throws Exception{
        if(tokens.length!=3){
            TopLevelParse.resustInfo="[ERROR] Use command only consists three tokens";
            throw new Exception();
        }
    }
}
