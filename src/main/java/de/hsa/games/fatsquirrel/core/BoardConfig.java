package de.hsa.games.fatsquirrel.core;

public class BoardConfig {
    private final XY size;//width, size
    //	private final int wall = 20;
    private final int badBeast;
    private final int goodBeast;
    private final int badPlant;
    private final int goodPlant;
    private final int entity;
    private final int wallCount; //rand - 4ecken sonst doppelt
    private final String masterBotPath;
    private final String miniBotPath;
    private final int turnCounter;

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

    public XY getSize() {
        return size;
    }

    public int getWallCount() {
        return wallCount;
    }

    public int getBadBeastAmount() {
        return badBeast;
    }

    public int getGoodBeastAmount() {
        return goodBeast;
    }

    public int getBadPlantAmount() {
        return badPlant;
    }

    public int getGoodPlantAmount() {
        return goodPlant;
    }

    public int getEntityAmount() {
        return entity;
    }

    public String getMasterBotPath() {
        return masterBotPath;
    }

    public String getMiniBotPath() {
        return miniBotPath;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

}
