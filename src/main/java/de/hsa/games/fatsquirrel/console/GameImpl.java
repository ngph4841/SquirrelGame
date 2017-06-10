package de.hsa.games.fatsquirrel.console;

import de.hsa.games.fatsquirrel.core.MiniSquirrel;
import de.hsa.games.fatsquirrel.util.Command;
import de.hsa.games.fatsquirrel.Game;
import de.hsa.games.fatsquirrel.State;
import de.hsa.games.fatsquirrel.UI;
import de.hsa.games.fatsquirrel.core.FlattenedBoard;
import de.hsa.games.fatsquirrel.core.MasterSquirrel;
import de.hsa.games.fatsquirrel.core.XY;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the Squirrel-Game
 */
public class GameImpl extends Game {
    private State state;
    private MasterSquirrel player;
    private FlattenedBoard context;
    private UI ui;
    private int FPS = 10;
    private String msg = "";
    private Logger logger;

    /**
     *
     * @param state The state contains all the information of the current game.
     * @param ui UserInterface which is used to interact with the User.
     * @throws Exception
     */
    public GameImpl(State state, UI ui) throws Exception {
        this.state = state;
        this.player = (MasterSquirrel) state.getBoard().getList().get(0);
        this.context = (FlattenedBoard) state.getBoardView();
        this.ui = ui;
        this.logger = Logger.getLogger("launcherLogger");
    }

    /**
     * Renders the current game state with the declared UI.
     * @throws Exception
     */
    @Override
    public void render() throws Exception{
        ui.setMsg(msg);
        msg = "";
        ui.render(state.getBoardView());
    }

    /**
     * Processes the user input out of the input buffer.
     * (has Reflection)
     * @throws Exception
     */
    @Override
    public void processInput() throws Exception {
        Command command = ui.getCommand();
        if (command != null) {
            String methodName = command.getCommandType().getMethodName();
            Object[] params = command.getParams();
            java.lang.reflect.Method method = this.getClass().getMethod(methodName, command.getCommandType().getParamTypes());
            method.invoke(this, params);
        }
    }

    /**
     * Updates the game state.
     * @throws Exception
     */
    public void update() throws Exception {
        state.update();
    }

    /**
     * This method spawns a MiniSquirrel child next to the MasterSquirrel parent
     * (if space is available).
     * @param energy The amount of energy which the MiniSquirrel child
     *               should start with from the MasterSquirrel parent.
     * @throws Exception Throws the NotEnoughEnergyException if the parent lacks
     * the needed Energy to birth a child.
     */
    public void spawn(int energy) throws Exception {
        MiniSquirrel temp = player.spawnChild(energy);
        int x = temp.getPosition().x;
        int y = temp.getPosition().y;
        if(state.getBoardView().getEntityType(x,y) != null) {
            state.getBoard().getList().add(temp);
        }else{
            msg = "not enough space for birth";
        }
    }

    /**
     * This method exits the application.
     */
    public void exit() {
        System.exit(1);
    }

    /**
     * This method prints out all the possible GameCommandTypes onto the console.
     */
    public void help() {
        for (GameCommandType e : GameCommandType.values()) {
            System.out.println("enter: " + e.getName() + " for " + e.getHelpText());
        }
    }

    /**
     * This method moves the MasterSquirrel if the move is valid.
     * (TryMove method from the EntityContext Interface)
     * @param moveDirection XY Vector in which  the MasterSquirrel should move.
     * @throws Exception
     */
    public void move(XY moveDirection) throws Exception {
        context.tryMove(player, moveDirection);
    }

    /**
     * Prints the HealthPoints of the Player onto the console.
     */
    public void masterMethod() {
        System.out.println("HP :" + player.getEnergy());
    }

    /**
     * This method is for the sequential game mode.
     * GameLoop with command buffering, game state updates and rendering of the game.
     * @throws Exception
     */
    public void run() throws Exception {
        render();
        while (true) {
            try {
                ui.commandBuffer();
                processInput();
            } catch (ScanException e) {
                msg = "wrong input, please try again";
                logger.log(Level.WARNING,msg);
            } catch (NotEnoughEnergyException f) {
                msg = "Not enough energy to spawn a child";
               logger.log(Level.WARNING,msg);
            }
            update();
            render();
        }
    }

    /**
     * This method has to be used together with the bufferInput() method.
     * This method only updates and renders the current game state.
     * @throws Exception
     */
    public void runLive() throws Exception {
        while (true) {
            render();
            try{
                ui.commandBuffer();
                processInput();
            } catch (ScanException e) {
                msg = "wrong input, please try again";
                logger.log(Level.WARNING,"wrong user input");
            } catch (NotEnoughEnergyException f) {
                msg = "Not enough energy to spawn a child";
                logger.log(Level.WARNING, msg);
            }
            update();
            Thread.sleep(FPS * 10);
        }
    }

    /**
     * Buffering of the user input.
     * @throws Exception
     */
    public void bufferInput() throws Exception {
        while (true) {
            ui.commandBuffer();
        }
    }

    /**
     * @return the current game state.
     */
    public State getState() {
        return state;
    }
}
