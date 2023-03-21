package edu.uob;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class ConditionExecute {
    Table table;
    Map<String,Integer> map;
    public void executeWithCondition(String[] tokens, List<Integer> attributeList,
            Map<String,Integer> map,Table table,int fromIndex)throws Exception{
        this.table=table;
        this.map=map;
        StringBuilder res=new StringBuilder();
        for (Integer integer : attributeList) {
            res.append(table.columnNames.get(integer));
            res.append("\t");
        }
        res.append("\n");
        for(int i=0;i<table.rows.size();i++){
            if(table.rows.get(i).size()==1){
                continue;
            }
            boolean bool = executeWithConditionHelp(tokens, i,fromIndex,map,table);
            if(bool){
                for (Integer integer : attributeList) {
                    String str=table.rows.get(i).get(integer);
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
        }
        TopLevelParse.resustInfo="[OK]\n"+res;
    }

    public boolean executeWithConditionHelp(String[] tokens,int rowNum,
           int fromIndex,Map<String,Integer> map,Table table)throws Exception{
        this.map=map;
        this.table=table;
        Deque<String> stack=new ArrayDeque<>();
        for(int i=fromIndex+3;i<tokens.length-1;i++){
            if(tokens[i].equals("(")){
                stack.offerFirst(tokens[i]);
            }else if(TopLevelParse.tools.attributeNameCheck(tokens[i])){
                if(map.getOrDefault(tokens[i].toLowerCase(),-1)==-1){
                    TopLevelParse.resustInfo="[ERROR] No "+tokens[i]+" attribute";
                    throw new Exception();
                }
                boolean bool = checkTreuOrFalse(tokens, i,rowNum);
                stack.offerFirst(String.valueOf(bool));
                executeStack1(stack);
                i+=2;
            }else if(TopLevelParse.tools.boolOperator.containsKey(tokens[i].toUpperCase())){
                stack.offerFirst(tokens[i]);
            }else if(tokens[i].equals(")")){
                executeStack2(stack);
            }
        }
        executeStack3(stack);
        return stack.pollFirst().equalsIgnoreCase("TRUE");
    }

    public boolean checkTreuOrFalse(String[] tokens,int index,int rowNum)throws Exception{
        String attributeName=tokens[index];
        String comparator=tokens[index+1];
        String str=tokens[index+2];
        int tempIndex=map.get(attributeName.toLowerCase());
        String value = table.rows.get(rowNum).get(tempIndex);
        if(comparator.equals("==") || comparator.equals("!=")){
            return compareEqualOrNot(str,value,comparator);
        }else if(comparator.equals(">") || comparator.equals("<") ||
                comparator.equals(">=") || comparator.equals("<=")){
            return compareDif(comparator,str,value);
        }else if(comparator.equalsIgnoreCase("LIKE")){
            return compareLike(str,value);
        }
        return true;
    }

    public boolean compareEqualOrNot(String str,String value,String comparator){
        if((str.equalsIgnoreCase("TRUE") || str.equalsIgnoreCase("FALSE"))
                &&(value.equalsIgnoreCase("TRUE") || value.equalsIgnoreCase("FALSE"))){
            if(comparator.equals("==")){
                return str.equalsIgnoreCase(value);
            }else{
                return !str.equalsIgnoreCase(value);
            }
        }
        if(str.charAt(0)=='\'' && value.charAt(0)=='\''){
            str=str.substring(1,str.length()-1);
            value=value.substring(1,value.length()-1);
            if(comparator.equals("==")){
                return str.equals(value);
            }else{
                return !str.equals(value);
            }
        }
        if(str.equalsIgnoreCase("null") && value.equalsIgnoreCase("null")){
            if(comparator.equals("==")){
                return str.equalsIgnoreCase(value);
            }else{
                return !str.equalsIgnoreCase(value);
            }
        }
        if((TopLevelParse.tools.floatLiteralCheck(str)||TopLevelParse.tools.integerLiteralCheck(str))&&
        (TopLevelParse.tools.floatLiteralCheck(value)||TopLevelParse.tools.integerLiteralCheck(value))){
            return compareNum(comparator,str,value);
        }
        return false;
    }

    public boolean compareLike(String str,String value){
        if(str.charAt(0)=='\'' && value.charAt(0)=='\''){
            str=str.substring(1,str.length()-1);
            value=value.substring(1,value.length()-1);
            return value.contains(str);
        }
        return false;
    }

    public boolean compareDif(String comparator,String str,String value){
        if(str.equalsIgnoreCase("TRUE") || str.equalsIgnoreCase("FALSE")){
            return false;
        }
        if(value.equalsIgnoreCase("TRUE") || value.equalsIgnoreCase("FALSE")){
            return false;
        }
        if(str.equalsIgnoreCase("NULL") || value.equalsIgnoreCase("NULL")){
            return false;
        }
        if((TopLevelParse.tools.floatLiteralCheck(str)||TopLevelParse.tools.integerLiteralCheck(str))&&
                (TopLevelParse.tools.floatLiteralCheck(value)||TopLevelParse.tools.integerLiteralCheck(value))){
            return compareNum(comparator,str,value);
        }

        if(TopLevelParse.tools.stringLiteralCheck(str) && TopLevelParse.tools.stringLiteralCheck(value)){
            return compareString(comparator,str,value);
        }
        return false;
    }

    public boolean compareString(String comparator,String str,String value){
        if(comparator.equals(">")){
            return value.compareTo(str)>0;
        }else if(comparator.equals(">=")){
            return value.compareTo(str)>=0;
        }else if(comparator.equals("<")){
            return value.compareTo(str)<0;
        }else if(comparator.equals("<=")){
            return value.compareTo(str)<=0;
        }
        return false;
    }

    public boolean compareNum(String comparator,String str,String value){
        float val1=0;
        float val2=0;
        if(TopLevelParse.tools.integerLiteralCheck(str)){
            val1=Integer.parseInt(str);
        }else if(TopLevelParse.tools.floatLiteralCheck(str)){
            val1=Float.parseFloat(str);
        }
        if(TopLevelParse.tools.integerLiteralCheck(value)){
            val2=Integer.parseInt(value);
        }else if(TopLevelParse.tools.floatLiteralCheck(value)){
            val2=Float.parseFloat(value);
        }
        if(comparator.equals(">")){
            return val2>val1;
        }else if(comparator.equals(">=")){
            return val2>=val1;
        }else if(comparator.equals("<")){
            return val2<val1;
        }else if(comparator.equals("<=")){
            return val2<=val1;
        }else if(comparator.equals("==")){
            return val2==val1;
        }else if(comparator.equals("!=")){
            return val2!=val1;
        }
        return false;
    }

    public void executeStack1(Deque<String> stack){
        if(stack.size()<3){
            return;
        }
        String s1 = stack.pollFirst();
        String s2 = stack.pollFirst();
        String s3 = stack.pollFirst();
        if(s2.equalsIgnoreCase("AND")){
            boolean res=Boolean.parseBoolean(s1) && Boolean.parseBoolean(s3);
            stack.offerFirst(String.valueOf(res));
        }else{
            stack.offerFirst(s3);
            stack.offerFirst(s2);
            stack.offerFirst(s1);
        }
    }

    public void executeStack2(Deque<String> stack){
        String s1=stack.pollFirst();
        String s2=stack.pollFirst();
        while(!s2.equals("(")){
            String s3=stack.pollFirst();
            boolean res=Boolean.parseBoolean(s1) || Boolean.parseBoolean(s3);
            stack.offerFirst(String.valueOf(res));
            s1=stack.pollFirst();
            s2=stack.pollFirst();
        }
        stack.offerFirst(s1);
        executeStack1(stack);
    }

    public void executeStack3(Deque<String> stack){
        while(stack.size()!=1){
            String s1=stack.pollFirst();
            String s2=stack.pollFirst();
            String s3=stack.pollFirst();
            boolean res=false;
            if(s2.equalsIgnoreCase("AND")){
                res=Boolean.parseBoolean(s1) && Boolean.parseBoolean(s3);
            }else if(s2.equalsIgnoreCase("OR")){
                res=Boolean.parseBoolean(s1) || Boolean.parseBoolean(s3);
            }
            stack.offerFirst(String.valueOf(res));
        }
    }
}
