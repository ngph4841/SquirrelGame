package de.hsa.games.fatsquirrel.core;

import java.util.ArrayList;

public class Board {
    private BoardConfig settings;
    //private EntitySet list;
    private ArrayList<Entity> list;
    private int entityCounter;

    public Board(BoardConfig settings) {
        this.settings = settings;    //load Configs
        this.entityCounter = 0;
        //this.list = new EntitySet(2 + settings.getEntityAmount() + settings.getWallCount());
        this.list = new ArrayList<>(2 + settings.getWallCount() + settings.getEntityAmount());
    }

    public String toString() {
        return list.toString();
    }

    public BoardConfig getConfig() {
        return settings;
    }

    public FlattenedBoard flatten() throws Exception {
        return new FlattenedBoard(this);
    }

    public ArrayList<Entity> getList() throws Exception { //return EntitySet
        return list;
    }

    public void fillSet() {
        fillOuterWalls();
        spawnBeastsPlants();
    }

    public void addPlayer(Entity entity){
        list.add(entity);
    }


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
