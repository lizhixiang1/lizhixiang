package edu.uob;
import java.io.File;
import java.util.*;

public class SelectCommand {
    private List<Integer> attributeList=new ArrayList<>();
    private Map<String,Integer> map=new HashMap<>();
    private Table table;
    public void executeSelectGeneral(String[] tokens)throws Exception{
        help();
        int fromIndex = helpCheckAttributeList(tokens);
        int len=tokens.length;
        if(tokens[len-3].equalsIgnoreCase("FROM")){
            executeWithoutCondition(tokens);
        }else{
            ConditionExecute conditionExecute = new ConditionExecute();
            conditionExecute.executeWithCondition(tokens,attributeList,map,table,fromIndex);
        }
    }

    private void executeWithoutCondition(String[] tokens)throws Exception{
        StringBuilder res= new StringBuilder();
        for (Integer integer : attributeList) {
            res.append(table.columnNames.get(integer));
            res.append("\t");
        }
        res.append("\n");
        for(int i=0;i<table.rows.size();i++){
            List<String> row=table.rows.get(i);
            if(row.size()==1){
                continue;
            }
            for (Integer integer : attributeList) {
                String str=row.get(integer);
                if(str.charAt(0)=='\''){
                    str=str.substring(1,str.length()-1);
                }
                res.append(str);
                res.append("\t");
            }
            if(i!=table.rows.size()-1){
                res.append("\n");
            }
        }
        TopLevelParse.resustInfo="[OK]\n"+ res;
    }

    private int helpCheckAttributeList(String[] tokens)throws Exception{
        int index=0;
        for(int i=0;i<tokens.length;i++){
            if(tokens[i].equalsIgnoreCase("FROM")){
                index=i;
                break;
            }
        }
        String tablePath="databases"+File.separator+Database.currentDatabase+File.separator+tokens[index+1].toLowerCase();
        File ta = new File(tablePath);
        if(!ta.exists() || !ta.isFile()){
            TopLevelParse.resustInfo="[ERROR] Current table does not exists.";
            throw new Exception();
        }
        table=Database.tables.get(tokens[index+1].toLowerCase());
        for(int i=0;i<table.columnNames.size();i++){
            map.put(table.columnNames.get(i).toLowerCase(),i);
        }
        if(index==2 && tokens[1].equals("*")){
            for(int i=0;i<table.columnNames.size();i++){
                attributeList.add(i);
            }
            return index;
        }
        for(int i=1;i<index;i+=2){
            if(map.getOrDefault(tokens[i].toLowerCase(),-1)==-1){
                TopLevelParse.resustInfo="[ERROR] No attribute "+tokens[i]+" in this table.";
                throw new Exception();
            }else{
                attributeList.add(map.get(tokens[i].toLowerCase()));
            }
        }
        return index;
    }

    private void help()throws Exception{
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
    }
}
