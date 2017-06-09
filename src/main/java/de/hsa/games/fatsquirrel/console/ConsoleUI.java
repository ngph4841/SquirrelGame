package de.hsa.games.fatsquirrel.console;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import de.hsa.games.fatsquirrel.core.*;
import de.hsa.games.fatsquirrel.util.Command;
import de.hsa.games.fatsquirrel.util.CommandScanner;
import de.hsa.games.fatsquirrel.UI;

public class ConsoleUI implements UI {
    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private Command buffer;
    private String msg;

    public void commandBuffer() throws Exception{
        CommandScanner scanner = new CommandScanner(GameCommandType.values(), input);
        buffer = scanner.next();
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Command getCommand() throws Exception {
        Command temp = buffer;
        buffer = null;
        return temp;
    }

    public void render(BoardView view) {
        int boardHeight = view.getSize().y;
        int boardWidth = view.getSize().x;
        int masterEnergy = 0;
        for (int j = 0; j < boardHeight; j++) {
            for (int i = 0; i < boardWidth; i++) {
                if (view.getEntityType(i, j) == null) {
                    System.out.print(".");
                    continue;
                }
                if (view.getEntityType(i, j) instanceof MasterSquirrel) {
                    System.out.print("M");
                    masterEnergy = view.getEntityType(i,j).getEnergy();
                    continue;
                }
                if (view.getEntityType(i, j) instanceof MiniSquirrel) {
                    System.out.print("m");
                    continue;
                }
                if (view.getEntityType(i, j) instanceof Wall) {
                    System.out.print("W");
                    continue;
                }
                if (view.getEntityType(i, j) instanceof BadBeast) {
                    System.out.print("X");
                    continue;
                }
                if (view.getEntityType(i, j) instanceof BadPlant) {
                    System.out.print("P");
                    continue;
                }
                if (view.getEntityType(i, j) instanceof GoodBeast) {
                    System.out.print("O");
                    continue;
                }
                if (view.getEntityType(i, j) instanceof GoodPlant) {
                    System.out.print("p");
                    continue;
                }
            }
            System.out.println();
        }
        System.out.println("HP:" + masterEnergy);
        System.out.println(msg);
    }
}