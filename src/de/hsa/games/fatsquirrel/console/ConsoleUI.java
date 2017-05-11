package de.hsa.games.fatsquirrel.console;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import de.hsa.games.fatsquirrel.Command;
import de.hsa.games.fatsquirrel.CommandScanner;
import de.hsa.games.fatsquirrel.UI;
import de.hsa.games.fatsquirrel.core.BadBeast;
import de.hsa.games.fatsquirrel.core.BadPlant;
import de.hsa.games.fatsquirrel.core.BoardView;
import de.hsa.games.fatsquirrel.core.GoodBeast;
import de.hsa.games.fatsquirrel.core.GoodPlant;
import de.hsa.games.fatsquirrel.core.MasterSquirrel;
import de.hsa.games.fatsquirrel.core.MiniSquirrel;
import de.hsa.games.fatsquirrel.core.Wall;

public class ConsoleUI implements UI {
    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private Command buffer;

    public void commandBuffer() throws Exception{
        CommandScanner scanner = new CommandScanner(GameCommandType.values(), input);
        buffer = scanner.next();
    }

    public Command getCommand() throws Exception {
//       CommandScanner scanner = new CommandScanner(GameCommandType.values(), input);
//       Command command = scanner.next();

//        return command;
        Command temp = buffer;
//        buffer = new Command();
        return buffer;
    }

    public void render(BoardView view) {
        int boardLength = view.getSize().getY();
        int boardWidth = view.getSize().getX();
        int masterEnergy = 0;
        for (int j = 0; j < boardLength; j++) {
            for (int i = 0; i < boardWidth; i++) {
                if (view.getEntityType(j, i) == null) {
                    System.out.print(".");
                    continue;
                }
                if (view.getEntityType(j, i) instanceof MasterSquirrel) {
                    System.out.print("M");
                    masterEnergy = view.getEntityType(j, i).getEnergy();
                    continue;
                }
                if (view.getEntityType(j, i) instanceof MiniSquirrel) {
                    System.out.print("m");
                    continue;
                }
                if (view.getEntityType(j, i) instanceof Wall) {
                    System.out.print("W");
                    continue;
                }
                if (view.getEntityType(j, i) instanceof BadBeast) {
                    System.out.print("X");
                    continue;
                }
                if (view.getEntityType(j, i) instanceof BadPlant) {
                    System.out.print("P");
                    continue;
                }
                if (view.getEntityType(j, i) instanceof GoodBeast) {
                    System.out.print("O");
                    continue;
                }
                if (view.getEntityType(j, i) instanceof GoodPlant) {
                    System.out.print("p");
                    continue;
                }
            }
            System.out.println();
        }
        System.out.println("HP: " + masterEnergy);
    }
}