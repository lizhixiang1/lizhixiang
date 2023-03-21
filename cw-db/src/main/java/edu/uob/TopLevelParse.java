package edu.uob;
public class TopLevelParse {
    public static String resustInfo="";

    public static Tools tools;
    public TopLevelParse(String command){
        Tokeniser tokeniser = new Tokeniser();
        tools=new Tools();
        String[] tokens = tokeniser.setup(command);
        try{
            checkLastToken(tokens);
            dispatchToSamllerParse(tokens);
        }catch (Exception e){

        }
    }

    private boolean checkLastToken(String[] tokens)throws Exception{
        if(!tokens[tokens.length-1].equals(";")){
            resustInfo="[ERROR] Sql must end with ';'.";
            throw new Exception();
        }
        return true;
    }

    private void dispatchToSamllerParse(String[] command)throws Exception{
        if(command[0].equalsIgnoreCase("USE")){
            new UseParser(command);
        }else if(command[0].equalsIgnoreCase("CREATE")){
            new CreateParser(command);
        }else if(command[0].equalsIgnoreCase("DROP")){
            new DropParser(command);
        }else if(command[0].equalsIgnoreCase("ALTER")){
            new AlterParser(command);
        }else if(command[0].equalsIgnoreCase("INSERT")){
            new InsertParser(command);
        }else if(command[0].equalsIgnoreCase("SELECT")){
            new SelectParser(command);
        }else if(command[0].equalsIgnoreCase("UPDATE")){
            new UpdateParser(command);
        }else if(command[0].equalsIgnoreCase("DELETE")){
            new DeleteParser(command);
        }else if(command[0].equalsIgnoreCase("JOIN")){
            new JoinParser(command);
        }else{
            resustInfo="[ERROR] Sql must start with valid command type.";
            throw new Exception();
        }
    }
}
