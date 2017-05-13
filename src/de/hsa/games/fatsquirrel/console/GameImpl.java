package de.hsa.games.fatsquirrel.console;

import de.hsa.games.fatsquirrel.util.Command;
import de.hsa.games.fatsquirrel.Game;
import de.hsa.games.fatsquirrel.State;
import de.hsa.games.fatsquirrel.UI;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.core.MasterSquirrel;
import de.hsa.games.fatsquirrel.core.XY;

public class GameImpl extends Game {

    private State state;
    private int charTurnCounter;
    private int stunTurnCounter;
    private MasterSquirrel player;
    private FlattenedBoard context;
    private UI ui;
    private int FPS = 10;

    public GameImpl(State state) throws Exception {
        this.state = state;
        this.charTurnCounter = 0;
        this.stunTurnCounter = 0;
        this.player = (MasterSquirrel) state.getBoard().getEntitySet().getEntity(0);
        this.context = (FlattenedBoard) state.getBoardView();
        this.ui = new ConsoleUI();
    }

    @Override
    public void render() {// Spielzustand auf ausgabemedium
        ui.render(state.getBoardView());
    }

    @Override
    public void processInput() throws Exception {// verarbeitet Benutzereingabe
        try {
            Command command = ui.getCommand();
            if (command != null) {
                XY direction = new XY(0, 0);

                String methodName = command.getCommandType().getMethodName();
                Object[] params = command.getParams();
                java.lang.reflect.Method method = this.getClass().getMethod(methodName, command.getCommandType().getParamTypes());
                method.invoke(this, params);
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
        try {
            state.getBoard().getEntitySet().plus(player.spawnChild(energy));
        } catch (NotEnoughEnergyException e) {
            System.out.println("Not enough Energy");
        }
    }

    public void exit() {
        System.exit(1);
    }

    public void help() {
        for (GameCommandType e : GameCommandType.values()) {
            System.out.println("write: " + e.getName() + " for " + e.getHelpText());
        }
    }

    public void move(XY moveDirection) throws Exception {
        context.tryMove(player, moveDirection);
    }

    public void masterMethod() {
        System.out.println("HP :" + player.getEnergy());
    }

    public void run() throws Exception {
        while (true) {
            render();
            ui.commandBuffer();
            processInput();
            update();
        }
    }

    public void runLive() throws Exception {
        while(true) {
            render();
            processInput();
            update();
            Thread.sleep(FPS);
        }
    }

    public void bufferInput() throws Exception {
        while (true) {
            try {
                ui.commandBuffer();
            } catch (ScanException e) {
                System.out.println("wrong input");
            } catch (NotEnoughEnergyException f) {
                System.out.println("Not enough Energy");
            }
        }
    }

}
