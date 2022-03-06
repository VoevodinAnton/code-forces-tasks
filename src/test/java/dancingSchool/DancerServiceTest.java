package dancingSchool;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DancerServiceTest {
    public static final String FILEPATH = "src/main/resources/dancing-school/Example1.txt";
    Dancer dancer;
    DancingSchools dancingSchools;
    DancerService dancerService;

    @BeforeMethod
    public void setUp() {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new FileReader(FILEPATH);
        } catch (IOException ex){
            ex.printStackTrace();
        }

        Pair<Dancer, DancingSchools> dancerAndDancingSchools = DancerService.readDancerAndDancingSchoolsFromStream(inputStreamReader);
        dancer = dancerAndDancingSchools.first;
        dancingSchools = dancerAndDancingSchools.second;
        dancerService = new DancerService(dancer);
    }

    @Test
    public void testReadDancerAndDancingSchoolsFromStream() {
        System.out.println(dancer);
        System.out.println(dancingSchools);
    }

    @Test
    public void testNumberOfNewDanceMoves() {

    }
}