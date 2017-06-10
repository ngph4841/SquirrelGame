package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.core.BoardView;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.util.Command;

/**
 * The UI Interface need specific implementations for each medium which should be used to interact
 * with the end-user.
 */
public interface UI {

    /**
     * This method returns the buffered input.
     * @return the command which was buffered
     * @throws Exception ScanException
     */
    public Command getCommand() throws Exception;

    /**
     * This method renders the current board state onto the desired medium.
     * @param view BoardView which is a 2d representation of the current board state
     */
    public void render(BoardView view);

    /**
     * This method buffers the user inputs into a command.
     * @throws Exception ScanException
     */
    public void commandBuffer()throws Exception;

    /**
     * This method sets a msg for inGame-events.
     * @param msg msg for the user
     */
    public void setMsg(String msg);

}