import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Prints passwords! The letters in each word are typed with every other finger for speed.
 * This results in a long, convoluted password that is fast to type and easy to remember.
 * Symbols and letters should be added between words for extra security.
 * See: https://xkcd.com/936/
 *
 * Word list: https://github.com/dwyl/english-words
 */
public class Generator {

    // ------------------------------------------------------
    private static final int PASSWORDS_TO_PRINT = 15;
    private static final int WORD_LENGTH = 4;
    private static final int WORDS_IN_PASSWORD = 3;
    private static final String FILE_NAME = "Resources/dictionary.txt";
    // ------------------------------------------------------

    private static ArrayList<String> all = new ArrayList<>();
    private static ArrayList<String> startsLeft = new ArrayList<>();
    private static ArrayList<String> startsRight = new ArrayList<>();
    private static final ArrayList<Character> leftHand = new ArrayList<>(Arrays.asList(
            'q', 'w', 'e', 'r', 't', 'a', 's', 'd', 'f', 'g', 'z', 'x', 'c', 'v', 'b'));
    private static final ArrayList<Character> rightHand = new ArrayList<>(Arrays.asList(
            'y', 'u', 'i', 'o', 'p', 'h', 'j', 'k', 'l', 'n', 'm'));

    public static void main(String[] args) {
        generateWords(); //Creates all words alternating characters

        sortStarts(); //Adds words that start with right or left

        //Make sure there are enough viable words
        if(all.isEmpty() || startsLeft.isEmpty() || startsRight.isEmpty()) {
            System.out.println("Not enough words meet your criteria. Cannot generate a password.");
            return;
        }
        System.out.println("There are " + all.size() + " acceptable words meeting your criteria. \n--------------");

        for(int i = 0; i < PASSWORDS_TO_PRINT; i++) {
            printPasswords(WORDS_IN_PASSWORD); //Prints some acceptable passwords
        }

        System.out.println("-------------- \nDon't forget to add symbols and numbers!");
    }

    private static void generateWords() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(everyOther(line) && line.length() >= WORD_LENGTH) {
                    all.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean everyOther(String line) {
        if(line.isEmpty()) {
            return false;
        }
        if(leftHand.contains(line.charAt(0))) {
            for(int i = 1; i < line.length(); i++) {
                if(i%2 == 0) { //Left Hand
                    if(!leftHand.contains(line.charAt(i))) {
                        return false;
                    }
                }
                else { //Right Hand
                    if(!rightHand.contains(line.charAt(i))) {
                        return false;
                    }
                }
            }
            return true;
        }
        else if(rightHand.contains(line.charAt(0))) {
            for(int i = 1; i < line.length(); i++) {
                if(i%2 == 0) { //Right Hand
                    if(!rightHand.contains(line.charAt(i))) {
                        return false;
                    }
                }
                else { //Left Hand
                    if(!leftHand.contains(line.charAt(i))) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private static void sortStarts() {
        for(String s : all) {
            if(leftHand.contains(s.charAt(0))) {
                startsLeft.add(s);
            }
            else {
                startsRight.add(s);
            }
        }
    }

    private static void printPasswords(int numWords) {
        ArrayList<String> password = new ArrayList<>();
        Random rand = new Random();

        //Get first word. Can be any word.
        password.add(all.get(rand.nextInt(all.size())));

        for(int i = 1; i < numWords; i++) {
            if(leftHand.contains(password.get(i-1).charAt(password.get(i-1).length()-1))){
                password.add(startsRight.get(rand.nextInt(startsRight.size())));
            }
            else {
                password.add(startsLeft.get(rand.nextInt(startsLeft.size())));
            }
        }
        for(String s : password) {
            System.out.print(s + " ");
        }
        System.out.println("");
    }
}
