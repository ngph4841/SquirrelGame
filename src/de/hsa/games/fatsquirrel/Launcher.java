package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.console.ConsoleUI;
import de.hsa.games.fatsquirrel.console.GameImpl;
import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.BoardConfig;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.gui.FxUI;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Launcher extends Application {
    public static BoardConfig settings = new BoardConfig(new XY(50, 50), 5, 5, 10, 10);

    public static void main(String[] args) throws Exception {
        Board board1 = new Board(settings);
        State state1 = new State(board1);
        int mode = 2;
        Game game;
        switch(mode){
            case 0:
                game = new GameImpl(state1, new ConsoleUI());
                game.run();
                break;
            case 1:
                game = new GameImpl(state1, new ConsoleUI());
                startGame(game);
                while(true) {
                    game.bufferInput();
                }
            case 2:
                game = new GameImpl(state1, FxUI.createInstance(settings.getSize()));
                Application.launch(args);
                game.bufferInput();
                break;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //...
        FxUI fxUI = FxUI.createInstance(settings.getSize());
        Board board = new Board(settings);
        State state = new State(board);
        final Game game = new GameImpl(state,fxUI);

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
