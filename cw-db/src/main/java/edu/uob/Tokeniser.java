package edu.uob;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Tokeniser {
    String[] specialCharacters = {"(",")",",",";",">","<","=","!","LIKE","LiKE","lIKE","LIke","LIKe"
    ,"LIke","LiKe","LikE","lIKe","lIkE","liKE","Like","lIke","liKe","LIKe","like"};
    ArrayList<String> tokens = new ArrayList<String>();

    String[] setup(String query)
    {
        // Remove any whitespace at the beginning and end of the query
        query = query.trim();
        // Split the query on single quotes (to separate out query characters from string literals)
        String[] fragments = query.split("'");
        for (int i=0; i<fragments.length; i++) {
            // Every odd fragment is a string literal, so just append it without any alterations
            if (i%2 != 0) tokens.add("'" + fragments[i] + "'");
                // If it's not a string literal, it must be query characters (which need further processing)
            else {
                // Tokenise the fragments into an array of strings
                String[] nextBatchOfTokens = tokenise(fragments[i]);
                // Then add these to the "result" array list (needs a bit of conversion)
                tokens.addAll(Arrays.asList(nextBatchOfTokens));
            }
        }
        String[] res=new String[tokens.size()];
        // Finally, loop through the result array list, printing out each token a line at a time
        for(int i=0; i<tokens.size(); i++){
            res[i]=tokens.get(i);
        }
        return res;
    }

    String[] tokenise(String input)
    {
        // Add in some extra padding spaces around the "special characters"
        // so we can be sure that they are separated by AT LEAST one space (possibly more)
        for(int i=0; i<specialCharacters.length ;i++) {
            input = input.replace(specialCharacters[i], " " + specialCharacters[i] + " ");
        }
        // Remove all double spaces (the previous replacements may had added some)
        // This is "blind" replacement - replacing if they exist, doing nothing if they don't
        while (input.contains("  ")) input = input.replaceAll("  ", " ");
        // Again, remove any whitespace from the beginning and end that might have been introduced
        input = input.trim();
        // Finally split on the space char (since there will now ALWAYS be a space between tokens)
        String[] split = input.split(" ");
        ArrayList<String> res=new ArrayList<>();
        for(int i=0;i<split.length;i++){
            if(split[i].equals("=")||split[i].equals(">")||split[i].equals("<")||split[i].equals("!")){
                if(split[i+1].equals("=")){
                    String temp=split[i]+split[i+1];
                    i++;
                    res.add(temp);
                }else{
                    res.add(split[i]);
                }
            }else{
                res.add(split[i]);
            }
        }
        split=new String[res.size()];
        for(int i=0;i<res.size();i++){
            split[i]=res.get(i);
        }
        return split;
    }

//    public String replace(String input){
//        boolean flag=false;
//        for(int i=0;i<input.length();i++){
//            if(input.charAt(i)=='\''){
//                flag=!flag;
//            }
//            if(input.charAt(i)==' ' && !flag){
//                input=input.substring(0,i)+"$"+input.substring(i+1);
//            }
//        }
//        return input;
//    }
}