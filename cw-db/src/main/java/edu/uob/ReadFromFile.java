package edu.uob;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ReadFromFile {
    public void read(String path,Table table){
        try (FileReader fileReader = new FileReader(path)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int index=0;
            while ((line = bufferedReader.readLine()) != null) {
                if(index==0){
                    readFirstLine(line,table);
                }else{
                    readRestLines(line,table);
                }
                index++;
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    public void readFirstLine(String firstLine,Table table){
        String[] columnsNames=firstLine.split("\t");
        Collections.addAll(table.columnNames,columnsNames);
    }

    public void readRestLines(String nextLine,Table table){
        ArrayList<String> temp=new ArrayList<>();
        String[] values=nextLine.split("\t");
        Collections.addAll(temp,values);
        table.rows.add(temp);
    }
}
