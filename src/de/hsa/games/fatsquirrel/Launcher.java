package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.console.GameImpl;
import de.hsa.games.fatsquirrel.core.Board;
import de.hsa.games.fatsquirrel.core.BoardConfig;
import de.hsa.games.fatsquirrel.core.XY;

import java.util.Timer;
import java.util.TimerTask;

public class Launcher {
    public static void main(String[] args) throws Exception {

        XY size = new XY(50, 50);
        BoardConfig settings = new BoardConfig(size, 5, 5, 10, 10);
        Board board1 = new Board(settings);
        State state1 = new State(board1);
        Game game1 = new GameImpl(state1);
//        game1.run(); // oldmethod

        startGame(game1);
        game1.bufferInput();
    }

    public static void startGame(Game game1)throws Exception{
        Timer timer = new Timer();
        Task t = new Task(game1);
        timer.schedule(t, 0, 2000);
    }

    static class Task extends TimerTask {
        private Game game1;
        public Task(Game game1){
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
