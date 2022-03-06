package completedTaskFive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class MainClassFive {
    public static void main(String[] args) {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);

        Films films = FilmService.readFilmsFromStream(inputStreamReader);

        if (films.isEmpty()) {
            return;
        }

        Films watchedFilms = new Films();

        Film currentFilm = films.get(0);
        while (true) {
            if (!FilmService.isInterestingFilm(currentFilm, films)) {
                currentFilm = FilmService.getInterestingFilm(currentFilm, films);
            } else {
                watchedFilms.add(currentFilm);
                Film tempFilm = currentFilm;
                currentFilm = FilmService.getNextFilm(tempFilm, films);
                //System.out.println(currentFilm);
            }
            if (currentFilm == null) {
                break;
            }
        }
        printResult(watchedFilms);
    }

    public static void printResult(Films watchedFilms) {
        if (watchedFilms == null || watchedFilms.isEmpty()) {
            System.out.println(0);
        }
        System.out.println(watchedFilms.size());
        for (Film film : watchedFilms) {
            System.out.print(film.getId() + " ");
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return films == null ? (Films) Collections.EMPTY_LIST : films;
    }


    public static boolean isInterestingFilm(Film currentFilm, Films films) {
        return films.stream()
                .noneMatch(film -> !film.equals(currentFilm) && currentFilm.getStartTime() <= film.getStartTime() && film.getStartTime() < currentFilm.getEndTime() && film.getInterest() > currentFilm.getInterest());
    }

    public static Film getInterestingFilm(Film currentFilm, Films films) {
        return films.stream()
                .filter(film -> !film.equals(currentFilm) && currentFilm.getStartTime() <= film.getStartTime() && film.getStartTime() < currentFilm.getEndTime() && film.getInterest() > currentFilm.getInterest())
                .findFirst()
                .get();

    }

    public static Film getNextFilm(Film currentFilm, Films films) {
        return films.stream()
                .filter(film -> !film.equals(currentFilm) && film.getStartTime() >= currentFilm.getEndTime()).findFirst().orElse(null);
    }
}
