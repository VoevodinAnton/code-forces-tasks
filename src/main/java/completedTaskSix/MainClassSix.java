package completedTaskSix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class MainClassSix {
    public static void main(String[] args) {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);

        Films films = FilmService.readFilmsFromStream(inputStreamReader);

        if (films.isEmpty()) {
            return;
        }

        int maxInterest = 0;
        Film firstFilm = null;
        Film secondFilm = null;
        int size = films.size();

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Film film = films.get(i);
                Film nextFilm = films.get(j);

                int interest = film.getInterest() + nextFilm.getInterest();
                if ((film.getEndTime() <= nextFilm.getStartTime()
                        || nextFilm.getEndTime() <= film.getStartTime())
                        && interest > maxInterest) {
                    maxInterest = interest;
                    firstFilm = film;
                    secondFilm = nextFilm;
                }
            }
        }

        printResult(maxInterest, firstFilm, secondFilm);
    }

    public static void printResult(int interest, Film firstFilm, Film secondFilm) {
        if (interest == 0) {
            System.out.println(interest);
            System.out.print(-1 + " " + -1);
            return;
        }

        if (firstFilm.getId() < secondFilm.getId()) {
            System.out.println(interest);
            System.out.print(firstFilm.getId() + " " + secondFilm.getId());
        } else {
            System.out.println(interest);
            System.out.print(secondFilm.getId() + " " + firstFilm.getId());
        }
    }
}

class Film {
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private int id;
    private int startTime;
    private int endTime;
    private int interest;

    public Film(int startTime, int endTime, int interest) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.startTime = startTime;
        this.endTime = endTime;
        this.interest = interest;
    }

    public int getId() {
        return id;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }


    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", interest=" + interest +
                '}';
    }
}

class Films extends ArrayList<Film> {

}

class FilmService {
    public static Films readFilmsFromStream(InputStreamReader inputStreamReader) {
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        Films films = new Films();
        try {
            int numbersOfFilms = Integer.parseInt(bufferedReader.readLine());
            for (int i = 0; i < numbersOfFilms; i++) {
                String[] line = bufferedReader.readLine().split(" ");
                films.add(new Film(Integer.parseInt(line[0].trim()), Integer.parseInt(line[1].trim()), Integer.parseInt(line[2].trim())));
            }
            bufferedReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return films == null ? (Films) Collections.EMPTY_LIST : films;
    }
}
