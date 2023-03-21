package edu.uob;

public class CreateParser extends GeneralParse{
    public CreateParser(String[] tokens)throws Exception{
        check(tokens);
        CreateCommand createCommand = new CreateCommand();
        createCommand.executeGeneral(tokens);
    }


    @Override
    public void check(String[] tokens) throws Exception {
        checkLength(tokens);
        if(!tokens[1].equalsIgnoreCase("TABLE") && !tokens[1].equalsIgnoreCase("DATABASE")){
            TopLevelParse.resustInfo="[ERROR] Create command must follow table/database.";
            throw new Exception();
        }
        if(!TopLevelParse.tools.tableNameCheck(tokens[2])){
            TopLevelParse.resustInfo="[ERROR] table/database name must be plain text and cannot be key words.";
            throw new Exception();
        }
        if(tokens[3].equals(";") && tokens.length==4){
            return;
        }
        checkWithAttributes(tokens);
    }

    @Override
    public void checkLength(String[] tokens) throws Exception {
        if(tokens.length<4){
            TopLevelParse.resustInfo="[ERROR] Create command has as least 4 tokens.";
            throw new Exception();
        }
    }

    public void checkWithAttributes(String[] tokens)throws Exception{
        if(tokens.length<7){
            TopLevelParse.resustInfo="[ERROR] Something wrong with attribute list.";
            throw new Exception();
        }
        if(!tokens[3].equals("(") || !tokens[tokens.length-2].equals(")")){
            TopLevelParse.resustInfo="[ERROR] Attribute list must start with ( and end with ).";
            throw new Exception();
        }
        if(!TopLevelParse.tools.attributeNameCheck(tokens[4])){
            TopLevelParse.resustInfo="[ERROR] Attribute must be plain text and cannot be key words.";
            throw new Exception();
        }
        if((tokens.length-3-4)!=0 && (tokens.length-3-4)%2!=0){
            TopLevelParse.resustInfo="[ERROR] Something wrong with attribute list.";
            throw new Exception();
        }
        for(int i=5;i<tokens.length-2;i+=2){
            if(!tokens[i].equals(",")){
                TopLevelParse.resustInfo="[ERROR] Attribute must separate by \",\";";
                throw new Exception();
            }
            if(!TopLevelParse.tools.attributeNameCheck(tokens[i+1])){
                TopLevelParse.resustInfo="[ERROR] Attribute must be plain text and cannot be key words.";
                throw new Exception();
            }
        }
    }
}
