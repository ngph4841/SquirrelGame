package de.hsa.games.fatsquirrel.console;

import de.hsa.games.fatsquirrel.gui.FxUI;
import de.hsa.games.fatsquirrel.util.Command;
import de.hsa.games.fatsquirrel.Game;
import de.hsa.games.fatsquirrel.State;
import de.hsa.games.fatsquirrel.UI;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.core.MasterSquirrel;
import de.hsa.games.fatsquirrel.core.XY;

public class GameImpl extends Game {
    private State state;;
    private MasterSquirrel player;
    private FlattenedBoard context;
    private UI ui;
    private int FPS = 10;
    private String msg = "";

    public GameImpl(State state, UI ui) throws Exception {
        this.state = state;
        this.player = (MasterSquirrel) state.getBoard().getEntitySet().getEntity(0);
        this.context = (FlattenedBoard) state.getBoardView();
        this.ui = ui;
    }

    @Override
    public void render() throws Exception{// Spielzustand auf ausgabemedium
        ui.setMsg(msg);
        msg = "";
        ui.render(state.getBoardView());
    }

    @Override
    public void processInput() throws Exception {// verarbeitet Benutzereingabe
        Command command = ui.getCommand();
        if (command != null) {
            String methodName = command.getCommandType().getMethodName();
            Object[] params = command.getParams();
            java.lang.reflect.Method method = this.getClass().getMethod(methodName, command.getCommandType().getParamTypes());
            method.invoke(this, params);
        }
    }

    public void update() throws Exception {// ver�ndert akt. Spielzustand
        state.update();
    }

    public void spawn(int energy) throws Exception {
            state.getBoard().getEntitySet().plus(player.spawnChild(energy));
    }

    public void exit() {
        System.exit(1);
    }

    public void help() {
        for (GameCommandType e : GameCommandType.values()) {
            System.out.println("enter: " + e.getName() + " for " + e.getHelpText());
        }
    }

    public void move(XY moveDirection) throws Exception {
        context.tryMove(player, moveDirection);
    }

    public void masterMethod() {
        System.out.println("HP :" + player.getEnergy());
    }

    public void run() throws Exception {
        render();
        while (true) {
            try {
                ui.commandBuffer();
                processInput();
            } catch (ScanException e) {
                msg = "wrong input, please try again";
            } catch (NotEnoughEnergyException f) {
                msg = "Not enough energy to spawn a child";
            }
            update();
            render();
        }
    }

    public void runLive() throws Exception {
        while (true) {
            render();
            try{
                ui.commandBuffer();
                processInput();
            } catch (ScanException e) {
                msg = "wrong input, please try again";
            } catch (NotEnoughEnergyException f) {
                msg = "Not enough energy to spawn a child";
            }
            update();
            Thread.sleep(FPS * 10);
        }
    }

    public void bufferInput() throws Exception {
        while (true) {
            ui.commandBuffer();
        }
    }

    public State getState() {
        return state;
    }
}
