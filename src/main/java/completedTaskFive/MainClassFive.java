package completedTaskFive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
        do {
            if (!FilmService.isInterestingFilm(currentFilm, films)) {
                currentFilm = FilmService.getInterestingFilm(currentFilm, films);
            } else {
                watchedFilms.add(currentFilm);
                Film tempFilm = currentFilm;
                currentFilm = FilmService.getNextFilm(tempFilm, films);
                //System.out.println(currentFilm);
            }
        } while (currentFilm != null);
        printResult(watchedFilms);
    }

    public static void printResult(Films watchedFilms) {
        StringBuilder result = new StringBuilder();
        if (watchedFilms == null || watchedFilms.isEmpty()) {
            System.out.println(0);
            return;
        }
        result.append(watchedFilms.size()).append("\n");
        for (Film film : watchedFilms) {
            result.append(film.getId()).append(" ");
        }
        System.out.println(result);
    }
}

class Film {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private final int id;
    private final int startTime;
    private final int endTime;
    private final int interest;

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


    public int getEndTime() {
        return endTime;
    }

    public int getInterest() {
        return interest;
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

        return films;
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
