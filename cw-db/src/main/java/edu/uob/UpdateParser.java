package edu.uob;

public class UpdateParser{
    public UpdateParser(String[] tokens)throws Exception{
        check(tokens);
        UpdateCommand updateCommand = new UpdateCommand();
        updateCommand.executeUpdate(tokens);
    }
    public void check(String[] tokens) throws Exception {
        if(tokens.length<11){
            TopLevelParse.resustInfo="[ERROR] Update command has at least 11 tokens";
            throw new Exception();
        }
        if(!TopLevelParse.tools.tableNameCheck(tokens[1])){
            TopLevelParse.resustInfo="[ERROR] Table name must be plain test and cannot be key words.";
            throw new Exception();
        }
        if(!tokens[2].equalsIgnoreCase("SET")){
            TopLevelParse.resustInfo="[ERROR] Table name must followed by set.";
            throw new Exception();
        }
        int index=-1;
        for(int i=3;i<tokens.length;i++){
            if(tokens[i].equalsIgnoreCase("where")){
                index=i;
                break;
            }
        }
        if(((index-3)%4)!=3){
            TopLevelParse.resustInfo="[ERROR] NameValueList wrong.";
            throw new Exception();
        }
        for(int i=3;i<index;i+=4){
            if(!TopLevelParse.tools.attributeNameCheck(tokens[i])){
                TopLevelParse.resustInfo="[ERROR] Attribute name false.";
                throw new Exception();
            }
            if(!tokens[i+1].equals("=")){
                TopLevelParse.resustInfo="[ERROR] Attribute value and attribute name must be separate by '='.";
                throw new Exception();
            }
            if(!TopLevelParse.tools.valueCheck(tokens[i+2])){
                TopLevelParse.resustInfo="[ERROR] Attribute value false.";
                throw new Exception();
            }
            if(i+4<index && !tokens[i+3].equals(",")){
                TopLevelParse.resustInfo="[ERROR] Attribute must be separate by ','.";
                throw new Exception();
            }
        }
        if(!TopLevelParse.tools.condition(tokens,index+1)){
            TopLevelParse.resustInfo="[ERROR] Condition wrong.";
            throw new Exception();
        }
    }
}
