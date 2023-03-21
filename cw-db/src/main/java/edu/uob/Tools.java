package edu.uob;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

public class Tools {
    HashMap<Character,Integer> digit=new HashMap<>();
    HashMap<Character,Integer> lowerCase=new HashMap<>();
    HashMap<Character,Integer> upperCase=new HashMap<>();
    HashMap<Character,Integer> symbol=new HashMap<>();
    HashMap<String,Integer> boolOperator=new HashMap<>();
    HashMap<String,Integer> keyWords=new HashMap<>();
    HashMap<String,Integer> comparator=new HashMap<>();


    public Tools(){
        initDigit();
        initLowerCase();
        initUpperCase();
        initSymbol();
        initKeyWords();
        initBoolOperator();
        initComparator();
    }

    private void initDigit(){
        char temp='0';
        for(int i=0;i<10;i++){
            digit.put(temp++,1);
        }
    }

    private void initLowerCase(){
        char temp='a';
        for(int i=0;i<26;i++){
            lowerCase.put(temp++,1);
        }
    }

    private void initUpperCase(){
        char temp='A';
        for(int i=0;i<26;i++){
            upperCase.put(temp++,1);
        }
    }

    private void initSymbol(){
        symbol.put('!',1);
        symbol.put('#',1);
        symbol.put('$',1);
        symbol.put('%',1);
        symbol.put('&',1);
        symbol.put('(',1);
        symbol.put(')',1);
        symbol.put('*',1);
        symbol.put('+',1);
        symbol.put(',',1);
        symbol.put('-',1);
        symbol.put('.',1);
        symbol.put('/',1);
        symbol.put(':',1);
        symbol.put(';',1);
        symbol.put('>',1);
        symbol.put('=',1);
        symbol.put('<',1);
        symbol.put('?',1);
        symbol.put('@',1);
        symbol.put('[',1);
        symbol.put(']',1);
        symbol.put('\\',1);
        symbol.put('^',1);
        symbol.put('_',1);
        symbol.put('`',1);
        symbol.put('{',1);
        symbol.put('}',1);
        symbol.put('~',1);
    }

    public void initKeyWords(){
        keyWords.put("USE",1);
        keyWords.put("CREATE",1);
        keyWords.put("DROP",1);
        keyWords.put("ALTER",1);
        keyWords.put("INSERT",1);
        keyWords.put("SELECT",1);
        keyWords.put("UPDATE",1);
        keyWords.put("DELETE",1);
        keyWords.put("JOIN",1);
        keyWords.put("TABLE",1);
        keyWords.put("DATABASE",1);
        keyWords.put("VALUES",1);
        keyWords.put("FROM",1);
        keyWords.put("INTO",1);
        keyWords.put("AND",1);
        keyWords.put("OR",1);
        keyWords.put("ON",1);
        keyWords.put("WHERE",1);
        keyWords.put("TRUE",1);
        keyWords.put("FALSE",1);
        keyWords.put("LIKE",1);
        keyWords.put("NULL",1);
        keyWords.put("SET",1);
    }

    public boolean isKeyWords(String a){
        return keyWords.containsKey(a.toUpperCase());
    }
    public void initBoolOperator(){
        boolOperator.put("AND",1);
        boolOperator.put("OR",1);
    }

    public void initComparator(){
        comparator.put("==",1);
        comparator.put(">",1);
        comparator.put("<",1);
        comparator.put(">=",1);
        comparator.put("<=",1);
        comparator.put("!=",1);
        comparator.put("LIKE",1);
        comparator.put("like",1);
    }
    public boolean letterCheck(Character ch){
        return lowerCase.containsKey(ch) || upperCase.containsKey(ch);
    }
    public boolean plainTextCheck(String str){
        for(int i=0;i<str.length();i++){
            char ch=str.charAt(i);
            if(!letterCheck(ch) && !digit.containsKey(ch)){
                return false;
            }
        }
        return true;
    }

    public boolean tableNameCheck(String str){
        return plainTextCheck(str) && !isKeyWords(str.toUpperCase());
    }

    public boolean databaseNameCheck(String str){
        return plainTextCheck(str) && !isKeyWords(str.toUpperCase());
    }

    public boolean digitSequenceCheck(String str){
        for(int i=0;i<str.length();i++){
            char ch=str.charAt(i);
            if(!digit.containsKey(ch)){
                return false;
            }
        }
        return true;
    }

    public boolean integerLiteralCheck(String str){
        if(digitSequenceCheck(str)){
            return true;
        }
        if(str.length()==1){
            return false;
        }
        char ch=str.charAt(0);
        if(ch=='+' || ch=='-'){
            String temp=str.substring(1);
            return digitSequenceCheck(temp);
        }
        return false;
    }

    public boolean booleanLiteralCheck(String str){
        return str.equalsIgnoreCase("TRUE") || str.equalsIgnoreCase("FALSE");
    }

    public boolean charLiteralCheck(char ch){
        return ch==' ' || letterCheck(ch) || symbol.containsKey(ch) || digit.containsKey(ch);
    }

