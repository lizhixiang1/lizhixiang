package edu.uob;

public class AlterParser extends GeneralParse{
    public AlterParser(String[] tokens)throws Exception{
        check(tokens);
        AlterCommand alterCommand = new AlterCommand();
        alterCommand.executeGeneral(tokens);
    }
    @Override
    public void check(String[] tokens) throws Exception {
        checkLength(tokens);
        if(!tokens[1].equalsIgnoreCase("TABLE")){
            TopLevelParse.resustInfo="[ERROR] Alter command fellow with \"TABLE\".";
            throw new Exception();
        }
        if(!TopLevelParse.tools.tableNameCheck(tokens[2])){
            TopLevelParse.resustInfo="[ERROR] Table name must be plain text.";
            throw new Exception();
        }
        if(!tokens[3].equalsIgnoreCase("ADD") && !tokens[3].equalsIgnoreCase("DROP")){
            TopLevelParse.resustInfo="[ERROR] Alteration type must be add|drop.";
            throw new Exception();
        }
        if(!TopLevelParse.tools.attributeNameCheck(tokens[4])) {
            TopLevelParse.resustInfo = "[ERROR] Attribute name must be plain text and cannot be kew words.";
            throw new Exception();
        }
    }

    @Override
    public void checkLength(String[] tokens) throws Exception {
        if(tokens.length!=6){
            TopLevelParse.resustInfo="[ERROR] Alter command has only 6 tokens";
            throw new Exception();
        }
    }
}
