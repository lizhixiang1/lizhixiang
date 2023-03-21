package edu.uob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyOwnTest {
    private DBServer server;

    // Create a new server _before_ every @Test
    @BeforeEach
    public void setup() {
        server = new DBServer();
    }

    // Random name generator - useful for testing "bare earth" queries (i.e. where tables don't previously exist)
    private String generateRandomName()
    {
        String randomName = "";
        for(int i=0; i<10 ;i++) randomName += (char)( 97 + (Math.random() * 25.0));
        return randomName;
    }

    private String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
                "Server took too long to respond (probably stuck in an infinite loop)");
    }

    // A basic test that creates a database, creates a table, inserts some test data, then queries it.
    // It then checks the response to see that a couple of the entries in the table are returned as expected
    @Test
    public void testBasicCreateAndUse() {
        String response = sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        assertTrue(response.contains("[ERROR]"), "Cannot create table before use a database.");
        String randomName = generateRandomName();
        response = sendCommandToServer("CREAT DATABASE " + randomName + ";");
        assertTrue(response.contains("[ERROR]"), "Create key word spell wrong");
        response = sendCommandToServer("CREATE DATABAS " + randomName + ";");
        assertTrue(response.contains("[ERROR]"), "Database key word spell wrong");
        response = sendCommandToServer("CREATE DATABASE " + "&AA" + ";");
        assertTrue(response.contains("[ERROR]"), "Database name must be plain text");
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        response=sendCommandToServer("CREATE TABLE marks1;");
        assertTrue(response.contains("[OK]"), "Unknown wrong!");
        response=sendCommandToServer("CREATE TABLE marks (name,id);");
        assertTrue(response.contains("[ERROR]"), "Cannot use id.");
        response=sendCommandToServer("CREATE TABLE marks (name,age,name);");
        assertTrue(response.contains("[ERROR]"), "Same attribute name.");
        response=sendCommandToServer("CREATE TABLE marks (name,age gender);");
        assertTrue(response.contains("[ERROR]"), "Unknown wrong");
        response=sendCommandToServer("CREATE TABLE marks (name,age, gender&;");
        assertTrue(response.contains("[ERROR]"), "Attribute name must be plain text.");
        response=sendCommandToServer("CREATE TABLE marks2 (name,age,gender);");
        assertTrue(response.contains("[OK]"), "Unknown wrong");
        response=sendCommandToServer("CREATE TABLE marks2;");
        assertTrue(response.contains("[ERROR]"), "Cannot create table with same name.");
        sendCommandToServer("Drop database "+randomName+";");
    }

    @Test
    public void testBasicDrop() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks1;");
        sendCommandToServer("CREATE TABLE marks2 (name,age,gender);");
        String response = sendCommandToServer("DROP TABLE marks1;");
        assertTrue(response.contains("[OK]"), "Unknown wrong");
        response = sendCommandToServer("DROP TABLE marks1;");
        assertTrue(response.contains("[ERROR]"), "Can not delete un-exist table");
        response = sendCommandToServer("DROP TABLE marks2;");
        assertTrue(response.contains("[OK]"), "Unknown wrong");
        response = sendCommandToServer("DROP TABLE marks2;");
        assertTrue(response.contains("[ERROR]"), "Can not delete un-exist table");
        response = sendCommandToServer("DROP DATABASE a;");
        assertTrue(response.contains("[ERROR]"), "Can not delete un-exist DATABASE.");
        response = sendCommandToServer("DROP DATABASE "+randomName.toUpperCase()+";");
        assertTrue(response.contains("[OK]"), "Unknown wrong");
    }

    @Test
    public void testBasicAlter() {
        String response=sendCommandToServer("Alter table task1 add age;");
        assertTrue(response.contains("[ERROR]"), "No Database chose.");
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE marks2 (name,age,gender);");
        response=sendCommandToServer("Alter table from add age;");
        assertTrue(response.contains("[ERROR]"), "Table name cannot be key words.");
        response=sendCommandToServer("Alter table marks1 add age;");
        assertTrue(response.contains("[ERROR]"), "Table does not exist.");
        response=sendCommandToServer("Alter table marks2 add id;");
        assertTrue(response.contains("[ERROR]"), "Cannot add id.");
        response=sendCommandToServer("Alter table marks2 drop id;");
        assertTrue(response.contains("[ERROR]"), "Cannot drop id.");
        response=sendCommandToServer("Alter table marks2 add name;");
        assertTrue(response.contains("[ERROR]"), "Name attribute has exist.");
        response=sendCommandToServer("Alter table marks2 add teacher;");
        assertTrue(response.contains("[OK]"), "Unknown wrong.");
        response=sendCommandToServer("Alter table marks2 add Teacher;");
        assertTrue(response.contains("[ERROR]"), "Cannot add same attribute");
        sendCommandToServer("DROP DATABASE "+randomName+";");
    }

    @Test
    public void testBasicInsert() {
        String response=sendCommandToServer("INSERT INTO mask1 VALUES (1,2,3);");
        assertTrue(response.contains("[ERROR]"), "No Database chose.");
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE mask1 (name,age,gender);");
        response=sendCommandToServer("INSERT INTO mask2 VALUES ('li',20,'male');");
        assertTrue(response.contains("[ERROR]"), "No this table.");
        response=sendCommandToServer("INSERT INTO mask1 VALUES (li,20,'male');");
        assertTrue(response.contains("[ERROR]"), "String literal wrong.");
        response=sendCommandToServer("INSERT INTO mask1 VALUES (20,'male');");
        assertTrue(response.contains("[ERROR]"), "Too less attribute");
        response=sendCommandToServer("INSERT INTO mask1 VALUES ('li',20,'male',NULL);");
        assertTrue(response.contains("[ERROR]"), "Too many attribute");
        response=sendCommandToServer("INSERT INTO mask1 VALUES ('li' 20 'male');");
        assertTrue(response.contains("[ERROR]"), "Attribute list must be separate by ,");
        response=sendCommandToServer("INSERT INTO mask1 VALUES ('li',.20,'male');");
        assertTrue(response.contains("[ERROR]"), "Number value wrong");
        response=sendCommandToServer("INSERT INTO mask1 VALUES ('li',20.,'male');");
        assertTrue(response.contains("[ERROR]"), "Number value wrong");
        response=sendCommandToServer("INSERT INTO mask1 VALUES ('li',+20,'male');");
        assertTrue(response.contains("[OK]"), "Unknown wrong");
        response=sendCommandToServer("INSERT INTO mask1 VALUES ('li',-20,'male');");
        assertTrue(response.contains("[OK]"), "Unknown wrong");
        response=sendCommandToServer("INSERT INTO mask1 VALUES ('li',+20.1,'male');");
        assertTrue(response.contains("[OK]"), "Unknown wrong");
        response=sendCommandToServer("INSERT INTO mask1 VALUES ('li',-20.1,'male');");
        assertTrue(response.contains("[OK]"), "Unknown wrong");
        response=sendCommandToServer("INSERT INTO mask1 VALUES ('li',TRUE,'male');");
        assertTrue(response.contains("[OK]"), "Unknown wrong");
        response=sendCommandToServer("INSERT INTO mask1 VALUES ('li',true,'male');");
        assertTrue(response.contains("[OK]"), "Unknown wrong");
        response=sendCommandToServer("INSERT INTO mask1 VALUES ('li',FALSE,'male');");
        assertTrue(response.contains("[OK]"), "Unknown wrong");
        response=sendCommandToServer("INSERT INTO mask1 VALUES ('li',false,'male');");
        assertTrue(response.contains("[OK]"), "Unknown wrong");
        response=sendCommandToServer("INSERT INTO mask1 VALUES ('li',NULL,'male');");
        assertTrue(response.contains("[OK]"), "Unknown wrong");
        response=sendCommandToServer("INSERT INTO mask1 VALUES ('li',null,'male');");
        assertTrue(response.contains("[OK]"), "Unknown wrong");
        response=sendCommandToServer("select * from mask1;");
        assertTrue(response.contains("[OK]\n" +
                "id\tname\tage\tgender\t\n" +
                "1\tli\t20\tmale\t\n" +
                "2\tli\t-20\tmale\t\n" +
                "3\tli\t20.1\tmale\t\n" +
                "4\tli\t-20.1\tmale\t\n" +
                "5\tli\tTRUE\tmale\t\n" +
                "6\tli\ttrue\tmale\t\n" +
                "7\tli\tFALSE\tmale\t\n" +
                "8\tli\tfalse\tmale\t\n" +
                "9\tli\tNULL\tmale\t\n" +
                "10\tli\tnull\tmale"), "Unknown wrong");
        sendCommandToServer("alter table mask1 drop name;");
        response=sendCommandToServer("select * from mask1;");
        assertTrue(response.contains("[OK]\n" +
                "id\tage\tgender\t\n" +
                "1\t20\tmale\t\n" +
                "2\t-20\tmale\t\n" +
                "3\t20.1\tmale\t\n" +
                "4\t-20.1\tmale\t\n" +
                "5\tTRUE\tmale\t\n" +
                "6\ttrue\tmale\t\n" +
                "7\tFALSE\tmale\t\n" +
                "8\tfalse\tmale\t\n" +
                "9\tNULL\tmale\t\n" +
                "10\tnull\tmale"), "Unknown wrong");
        sendCommandToServer("alter table mask1 add name;");
        response=sendCommandToServer("select * from mask1;");
        assertTrue(response.contains("[OK]\n" +
                "id\tage\tgender\tname\t\n" +
                "1\t20\tmale\tNULL\t\n" +
                "2\t-20\tmale\tNULL\t\n" +
                "3\t20.1\tmale\tNULL\t\n" +
                "4\t-20.1\tmale\tNULL\t\n" +
                "5\tTRUE\tmale\tNULL\t\n" +
                "6\ttrue\tmale\tNULL\t\n" +
                "7\tFALSE\tmale\tNULL\t\n" +
                "8\tfalse\tmale\tNULL\t\n" +
                "9\tNULL\tmale\tNULL\t\n" +
                "10\tnull\tmale\tNULL"), "Unknown wrong");
        sendCommandToServer("alter table mask1 add parent;");
        response=sendCommandToServer("insert into mask1 values (22,'male','lee');");
        assertTrue(response.contains("[ERROR]"), "Too less attribute");
        response=sendCommandToServer("insert into mask1 values (22,'male','lee','zhi','xiang');");
        assertTrue(response.contains("[ERROR]"), "Too many attribute");
        response=sendCommandToServer("insert into mask1 values (22,'male','lee','zhi');");
        assertTrue(response.contains("OK"), "Unknown wrong.");
        response=sendCommandToServer("select * from mask1;");
        assertTrue(response.contains("[OK]\n" +
                "id\tage\tgender\tname\tparent\t\n" +
                "1\t20\tmale\tNULL\tNULL\t\n" +
                "2\t-20\tmale\tNULL\tNULL\t\n" +
                "3\t20.1\tmale\tNULL\tNULL\t\n" +
                "4\t-20.1\tmale\tNULL\tNULL\t\n" +
                "5\tTRUE\tmale\tNULL\tNULL\t\n" +
                "6\ttrue\tmale\tNULL\tNULL\t\n" +
                "7\tFALSE\tmale\tNULL\tNULL\t\n" +
                "8\tfalse\tmale\tNULL\tNULL\t\n" +
                "9\tNULL\tmale\tNULL\tNULL\t\n" +
                "10\tnull\tmale\tNULL\tNULL\t\n" +
                "11\t22\tmale\tlee\tzhi"), "Unknown wrong.");
        sendCommandToServer("DROP DATABASE "+randomName+";");
    }

    @Test
    public void testBasicSelect() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE mask1 (name,age,gender);");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',+20,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',-20,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',+20.1,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',-20.1,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',TRUE,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',true,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',FALSE,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',false,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',NULL,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',null,'male');");
        String response = sendCommandToServer("SELECT * FROM mask1 WHERE name=='li';");
        assertTrue(response.contains("[OK]\n" +
                "id\tname\tage\tgender\t\n" +
                "1\tli\t20\tmale\t\n" +
                "2\tli\t-20\tmale\t\n" +
                "3\tli\t20.1\tmale\t\n" +
                "4\tli\t-20.1\tmale\t\n" +
                "5\tli\tTRUE\tmale\t\n" +
                "6\tli\ttrue\tmale\t\n" +
                "7\tli\tFALSE\tmale\t\n" +
                "8\tli\tfalse\tmale\t\n" +
                "9\tli\tNULL\tmale\t\n" +
                "10\tli\tnull\tmale"), "Unknown wrong");
        response = sendCommandToServer("SELECT name,name,id,id FROM mask1 WHERE name=='li';");
        assertTrue(response.contains("[OK]\n" +
                "name\tname\tid\tid\t\n" +
                "li\tli\t1\t1\t\n" +
                "li\tli\t2\t2\t\n" +
                "li\tli\t3\t3\t\n" +
                "li\tli\t4\t4\t\n" +
                "li\tli\t5\t5\t\n" +
                "li\tli\t6\t6\t\n" +
                "li\tli\t7\t7\t\n" +
                "li\tli\t8\t8\t\n" +
                "li\tli\t9\t9\t\n" +
                "li\tli\t10\t10"),"");
        response = sendCommandToServer("SELECT name,id,gender,age FROM mask1 WHERE name=='li' and gender=='male';");
        assertTrue(response.contains("[OK]\n" +
                "name\tid\tgender\tage\t\n" +
                "li\t1\tmale\t20\t\n" +
                "li\t2\tmale\t-20\t\n" +
                "li\t3\tmale\t20.1\t\n" +
                "li\t4\tmale\t-20.1\t\n" +
                "li\t5\tmale\tTRUE\t\n" +
                "li\t6\tmale\ttrue\t\n" +
                "li\t7\tmale\tFALSE\t\n" +
                "li\t8\tmale\tfalse\t\n" +
                "li\t9\tmale\tNULL\t\n" +
                "li\t10\tmale\tnull"), "Unknown wrong");
        response = sendCommandToServer("SELECT name,id,gender,age FROM mask1 WHERE age>=20 or age==true or age==false and id==7;");
        assertTrue(response.contains("[OK]\n" +
                "name\tid\tgender\tage\t\n" +
                "li\t1\tmale\t20\t\n" +
                "li\t3\tmale\t20.1\t\n" +
                "li\t5\tmale\tTRUE\t\n" +
                "li\t6\tmale\ttrue\t\n" +
                "li\t7\tmale\tFALSE"), "Unknown wrong");
        response = sendCommandToServer("SELECT name,id,gender,age FROM mask1 WHERE (age>=20 or age==true) or (age==false and id==7);");
        assertTrue(response.contains("[OK]\n" +
                "name\tid\tgender\tage\t\n" +
                "li\t1\tmale\t20\t\n" +
                "li\t3\tmale\t20.1\t\n" +
                "li\t5\tmale\tTRUE\t\n" +
                "li\t6\tmale\ttrue\t\n" +
                "li\t7\tmale\tFALSE"), "Unknown wrong");
        response = sendCommandToServer("SELECT name,id,gender,age FROM mask1 WHERE (name like 'l') and (id>4);");
        assertTrue(response.contains("[OK]\n" +
                "name\tid\tgender\tage\t\n" +
                "li\t5\tmale\tTRUE\t\n" +
                "li\t6\tmale\ttrue\t\n" +
                "li\t7\tmale\tFALSE\t\n" +
                "li\t8\tmale\tfalse\t\n" +
                "li\t9\tmale\tNULL"), "Unknown wrong");
        response = sendCommandToServer("SELECT name,id,gender,age FROM mask1 WHERE (name like 'L') or (id==4);");
        assertTrue(response.contains("[OK]\n" +
                "name\tid\tgender\tage\t\n" +
                "li\t4\tmale\t-20.1"), "Unknown wrong");
        response = sendCommandToServer("SELECT name,id,gender,age FROM mask1 WHERE (name like null) or (id>=false);");
        assertTrue(response.contains("[OK]\n" +
                "name\tid\tgender\tage"), "Unknown wrong");
        sendCommandToServer("update mask1 set name=1,name=2,name=3 where name=='li';");
        response=sendCommandToServer("SELECT name,name,id,id FROM mask1 WHERE name==3;");
        assertTrue(response.contains("[OK]\n" +
                "name\tname\tid\tid\t\n" +
                "3\t3\t1\t1\t\n" +
                "3\t3\t2\t2\t\n" +
                "3\t3\t3\t3\t\n" +
                "3\t3\t4\t4\t\n" +
                "3\t3\t5\t5\t\n" +
                "3\t3\t6\t6\t\n" +
                "3\t3\t7\t7\t\n" +
                "3\t3\t8\t8\t\n" +
                "3\t3\t9\t9\t\n" +
                "3\t3\t10\t10"), "Unknown wrong");
        response=sendCommandToServer("SELECT name,name,id,id FROM mask1 WHERE name<=3;");
        assertTrue(response.contains("[OK]\n" +
                "name\tname\tid\tid\t\n" +
                "3\t3\t1\t1\t\n" +
                "3\t3\t2\t2\t\n" +
                "3\t3\t3\t3\t\n" +
                "3\t3\t4\t4\t\n" +
                "3\t3\t5\t5\t\n" +
                "3\t3\t6\t6\t\n" +
                "3\t3\t7\t7\t\n" +
                "3\t3\t8\t8\t\n" +
                "3\t3\t9\t9\t\n" +
                "3\t3\t10\t10"), "Unknown wrong");
        response=sendCommandToServer("SELECT name,name,id,id FROM mask1 WHERE name>=3;");
        assertTrue(response.contains("[OK]\n" +
                "name\tname\tid\tid\t\n" +
                "3\t3\t1\t1\t\n" +
                "3\t3\t2\t2\t\n" +
                "3\t3\t3\t3\t\n" +
                "3\t3\t4\t4\t\n" +
                "3\t3\t5\t5\t\n" +
                "3\t3\t6\t6\t\n" +
                "3\t3\t7\t7\t\n" +
                "3\t3\t8\t8\t\n" +
                "3\t3\t9\t9\t\n" +
                "3\t3\t10\t10"), "Unknown wrong");
        response=sendCommandToServer("SELECT name,name,id,id FROM mask1 WHERE name<=3.0;");
        assertTrue(response.contains("[OK]\n" +
                "name\tname\tid\tid\t\n" +
                "3\t3\t1\t1\t\n" +
                "3\t3\t2\t2\t\n" +
                "3\t3\t3\t3\t\n" +
                "3\t3\t4\t4\t\n" +
                "3\t3\t5\t5\t\n" +
                "3\t3\t6\t6\t\n" +
                "3\t3\t7\t7\t\n" +
                "3\t3\t8\t8\t\n" +
                "3\t3\t9\t9\t\n" +
                "3\t3\t10\t10"), "Unknown wrong");
        response = sendCommandToServer("SELECT * FROM mask1 where genderlike'male';");
        assertTrue(response.contains("[OK]\n" +
                "id\tname\tage\tgender\t\n" +
                "1\t3\t20\tmale\t\n" +
                "2\t3\t-20\tmale\t\n" +
                "3\t3\t20.1\tmale\t\n" +
                "4\t3\t-20.1\tmale\t\n" +
                "5\t3\tTRUE\tmale\t\n" +
                "6\t3\ttrue\tmale\t\n" +
                "7\t3\tFALSE\tmale\t\n" +
                "8\t3\tfalse\tmale\t\n" +
                "9\t3\tNULL\tmale\t\n" +
                "10\t3\tnull\tmale"), "Unknown wrong");
        response = sendCommandToServer("SELECT * FROM mask1 where gender like 'male';");
        assertTrue(response.contains("[OK]\n" +
                "id\tname\tage\tgender\t\n" +
                "1\t3\t20\tmale\t\n" +
                "2\t3\t-20\tmale\t\n" +
                "3\t3\t20.1\tmale\t\n" +
                "4\t3\t-20.1\tmale\t\n" +
                "5\t3\tTRUE\tmale\t\n" +
                "6\t3\ttrue\tmale\t\n" +
                "7\t3\tFALSE\tmale\t\n" +
                "8\t3\tfalse\tmale\t\n" +
                "9\t3\tNULL\tmale\t\n" +
                "10\t3\tnull\tmale"), "Unknown wrong");
        response = sendCommandToServer("SELECT * FROM mask1 where genderliKe'male';");
        assertTrue(response.contains("[OK]\n" +
                "id\tname\tage\tgender\t\n" +
                "1\t3\t20\tmale\t\n" +
                "2\t3\t-20\tmale\t\n" +
                "3\t3\t20.1\tmale\t\n" +
                "4\t3\t-20.1\tmale\t\n" +
                "5\t3\tTRUE\tmale\t\n" +
                "6\t3\ttrue\tmale\t\n" +
                "7\t3\tFALSE\tmale\t\n" +
                "8\t3\tfalse\tmale\t\n" +
                "9\t3\tNULL\tmale\t\n" +
                "10\t3\tnull\tmale"), "Unknown wrong");
        response = sendCommandToServer("SELECT * FROM mask1 where genderLike'male';");
        assertTrue(response.contains("[OK]\n" +
                "id\tname\tage\tgender\t\n" +
                "1\t3\t20\tmale\t\n" +
                "2\t3\t-20\tmale\t\n" +
                "3\t3\t20.1\tmale\t\n" +
                "4\t3\t-20.1\tmale\t\n" +
                "5\t3\tTRUE\tmale\t\n" +
                "6\t3\ttrue\tmale\t\n" +
                "7\t3\tFALSE\tmale\t\n" +
                "8\t3\tfalse\tmale\t\n" +
                "9\t3\tNULL\tmale\t\n" +
                "10\t3\tnull\tmale"), "Unknown wrong");
        response = sendCommandToServer("SELECT * FROM mask1 where genderLIke'male';");
        assertTrue(response.contains("[OK]\n" +
                "id\tname\tage\tgender\t\n" +
                "1\t3\t20\tmale\t\n" +
                "2\t3\t-20\tmale\t\n" +
                "3\t3\t20.1\tmale\t\n" +
                "4\t3\t-20.1\tmale\t\n" +
                "5\t3\tTRUE\tmale\t\n" +
                "6\t3\ttrue\tmale\t\n" +
                "7\t3\tFALSE\tmale\t\n" +
                "8\t3\tfalse\tmale\t\n" +
                "9\t3\tNULL\tmale\t\n" +
                "10\t3\tnull\tmale"), "Unknown wrong");
        response = sendCommandToServer("SELECT * FROM mask1 where genderLIKe'male';");
        assertTrue(response.contains("[OK]\n" +
                "id\tname\tage\tgender\t\n" +
                "1\t3\t20\tmale\t\n" +
                "2\t3\t-20\tmale\t\n" +
                "3\t3\t20.1\tmale\t\n" +
                "4\t3\t-20.1\tmale\t\n" +
                "5\t3\tTRUE\tmale\t\n" +
                "6\t3\ttrue\tmale\t\n" +
                "7\t3\tFALSE\tmale\t\n" +
                "8\t3\tfalse\tmale\t\n" +
                "9\t3\tNULL\tmale\t\n" +
                "10\t3\tnull\tmale"), "Unknown wrong");
        response = sendCommandToServer("SELECT * FROM mask1 where genderLIKE'male';");
        assertTrue(response.contains("[OK]\n" +
                "id\tname\tage\tgender\t\n" +
                "1\t3\t20\tmale\t\n" +
                "2\t3\t-20\tmale\t\n" +
                "3\t3\t20.1\tmale\t\n" +
                "4\t3\t-20.1\tmale\t\n" +
                "5\t3\tTRUE\tmale\t\n" +
                "6\t3\ttrue\tmale\t\n" +
                "7\t3\tFALSE\tmale\t\n" +
                "8\t3\tfalse\tmale\t\n" +
                "9\t3\tNULL\tmale\t\n" +
                "10\t3\tnull\tmale"), "Unknown wrong");
        response = sendCommandToServer("SELECT name1 FROM mask1 WHERE name=='li';");
        assertTrue(response.contains("[ERROR]"), "attribute name1 does not exist");
        response = sendCommandToServer("SELECT name1,age FROM mask1 WHERE name=='li';");
        assertTrue(response.contains("[ERROR]"), "attribute name1 does not exist");
        response = sendCommandToServer("SELECT age,name1 FROM mask1 WHERE name=='li';");
        assertTrue(response.contains("[ERROR]"), "attribute name1 does not exist");
        response = sendCommandToServer("SELECT age,name1,gender FROM mask1 WHERE name=='li';");
        assertTrue(response.contains("[ERROR]"), "attribute name1 does not exist");
        response = sendCommandToServer("SELECT age,name1,gender, FROM mask1 WHERE name=='li';");
        assertTrue(response.contains("[ERROR]"), "attribute list wrong");
        response = sendCommandToServer("SELECT age,name1 gender, FROM mask1 WHERE name=='li';");
        assertTrue(response.contains("[ERROR]"), "attribute list wrong");
        response = sendCommandToServer("SELECT age,name1, ,gender FROM mask1 WHERE name=='li';");
        assertTrue(response.contains("[ERROR]"), "attribute list wrong");
        response = sendCommandToServer("SELECT age,name&,gender FROM mask1 WHERE name=='li';");
        assertTrue(response.contains("[ERROR]"), "attribute list wrong");
        response = sendCommandToServer("SELECT age,name,gender FROM mask1 WHERE ((name=='li');");
        assertTrue(response.contains("[ERROR]"), "Condition wrong");
        response = sendCommandToServer("SELECT age,name,gender FROM mask1 WHERE ((name=='li'));");
        assertTrue(response.contains("[ERROR]"), "Condition wrong");
        response = sendCommandToServer("SELECT age,name,gender FROM mask1 WHERE (name=='li' and name1==a);");
        assertTrue(response.contains("[ERROR]"), "Condition wrong");
        response = sendCommandToServer("SELECT age,name,gender FROM mask1 WHERE (name=='li' and name&==a);");
        assertTrue(response.contains("[ERROR]"), "Condition wrong");
        response = sendCommandToServer("SELECT age,name,gender FROM mask1 WHERE (name=='li' & name=='a');");
        assertTrue(response.contains("[ERROR]"), "Condition wrong");
        response = sendCommandToServer("SELECT age,name,gender FROM mask1 WHERE (name=='li' and name1=='a');");
        assertTrue(response.contains("[ERROR]"), "Condition wrong");
        response = sendCommandToServer("SELECT age,name,gender FROM mask1 WHERE ((name=='li') and name=='a');");
        assertTrue(response.contains("[OK]"), "Unknown wrong");
        response = sendCommandToServer("SELECT age,name,gender FROM mask1 WHERE ((name=='li') and name=='a') and age>=1;");
        assertTrue(response.contains("[OK]"), "Unknown wrong");
        response = sendCommandToServer("SELECT age,name,gender FROM mask1 WHERE (name=='li' and name1=='a');;");
        assertTrue(response.contains("[ERROR]"), "Condition wrong");
        response = sendCommandToServer("SELECT age,name,gender FROM mask1 WHERE (name=='li' and) name1=='a');");
        assertTrue(response.contains("[ERROR]"), "Condition wrong");
        sendCommandToServer("DROP DATABASE "+randomName+";");
    }
    @Test
    public void testBasicUpdate() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE mask1 (name,age,gender);");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',+20,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',-20,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',+20.1,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',-20.1,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',TRUE,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',true,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',FALSE,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',false,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',NULL,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',null,'male');");
        String response=sendCommandToServer("update mask1 set id=2 where name=='li';");
        assertTrue(response.contains("[ERROR]"), "Cannot update id.");
        response=sendCommandToServer("update mask2 set id=2 where name=='li';");
        assertTrue(response.contains("[ERROR]"), "Current table does not exists.");
        response=sendCommandToServer("update mask1 set1 id=2 where name=='li';");
        assertTrue(response.contains("[ERROR]"), "Table name must followed by set.");
        response=sendCommandToServer("update mask1 set name1=1,name=2,name=3 where name=='li';");
        assertTrue(response.contains("[ERROR]"), "No such attribute in this table.");
        response=sendCommandToServer("update mask1 set name=1,name=2,name==3 where name=='li';");
        assertTrue(response.contains("[ERROR]"), "Attribute value and attribute name must be separate by '='.");
        response=sendCommandToServer("update mask1 set name=1,name=2,name=3 where name=='li';");
        assertTrue(response.contains("[OK]"), "Unknown wrong.");
        sendCommandToServer("DROP DATABASE "+randomName+";");
    }

    @Test
    public void testBasicDelete() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE mask1 (name,age,gender);");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',+20,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',-20,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',+20.1,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',-20.1,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',TRUE,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',true,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',FALSE,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',false,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',NULL,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',null,'male');");
        String response = sendCommandToServer("DELETE FROM mask1 where age==true;");
        assertTrue(response.contains("[OK]"), "Unknown wrong.");
        response=sendCommandToServer("SELECT * FROM mask1;");
        assertTrue(response.contains("[OK]\n" +
                "id\tname\tage\tgender\t\n" +
                "1\tli\t20\tmale\t\n" +
                "2\tli\t-20\tmale\t\n" +
                "3\tli\t20.1\tmale\t\n" +
                "4\tli\t-20.1\tmale\t\n" +
                "7\tli\tFALSE\tmale\t\n" +
                "8\tli\tfalse\tmale\t\n" +
                "9\tli\tNULL\tmale\t\n" +
                "10\tli\tnull\tmale"), "Unknown wrong.");
        response = sendCommandToServer("DELETE FROM mask1 where age==false;");
        assertTrue(response.contains("[OK]"), "Unknown wrong.");
        response=sendCommandToServer("SELECT * FROM mask1;");
        assertTrue(response.contains("[OK]\n" +
                "id\tname\tage\tgender\t\n" +
                "1\tli\t20\tmale\t\n" +
                "2\tli\t-20\tmale\t\n" +
                "3\tli\t20.1\tmale\t\n" +
                "4\tli\t-20.1\tmale\t\n" +
                "9\tli\tNULL\tmale\t\n" +
                "10\tli\tnull\tmale"), "Unknown wrong.");
        response = sendCommandToServer("DELETE FROM mask1 where age>=+20;");
        assertTrue(response.contains("[OK]"), "Unknown wrong.");
        response=sendCommandToServer("SELECT * FROM mask1;");
        assertTrue(response.contains("[OK]\n" +
                "id\tname\tage\tgender\t\n" +
                "2\tli\t-20\tmale\t\n" +
                "4\tli\t-20.1\tmale\t\n" +
                "9\tli\tNULL\tmale\t\n" +
                "10\tli\tnull\tmale\t"), "Unknown wrong.");
        response = sendCommandToServer("DELETE FROM mask1 where age<=-20;");
        assertTrue(response.contains("[OK]"), "Unknown wrong.");
        response=sendCommandToServer("SELECT * FROM mask1;");
        assertTrue(response.contains("[OK]\n" +
                "id\tname\tage\tgender\t\n" +
                "9\tli\tNULL\tmale\t\n" +
                "10\tli\tnull\tmale\t"), "Unknown wrong.");
        response = sendCommandToServer("DELETE FROM mask1 where age==null;");
        assertTrue(response.contains("[OK]"), "Unknown wrong.");
        response=sendCommandToServer("SELECT * FROM mask1;");
        assertTrue(response.contains("[OK]\n" +
                "id\tname\tage\tgender\t\n"), "Unknown wrong.");
        sendCommandToServer("DROP DATABASE "+randomName+";");
    }


    @Test
    public void testBasicJoin() {
        String randomName = generateRandomName();
        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        sendCommandToServer("USE " + randomName + ";");
        sendCommandToServer("CREATE TABLE mask1 (name,age,gender);");
        sendCommandToServer("INSERT INTO mask1 VALUES ('li',20,'male');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('zhi',21,'female');");
        sendCommandToServer("INSERT INTO mask1 VALUES ('xiang',22,'male');");
        sendCommandToServer("CREATE TABLE mask2 (name,age,gender);");
        sendCommandToServer("INSERT INTO mask2 VALUES ('zhao',20,'male');");
        sendCommandToServer("INSERT INTO mask2 VALUES ('wen',21,'female');");
        sendCommandToServer("INSERT INTO mask2 VALUES ('jie',22,'male');");
        sendCommandToServer("INSERT INTO mask2 VALUES ('gao',22,null);");
        String response = sendCommandToServer("join mask1 and mask2 on age and age;");
        assertTrue(response.contains("[OK]\n" +
                "id\tmask1.name\tmask1.gender\tmask2.name\tmask2.gender\n" +
                "1\tli\tmale\tzhao\tmale\n" +
                "2\tzhi\tfemale\twen\tfemale\n" +
                "3\txiang\tmale\tjie\tmale\n" +
                "4\txiang\tmale\tgao\tnull"), "Unknown wrong.");
        response = sendCommandToServer("join mask1 and mask2 on gender and gender;");
        assertTrue(response.contains("[OK]\n" +
                "id\tmask1.name\tmask1.age\tmask2.name\tmask2.age\n" +
                "1\tli\t20\tzhao\t20\n" +
                "2\tli\t20\tjie\t22\n" +
                "3\tzhi\t21\twen\t21\n" +
                "4\txiang\t22\tzhao\t20\n" +
                "5\txiang\t22\tjie\t22"), "Unknown wrong.");
        response = sendCommandToServer("join mask1 and mask2 on id and id;");
        assertTrue(response.contains("[OK]\n" +
                "id\tmask1.name\tmask1.age\tmask1.gender\tmask2.name\tmask2.age\tmask2.gender\n" +
                "1\tli\t20\tmale\tzhao\t20\tmale\n" +
                "2\tzhi\t21\tfemale\twen\t21\tfemale\n" +
                "3\txiang\t22\tmale\tjie\t22\tmale"), "Unknown wrong.");
        response = sendCommandToServer("join mask1 and mask2 on id and age;");
        assertTrue(response.contains("[OK]\n" +
                "id\tmask1.name\tmask1.age\tmask1.gender\tmask2.name\tmask2.gender"), "Unknown wrong.");
        response = sendCommandToServer("join mask1 and mask2 on name and gender1;");
        assertTrue(response.contains("[ERROR]"), "Unknown wrong.");
        response = sendCommandToServer("join mask1 and mask2 on name1 and gender;");
        assertTrue(response.contains("[ERROR]"), "Unknown wrong.");
        response = sendCommandToServer("join mask3 and mask2 on name and gender;");
        assertTrue(response.contains("[ERROR]"), "Unknown wrong.");
        response = sendCommandToServer("join mask1 and mask3 on name and gender;");
        assertTrue(response.contains("[ERROR]"), "Unknown wrong.");
        response = sendCommandToServer("join mask1 and mask2 on1 name and gender;");
        assertTrue(response.contains("[ERROR]"), "Unknown wrong.");
        response = sendCommandToServer("join mask1 and mask3 on name and@ gender;");
        assertTrue(response.contains("[ERROR]"), "Unknown wrong.");
        response = sendCommandToServer("join mask1 and@ mask2 on name and gender1;");
        assertTrue(response.contains("[ERROR]"), "Unknown wrong.");
        sendCommandToServer("DROP DATABASE "+randomName+";");
    }
}
