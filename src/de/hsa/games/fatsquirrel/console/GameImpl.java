package de.hsa.games.fatsquirrel.console;

import de.hsa.games.fatsquirrel.Command;
import de.hsa.games.fatsquirrel.Game;
import de.hsa.games.fatsquirrel.State;
import de.hsa.games.fatsquirrel.UI;
import de.hsa.games.fatsquirrel.consoletest.MyFavouriteCommandType;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.core.MasterSquirrel;
import de.hsa.games.fatsquirrel.core.XY;

public class GameImpl extends Game {

    private State state;
    private int charTurnCounter;
    private int stunTurnCounter;
    private MasterSquirrel player;

    public GameImpl(State state) throws Exception {
        this.state = state;
        this.charTurnCounter = 0;
        this.stunTurnCounter = 0;
        this.player = (MasterSquirrel) state.getBoard().getEntitySet().getEntity(0);
    }

    @Override
    public void render() {// Spielzustand auf ausgabemedium
        UI output = new ConsoleUI();
        output.render(state.getBoardView());
    }

    @Override
    public void processInput() throws Exception {// verarbeitet Benutzereingaben
        UI input = new ConsoleUI();
        FlattenedBoard context = (FlattenedBoard) state.getBoardView();
        try {
            Command command = input.getCommand();
//            String methodName = command.getCommandType().getMethodName();
            XY direction = new XY(0, 0);

            switch ((GameCommandType) command.getCommandType()) {
                case HELP:
                    for (GameCommandType e : GameCommandType.values()) {
                        System.out.println("write: " + e.getName() + " for " + e.getHelpText());
                    }
                    break;
                case EXIT:
                    System.exit(1);
                case ALL:
                    break;
                case LEFT:
                    direction = (XY) command.getParams()[0];
                    break;
                case UP:
                    direction = (XY) command.getParams()[0];
                    break;
                case DOWN:
                    direction = (XY) command.getParams()[0];
                    break;
                case RIGHT:
                    direction = (XY) command.getParams()[0];
                    break;
                case MASTER_ENERGY:
                    player.getEnergy();
                    break;
                case SPAWN_MINI:
                    state.getBoard().getEntitySet().plus(player.spawnChild((int) command.getParam(0)));
                    break;
            }

            if (stunTurnCounter == 0) {
                context.tryMove(player, direction);
            } else {
                stunTurnCounter++;
            }
            if (stunTurnCounter == 3) {
                stunTurnCounter = 0;
                player.cleanse();
            }
        } catch (ScanException e) {
            System.out.println("wrong input");
            // command = new Command(GameCommandType.HELP, new Object[1]);
        } catch (NotEnoughEnergyException f) {
            System.out.println("Not enough Energy");
        }
    }

    public void update() throws Exception {// ver�ndert akt. Spielzustand
        state.update();
        player = (MasterSquirrel) state.getBoard().getEntitySet().getEntity(0);
    }

    public void spawn(int energy) throws Exception {
        state.getBoard().getEntitySet().plus(player.spawnChild(energy));
    }

    public void run() throws Exception {
        while (true) {
            render();
            processInput();
            update();
        }
    }

}
