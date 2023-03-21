package edu.uob;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
public class CreateCommand {

    public void executeGeneral(String[] tokens)throws Exception{
        if(tokens[1].equalsIgnoreCase("DATABASE")){
            if(tokens.length!=4){
                TopLevelParse.resustInfo="[ERROR] Too many tokens.";
                throw new Exception();
            }
            executeCreateDatabase(tokens[2]);
        }else if(tokens[1].equalsIgnoreCase("TABLE")){
            if(tokens.length==4){
                executeCreateTable1(tokens[2]);
            }else if(tokens.length>4){
                executeCreateTable2(tokens);
            }
        }
    }

    public void executeCreateDatabase(String databaseName)throws Exception{
        String directoryPath = "databases"+File.separator+databaseName;
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            TopLevelParse.resustInfo="[ERROR] Database exists.";
            throw new Exception();
        }
        if(directory.mkdir()){
            TopLevelParse.resustInfo="[OK]";
        }else{
            TopLevelParse.resustInfo="[ERROR] Cannot make database.";
            throw new Exception();
        }
    }

    //create table without attributes
    public void executeCreateTable1(String tableName)throws Exception{
        executeCreateTableHelp(tableName.toLowerCase());
        Table table1 = new Table(tableName.toLowerCase());
        table1.columnNames.add("id");
        Database.tables.put(tableName.toLowerCase(),table1);
        String tablePath="databases"+File.separator+Database.currentDatabase+File.separator+tableName.toLowerCase();
        BufferedWriter writer = new BufferedWriter(new FileWriter(tablePath));
        writer.write("id");
        writer.close();

    }

    public void executeCreateTableHelp(String tableName)throws Exception{
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
        if(table.createNewFile()){
            TopLevelParse.resustInfo="[OK]";
        }else{
            TopLevelParse.resustInfo="[ERROR] Table exists.";
            throw new Exception();
        }
    }
    //create table with attribute
    public void executeCreateTable2(String[] tokens)throws Exception{
        HashMap<String,Integer> map=new HashMap<>();
        Table table = new Table(tokens[2].toLowerCase());
        table.columnNames.add("id");
        map.put("id",1);
        if(map.containsKey(tokens[4].toLowerCase())){
            TopLevelParse.resustInfo="[ERROR] Same Attribute it not allowed";
            throw new Exception();
        }
        table.columnNames.add(tokens[4]);
        map.put(tokens[4].toLowerCase(),1);
        for(int i=5;i<tokens.length-2;i+=2){
            if(map.containsKey(tokens[i+1].toLowerCase())){
                TopLevelParse.resustInfo="[ERROR] Same Attribute it not allowed";
                throw new Exception();
            }
            table.columnNames.add(tokens[i+1]);
            map.put(tokens[i+1].toLowerCase(),1);
        }
        executeCreateTableHelp(tokens[2].toLowerCase());
        Database.tables.put(tokens[2].toLowerCase(),table);
        Database.writeTablesToFile(tokens[2].toLowerCase());
    }
}
