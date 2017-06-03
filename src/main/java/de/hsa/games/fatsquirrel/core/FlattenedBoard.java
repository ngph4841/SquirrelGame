package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.util.MainLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class FlattenedBoard implements BoardView, EntityContext {
    private Board board;
    private Entity[][] flatBoard;
    private BoardConfig settings;

    public FlattenedBoard(Board board) throws Exception { // 2dim.array
        this.board = board;
        this.settings = board.getConfig();
        this.flatBoard = new Entity[settings.getSize().x][settings.getSize().y];

        ArrayList<Entity> list = board.getList(); //EntitySet
        int x;
        int y;
        for (int i = 0; i < list.size(); i++) { // check all entities in the
            // EntitySet
            if (list.get(i) == null) {
                break;
            }
            x = list.get(i).getPosition().x;
            y = list.get(i).getPosition().y;
            flatBoard[x][y] = list.get(i); // add Entities from EntitySet
        }
    }

    public Entity getEntityType(int x, int y) {
        return flatBoard[x][y];
    }

    public XY getSize() {
        return settings.getSize();
    }

    public Entity getEntityType(XY xy) {
        if (xy.x >= settings.getSize().x || xy.y >= settings.getSize().y) {            //  A U F P A S S E N (grose gleich zu groser)
            return null;
        }
        return flatBoard[xy.x][xy.y];
    }

    @Override
    public Board getBoard() {
        return board;
    }

    public void tryMove(MiniSquirrel mini, XY moveDirection) throws Exception {
        int x = mini.getPosition().x + moveDirection.x; // calc new
        // pos
        int y = mini.getPosition().y + moveDirection.y;
        if (getEntityType(x,y) != null) { // if null no check
            int deltaEnergy = getEntityType(x,y).getEnergy();
            if (getEntityType(x,y) instanceof Wall) {
                mini.updateEnergy(deltaEnergy);
                mini.stun();
                MainLogger.log(Level.INFO, "MiniSquirrel at: " + mini.getPosition().toString() + "just got stunned!");
                return;
            } else if (getEntityType(x,y) instanceof MasterSquirrel) {
                MasterSquirrel temp = (MasterSquirrel) getEntityType(x,y);
                if (temp.checkIfChild(mini)) {
                    getEntityType(x,y).updateEnergy(mini.getEnergy());
                    kill(mini);
                    return;
                } else {
                    getEntityType(x,y).updateEnergy(150);
                    kill(mini);
                    return;
                }
            } else if (getEntityType(x,y) instanceof MiniSquirrel) {
                MiniSquirrel temp = (MiniSquirrel) getEntityType(x,y);
                if (mini.getParentId() != temp.getParentId()) {
                    kill(mini);
                    kill(getEntityType(x,y));
                    return;
                }
            } else if (getEntityType(x,y) instanceof BadBeast) {
                mini.updateEnergy(deltaEnergy);
                BadBeast temp = (BadBeast) getEntityType(x,y);
                temp.bite();
                MainLogger.log(Level.INFO, "BadBeast at: " + temp.getPosition().toString() + " attacked a Squirrel at: "
                        + mini.getPosition().toString());
                if (temp.getBite() <= 0) {
                    killAndReplace(temp);
                }
                if (mini.getEnergy() <= 0) {
                    kill(mini);
                    return;
                }
            } else {
                killAndReplace(getEntityType(x,y));
            }
            mini.updateEnergy(deltaEnergy);
        }
        mini.updateEnergy(-1); // loses 1energy
        if (mini.getEnergy() <= 0) {
            kill(mini);
            return;
        }
        move(mini, new XY(x, y));
    }

    public void tryMove(GoodBeast good, XY moveDirection) throws Exception {
        int x = good.getPosition().x + moveDirection.x;
        int y = good.getPosition().y + moveDirection.y;
        if (getEntityType(x,y) != null) {
            return;
        }
        move(good, new XY(x, y));
    }

    public void tryMove(BadBeast bad, XY moveDirection) throws Exception {
        int x = bad.getPosition().x + moveDirection.x;
        int y = bad.getPosition().y + moveDirection.y;
        if (getEntityType(x,y) != null) {
            if (getEntityType(x,y) instanceof Squirrel) {
                bad.bite();
                MainLogger.log(Level.INFO,
                        "BadBeast at: " + bad.getPosition().toString() + " attacked a Squirrel at: " + x + "/" + y);
                getEntityType(x,y).updateEnergy(bad.getEnergy());
                if (getEntityType(x,y) instanceof MiniSquirrel) {
                    if (getEntityType(x,y).getEnergy() <= 0) {
                        kill(getEntityType(x,y));
                    }
                }
                if (bad.getBite() <= 0) {
                    killAndReplace(bad);
                    return;
                }
            }
            return;
        }
        move(bad, new XY(x, y));
    }

    public void tryMove(MasterSquirrel master, XY moveDirection) throws Exception {

        if (!master.getStun()) {
            int x = master.getPosition().x+ moveDirection.x;
            int y = master.getPosition().y + moveDirection.y;
            if (getEntityType(x,y) == null) {
                move(master, new XY(x, y));
            } else {
                int deltaEnergy = getEntityType(x,y).getEnergy(); // how much eng
                if (getEntityType(x,y) instanceof Wall) { // wall stun
                    master.stun();
                    master.updateEnergy(deltaEnergy);
                    MainLogger.log(Level.INFO,
                            "MasterSquirrel at: " + master.getPosition().toString() + "just got stunned!");
                } else if (getEntityType(x,y) instanceof MasterSquirrel) { // nothing
                    return;
                } else if (getEntityType(x,y) instanceof MiniSquirrel) {
                    if (master.checkIfChild(getEntityType(x,y))) {
                        master.updateEnergy(deltaEnergy); // child gives mama
                        // gift
                    } else {
                        master.updateEnergy(150); // takes eng from rndm child
                    }
                    kill(getEntityType(x,y)); // child dies :(
                } else if (getEntityType(x,y) instanceof BadBeast) {
                    master.updateEnergy(deltaEnergy);
                    BadBeast temp = (BadBeast) getEntityType(x,y);
                    temp.bite();
                    MainLogger.log(Level.INFO, "BadBeast at: " + temp.getPosition().toString()
                            + " attacked a Squirrel at: " + master.getPosition().toString());
                    if (temp.getBite() <= 0) {
                        killAndReplace(temp);
                    }
                    return;
                } else if (getEntityType(x,y) instanceof GoodBeast | getEntityType(x,y) instanceof GoodPlant
                        | getEntityType(x,y) instanceof BadPlant) {
                    master.updateEnergy(deltaEnergy); // plants&goodbeast
                    killAndReplace(getEntityType(x,y));
                    move(master, new XY(x, y));
                }
            }
            if (master.getEnergy() < 0) {
                master.updateEnergy(Math.abs(master.getEnergy()));
            }

        } else { // stun impl
            master.increaseStunCounter();
            if (master.getStunCounter() >= 3) {
                master.resetStunCounter();
                master.cleanse();
            }
        }
    }

    public Squirrel nearestPlayer(XY position) {
        Entity[] squirrelPosition = new Entity[100];
        int counter = 0; // for squirrels
        int boardHeight = settings.getSize().y;
        int boardWidth = settings.getSize().x;
        for (int j = 0; j < boardHeight; j++) { // find all squirrels
            for (int i = 0; i < boardWidth; i++) {
                if (getEntityType(i,j) instanceof Squirrel) {
                    squirrelPosition[counter++] = getEntityType(i,j);
                }
            }
        }
        // calc abs near
        int distanceY = Math.abs(squirrelPosition[0].getPosition().y - position.y);
        int distanceX = Math.abs(squirrelPosition[0].getPosition().x - position.x);
        int index = 0;
        for (int i = 1; i < counter; i++) {
            int distanceX2 = Math.abs(squirrelPosition[i].getPosition().x - position.x);
            int distanceY2 = Math.abs(squirrelPosition[i].getPosition().y - position.y);
            if (distanceX2 <= distanceX && distanceY2 <= distanceY) {
                index = i;
                distanceX = distanceX2;
                distanceY = distanceY2;
            }
        }
        return (Squirrel) squirrelPosition[index];
    }

    public void killAndReplace(Entity entity) throws Exception {
        if (!(entity instanceof Wall)) {
            int randomX = 0;
            int randomY = 0;
            while (getEntityType(randomX,randomY) != null) { // as long as the
                // field isnt null
                randomX = 1 + (int) (Math.random() * (settings.getSize().x - 2));
                randomY = 1 + (int) (Math.random() * (settings.getSize().y - 2));
            }
            XY temp = new XY(randomX, randomY);
            if (entity instanceof BadBeast) {
                BadBeast bad = new BadBeast(entity.getId(), temp);
                board.getList().add(bad);
            } else if (entity instanceof GoodBeast) {
                GoodBeast good = new GoodBeast(entity.getId(), temp);
                board.getList().add(good);
            } else if (entity instanceof BadPlant) {
                BadPlant bad = new BadPlant(entity.getId(), temp);
                board.getList().add(bad);
            } else if (entity instanceof GoodPlant) {
                GoodPlant good = new GoodPlant(entity.getId(), temp);
                board.getList().add(good);
            }
            kill(entity); // kill & update flatboard
            MainLogger.log(Level.INFO,
                    "a new " + entity.getClass().toString() + " at: " + temp.toString() + " has just appeared");
        }
    }

    public void kill(Entity entity) throws Exception {
        if (!(entity instanceof Wall)) {
            MainLogger.log(Level.INFO,
                    entity.getClass().toString() + " at: " + entity.getPosition().toString() + " has just died");
            board.getList().remove(entity);
            refresh();
        }
    }

    public void move(Entity entity, XY position) throws Exception {
        if(position.x < 0 | position.y < 0){
            return;
        }
        MainLogger.log(Level.INFO, entity.getClass().toString() + " moved:" + entity.getPosition().toString() + "->"
                + position.toString());
        entity.setPosition(position);
        refresh();
    }

    public void refresh() throws Exception {
//        EntitySet list = board.getList();
        Entity[][] temp = new Entity[settings.getSize().x][settings.getSize().y];
        int x;
        int y;

        //foreach loop refreshing the grid
        for (Entity entity : board.getList()) {
            x = entity.getPosition().x;
            y = entity.getPosition().y;
            temp[x][y] = entity;
        }

        //EntitySet loop
//        for (int i = 0; i < list.size(); i++) { // refresh the flatboard
//            if (list.get(i) == null) {
//                break;
//            }
//            x = list.get(i).getPosition().x;
//            y = list.get(i).getPosition().y;
//            temp[x][y] = list.get(i);
//        }
        this.flatBoard = temp;
    }

}
