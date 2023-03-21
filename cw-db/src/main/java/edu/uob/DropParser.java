package edu.uob;

public class DropParser extends GeneralParse{
    public DropParser(String[] tokens)throws Exception{
        check(tokens);
        DropCommand dropCommand = new DropCommand();
        dropCommand.executeGeneral(tokens);
    }
    @Override
    public void check(String[] tokens) throws Exception {
        checkLength(tokens);
        if(!tokens[1].equalsIgnoreCase("TABLE") && !tokens[1].equalsIgnoreCase("DATABASE")){
            TopLevelParse.resustInfo="[ERROR} Drop command follow table/database.";
            throw new Exception();
        }
        if(!TopLevelParse.tools.plainTextCheck(tokens[2])){
            TopLevelParse.resustInfo="[ERROR] Table/Database name must be plain text.";
            throw new Exception();
        }
    }

    @Override
    public void checkLength(String[] tokens) throws Exception {
        if(tokens.length!=4){
            TopLevelParse.resustInfo="[ERROR} Drop command only has four tokens.";
            throw new Exception();
        }
    }
}
