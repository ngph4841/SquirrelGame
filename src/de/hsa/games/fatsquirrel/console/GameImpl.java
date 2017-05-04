package de.hsa.games.fatsquirrel.console;

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

    public GameImpl(State state) throws Exception {
        this.state = state;
        this.charTurnCounter = 0;
        this.stunTurnCounter = 0;
        this.player = (MasterSquirrel) state.getBoard().getEntitySet().getEntity(0);
    }

    @Override
    public void render() {//Spielzustand auf ausgabemedium
        UI output = new ConsoleUI();
        output.render(state.getBoardView());
    }

    @Override
    public void processInput() throws Exception {//verarbeitet Benutzereingaben
        UI input = new ConsoleUI();
        FlattenedBoard context = (FlattenedBoard) state.getBoardView();
        XY direction = input.getCommand();
        if (stunTurnCounter == 0) {
            context.tryMove(player, direction);
        } else {
            stunTurnCounter++;
        }
        if (stunTurnCounter == 3) {
            stunTurnCounter = 0;
            player.cleanse();
        }
    }

    public void update() throws Exception {//ver�ndert akt. Spielzustand
        state.update();
    }

    public void run() throws Exception {
        while (true) {
            render();
            processInput();
            update();
        }
    }

}
