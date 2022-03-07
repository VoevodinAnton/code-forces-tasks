package dancingSchool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainClassDancingSchool {

    public static void main(String[] args){
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);

        Pair<Dancer, DancingSchools> dancerAndDancingSchools = DancerService.readDancerAndDancingSchoolsFromStream(inputStreamReader);

        Dancer dancer = dancerAndDancingSchools.first;
        DancingSchools dancingSchools = dancerAndDancingSchools.second;
        DancerService dancerService = new DancerService(dancer);
        //System.out.println("moves to learn: " + dancer.getDanceMovesToLearn());
        //DancingSchools remainingDancingSchool = new DancingSchools(dancingSchools);
        int p = 0;

        StringBuilder schoolsWithTwoOrMoreNewDancingMove = new StringBuilder();
        int countSchoolsWithTwoOrMoreNewDancingMove = 0;
        for (DancingSchool dancingSchool: dancingSchools){
            List<String> newDanceMovesForDancer = dancerService.getNewDanceMovesForDancer(dancingSchool);

            if (newDanceMovesForDancer.size() >= 2) {
                dancer.getDanceMovesToLearn().removeAll(newDanceMovesForDancer);
                //System.out.println("dancing school " + dancingSchool.getId() + " moves to learn " + dancer.getDanceMovesToLearn());
                //remainingDancingSchool.remove(dancingSchool);
                schoolsWithTwoOrMoreNewDancingMove.append(dancingSchool.getId()).append(" ");
                countSchoolsWithTwoOrMoreNewDancingMove++;
            }
            /*
            else if (newDanceMovesForDancer.size() == 0) {
                remainingDancingSchool.remove(dancingSchool);
            }
             */
        }

        if (dancer.getDanceMovesToLearn().isEmpty()){
            p = 2;
            printResult(p, schoolsWithTwoOrMoreNewDancingMove.toString(), countSchoolsWithTwoOrMoreNewDancingMove, null, 0);
            return;
        }
        StringBuilder schoolsWithOneNewDancingMove = new StringBuilder();
        int countSchoolsWithOneNewDancingMove = 0;
        for (DancingSchool dancingSchool: dancingSchools){
            List<String> newDanceMovesForDancer = dancerService.getNewDanceMovesForDancer(dancingSchool);

            if (newDanceMovesForDancer.size() == 1){
                dancer.getDanceMovesToLearn().removeAll(newDanceMovesForDancer);
                schoolsWithOneNewDancingMove.append(dancingSchool.getId()).append(" ");
                countSchoolsWithOneNewDancingMove++;
                //System.out.println("1 dancing school " + dancingSchool.getId() + " moves to learn " + dancer.getDanceMovesToLearn());
            }
        }

        if (dancer.getDanceMovesToLearn().isEmpty()){
            p = 1;
        }

        printResult(p, schoolsWithTwoOrMoreNewDancingMove.toString(), countSchoolsWithTwoOrMoreNewDancingMove, schoolsWithOneNewDancingMove.toString(), countSchoolsWithOneNewDancingMove);
    }

    public static void printResult(int p, String schoolsWithTwoOrMoreNewDancingMove, int countSchoolsWithTwoOrMoreNewDancingMove, String schoolsWithOneNewDancingMove, int  countSchoolsWithOneNewDancingMove){
        if (p == 0){
            System.out.println(p);
            return;
        }
        if (p > 0){
            System.out.println(p);
            System.out.println(countSchoolsWithTwoOrMoreNewDancingMove);
            if (schoolsWithTwoOrMoreNewDancingMove.isEmpty()){
                System.out.println(0);
            }else {
                System.out.println(schoolsWithTwoOrMoreNewDancingMove);
            }
            if (p == 1){
                System.out.println(countSchoolsWithOneNewDancingMove);
                System.out.println(schoolsWithOneNewDancingMove);
            }
        }
    }
}

class DancingSchool{
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private int id;
    private List<String> danceMoves;

    public DancingSchool(List<String> danceMoves){
        this.id = ID_GENERATOR.getAndIncrement();
        this.danceMoves = danceMoves;
    }

    public int getId() {
        return id;
    }

    public List<String> getDanceMoves() {
        return danceMoves;
    }

    @Override
    public String toString() {
        return "DancingSchool{" +
                "id=" + id +
                ", danceMoves=" + danceMoves +
                '}';
    }
}

class DancingSchools extends ArrayList<DancingSchool>{

}

class Dancer{
    private List<String> danceMovesToLearn;

    public Dancer(List<String> danceMovesToLearn) {
        this.danceMovesToLearn = danceMovesToLearn;
    }

    public List<String> getDanceMovesToLearn() {
        return danceMovesToLearn;
    }

    @Override
    public String toString() {
        return "Dancer{" +
                "danceMovesToLearn=" + danceMovesToLearn +
                '}';
    }
}

class DancerService{
    private Dancer dancer;

    public DancerService(Dancer dancer) {
        this.dancer = dancer;
    }

    public static Pair<Dancer, DancingSchools> readDancerAndDancingSchoolsFromStream(InputStreamReader inputStreamReader) {
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        List<String> danceMovesToLearn = null;
        try {
            bufferedReader.readLine();
            danceMovesToLearn = new ArrayList<>(Arrays.asList(bufferedReader.readLine().split(" ")));
        } catch (IOException ex){
            ex.printStackTrace();
        }
        Dancer dancer = new Dancer(danceMovesToLearn);

        DancingSchools dancingSchools = new DancingSchools();
        try{
            int numbersOfDancingSchools = Integer.parseInt(bufferedReader.readLine());

            for (int i = 0; i < numbersOfDancingSchools; i++){
                String[] line = bufferedReader.readLine().split(" ");
                dancingSchools.add(new DancingSchool(new ArrayList<>(Arrays.asList(line))));
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }

        return new Pair<>(dancer, dancingSchools);
    }

    public List<String> getNewDanceMovesForDancer(DancingSchool dancingSchool){
        return dancingSchool.getDanceMoves().stream()
                .filter(dancingMove -> dancer.getDanceMovesToLearn().contains(dancingMove)).collect(Collectors.toList());
    }
}

class Pair<T1, T2>{
    public T1 first;
    public T2 second;
    public Pair(T1 _first, T2 _second) {
        first = _first;
        second = _second;
    }
}


