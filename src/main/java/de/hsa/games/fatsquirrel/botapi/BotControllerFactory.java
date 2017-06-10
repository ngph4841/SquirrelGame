package de.hsa.games.fatsquirrel.botapi;

/**
 * The BotControllerFactory Interfaces creates BotController
 * (This Interface can create BotControllers via lambda expressions)
 * Created by Freya on 19.05.2017.
 */
public interface BotControllerFactory {
    /**
     * This method creates a MaasterBotController.
     * @param classPath ClassPath String for dynamical initialising.
     * @return MasterBotController
     */
    public BotController createMasterBotController(String classPath);

    /**
     * This method creates a MiniBotController.
     * @param classPath ClassPath String for dynamical initialising.
     * @return MiniBotController
     */
    public BotController createMiniBotController(String classPath);
}
