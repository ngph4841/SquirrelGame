package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.console.ConsoleUI;
import de.hsa.games.fatsquirrel.console.GameImpl;
import de.hsa.games.fatsquirrel.core.*;
import de.hsa.games.fatsquirrel.gui.FxUI;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Launcher extends Application {
    public static BoardConfig settings = new BoardConfig(new XY(50, 50), 5, 5, 10, 10, 1000,
            "de.hsa.games.fatsquirrel.botimpls.BotControllerMaster","de.hsa.fatsquirrel.botimpls.BotControllerMini");
    public static int mode = 3;


//    public static Logger logger = Logger.getLogger(Launcher.class.getSimpleName());
//    private FileHandler fileHandler = new FileHandler("Log.txt");
//    private SimpleFormatter formatter = new SimpleFormatter();
    public Launcher() throws IOException {
    }

    public static void main(String[] args) throws Exception {
        Board board = new Board(settings,mode);
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
                Application.launch(args);
                break;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FxUI fxUI = FxUI.createInstance(settings.getSize());
        Board board = new Board(settings,mode);
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

}
