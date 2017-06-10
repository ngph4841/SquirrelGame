package de.hsa.games.fatsquirrel.botapi;

/**
 * The BotController Interface needs specific Implementations for each Bot(Master/Mini).
 * Created by Gruppe16 on 19.05.2017.
 */

public interface BotController {
    /**
     * The nextStep method has all the implemtations of the bot's behaviour.
     * @param view ControllerContext for interaction with the game engine.
     * @throws Exception Declare Exceptions for unwanted access of information by the bot.
     */
    public void nextStep(ControllerContext view) throws Exception;

    /**
     * @param view ControllerContext for interaction with the game engine.
     * @return The remaining Steps in the current round.
     * @throws Exception
     */
    public int getRemainingSteps(ControllerContext view) throws Exception;
}
