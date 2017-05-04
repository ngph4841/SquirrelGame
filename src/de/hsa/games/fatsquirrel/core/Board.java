package de.hsa.games.fatsquirrel.core;

public class Board {
    private BoardConfig settings;
    private EntitySet list;
    private int entityCounter;

    public Board(BoardConfig settings) {
        this.settings = settings;    //load Configs
        this.entityCounter = 0;
        this.list = new EntitySet(settings.getEntityAmount() + settings.getWallCount());//EntitySetlength
        fillSet();
    }

    public String toString() {
        return list.toString();
    }

    public BoardConfig getConfig() {
        return settings;
    }

    public BoardView flatten() throws Exception {
        return new FlattenedBoard(this);
    }

    public EntitySet getEntitySet() throws Exception {
        return list;
    }

    public void fillSet() {
        list.add(new MasterSquirrel(2000, new XY(settings.getSize().getX() / 2, settings.getSize().getY() / 2)));
        fillOuterWalls();
        spawnBeastsPlants();
    }

    private void fillOuterWalls() { // creats a border of walls on the field
        int wallCounter = -1; //negative ID for walls

        for (int i = 0; i < settings.getSize().getX(); i++) {//toprow
            list.add(new Wall(wallCounter, new XY(i, 0)));
            wallCounter--;
        }

        for (int i = 0; i < settings.getSize().getX(); i++) {//botrow
            list.add(new Wall(wallCounter, new XY(i, settings.getSize().getY() - 1)));
            wallCounter--;
        }

        for (int i = 1; i < settings.getSize().getY(); i++) {    //doenst overlap existing walls
            list.add(new Wall(wallCounter, new XY(0, i)));
            wallCounter--;
        }

        for (int i = 1; i < settings.getSize().getY(); i++) {
            list.add(new Wall(wallCounter, new XY(settings.getSize().getX() - 1, i)));
            wallCounter--;
        }
    }

    private void spawnBeastsPlants() { // creates Entities
        int z = 0;
        for (int j = 0; j < 4; j++) {
            if (j == 0) {
                z = settings.getBadBeastAmount();
            }
            if (j == 1) {
                z = settings.getGoodBeastAmount();
            }
            if (j == 2) {
                z = settings.getBadPlantAmount();
            }
            if (j == 3) {
                z = settings.getGoodPlantAmount();
            }

            for (int i = 0; i < z; i++) { // for all entities in settings
                int randomX = 1 + (int) (Math.random() * (settings.getSize().getX() - 2));
                int randomY = 1 + (int) (Math.random() * (settings.getSize().getY() - 2));
                XY temp = new XY(randomX, randomY);

                for (int a = settings.getWallCount(); a < list.length(); a++) {
                    // check
                    // in
                    // EntitySet
                    // for
                    // Entities
                    // w/o
                    // walls
                    if (temp.equals(list.getEntity(a))) { // repeats if there is already an Ent.
                        i--;
                        continue;
                    }
                }
                if (j == 0) {
                    list.add(new BadBeast(entityCounter, new XY(randomX, randomY)));
                    entityCounter++;
                    continue;
                }
                if (j == 1) {
                    list.add(new GoodBeast(entityCounter, new XY(randomX, randomY)));
                    entityCounter++;
                    continue;
                }
                if (j == 2) {
                    list.add(new BadPlant(entityCounter, new XY(randomX, randomY)));
                    entityCounter++;
                    continue;
                }
                if (j == 3) {
                    list.add(new GoodPlant(entityCounter, new XY(randomX, randomY)));
                    entityCounter++;
                    continue;
                }
            }
        }
    }
}
