package completedTaskFour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainClassFour {
    public static void main(String[] args) {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);

        TranslatedWords translatedWords = TranslatedWordsService.readTaskFromStream(inputStreamReader);

        if (translatedWords == null || translatedWords.isEmpty()) {
            return;
        }

        Map<String, List<TranslatedWord>> translatedWordsByWord = TranslatedWordsService.getTranslatedWordsByWord(translatedWords);
        SortedSet<String> uniqueTranslatedWords = new TreeSet<>(translatedWordsByWord.keySet());
        System.out.println(uniqueTranslatedWords.size());
        for (String translatedWord : uniqueTranslatedWords) {
            int maxDistance = TranslatedWordsService.getMaxLengthBetweenCorrectlyTranslatedWords(translatedWordsByWord.get(translatedWord));
            int minDistance = TranslatedWordsService.getMinLengthBetweenIncorrectlyTranslatedWords((translatedWordsByWord.get(translatedWord)));
            printResult(translatedWord, maxDistance, minDistance);
        }
    }

    static void printResult(String translatedWord, int maxDistance, int minDistance) {
        StringBuilder line = new StringBuilder();
        line.append(translatedWord + " ");
        if (maxDistance == -1) {
            line.append("ND" + " ");
        } else {
            line.append(maxDistance + " ");
        }
        if (minDistance == -1) {
            line.append("ND");
        } else {
            line.append(minDistance);
        }
        System.out.println(line);
    }
}

class TranslatedWord {
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    private int id;
    private String word;
    private boolean isCorrectTranslation;

    public TranslatedWord(String word, boolean isCorrectTranslation) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.word = word;
        this.isCorrectTranslation = isCorrectTranslation;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isCorrectTranslation() {
        return isCorrectTranslation;
    }

    public void setCorrectTranslation(boolean correctTranslation) {
        isCorrectTranslation = correctTranslation;
    }

    @Override
    public String toString() {
        return "TranslatedWord{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", isCorrectTranslation=" + isCorrectTranslation +
                '}';
    }
}

class TranslatedWords extends ArrayList<TranslatedWord> {
    public TranslatedWords(Collection<? extends TranslatedWord> c) {
        super(c);
    }

    public TranslatedWords() {
    }
}

class TranslatedWordsService {
    public static TranslatedWords readTaskFromStream(InputStreamReader inputStreamReader) {
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        List<String> lines = new ArrayList<>();
        try {
            int numbersOfTasks = Integer.parseInt(bufferedReader.readLine());
            for (int i = 0; i < numbersOfTasks; i++) {
                lines.add(bufferedReader.readLine());
            }
            bufferedReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        TranslatedWords translatedWords = new TranslatedWords();
        for (String line : lines) {
            String[] linePart = line.split(" ");
            translatedWords.add(new TranslatedWord(linePart[0], linePart[1].trim().equals("+")));
        }

        return translatedWords == null ? (TranslatedWords) Collections.EMPTY_LIST : translatedWords;
    }

    public static Map<String, List<TranslatedWord>> getTranslatedWordsByWord(TranslatedWords translatedWords) {
        if (translatedWords == null || translatedWords.isEmpty()) {
            return Collections.EMPTY_MAP;
        }
        return translatedWords.stream().parallel()
                .collect(Collectors.groupingBy(TranslatedWord::getWord, Collectors.toList()));
    }

    public static int getMaxLengthBetweenCorrectlyTranslatedWords(List<TranslatedWord> translatedWords) {
        if (translatedWords == null || translatedWords.isEmpty() || translatedWords.size() == 1) {
            return -1;
        }

        int maxDistance = -1;
        for (int i = 0; i < translatedWords.size() - 1; i++) {
            TranslatedWord prevTranslatedWord = translatedWords.get(i);
            TranslatedWord translatedWord = translatedWords.get(i + 1);
            if (prevTranslatedWord.isCorrectTranslation() && translatedWord.isCorrectTranslation()) {
                int distance = Math.abs(prevTranslatedWord.getId() - translatedWord.getId());
                if (distance > maxDistance) {
                    maxDistance = distance;
                }
            }
        }
        if (maxDistance == -1) {
            return maxDistance;
        } else {
            return maxDistance - 1;
        }
    }

    public static int getMinLengthBetweenIncorrectlyTranslatedWords(List<TranslatedWord> translatedWords) {
        if (translatedWords == null || translatedWords.isEmpty() || translatedWords.size() == 1) {
            return -1;
        }

        int minDistance = -1;
        for (int i = 0; i < translatedWords.size() - 1; i++) {
            TranslatedWord prevTranslatedWord = translatedWords.get(i);
            TranslatedWord translatedWord = translatedWords.get(i + 1);
            if (!prevTranslatedWord.isCorrectTranslation() && !translatedWord.isCorrectTranslation()) {
                int distance = Math.abs(prevTranslatedWord.getId() - translatedWord.getId());
                if (minDistance == -1) {
                    minDistance = distance;
                }
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
        }
        if (minDistance == -1) {
            return minDistance;
        } else {
            return minDistance - 1;
        }
    }
}
