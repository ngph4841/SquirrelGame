package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.BoardView;
import de.hsa.games.fatsquirrel.core.XY;

public interface UI {

    public Command getCommand() throws Exception;

    public void render(BoardView view);

}