package edu.uob;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UpdateCommand {
    //"UPDATE " [TableName] " SET " <NameValueList> " WHERE " <Condition>
    Table table=null;
    Map<Integer,String> newVal=new HashMap<>();
    Map<String,Integer> map=new HashMap<>();
    public void executeUpdate(String[] tokens)throws Exception{
        help(tokens[1]);
        int index=-1;
        for(int i=3;i<tokens.length;i++){
            if(tokens[i].equalsIgnoreCase("where")){
                index=i;
                break;
            }
        }
        preProcessing(tokens,index);
        ConditionExecute conditionExecute = new ConditionExecute();
        for(int i=0;i<table.rows.size();i++){
            if(table.rows.get(i).size()==1){
                continue;
            }
            boolean bool = conditionExecute.executeWithConditionHelp(tokens, i, index-2,map,table);
            if(bool){
                for(Map.Entry<Integer,String> entry:newVal.entrySet()){
                    Integer key = entry.getKey();
                    String value = entry.getValue();
                    table.rows.get(i).set(key,value);
                }
            }
        }
        Database.writeTablesToFile(tokens[1].toLowerCase());
        TopLevelParse.resustInfo="[OK]";


    }

    private void help(String tableName)throws Exception{
        if(Database.currentDatabase==null){
            TopLevelParse.resustInfo="[ERROR] No database choose.";
            throw new Exception();
        }
        String directoryPath = "databases"+File.separator+Database.currentDatabase;
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            TopLevelParse.resustInfo="[ERROR] Current database does not exists.";
            throw new Exception();
        }
        String tablePath="databases"+File.separator+Database.currentDatabase+File.separator+tableName.toLowerCase();
        File tb = new File(tablePath);
        if(!tb.exists() || !tb.isFile()){
            TopLevelParse.resustInfo="[ERROR] Current table does not exists.";
            throw new Exception();
        }
        table=Database.tables.get(tableName.toLowerCase());
    }

    public void preProcessing(String[] tokens,int index)throws Exception{
        for(int i=0;i<table.columnNames.size();i++){
            map.put(table.columnNames.get(i).toLowerCase(),i);
        }
        for(int i=3;i<index;i+=4){
            Integer tempIndex = map.getOrDefault(tokens[i], -1);
            if(tempIndex==-1){
                TopLevelParse.resustInfo="[ERROR] No such attribute in this table.";
                throw new Exception();
            }
            if(tempIndex==0){
                TopLevelParse.resustInfo="[ERROR] Cannot update id.";
                throw new Exception();
            }
            newVal.put(tempIndex,tokens[i+2]);
        }
    }
}
