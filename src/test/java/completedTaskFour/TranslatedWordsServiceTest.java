package completedTaskFour;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class TranslatedWordsServiceTest {
    public static final String FILEPATH = "src/main/resources/task4/translatedWordsExample1";
    TranslatedWords translatedWords;
    Map<String, List<TranslatedWord>> translatedWordsByWord;

    @BeforeMethod
    public void setUp() {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new FileReader(FILEPATH);
        } catch (IOException ex){
            ex.printStackTrace();
        }

        translatedWords = TranslatedWordsService.readTaskFromStream(inputStreamReader);
        translatedWordsByWord = TranslatedWordsService.getTranslatedWordsByWord(translatedWords);
    }

    @Test
    public void testReadTaskFromStream() {
        for (TranslatedWord translatedWord: translatedWords){
            System.out.println(translatedWord);
        }
    }

    @Test
    public void testGetTranslatedWordsByWord() {
        System.out.println(translatedWordsByWord);
    }

    @Test
    public void testGetMaxLengthBetweenCorrectlyTranslatedWords() {
        System.out.println(TranslatedWordsService.getMaxLengthBetweenCorrectlyTranslatedWords(translatedWordsByWord.get("queue")));
    }

    @Test
    public void testGetMinLengthBetweenIncorrectlyTranslatedWords() {
        System.out.println(TranslatedWordsService.getMinLengthBetweenIncorrectlyTranslatedWords(translatedWordsByWord.get("vertex")));
    }
}