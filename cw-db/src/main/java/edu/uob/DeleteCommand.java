package edu.uob;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DeleteCommand {
    //"DELETE FROM " [TableName] " WHERE " [Condition]
    Table table;
    public void executeDelete(String[] tokens)throws Exception{
        help(tokens);
        Map<String ,Integer> map=new HashMap<>();
        for(int i=0;i<table.columnNames.size();i++){
            map.put(table.columnNames.get(i).toLowerCase(),i);
        }
        ConditionExecute conditionExecute = new ConditionExecute();
        for(int i=0;i<table.rows.size();i++){
            if(table.rows.get(i).size()==1){
                continue;
            }
            boolean bool = conditionExecute.executeWithConditionHelp(tokens, i, 1,map,table);
            if(bool){
                for(int j=table.rows.get(i).size()-1;j>0;j--){
                    table.rows.get(i).remove(j);
                }
            }
        }
        Database.writeTablesToFile(tokens[2].toLowerCase());
        TopLevelParse.resustInfo="[OK]";
    }

    private void help(String[] tokens)throws Exception{
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
        String tablePath="databases"+File.separator+Database.currentDatabase+File.separator+tokens[2].toLowerCase();
        File ta = new File(tablePath);
        if(!ta.exists() || !ta.isFile()){
            TopLevelParse.resustInfo="[ERROR] Current table does not exists.";
            throw new Exception();
        }
        table=Database.tables.get(tokens[2].toLowerCase());
    }
}
