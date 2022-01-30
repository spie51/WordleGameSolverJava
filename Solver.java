import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Solver{

    List<String> wordList = new ArrayList<>();

    public Solver() {
        try {
            wordList = Files.readAllLines(new File("validwords.txt").toPath(), Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void solveGame(){
        String recommendation = "steam";
        String clue;

        int guessesNeeded = 1;

        HashSet<Character> guessedLetters = new HashSet<>();
        HashSet<Character> wrongLetters = new HashSet<>();
        HashMap<Character, Integer> correctLetters = new HashMap<>();
        HashMap<Character, Integer> misplacedLetters = new HashMap<>();
        int totalRight = 0;            

        System.out.println("Welcome to spie51's Wordle Solver!");
        System.out.println("This will try and help you guess what your word is.");
        System.out.println("We will first recommend a word for you to guess.");
        System.out.println("You should then play the word, then input the results you receive.");
        System.out.println("Please input your clues in one consecutive 5-character string, with \"G's\" and \"Y's\".");
        System.out.println("A \"G\" means that the letter is in the right location.");
        System.out.println("A \"Y\" means that the letter is in the word, but not in the right location.");
        System.out.println("Any other character means that the letter isn't in the word.");
        System.out.println("Good Luck!");

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the word you used as your first guess. We recommend the word " + recommendation + " if you don't know where to start.");
        recommendation = sc.nextLine().toLowerCase();

        if(recommendation.length() != 5){
            System.out.println("This word isn't valid, so we'll just use STEAM.");
            recommendation = "steam";
        }
        
        while(wordList.size() > 1){
            System.out.println("Enter the clues you receive after inputting " + recommendation.toUpperCase());
            clue = sc.nextLine().toUpperCase();

            if (clue.length() != 5){
                System.out.println("Invalid Clues!");
                continue;
            }

            for (int i = 0; i < clue.length(); i++){
                char ch = clue.charAt(i);
                if(ch == 'G'){
                    correctLetters.put(recommendation.charAt(i), i);
                    guessedLetters.add(recommendation.charAt(i));
                    totalRight++;
                }
                else if(ch == 'Y'){
                    guessedLetters.add(recommendation.charAt(i));
                    misplacedLetters.put(recommendation.charAt(i), i);
                }
                else{
                    wrongLetters.add(recommendation.charAt(i));
                }
            }

            if(totalRight == 5){
                System.out.println("Great! We successfully guessed the word " + recommendation.toUpperCase() + " in " + guessesNeeded + " guesses!");
                sc.close();
                return;
            }
            else{
                wordList.remove(recommendation);
                totalRight = 0;
            }

            String s;

            outerloop:
            for(int j = 0; j < wordList.size(); j++){
                s = wordList.get(j);

                for(char cn : wrongLetters){
                    if(s.contains(String.valueOf(cn))){
                        wordList.remove(s);
                        j--;
                        continue outerloop;
                    }
                }
                for(char cy : guessedLetters){
                    if(!s.contains(String.valueOf(cy))){
                        wordList.remove(s);
                        j--;
                        continue outerloop;
                    }
                }
                for(char cg : correctLetters.keySet()){
                    if(s.charAt(correctLetters.get(cg)) != cg){
                        wordList.remove(s);
                        j--;
                        continue outerloop;
                    }
                }
                for(char m : misplacedLetters.keySet()){
                    if(s.charAt(misplacedLetters.get(m)) == misplacedLetters.get(m)){
                        wordList.remove(s);
                        j--;
                        continue outerloop;
                    }
                }
            }

            guessesNeeded++;

            if(wordList.size() > 1){
                recommendation = wordList.get((int) Math.floor(Math.random() * wordList.size()));
                System.out.println(wordList.size() + " possibilities remaining. Our next guess is " + recommendation.toUpperCase());
            }      
        }
        if(wordList.size() == 1){
            System.out.println("The word should be " + wordList.get(0).toUpperCase() + ". It only took " + guessesNeeded + " guesses to get here!");
            sc.close();
            return;
        }
        else{
            System.out.println("Sorry! The word doesn't seem to be in my Word Bank.");
        }
        
        sc.close();
    }
}
