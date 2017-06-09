package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.*;
import de.hsa.games.fatsquirrel.core.Character;
import sun.util.resources.cldr.ebu.LocaleNames_ebu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class State {
    private Board board;
    private int turnCounter;
    private Map<String, List<Integer>> highScore;
    private Logger logger;
    private File highScores = new File("Highscore.properties");

    public State(Board board) throws Exception {
        this.board = board;
        this.turnCounter = board.getConfig().getTurnCounter();
        this.highScore = new HashMap<String,List<Integer>>();
        this.logger = Logger.getLogger("");

        loadHighScore();
    }

    public Board getBoard() {
        return board;
    }

    public BoardView getBoardView() throws Exception {
        return board.flatten();
    }

    public void update() throws Exception {
        if (turnCounter > 0) {
            EntityContext context = board.flatten();
            //this.board.getList().moveAll(context);        //call nextStep in all entities

            //loop nextStep call
            for (int i = 0; i < board.getList().size(); i++) {
                if (board.getList().get(i) instanceof Character | board.getList().get(i) instanceof Squirrel) {
                    board.getList().get(i).nextStep(context);
                }
            }
            turnCounter--;
        } else {
            this.turnCounter = board.getConfig().getTurnCounter();
            updateHighScore();
            this.board = new Board(board.getConfig(), board.getMode());
        }

    }

    private void updateHighScore() throws Exception {
        FileWriter fileWriter = new FileWriter(highScores);
        String key = "";
        for (int i = 0; i < board.getList().size(); ++i) {
            if (board.getList().get(i) instanceof MasterSquirrelBot) {
                MasterSquirrelBot temp = (MasterSquirrelBot) board.getList().get(i);

                key = temp.getClass().toString(); //get Key
                // check if there is already an entry
                if (highScore.containsKey(key) == false) {
                    highScore.put(key, new Vector<Integer>());
                }   //add points
                highScore.get(key).add(temp.getEnergy());
            }
        }
        // Handle high score
        System.out.printf("HighScores:");
        String loggerString = "";
        for (String key2 : highScore.keySet()) {
            // Sort
            sortHighScore(highScore.get(key2));
            // Print
            System.out.println(key2 + ":");
            printHighScore(highScore.get(key2));
            loggerString += key2 + ": " +highScore.get(key2) + "\n";
            System.out.println();
        }
        fileWriter.write(loggerString + "\n");
        logger.log(Level.WARNING, "HighScores:"+ "\n" + loggerString);
    }

    private void sortHighScore(List<Integer> intList) {
        boolean allSorted = false;

        while (allSorted == false) {
            allSorted = true;

            for (int i = 1; i < intList.size(); ++i) {
                if (intList.get(i) < intList.get(i - 1)) {
                    Collections.swap(intList, i, i - 1);
                    allSorted = false;
                    break;
                }
            }
        }
    }

    private void printHighScore(List<Integer> intList) {
        int averageScore = 0;

        for (int i = 0; i < intList.size(); ++i) {
            System.out.println((i + 1) + ". : " + intList.get(i) + " Points.");
            averageScore += intList.get(i);

            if (i == intList.size() - 1) {
                System.out.println("averageScore:" + averageScore / (i + 1));
            }
        }

    }

    private void loadHighScore(){
        Scanner scanner = null;

        try {
            scanner = new Scanner(highScores);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.log(Level.WARNING,"File 'Highscore.properties' was not found.");
        }
        while(scanner.hasNextLine()){
            scanner.nextLine();
        }

    }
}
