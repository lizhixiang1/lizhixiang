package edu.uob;
import java.io.*;

import java.util.List;

public class AlterCommand {
    public void executeGeneral(String[] tokens)throws Exception{
        if(tokens[3].equalsIgnoreCase("ADD")){
            executeAddCommand(tokens);
        }else if(tokens[3].equalsIgnoreCase("DROP")){
            executeDropCommand(tokens);
        }
    }

    public void executeAddCommand(String[] tokens)throws Exception{
        help(tokens[2].toLowerCase());
        Table table=Database.tables.get(tokens[2].toLowerCase());
        for(String str:table.columnNames){
            if(str.equalsIgnoreCase(tokens[4])){
                TopLevelParse.resustInfo="[ERROR] Cannot add exist attribute";
                throw new Exception();
            }
            TopLevelParse.tools.isKeyWords(tokens[4].toUpperCase());
        }
        table.columnNames.add(tokens[4]);
        for(List<String> row:table.rows){
            row.add("NULL");
        }
        Database.writeTablesToFile(tokens[2].toLowerCase());
        TopLevelParse.resustInfo="[OK]";
    }

    public void executeDropCommand(String[] tokens)throws Exception{
        help(tokens[2]);
        if(tokens[4].equalsIgnoreCase("id")){
            TopLevelParse.resustInfo="[ERROR] Can not delete id.";
            throw new Exception();
        }
        int index=-1;
        Table table=Database.tables.get(tokens[2].toLowerCase());
        for(int i=1;i<table.columnNames.size();i++){
            if(table.columnNames.get(i).equalsIgnoreCase(tokens[4])){
                index=i;
            }
        }
        if(index==-1){
            TopLevelParse.resustInfo="[ERROR] Attribute does not exist.";
            throw new Exception();
        }

        table.columnNames.remove(index);
        for(List<String> row:table.rows){
            row.remove(index);
        }
        Database.writeTablesToFile(table.tableName.toLowerCase());
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
        File table = new File(tablePath);
        if(!table.exists() || !table.isFile()){
            TopLevelParse.resustInfo="[ERROR] Current table does not exists.";
            throw new Exception();
        }
    }

}





