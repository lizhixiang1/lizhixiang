package edu.uob;

import java.io.File;
import java.util.List;

public class JoinCommand {
    Table table1;
    Table table2;
    public void executeJoin(String[] tokens)throws Exception{
        executeJoinHelp(tokens);
        table1=Database.tables.get(tokens[1].toLowerCase());
        table2=Database.tables.get(tokens[3].toLowerCase());
        int col1 = table1.getCol(tokens[5]);
        if(col1==-1){
            TopLevelParse.resustInfo="[ERROR] "+tokens[5]+" does not exist in the table "+tokens[1];
            throw new Exception();
        }
        int col2 = table2.getCol(tokens[7]);
        if(col2==-1){
            TopLevelParse.resustInfo="[ERROR] "+tokens[7]+" does not exist in the table "+tokens[3];
            throw new Exception();
        }
        StringBuilder res=new StringBuilder("id");
        for(int i=1;i<table1.columnNames.size();i++){
            if(!table1.columnNames.get(i).equals(tokens[5])){
                res.append("\t");
                res.append(table1.tableName).append(".").append(table1.columnNames.get(i));
            }
        }
        for(int i=1;i<table2.columnNames.size();i++){
            if(!table2.columnNames.get(i).equals(tokens[7])){
                res.append("\t");
                res.append(table2.tableName).append(".").append(table2.columnNames.get(i));
            }
        }
        res.append("\n");
        connect(res,col1,col2,tokens);
    }

    public void connect(StringBuilder res,int col1,int col2,String[] tokens){
        int index=1;
        for(List<String> row:table1.rows){
            if(row.size()==1){
                continue;
            }
            for(List<String> temp:table2.rows){
                if(temp.size()==1){
                    continue;
                }
                if(row.get(col1).equals(temp.get(col2))){
                    res.append(index++);
                    for(int i=1;i<row.size();i++){
                        if(!table1.columnNames.get(i).equalsIgnoreCase(tokens[5])){
                            res.append("\t");
                            String str=row.get(i);
                            res.append(str.charAt(0)=='\''?str.substring(1,str.length()-1):str);
                        }
                    }
                    for(int i=1;i<temp.size();i++){
                        if(!table2.columnNames.get(i).equalsIgnoreCase(tokens[7])){
                            res.append("\t");
                            String str=temp.get(i);
                            res.append(str.charAt(0)=='\''?str.substring(1,str.length()-1):str);
                        }
                    }
                    res.append("\n");
                }
            }
        }
        TopLevelParse.resustInfo="[OK]\n"+res.toString();
    }

    public void executeJoinHelp(String[] tokens)throws Exception{
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
        String tablePath1="databases"+File.separator+Database.currentDatabase+File.separator+tokens[1].toLowerCase();
        String tablePath2="databases"+File.separator+Database.currentDatabase+File.separator+tokens[3].toLowerCase();
        File table1 = new File(tablePath1);
        File table2 = new File(tablePath2);
        if(!table1.exists()){
            TopLevelParse.resustInfo="[ERROR] " +tokens[1]+ " does not exists.";
            throw new Exception();
        }
        if(!table2.exists()){
            TopLevelParse.resustInfo="[ERROR] " +tokens[3]+ " does not exists.";
            throw new Exception();
        }
    }
}
