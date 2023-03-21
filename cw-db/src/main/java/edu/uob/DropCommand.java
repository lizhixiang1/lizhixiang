package edu.uob;

import java.io.File;
import java.util.Objects;

public class DropCommand {
    public void executeGeneral(String[] tokens)throws Exception{
        if(tokens[1].equalsIgnoreCase("TABLE")){
            executeDropTable(tokens[2]);
        }else if(tokens[1].equalsIgnoreCase("DATABASE")){
            executeDropDatabase(tokens[2]);
        }
    }

    public void executeDropDatabase(String databaseName)throws Exception{
        String directoryPath = "databases"+File.separator+databaseName;
        File directory = new File(directoryPath);
        if(!directory.exists() || !directory.isDirectory()){
            TopLevelParse.resustInfo="[ERROR] Database does not exist.";
            throw new Exception();
        }
        for(File file: directory.listFiles()){
            file.delete();
        }
        if (directory.delete()) {
            TopLevelParse.resustInfo="[OK]";
            if(Database.currentDatabase!=null && Database.currentDatabase.equals(databaseName)){
                Database.tables=null;
                Database.currentDatabase=null;
            }
        }
    }

    public void executeDropTable(String tableName)throws Exception{
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
        if(table.delete()){
            Database.tables.remove(tableName.toLowerCase());
            TopLevelParse.resustInfo="[OK]";
        }else{
            TopLevelParse.resustInfo="[ERROR] Table does not exists.";
            throw new Exception();
        }
    }
}
