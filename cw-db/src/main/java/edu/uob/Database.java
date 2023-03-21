package edu.uob;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    static Map<String,Table> tables=null;
    static String currentDatabase=null;

    public static void initalTables(){
        tables=new HashMap<>();
        File directory = new File("databases"+File.separator+currentDatabase);
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                Table table = new Table(file.getName());
                table.initTableFromFile(directory+File.separator+file.getName());
                tables.put(table.tableName,table);
            }
        }
    }

    public static void writeTablesToFile(String tableName) throws IOException {
        String filePath="databases"+File.separator+Database.currentDatabase+File.separator+tableName;
        Table temp=tables.get(tableName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for(String str:temp.columnNames){
            writer.write(str);
            writer.write("\t");
        }
        writer.write("\n");
        for(List<String> row:temp.rows){
            for(String str:row){
                writer.write(str);
                writer.write("\t");
            }
            writer.write("\n");
        }
        writer.close();
    }







}
