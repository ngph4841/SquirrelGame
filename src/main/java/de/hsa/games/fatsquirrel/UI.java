package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.BoardView;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.util.Command;

public interface UI {

    public Command getCommand() throws Exception;

    public void render(BoardView view);

    public void commandBuffer()throws Exception;

    public void setMsg(String msg);

}