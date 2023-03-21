package edu.uob;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InsertCommand {
    public void executeInsertcommand(String[] tokens)throws Exception{
        help(tokens[2].toLowerCase());
        Table table=Database.tables.get(tokens[2].toLowerCase());
        int len=table.columnNames.size()-1;
        if((tokens.length-6)/2<len){
            TopLevelParse.resustInfo="[ERROR] Values number is too less.";
            throw new Exception();
        }else if((tokens.length-6)/2>len){
            TopLevelParse.resustInfo="[ERROR] Values number is too many.";
            throw new Exception();
        }
        List<String> temp=new ArrayList<>();
        temp.add(String.valueOf(table.rows.size()+1));
        for(int i=5;i<tokens.length-2;i+=2){
            if(TopLevelParse.tools.integerLiteralCheck(tokens[i]) ||
                    TopLevelParse.tools.floatLiteralCheck(tokens[i])){
                if(tokens[i].charAt(0)=='+'){
                    temp.add(tokens[i].substring(1));
                }else{
                    temp.add(tokens[i]);
                }
            }else{
                temp.add(tokens[i]);
            }
        }
        table.rows.add(temp);
        Database.writeTablesToFile(tokens[2].toLowerCase());
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
