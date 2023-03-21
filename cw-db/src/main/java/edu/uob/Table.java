package edu.uob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Table {
    public List<String> columnNames;
    public String tableName;
    public List<List<String>> rows;

    public Table(String tableName){
        this.tableName=tableName;
        columnNames=new ArrayList<>();
        rows= new ArrayList<>();
    }

    public void initTableFromFile(String fileName){
        ReadFromFile readFromFile = new ReadFromFile();
        readFromFile.read(fileName,this);
    }

    public int getCol(String name){
        for(int i=0;i<columnNames.size();i++){
            if(columnNames.get(i).equals(name)){
                return i;
            }
        }
        return -1;
    }

}
