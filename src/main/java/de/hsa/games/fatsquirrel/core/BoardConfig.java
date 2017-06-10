package de.hsa.games.fatsquirrel.core;

/**
 * The BoardConfig class is immutable and contains all the settings from the Game.
 */
public class BoardConfig {
    private final XY size;
    private final int badBeast;
    private final int goodBeast;
    private final int badPlant;
    private final int goodPlant;
    private final int entity;
    private final int wallCount;
    private final String masterBotPath;
    private final String miniBotPath;
    private final int turnCounter;

    /**
     * The parameters are the settings for the game.
     * @param size XY: the x-coordinate is the width and the y-coordinate is the height
     * @param badBeast amount of BadBeasts
     * @param goodBeast amount of GoodBeasts
     * @param badPlant amount of BadPlants
     * @param goodPlant amount of GoodPlants
     * @param turnCounter How many turns a round consists of
     * @param masterBotPath class path of the master bot
     * @param miniBotPath class path of the mini bot
     */
    public BoardConfig(XY size, int badBeast, int goodBeast, int badPlant, int goodPlant,int turnCounter, String masterBotPath, String miniBotPath) {
        this.size = size;
        this.badBeast = badBeast;
        this.goodBeast = goodBeast;
        this.badPlant = badPlant;
        this.goodPlant = goodPlant;
        this.entity = badBeast + goodBeast + badPlant + goodPlant;
        this.wallCount = (size.x * 2 + size.y * 2) - 4;
        this.masterBotPath = masterBotPath;
        this.miniBotPath = miniBotPath;
        this.turnCounter = turnCounter;
    }

    /**
     * This method returns the board size in a XY class.
     * (the x-coordinate is the width and the y-coordinate is the height)
     * @return the size of the board in a XY class
     */
    public XY getSize() {
        return size;
    }

    /**
     * This method returns the amount of entities the outer wall consists of.
     * @return amount of the outer wall entities
     */
    public int getWallCount() {
        return wallCount;
    }

    /**
     * This method returns how many BadBeast should be in the game.
     * @return how many BadBeast should be in the game
     */
    public int getBadBeastAmount() {
        return badBeast;
    }

    /**
     * This method returns how many GoodBeast should be in the game.
     * @return how many GoodBeast should be in the game
     */
    public int getGoodBeastAmount() {
        return goodBeast;
    }

    /**
     * This method returns how many BadPlants should be in the game.
     * @return how many BadPlants should be in the game
     */
    public int getBadPlantAmount() {
        return badPlant;
    }

    /**
     * This method returns how many GoodPlants should be in the game.
     * @return how many GoodPlants should be in the game
     */
    public int getGoodPlantAmount() {
        return goodPlant;
    }

    /**
     * This method returns the entirety of all the entities that should be in the game.
     * @return entirety of the entities in the game
     */
    public int getEntityAmount() {
        return entity;
    }

    /**
     * This method returns the class path of the MasterSquirrelBot.
     * @return class path of the MasterSquirrelBot
     */
    public String getMasterBotPath() {
        return masterBotPath;
    }

    /**
     * This method returns the calss path of the MiniSquirrelBot.
     * @return class path of the MiniSquirrelBot
     */
    public String getMiniBotPath() {
        return miniBotPath;
    }

    /**
     * This method returns the amount of turns a round consists of.
     * @return amount of turns a round consists of
     */
    public int getTurnCounter() {
        return turnCounter;
    }

}
