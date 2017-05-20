package de.hsa.games.fatsquirrel.botapi;

/**
 * Created by Freya on 19.05.2017.
 */
public interface BotControllerFactory {
    public BotController createMasterBotController();
    public BotController createMiniBotController();
}
