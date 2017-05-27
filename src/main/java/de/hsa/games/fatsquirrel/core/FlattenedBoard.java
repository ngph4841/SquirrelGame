package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.util.MainLogger;

import java.awt.List;
import java.util.logging.Level;

public class FlattenedBoard implements BoardView, EntityContext {
    private Board board;
    private Entity[][] flatBoard;
    private BoardConfig settings;
	MainLogger mainLogger;

    public FlattenedBoard(Board board) throws Exception { // 2dim.array
        this.board = board;
        this.settings = board.getConfig();
        this.flatBoard = new Entity[settings.getSize().x][settings.getSize().y];
        this.mainLogger = new MainLogger(this.getClass().toString());

        EntitySet list = board.getEntitySet();
        int x;
        int y;
        for (int i = 0; i < list.length(); i++) { // check all entities in the
            // EntitySet
            if (list.getEntity(i) == null) {
                break;
            }
            x = list.getEntity(i).getPosition().x; // get coordinates from
            // the entity
            y = list.getEntity(i).getPosition().y;
            flatBoard[x][y] = list.getEntity(i); // add Entities from EntitySet
            // to flatBoard
        }
    }

    public Entity[][] getFlattenedBoard() {
        return flatBoard;
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
        if (flatBoard[x][y] != null) { // if null no check
            int deltaEnergy = flatBoard[x][y].getEnergy();
            if (flatBoard[x][y] instanceof Wall) {
                mini.updateEnergy(deltaEnergy);
                mini.stun();
                mainLogger.log(Level.INFO, "MiniSquirrel at: " + mini.getPosition().toString() + "just got stunned!");
                return;
            } else if (flatBoard[x][y] instanceof MasterSquirrel) {
                MasterSquirrel temp = (MasterSquirrel) flatBoard[x][y];
                if (temp.checkIfChild(mini)) {
                    flatBoard[x][y].updateEnergy(mini.getEnergy());
                    kill(mini);
                    return;
                } else {
                    flatBoard[x][y].updateEnergy(150);
                    kill(mini);
                    return;
                }
            } else if (flatBoard[x][y] instanceof MiniSquirrel) {
                MiniSquirrel temp = (MiniSquirrel) flatBoard[x][y];
                if (mini.getParentId() != temp.getParentId()) {
                    kill(mini);
                    kill(flatBoard[x][y]);
                    return;
                }
            } else if (flatBoard[x][y] instanceof BadBeast) {
                mini.updateEnergy(deltaEnergy);
                BadBeast temp = (BadBeast) flatBoard[x][y];
                temp.bite();
                mainLogger.log(Level.INFO, "BadBeast at: " + temp.getPosition().toString() + " attacked a Squirrel at: "
                        + mini.getPosition().toString());
                if (temp.getBite() <= 0) {
                    killAndReplace(temp);
                }
                if (mini.getEnergy() <= 0) {
                    kill(mini);
                    return;
                }
            } else {
                killAndReplace(flatBoard[x][y]);
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
        if (flatBoard[x][y] != null) {
            return;
        }
        move(good, new XY(x, y));
    }

    public void tryMove(BadBeast bad, XY moveDirection) throws Exception {
        int x = bad.getPosition().x + moveDirection.x;
        int y = bad.getPosition().y + moveDirection.y;
        if (flatBoard[x][y] != null) {
            if (flatBoard[x][y] instanceof Squirrel) {
                bad.bite();
                mainLogger.log(Level.INFO,
                        "BadBeast at: " + bad.getPosition().toString() + " attacked a Squirrel at: " + x + "/" + y);
                flatBoard[x][y].updateEnergy(bad.getEnergy());
                if (flatBoard[x][y] instanceof MiniSquirrel) {
                    if (flatBoard[x][y].getEnergy() <= 0) {
                        kill(flatBoard[x][y]);
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
            if (flatBoard[x][y] == null) {
                move(master, new XY(x, y));
            } else {
                int deltaEnergy = flatBoard[x][y].getEnergy(); // how much eng
                if (flatBoard[x][y] instanceof Wall) { // wall stun
                    master.stun();
                    master.updateEnergy(deltaEnergy);
                    mainLogger.log(Level.INFO,
                            "MasterSquirrel at: " + master.getPosition().toString() + "just got stunned!");
                } else if (flatBoard[x][y] instanceof MasterSquirrel) { // nothing
                    return;
                } else if (flatBoard[x][y] instanceof MiniSquirrel) {
                    if (master.checkIfChild(flatBoard[x][y])) {
                        master.updateEnergy(deltaEnergy); // child gives mama
                        // gift
                    } else {
                        master.updateEnergy(150); // takes eng from rndm child
                    }
                    kill(flatBoard[x][y]); // child dies :(
                } else if (flatBoard[x][y] instanceof BadBeast) {
                    master.updateEnergy(deltaEnergy);
                    BadBeast temp = (BadBeast) flatBoard[x][y];
                    temp.bite();
                    mainLogger.log(Level.INFO, "BadBeast at: " + temp.getPosition().toString()
                            + " attacked a Squirrel at: " + master.getPosition().toString());
                    if (temp.getBite() <= 0) {
                        killAndReplace(temp);
                    }
                    return;
                } else if (flatBoard[x][y] instanceof GoodBeast | flatBoard[x][y] instanceof GoodPlant
                        | flatBoard[x][y] instanceof BadPlant) {
                    master.updateEnergy(deltaEnergy); // plants&goodbeast
                    killAndReplace(flatBoard[x][y]);
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
                if (flatBoard[i][j] instanceof Squirrel) {
                    squirrelPosition[counter++] = flatBoard[i][j];
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
            while (flatBoard[randomX][randomY] != null) { // as long as the
                // field isnt null
                randomX = 1 + (int) (Math.random() * (settings.getSize().x - 2));
                randomY = 1 + (int) (Math.random() * (settings.getSize().y - 2));
            }
            XY temp = new XY(randomX, randomY);
            if (entity instanceof BadBeast) {
                BadBeast bad = new BadBeast(entity.getId(), temp);
                board.getEntitySet().plus(bad);
            } else if (entity instanceof GoodBeast) {
                GoodBeast good = new GoodBeast(entity.getId(), temp);
                board.getEntitySet().plus(good);
            } else if (entity instanceof BadPlant) {
                BadPlant bad = new BadPlant(entity.getId(), temp);
                board.getEntitySet().plus(bad);
            } else if (entity instanceof GoodPlant) {
                GoodPlant good = new GoodPlant(entity.getId(), temp);
                board.getEntitySet().plus(good);
            }
            kill(entity); // kill & update flatboard
            mainLogger.log(Level.INFO,
                    "a new " + entity.getClass().toString() + " at: " + temp.toString() + " has just appeared");
        }
    }

    public void kill(Entity entity) throws Exception {
        if (!(entity instanceof Wall)) {
            mainLogger.log(Level.INFO,
                    entity.getClass().toString() + " at: " + entity.getPosition().toString() + " has just died");
            board.getEntitySet().remove(entity);
            refresh();
        }
    }

    public void move(Entity entity, XY position) throws Exception {
        mainLogger.log(Level.INFO, entity.getClass().toString() + " moved:" + entity.getPosition().toString() + "->"
                + position.toString());
        entity.setPosition(position);
        refresh();
    }

    public void refresh() throws Exception {
        EntitySet list = board.getEntitySet();
        Entity[][] temp = new Entity[settings.getSize().x][settings.getSize().y];
        int x;
        int y;
        for (int i = 0; i < list.length(); i++) { // refresh the flatboard
            if (list.getEntity(i) == null) {
                break;
            }
            x = list.getEntity(i).getPosition().x;
            y = list.getEntity(i).getPosition().y;
            temp[x][y] = list.getEntity(i);
        }
        this.flatBoard = temp;
    }

}