    public boolean stringLiteralCheck(String str){
        if(str.equals("")){
            return true;
        }
        for(int i=0;i<str.length();i++){
            char ch=str.charAt(i);
            if(!charLiteralCheck(ch)){
                return false;
            }
        }
        return true;
    }

    public boolean floatLiteralCheck(String str){
        char temp=str.charAt(0);
        if(temp=='+' || temp=='-'){
            String substring = str.substring(1);
            return floatLiteralCheckHelp(substring);
        }
        return floatLiteralCheckHelp(str);
    }

    public boolean floatLiteralCheckHelp(String str){
        int index=-1;
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)=='.'){
                index=i;
                break;
            }
        }
        if(index==-1 || index==0 || index==str.length()-1){
            return false;
        }
        String temp1=str.substring(0,index);
        String temp2=str.substring(index+1);
        return digitSequenceCheck(temp1) && digitSequenceCheck(temp2);
    }
    public boolean valueCheck(String str){
        if(booleanLiteralCheck(str) || integerLiteralCheck(str) || floatLiteralCheck(str) || str.equalsIgnoreCase("NULL")){
            return true;
        }
        if(str.length()<2){
            return false;
        }
        if(str.length()==2){
            return str.charAt(0)=='\'' && str.charAt(1)=='\'';
        }
        if(str.charAt(0)=='\'' && str.charAt(str.length()-1)=='\''){
            String substring = str.substring(1,str.length()-1);
            return stringLiteralCheck(substring);
        }
        return false;
    }

    public boolean attributeNameCheck(String str){
        if(plainTextCheck(str) && !isKeyWords(str.toUpperCase())){
            return true;
        }
        int index=-1;
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)=='.'){
                index=i;
                break;
            }
        }
        if(index==-1 || index==0 || index==str.length()-1){
            return false;
        }
        String temp1=str.substring(0,index);
        String temp2=str.substring(index+1);
        return tableNameCheck(temp1) && tableNameCheck(temp2);
    }

    public boolean isWildAttributeList(String[] tokens,int startIndex,int endIndex){
        if(startIndex==endIndex){
            return false;
        }
        if(endIndex-startIndex==1){
            return tokens[startIndex].equalsIgnoreCase("*") || attributeNameCheck(tokens[startIndex]);
        }
        for(int i=startIndex;i<endIndex;i+=2){
            if(!attributeNameCheck(tokens[i])){
                return false;
            }
            if(i+1<endIndex){
                return tokens[i+1].equalsIgnoreCase(",");
            }
        }
        return true;
    }

    public boolean condition(String[] tokens,int start){
        Deque<String> stack=new ArrayDeque<String>();
        for(int i=start;i<tokens.length-1;i++){
            if(tokens[i].equals("(")){
                if(fourthCondition(tokens,i+1, tokens.length-2)!=-1 && tokens[i+4].equals(")")){
                    stack.offerFirst("value");
                    i+=4;
                }else{
                    stack.offerFirst(tokens[i]);
                }
            }else if(attributeNameCheck(tokens[i])){
                if(fourthCondition(tokens,i, tokens.length-2)==-1){
                    return false;
                }
                stack.offerFirst("value");
                i+=2;
            }else if(tokens[i].equalsIgnoreCase("AND") || tokens[i].equalsIgnoreCase("OR")){
                stack.offerFirst(tokens[i]);
            }else if(tokens[i].equals(")")){
                if(!executeStack2(stack)){
                    return false;
                }
            }else{
                return false;
            }
        }
        return executeStack3(stack);
    }

    public boolean executeStack2(Deque<String> stack){
        if(stack.size()<2){
            return false;
        }
        String s1=stack.pollFirst();
        String s2=stack.pollFirst();
        boolean flag=true;
        while(stack.size()>=2 && !s2.equals("(")){
            String s3=stack.pollFirst();
            if(s1.equals("value")&&s3.equals("value")){
                if(s2.equalsIgnoreCase("and")||s2.equalsIgnoreCase("or")){
                    stack.offerFirst("value");
                }else{
                    return false;
                }
            }else{
                return false;
            }
            s1=stack.pollFirst();
            s2=stack.pollFirst();
            flag=false;
        }
        if(flag){
            return false;
        }else{
            stack.pollFirst();
            stack.pollFirst();
            stack.offerFirst("value");
        }
        return true;
    }

    public boolean executeStack3(Deque<String> stack){
        while(stack.size()>=3){
            String s1=stack.pollFirst();
            String s2=stack.pollFirst();
            String s3=stack.pollFirst();
            if(s1.equals("value")&&s3.equals("value")){
                if(s2.equalsIgnoreCase("and")||s2.equalsIgnoreCase("or")){
                    stack.offerFirst("value");
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
        if(stack.size()!=1 || !stack.pollFirst().equals("value")){
            return false;
        }
        return true;
    }
    public int fourthCondition(String[] tokens,int start,int end){
        if(end-start<2){
            return -1;
        }
        if(!attributeNameCheck(tokens[start])){
            return -1;
        }
        if(!comparator.containsKey(tokens[start+1]) && !tokens[start+1].equalsIgnoreCase("like")){
            return -1;
        }
        if(!valueCheck(tokens[start+2])){
            return -1;
        }
        return start+2;
    }
}