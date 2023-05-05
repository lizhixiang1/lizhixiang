package edu.uob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class ExampleStageTests1 {
    private GameServer server;
    @BeforeEach
    void setup() {
        File entitiesFile = Paths.get("config" + File.separator + "basi-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "basi-actions.xml").toAbsolutePath().toFile();
        new GameServer(entitiesFile, actionsFile);
        entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
        server = new GameServer(entitiesFile, actionsFile);
    }

    String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
                "Server took too long to respond (probably stuck in an infinite loop)");
    }

    @Test
    void test1() {
        String response = sendCommandToServer("simon: a :look");
        assertTrue(response.contains("Something wrong!"), "Something wrong");
        response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");
        assertTrue(response.contains("log cabin"), "Did not see a description of the room in response to look");
        assertTrue(response.contains("magic potion"), "Did not see a description of artifacts in response to look");
        assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
        assertTrue(response.contains("axe"), "Did not see axe");
        assertTrue(response.contains("coin"), "Did not see coin");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get trapdoor");
        response = sendCommandToServer("simon: look");
        assertFalse(response.contains("magic potion"), "Did not see a description of artifacts in response to look");
        assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
        assertFalse(response.contains("axe"), "Did not see axe");
        assertFalse(response.contains("coin"), "Did not see coin");
        response = sendCommandToServer("simon: inv");
        assertTrue(response.contains("magic potion"), "Did not see a description of artifacts in response to look");
        assertFalse(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
        assertTrue(response.contains("axe"), "Did not see axe");
        assertTrue(response.contains("coin"), "Did not see coin");
        sendCommandToServer("simon: drop axe");
        sendCommandToServer("simon: drop coin");
        sendCommandToServer("simon: drop potion");
        sendCommandToServer("simon: drop  trapdoor");
        response = sendCommandToServer("simon: look");
        assertTrue(response.contains("magic potion"), "Did not see a description of artifacts in response to look");
        assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
        assertTrue(response.contains("axe"), "Did not see axe");
        assertTrue(response.contains("coin"), "Did not see coin");
        test2();
    }

    void test2() {
        String response;
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: get key");
        response=sendCommandToServer("simon: cut tree");
        assertTrue(response.contains("You cut down the tree with the axe"), "Something wrong");
        response=sendCommandToServer("simon: look");
        assertTrue(response.contains("A heavy wooden log"), "Something wrong");
        sendCommandToServer("simon: get log");
        response=sendCommandToServer("simon: look");
        assertFalse(response.contains("A heavy wooden log"), "Something wrong");
        response=sendCommandToServer("simon: inv");
        assertTrue(response.contains("A heavy wooden log"), "Something wrong");
        test3();
    }

    void test3() {
        sendCommandToServer("simon: goto cabin");
        String response=sendCommandToServer("simon: look");
        assertFalse(response.contains("cellar"), "Did see cellar");
        response=sendCommandToServer("simon: open trapdoor");
        assertTrue(response.contains("You unlock the door and see steps leading down into a cellar"), "Something wrong");
        response=sendCommandToServer("simon: look");
        assertTrue(response.contains("cellar"), "Did not see cellar");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: fight elf");
        sendCommandToServer("simon: hit elf");
        sendCommandToServer("simon: hit elf");
        response=sendCommandToServer("simon: look");
        assertTrue(response.contains("cabin"), "cabin");
        response=sendCommandToServer("simon: inv");
        assertFalse(response.contains("A bottle of magic potion"), "Did see cellar");
        assertFalse(response.contains("A heavy wooden log"), "Something wrong");
        assertFalse(response.contains("A razor sharp axe"), "Something wrong");
        assertFalse(response.contains("A silver coin"), "Something wrong");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: look");
        sendCommandToServer("simon: get potion");
        sendCommandToServer("simon: get log");
        sendCommandToServer("simon: get axe");
        sendCommandToServer("simon: get coin");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        test4();
    }

    void test4() {
        sendCommandToServer("simon: look");
        String response=sendCommandToServer("simon: blow horn");
        assertTrue(response.contains("You blow the horn and as if by magic, a lumberjack appears !"), "Something wrong");
        response=sendCommandToServer("simon: look");
        assertTrue(response.contains("A burly wood cutter"), "Something wrong");
        sendCommandToServer("simon: bridge log");
        response=sendCommandToServer("simon: look");
        assertTrue(response.contains("clearing"), "Something wrong");
        sendCommandToServer("simon: look");
        response=sendCommandToServer("simon: goto clearing");
        assertTrue(response.contains("clearing"), "Something wrong");
        assertTrue(response.contains("It looks like the soil has been recently disturbed"), "Something wrong");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto cellar");
        response=sendCommandToServer("simon: inventory");
        assertTrue(response.contains("coin"), "Something wrong");
        response=sendCommandToServer("simon: pay elf coin");
        assertTrue(response.contains("You pay the elf your silver coin and he produces a shovel"), "Something wrong");
        response=sendCommandToServer("simon: inventory");
        assertFalse(response.contains("coin"), "Something wrong");
        response=sendCommandToServer("simon: look");
        assertTrue(response.contains("shovel"), "Something wrong");
        test5();
    }

    void test5() {
        sendCommandToServer("simon: get shovel");
        sendCommandToServer("simon: goto cellar");
        sendCommandToServer("simon: goto cabin");
        sendCommandToServer("simon: goto forest");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: goto clearing");
        String response=sendCommandToServer("simon: dig ground shovel");
        assertTrue(response.contains("You dig into the soft ground and unearth a pot of gold !!!"), "Something wrong");
        response=sendCommandToServer("simon: look");
        assertTrue(response.contains("gold"), "Something wrong");
        assertTrue(response.contains("hole"), "Something wrong");
        assertFalse(response.contains("It looks like the soil has been recently disturbed"), "Something wrong");
        sendCommandToServer("simon: goto riverbank");
        sendCommandToServer("simon: get horn");
        sendCommandToServer("simon: goto clearing");
        sendCommandToServer("simon: blow horn");
        response=sendCommandToServer("simon: look");
        assertTrue(response.contains("A burly wood cutter"), "Something wrong");
        response=sendCommandToServer("simon: drop a");
        assertTrue(response.contains("Cannot find this item in your inventory"), "Something wrong");
        response=sendCommandToServer("simon: drink potion");
        assertTrue(response.contains("You drink the potion and your health improves"), "Something wrong");
        response=sendCommandToServer("simon: drink potion");
        assertTrue(response.contains("Something goes wrong"), "Something wrong");
        response=sendCommandToServer("simon: dig ground shovel");
        assertTrue(response.contains("Something goes wrong"), "Something wrong");
        test6();
    }

    void test6(){
        String response=sendCommandToServer("zhixiang: look");
        assertTrue(response.contains("simon:clearing"), "Something wrong");
    }



    @Test
    void testGameActionTriggerParser(){
        ActionParser basicFunctionAction = new ActionParser();
        try{
            String command="cut goto";
            String[] commandTokens = command.split(" ");
            basicFunctionAction.parseActionTrigger(commandTokens, command);
        }catch (Exception e){
            assertEquals("No more than two different meaning triggers", e.getMessage(), "Something wrong!");
        }
        try{
            String command="goto goto";
            String[] commandTokens = command.split(" ");
            basicFunctionAction.parseActionTrigger(commandTokens, command);
        }catch (Exception e){
            assertEquals("No more than two pre defined trigger", e.getMessage(), "Something wrong!");
        }
        try{
            String command="cut cut";
            String[] commandTokens = command.split(" ");
            ArrayList<GameAction> action = basicFunctionAction.parseActionTrigger(commandTokens, command);
            assertEquals("cut", action.get(0).trigger, "Something wrong!");

            command="cut down cut";
            commandTokens = command.split(" ");
            action = basicFunctionAction.parseActionTrigger(commandTokens, command);
            assertEquals("cut", action.get(0).trigger, "Something wrong!");

            command="drink something";
            commandTokens = command.split(" ");
            action = basicFunctionAction.parseActionTrigger(commandTokens, command);
            assertEquals("drink", action.get(0).trigger, "Something wrong!");
        }catch (Exception ignored){}
    }

    @Test
    void testGameActionSubjectParser(){
        ActionParser basicFunctionAction = new ActionParser();
        GameAction action=DispatchCenter.actions.get("cut").get(0);
        String[] commandTokens="cut tree tree".split(" ");
        boolean flag = basicFunctionAction.parseActionSubjects(commandTokens, action);
        assertTrue(flag,"something wrong");

        commandTokens="cut tree".split(" ");
        flag = basicFunctionAction.parseActionSubjects(commandTokens, action);
        assertTrue(flag,"something wrong");

        commandTokens="cut tree key".split(" ");
        flag = basicFunctionAction.parseActionSubjects(commandTokens, action);
        assertFalse(flag,"something wrong");


        action=DispatchCenter.actions.get("goto").get(0);
        commandTokens="goto tree".split(" ");
        flag = basicFunctionAction.parseActionSubjects(commandTokens, action);
        assertFalse(flag,"something wrong");

        action=DispatchCenter.actions.get("goto").get(0);
        commandTokens="goto something".split(" ");
        flag = basicFunctionAction.parseActionSubjects(commandTokens, action);
        assertTrue(flag,"something wrong");
    }

    @Test
    void testMatch(){
        ActionParser actionParser = new ActionParser();
        String command="cut down tree";
        String trigger="cut down";
        int match = actionParser.match(command, trigger);
        assertEquals(match,0,"Something wrong");

        command="cut downs tree";
        match = actionParser.match(command, trigger);
        assertEquals(match,-1,"Something wrong");

        command="cut  down tree";
        match = actionParser.match(command, trigger);
        assertEquals(match,-1,"Something wrong");

        command="tree cut down";
        match = actionParser.match(command, trigger);
        assertEquals(match,5,"Something wrong");

        command="tree cut downs";
        match = actionParser.match(command, trigger);
        assertEquals(match,-1,"Something wrong");
    }

    @Test
    void testParserAction1(){
        ActionParser actionParser = new ActionParser();
        String command="cut tree";
        String[] commandTokens = command.split(" ");
        GameAction action= actionParser.parserAction(commandTokens,command);
        assertEquals(action.trigger,"cut","Something wrong");

        command="fight elf";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertEquals(action.trigger,"fight","Something wrong");

        command="fight fight elf";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertEquals(action.trigger,"fight","Something wrong");

        command="fight hit against elf";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertEquals(action.trigger,"fight","Something wrong");

        command="hit fight against elf";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertEquals(action.trigger,"hit","Something wrong");
    }

    @Test
    void testParserAction2(){
        ActionParser actionParser = new ActionParser();
        String command="cut look tree";
        String[] commandTokens = command.split(" ");
        GameAction action= actionParser.parserAction(commandTokens,command);
        assertNull(action, "Something wrong");
        command="look look";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertNull(action, "Something wrong");
        command="look goto";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertNull(action, "Something wrong");
        command="hit drink against elf";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertNull(action, "Something wrong");
        command="look a";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertNull(action, "Something wrong");
        command="inventory a";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertNull(action, "Something wrong");
        command="inv a";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertNull(action, "Something wrong");
    }

    @Test
    void testParserAction3(){
        ActionParser actionParser = new ActionParser();
        String command="get";
        String[] commandTokens = command.split(" ");
        GameAction action= actionParser.parserAction(commandTokens,command);
        assertNull(action, "Something wrong");

        command="drop";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertNull(action, "Something wrong");

        command="goto";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertNull(action, "Something wrong");

        command="get a";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertEquals(action.trigger,"get","Something wrong");

        command="drop a";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertEquals(action.trigger,"drop","Something wrong");

        command="goto a";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertEquals(action.trigger,"goto","Something wrong");
    }

    @Test
    void testParserAction4(){
        ActionParser actionParser = new ActionParser();
        String command="look";
        String[] commandTokens = command.split(" ");
        GameAction action= actionParser.parserAction(commandTokens,command);
        assertEquals(action.trigger,"look","Something wrong");

        command="inventory";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertEquals(action.trigger,"inventory","Something wrong");

        command="inv";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertEquals(action.trigger,"inv","Something wrong");

        command="a get";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertNull(action, "Something wrong");

        command="a drop";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertNull(action, "Something wrong");

        command="a goto";
        commandTokens = command.split(" ");
        action= actionParser.parserAction(commandTokens,command);
        assertNull(action, "Something wrong");
    }

}
