package edu.uob;
public class JoinParser extends GeneralParse{
    public JoinParser(String[] tokens)throws Exception{
        checkLength(tokens);
        JoinCommand joinCommand = new JoinCommand();
        joinCommand.executeJoin(tokens);
    }
    @Override
    public void check(String[] tokens) throws Exception {
        checkLength(tokens);
    }

    @Override
    public void checkLength(String[] tokens) throws Exception {
        if(tokens.length!=9){
            TopLevelParse.resustInfo="[ERROR] Join command has 9 tokens";
            throw new Exception();
        }
        if(!TopLevelParse.tools.databaseNameCheck(tokens[1])){
            TopLevelParse.resustInfo="[ERROR] Please input valid table name";
            throw new Exception();
        }
        if(!tokens[2].equalsIgnoreCase("AND")){
            TopLevelParse.resustInfo="[ERROR] And should lay between tow table name.";
            throw new Exception();
        }
        if(!TopLevelParse.tools.databaseNameCheck(tokens[3])){
            TopLevelParse.resustInfo="[ERROR] Please input valid table name";
            throw new Exception();
        }
        if(!tokens[4].equalsIgnoreCase("ON")){
            TopLevelParse.resustInfo="[ERROR] On should appear in the table name.";
            throw new Exception();
        }
        if(!TopLevelParse.tools.attributeNameCheck(tokens[5])){
            TopLevelParse.resustInfo="[ERROR] Please input valid attribute name";
            throw new Exception();
        }
        if(!tokens[2].equalsIgnoreCase("AND")){
            TopLevelParse.resustInfo="[ERROR] And should lay between tow attribute name.";
            throw new Exception();
        }
        if(!TopLevelParse.tools.attributeNameCheck(tokens[7])){
            TopLevelParse.resustInfo="[ERROR] Please input valid attribute name";
            throw new Exception();
        }
    }
}
