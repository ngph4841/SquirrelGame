package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.console.ConsoleUI;
import de.hsa.games.fatsquirrel.console.GameImpl;
import de.hsa.games.fatsquirrel.core.*;
import de.hsa.games.fatsquirrel.gui.FxUI;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Launcher extends Application {
    public static BoardConfig settings = new BoardConfig(new XY(50, 50), 5, 5, 10, 10, 50,
            "de.hsa.games.fatsquirrel.botimpls.BotControllerMaster", "de.hsa.fatsquirrel.botimpls.BotControllerMini");
    public static int mode = 3;

    private static Logger launcherLogger;
    static {
        launcherLogger = Logger.getLogger("");
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("Log.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
        launcherLogger.addHandler(fileHandler);
    }

    public Launcher() throws IOException {
    }

    public static void main(String[] args) throws Exception {
        Board board = new Board(settings, mode);
        State state;
        Game game;

        switch (mode) {
            case 0: //1frame per input
                state = new State(board);
                game = new GameImpl(state, new ConsoleUI());
                game.run();
            case 1: //alot of frames
                state = new State(board);
                game = new GameImpl(state, new ConsoleUI());
                startGame(game);
                while (true) {
                    game.bufferInput();
                }
            case 2: //javafx SinglePlayer and Bots
            case 3:
                launcherLogger.log(Level.INFO,"Game started in javaFx!");
                Application.launch(args);
                break;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        loadSettings(); //<---loadingScreen

        FxUI fxUI = FxUI.createInstance(settings.getSize());
        Board board = new Board(settings, mode);
        State state = new State(board);
        final Game game = new GameImpl(state, fxUI);

        primaryStage.setScene(fxUI);
        primaryStage.setTitle("Diligent Squirrel");
        fxUI.getWindow().setOnCloseRequest((WindowEvent e) -> {
            System.exit(-1);
        });
        primaryStage.show();
        startGame(game);
    }

    public static void startGame(Game game1) throws Exception {
        Timer timer = new Timer();
        Task t = new Task(game1);
        timer.schedule(t, 10);
    }

    static class Task extends TimerTask {
        private Game game1;

        public Task(Game game1) {
            this.game1 = game1;
        }

        @Override
        public void run() {
            try {
                game1.runLive();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadSettings(){
        Scanner scanner = null;
        boolean XYNotLoadedYet = true;
        String settingsLoader = "";
        String[] settingsValues = new String[9];
        String[] temp = new String[2];
        int values = 2;

        try { //load settings.txt
            scanner = new Scanner(new File("Settings.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            launcherLogger.log(Level.WARNING,"File 'Settings.txt' was not found.");
        }

        //read each line which has 1 setting
        while(scanner.hasNextLine()){
            //XY extra since it has 2 int
            if(XYNotLoadedYet){
                temp = new String[3];
                settingsLoader = scanner.nextLine();
                temp = settingsLoader.split(" ");
                settingsValues[0] = temp[1];
                settingsValues[1] = temp[2];
                XYNotLoadedYet = false;
            } else {
                settingsLoader = scanner.nextLine();
                temp = settingsLoader.split(" ");
                settingsValues[values++] = temp[1];
            }
        }

        //parse strings from settings into BoardConfig except botPaths
        XY size = new XY(Integer.parseInt(settingsValues[0]),Integer.parseInt(settingsValues[1]));
        settings = new BoardConfig(size,Integer.parseInt(settingsValues[2]),Integer.parseInt(settingsValues[3]),Integer.parseInt(settingsValues[4]),
                Integer.parseInt(settingsValues[5]),Integer.parseInt(settingsValues[6]),settingsValues[7],settingsValues[8]);
    }

}
