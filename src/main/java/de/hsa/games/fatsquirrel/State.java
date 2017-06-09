package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.*;
import de.hsa.games.fatsquirrel.core.Character;
import sun.util.resources.cldr.ebu.LocaleNames_ebu;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class State {
    private Board board;
    private int turnCounter;
    private Map<String, List<Integer>> highScore;
    private Logger logger;
    private Properties properties;

    public State(Board board) throws Exception {
        this.board = board;
        this.turnCounter = board.getConfig().getTurnCounter();
        this.highScore = new HashMap<String,List<Integer>>();
        this.logger = Logger.getLogger("launcherLogger");
        this.properties = new Properties();

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
        String key = "";
        for (int i = 0; i < board.getList().size(); ++i) {
            if (board.getList().get(i) instanceof MasterSquirrelBot) {
                MasterSquirrelBot temp = (MasterSquirrelBot) board.getList().get(i);
                key = temp.getClass().toString(); //load the className into a String:Key

                // check if there is already an entry for the className
                if (highScore.containsKey(key) == false) {
                    highScore.put(key, new Vector<Integer>());
                }
                highScore.get(key).add(temp.getEnergy()); //add points
            }
        }

        // Handle high score
        String loggerString = "";
        for (String key2 : highScore.keySet()) {
            sortHighScore(highScore.get(key2));

            // ConsolePrint
            //System.out.println(key2 + ":" + "\n");
            //printHighScore(highScore.get(key2));

            //appending to the string to save into the log.txt
            loggerString += key2 + ": " +highScore.get(key2) + "\n";

            //storing the Highscore in properties
            properties.setProperty(key2,highScore.get(key2).toString());
        }
        //saving the highscore in the datafiles
        properties.store(new FileWriter(new File("Highscore.properties")),"");
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

    private void printHighScore(List<Integer> intList) { //printing the Highscore onto the console
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
        try {
            //load settings.properties
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("Highscore.properties"));
            properties.load(bufferedInputStream);
            bufferedInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING,e.getMessage());
        }
    }

}
