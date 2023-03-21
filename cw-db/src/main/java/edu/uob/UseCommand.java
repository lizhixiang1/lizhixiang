package edu.uob;

import java.io.File;

public class UseCommand {
    public void useDatabase(String databaseName)throws Exception{
        String directoryPath = "databases"+File.separator+databaseName;
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            TopLevelParse.resustInfo="[ERROR] Database does not exist.";
            throw new Exception();
        }
        Database.currentDatabase=databaseName;
        TopLevelParse.resustInfo="[OK]";
        Database.initalTables();
    }
}
