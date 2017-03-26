package ch.heigvd.res.labs.roulette.net.client;

import ch.heigvd.res.labs.roulette.data.Student;
import ch.heigvd.res.labs.roulette.net.protocol.RouletteV2Protocol;
import ch.heigvd.schoolpulse.TestAuthor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This class contains automated tests to validate the client and the server
 * implementation of the Roulette Protocol (version 1)
 *
 * @author Zeller Quentin
 */
public class RouletteV2qrzellerTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public EphemeralClientServerPair roulettePair = new EphemeralClientServerPair(RouletteV2Protocol.VERSION);


    @Test
    @TestAuthor(githubId = "qrzeller")
    public void checkTheServerParameter() throws IOException{

        assert roulettePair.getServer().getPort() == RouletteV2Protocol.DEFAULT_PORT
               &&RouletteV2Protocol.DEFAULT_PORT == 2613
                : "The server should have the correct port...";

        assert roulettePair.getClient().getProtocolVersion() == RouletteV2Protocol.VERSION:
                "The protocol version should be V2";
    }


    @Test
    @TestAuthor(githubId = "qrzeller")
    public void theServerShouldNotHaveStudentAndAfterClean() throws IOException {

        assert roulettePair.getClient().getNumberOfStudents() == 0:"After clear the DB must be clean";

        IRouletteV2Client client = (RouletteV2ClientImpl) roulettePair.getClient();

        client.clearDataStore();//clear empty

        client.loadStudent("123 21");
        client.loadStudent("Abc De");
        client.loadStudent("Jean-mich dupuis|@#¼||");

        client.clearDataStore();

        assert client.getNumberOfStudents() == 0:"After clear the DB must be clean";
        client.clearDataStore();//clear already cleared
    }


    @Test
    @TestAuthor(githubId = "qrzeller")
    public void weCouldGetTheListOfStudent() throws IOException {

        IRouletteV2Client client = (IRouletteV2Client) roulettePair.getClient();

        List<Student> handMadeStudent = new LinkedList<>();
        handMadeStudent.add(new Student("first student"));
        handMadeStudent.add(new Student("Second Student"));
        handMadeStudent.add(new Student("THIRD STUDENT"));

        client.loadStudents(handMadeStudent);
        List<Student> students = client.listStudents();
        assert students.equals(handMadeStudent):
                "Student are not equals";
    }
}
