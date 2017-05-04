package de.hsa.games.fatsquirrel.console;

import de.hsa.games.fatsquirrel.UI;
import de.hsa.games.fatsquirrel.core.BadBeast;
import de.hsa.games.fatsquirrel.core.BadPlant;
import de.hsa.games.fatsquirrel.core.BoardView;
import de.hsa.games.fatsquirrel.core.GoodBeast;
import de.hsa.games.fatsquirrel.core.GoodPlant;
import de.hsa.games.fatsquirrel.core.MasterSquirrel;
import de.hsa.games.fatsquirrel.core.Wall;
import de.hsa.games.fatsquirrel.core.XY;

public class ConsoleUI implements UI {

    public XY getCommand() throws Exception {
        int direction = 0;
        direction = (int) System.in.read() - '0';
        System.in.read();
        System.in.read();

        int x = 0;
        int y = 0;
        switch (direction) {
            case 1:
                x = -1;
                y = 0;
                break;

            case 2:
                x = 0;
                y = 1;
                break;

            case 3:
                x = 1;
                y = 0;
                break;

            case 5:
                x = 0;
                y = -1;
                break;
        }
        return new XY(y, x);
    }

    public void render(BoardView view) {
        int boardLength = view.getSize().getY();
        int boardWidth = view.getSize().getX();
        for (int j = 0; j < boardLength; j++) {
            for (int i = 0; i < boardWidth; i++) {
                if (view.getEntityType(j, i) == null) {
                    System.out.print(".");
                    continue;
                }
                if (view.getEntityType(j, i) instanceof MasterSquirrel) {
                    System.out.print("M");
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
    }
}
