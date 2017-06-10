package de.hsa.games.fatsquirrel.core;

import java.util.ArrayList;

/**
 * The Board class contains all the information of the game board.
 *(Entities, BoardConfig)
 */
public class Board {
    private BoardConfig settings;
    private ArrayList<Entity> list;
    private int entityCounter;
    private int mode;

    /**
     * The settings are used for the initial filling of entities in the board.
     * The mode is used to determine if the game mode is single player or bots.(1-3)
     * @param settings game settings
     * @param mode game mode
     */
    public Board(BoardConfig settings, int mode) {
        this.settings = settings;    //load Configs
        this.entityCounter = 0;
        this.list = new ArrayList<>(2 + settings.getWallCount() + settings.getEntityAmount());
        this.mode = mode;
        //choosing gameMode
        switch (mode) {
            case 0:
            case 1:
            case 2:
                //singlePlayer
                addPlayer(new MasterSquirrel(2000, 2000, new XY(settings.getSize().x / 2, settings.getSize().y / 2)));
                break;
            case 3:
                //1bot
                //addPlayer(new MasterSquirrelBot(2000, 2000, new XY(settings.getSize().x / 2, settings.getSize().y / 2)));

                //2bot
                addPlayer(new MasterSquirrelBot(2000, 2000, new XY(settings.getSize().x - 2, settings.getSize().y - 2),settings.getMasterBotPath(), settings.getMiniBotPath()));
                addPlayer(new MasterSquirrelBot(3000, 2000, new XY(2, 2),settings.getMasterBotPath() ,settings.getMiniBotPath()));
                break;
        }
        fillSet();
    }

    /**
     * This method returns the current game mode.
     * @return the current game mode
     */
    public int getMode(){
        return mode;
    }

    /**
     * This method returns all the entities that are in the board object in a a String format.
     * @return all the entities in a String format
     */
    public String toString() {
        return list.toString();
    }

    /**
     * This method returns the BoardConfig of the board.
     * @return
     */
    public BoardConfig getConfig() {
        return settings;
    }

    /**
     * This method returns a 2- dimensional version of the board for easier visualisation.
     * @return This Board as a FlattenedBoard
     * @throws Exception
     */
    public FlattenedBoard flatten() throws Exception {
        return new FlattenedBoard(this);
    }

    /**
     * This method returns a List of all the Entities in the board.
     * @return List of all the Entities
     * @throws Exception
     */
    public ArrayList<Entity> getList() throws Exception { //return EntitySet
        return list;
    }

    /**
     * This method fills the Board with the amount of entities
     * which are specified in the BoardConfig.
     * The method uses the private methods fillOuterWalls() and spawnBeastsPlants().
     */
    public void fillSet() {
        fillOuterWalls();
        spawnBeastsPlants();
    }

    /**
     * This method is used to add the player entity into the board.
     * @param entity player entity
     */
    public void addPlayer(Entity entity) {
        list.add(entity);
    }

    /**
     * This method fills the border of the board with walls.
     */
    private void fillOuterWalls() { // creats a border of walls on the field
        int wallCounter = -1; //negative ID for walls

        for (int i = 0; i < settings.getSize().x; i++) {//toprow
            list.add(new Wall(wallCounter, new XY(i, 0)));
            wallCounter--;
        }

        for (int i = 0; i < settings.getSize().x; i++) {//botrow
            list.add(new Wall(wallCounter, new XY(i, settings.getSize().y - 1)));
            wallCounter--;
        }

        for (int i = 1; i < settings.getSize().y; i++) {    //doenst overlap existing walls
            list.add(new Wall(wallCounter, new XY(0, i)));
            wallCounter--;
        }

        for (int i = 1; i < settings.getSize().y; i++) {
            //list.add(new Wall(wallCounter, new XY(settings.getSize().y - 1, i)));
            list.add(new Wall(wallCounter, new XY(settings.getSize().y - 1, i)));
            wallCounter--;
        }
    }

    /**
     * This method initialises the amount of Beast and Plants which
     * are specified in the BoardConfig, at random Positions.
     */
    private void spawnBeastsPlants() { // creates Entities
        int z = 0;
        for (int j = 0; j < 4; j++) {
            switch (j) {
                case 0:
                    z = settings.getBadBeastAmount();
                    break;
                case 1:
                    z = settings.getGoodBeastAmount();
                    break;
                case 2:
                    z = settings.getBadPlantAmount();
                    break;
                case 3:
                    z = settings.getGoodPlantAmount();
                    break;
            }

            for (int i = 0; i < z; i++) { // for all entities in settings
                int randomX = 1 + (int) (Math.random() * (settings.getSize().x - 2));
                int randomY = 1 + (int) (Math.random() * (settings.getSize().y - 2));
                XY temp = new XY(randomX, randomY);

                for (int a = settings.getWallCount(); a < list.size(); a++) {
                    if (temp.equals(list.get(a))) { // repeats if there is already an Ent.
                        i--;
                        continue;
                    }
                }
                switch (j) {
                    case 0:
                        list.add(new BadBeast(entityCounter, new XY(randomX, randomY)));
                        entityCounter++;
                        break;
                    case 1:
                        list.add(new GoodBeast(entityCounter, new XY(randomX, randomY)));
                        entityCounter++;
                        break;
                    case 2:
                        list.add(new BadPlant(entityCounter, new XY(randomX, randomY)));
                        entityCounter++;
                        break;
                    case 3:
                        list.add(new GoodPlant(entityCounter, new XY(randomX, randomY)));
                        entityCounter++;
                        break;
                }
            }
        }
    }
}
