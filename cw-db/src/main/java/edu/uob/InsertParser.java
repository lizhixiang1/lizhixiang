package edu.uob;

public class InsertParser extends GeneralParse{
    public  InsertParser(String[] tokens)throws Exception{
        check(tokens);
        InsertCommand insertCommand = new InsertCommand();
        insertCommand.executeInsertcommand(tokens);
    }
    @Override
    public void check(String[] tokens) throws Exception {
        checkLength(tokens);
        if(!tokens[4].equalsIgnoreCase("(")){
            TopLevelParse.resustInfo="[ERROR] Value list must start with (.";
            throw new Exception();
        }
        if(!TopLevelParse.tools.valueCheck(tokens[5])){
            TopLevelParse.resustInfo="[ERROR] Value type wrong.";
            throw new Exception();
        }
        for(int i=6;i<tokens.length-2;i+=2){
            if(!tokens[i].equals(",")){
                TopLevelParse.resustInfo="[ERROR] Value must be separate by \",\".";
                throw new Exception();
            }
            if(!TopLevelParse.tools.valueCheck(tokens[i+1])){
                TopLevelParse.resustInfo="[ERROR] Value type wrong.";
                throw new Exception();
            }
        }
        if(!tokens[tokens.length-2].equalsIgnoreCase(")")){
            TopLevelParse.resustInfo="[ERROR] Value list must start with ).";
            throw new Exception();
        }
    }

    @Override
    public void checkLength(String[] tokens) throws Exception {
        if(tokens.length<=7){
            TopLevelParse.resustInfo="[ERROR] Insert command at least has 8 tokens";
            throw new Exception();
        }
        if(tokens.length%2!=0 ){
            TopLevelParse.resustInfo="[ERROR] Insert command tokens number is even.";
            throw new Exception();
        }
        if(!tokens[1].equalsIgnoreCase("INTO")){
            TopLevelParse.resustInfo="[ERROR] Insert command follow by \"INTO\".";
            throw new Exception();
        }
        if(!TopLevelParse.tools.tableNameCheck(tokens[2])){
            TopLevelParse.resustInfo="[ERROR] Table Name must be plain text.";
            throw new Exception();
        }
        if(!tokens[3].equalsIgnoreCase("VALUES")){
            TopLevelParse.resustInfo="[ERROR] Table name follow by \"VALUES\".";
            throw new Exception();
        }
    }
}
