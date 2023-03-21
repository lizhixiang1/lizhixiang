package edu.uob;

public class SelectParser extends GeneralParse{
    public SelectParser(String[] tokens)throws Exception{
        check(tokens);
        SelectCommand selectCommand = new SelectCommand();
        selectCommand.executeSelectGeneral(tokens);
    }
    @Override
    public void check(String[] tokens) throws Exception {
        checkLength(tokens);
    }

    public void checkLength(String[] tokens) throws Exception {
        if(tokens.length<5){
            TopLevelParse.resustInfo="[ERROR] Select command has at least 5 tokens";
            throw new Exception();
        }
        int index=-1;
        for(int i=1;i<tokens.length;i++){
            if(tokens[i].equalsIgnoreCase("FROM")){
                index=i;
                break;
            }
        }
        if(index==-1){
            TopLevelParse.resustInfo="[ERROR] Select command must has FROM key word.";
            throw new Exception();
        }
        if((index-1)%2!=1){
            TopLevelParse.resustInfo="[ERROR] Wild attribute list wrong.";
            throw new Exception();
        }
        if(!TopLevelParse.tools.isWildAttributeList(tokens,1,index)){
            TopLevelParse.resustInfo="[ERROR] Wild attribute list wrong.";
            throw new Exception();
        }
        if(index+1<tokens.length-1 && !TopLevelParse.tools.tableNameCheck(tokens[index+1])){
            TopLevelParse.resustInfo="[ERROR] Table name must be plain test and cannot be key words.";
            throw new Exception();
        }
        if(index+2==tokens.length-1 && tokens[index+2].equals(";")){
            return;
        }
        checkHelp(tokens,index);
    }

    public void checkHelp(String[] tokens,int index)throws Exception{
        if(index+2<tokens.length-1 && !tokens[index+2].equalsIgnoreCase("WHERE")){
            TopLevelParse.resustInfo="[ERROR] Table name must follow by key word where.";
            throw new Exception();
        }
        if(!TopLevelParse.tools.condition(tokens,index+3)){
            TopLevelParse.resustInfo="[ERROR] Condition wrong.";
            throw new Exception();
        }
    }
}
