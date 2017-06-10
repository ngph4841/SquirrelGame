package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.util.MainLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The FlattenedBoard class implements 2 Interfaces, the BoardView and EntityContext.
 * This class is a 2-dimensional visualisation of the board(BoardView) and also the class
 * that contains the logic of entity collisions.
 */
public class FlattenedBoard implements BoardView, EntityContext {
    private Board board;
    private Entity[][] flatBoard;
    private BoardConfig settings;
    private Logger logger;

    /**
     * Constructor which creates a 2-dimensional board with entities.
     * @param board Board that contains the information
     * @throws Exception
     */
    public FlattenedBoard(Board board) throws Exception { // 2dim.array
        this.board = board;
        this.settings = board.getConfig();
        this.flatBoard = new Entity[settings.getSize().x][settings.getSize().y];
        this.logger = Logger.getLogger("launcherLogger");

        ArrayList<Entity> list = board.getList(); //EntitySet
        int x;
        int y;
        for (int i = 0; i < list.size(); i++) { // check all entities
            if (list.get(i) == null) {
                break;
            }
            x = list.get(i).getPosition().x;
            y = list.get(i).getPosition().y;
            flatBoard[x][y] = list.get(i); // add Entities from the list
        }
    }

    /**
     * This method returns the Entity that is at the position: x/y.
     * @param x x-coordinate
     * @param y y-coordinate
     * @return
     */
    public Entity getEntityType(int x, int y) {
        return flatBoard[x][y];
    }

    /**
     * This method returns the size of the board.
     * @return size of the board XY(width,height)
     */
    public XY getSize() {
        return settings.getSize();
    }

    /**
     * This method returns the Entity which is at the position XY
     * @param xy position in the board which is desired
     * @return
     */
    public Entity getEntityType(XY xy) {
        if (xy.x >= settings.getSize().x || xy.y >= settings.getSize().y) {            //  A U F P A S S E N (grose gleich zu groser)
            return null;
        }
        return flatBoard[xy.x][xy.y];
    }

    /**
     * This method returns the current board.
     * @return
     */
    @Override
    public Board getBoard() {
        return board;
    }

    /**
     * This method returns the amount of turns a round consists of.
     * @return amount of turns a round consists of
     */
    @Override
    public int getStepCounter() {
        return settings.getTurnCounter();
    }

    /**
     * This method tries to move the MiniSquirrel with the moveDirection XY and
     * checks if the move is valid.
     * @param mini MiniSquirrel
     * @param moveDirection Direction of the desired move
     * @throws Exception OutOfBoundsException if the move would move the entity out of the board
     */
    public void tryMove(MiniSquirrel mini, XY moveDirection) throws Exception {
        int x = mini.getPosition().x + moveDirection.x; // calc new pos
        int y = mini.getPosition().y + moveDirection.y;

        if (getEntityType(x, y) != null) { // if null no check
            int deltaEnergy = getEntityType(x, y).getEnergy();
            if (getEntityType(x, y) instanceof Wall) {
                mini.updateEnergy(deltaEnergy);
                mini.stun();
                logger.log(Level.INFO, "MiniSquirrel at: " + mini.getPosition().toString() + "just got stunned!");
                return;
            } else if (getEntityType(x, y) instanceof MasterSquirrel) {
                MasterSquirrel temp = (MasterSquirrel) getEntityType(x, y);
                if (temp.checkIfChild(mini)) {
                    getEntityType(x, y).updateEnergy(mini.getEnergy());
                    kill(mini);
                    return;
                } else {
                    getEntityType(x, y).updateEnergy(150);
                    kill(mini);
                    return;
                }
            } else if (getEntityType(x, y) instanceof MiniSquirrel) {
                MiniSquirrel temp = (MiniSquirrel) getEntityType(x, y);
                if (mini.getParentId() != temp.getParentId()) {
                    kill(mini);
                    kill(getEntityType(x, y));
                    return;
                }
            } else if (getEntityType(x, y) instanceof BadBeast) {
                mini.updateEnergy(deltaEnergy);
                BadBeast temp = (BadBeast) getEntityType(x, y);
                temp.bite();
                logger.log(Level.INFO, "BadBeast at: " + temp.getPosition().toString() + " attacked a Squirrel at: "
                        + mini.getPosition().toString());
                if (temp.getBite() <= 0) {
                    killAndReplace(temp);
                }
                if (mini.getEnergy() <= 0) {
                    kill(mini);
                    return;
                }
            } else {
                killAndReplace(getEntityType(x, y));
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

    /**
     * This method tries to move the GoodBeast with the moveDirection XY and
     * checks if the move is valid.
     * @param good GoodBeast
     * @param moveDirection Direction of the desired move
     * @throws Exception OutOfBoundsException if the move would move the entity out of the board
     */
    public void tryMove(GoodBeast good, XY moveDirection) throws Exception {
        int x = good.getPosition().x + moveDirection.x;
        int y = good.getPosition().y + moveDirection.y;
        if (getEntityType(x, y) != null) {
            return;
        }
        move(good, new XY(x, y));
    }

    /**
     * This method tries to move the BadBeast with the moveDirection XY and
     * checks if the move is valid.
     * @param bad BadBeast
     * @param moveDirection Direction of the desired move
     * @throws Exception OutOfBoundsException if the move would move the entity out of the board
     */
    public void tryMove(BadBeast bad, XY moveDirection) throws Exception {
        int x = bad.getPosition().x + moveDirection.x;
        int y = bad.getPosition().y + moveDirection.y;
        if (getEntityType(x, y) != null) {
            if (getEntityType(x, y) instanceof Squirrel) {
                bad.bite();
                logger.log(Level.INFO,
                        "BadBeast at: " + bad.getPosition().toString() + " attacked a Squirrel at: " + x + "/" + y);
                getEntityType(x, y).updateEnergy(bad.getEnergy());
                if (getEntityType(x, y) instanceof MiniSquirrel) {
                    if (getEntityType(x, y).getEnergy() <= 0) {
                        kill(getEntityType(x, y));
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

    /**
     * This method tries to move the MasterSquirrel with the moveDirection XY and
     * checks if the move is valid.
     * @param master MasterSquirrel
     * @param moveDirection Direction of the desired move
     * @throws Exception OutOfBoundsException if the move would move the entity out of the board
     */
    public void tryMove(MasterSquirrel master, XY moveDirection) throws Exception {
        if (!master.getStun()) {
            int x = master.getPosition().x + moveDirection.x;
            int y = master.getPosition().y + moveDirection.y;
            if (getEntityType(x, y) == null) {
                move(master, new XY(x, y));
            } else {
                int deltaEnergy = getEntityType(x, y).getEnergy(); // how much eng
                if (getEntityType(x, y) instanceof Wall) { // wall stun
                    master.stun();
                    master.updateEnergy(deltaEnergy);
                    logger.log(Level.INFO,
                            "MasterSquirrel at: " + master.getPosition().toString() + "just got stunned!");
                } else if (getEntityType(x, y) instanceof MasterSquirrel) { // nothing
                    return;
                } else if (getEntityType(x, y) instanceof MiniSquirrel) {
                    if (master.checkIfChild(getEntityType(x, y))) {
                        master.updateEnergy(deltaEnergy); // child gives mama
                        // gift
                    } else {
                        master.updateEnergy(150); // takes eng from rndm child
                    }
                    kill(getEntityType(x, y)); // child dies :(
                } else if (getEntityType(x, y) instanceof BadBeast) {
                    master.updateEnergy(deltaEnergy);
                    BadBeast temp = (BadBeast) getEntityType(x, y);
                    temp.bite();
                    logger.log(Level.INFO, "BadBeast at: " + temp.getPosition().toString()
                            + " attacked a Squirrel at: " + master.getPosition().toString());
                    if (temp.getBite() <= 0) {
                        killAndReplace(temp);
                    }
                    return;
                } else if (getEntityType(x, y) instanceof GoodBeast | getEntityType(x, y) instanceof GoodPlant
                        | getEntityType(x, y) instanceof BadPlant) {
                    master.updateEnergy(deltaEnergy); // plants&goodbeast
                    killAndReplace(getEntityType(x, y));
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

    /**
     * This method returns the nearest Squirrel in the area from the entity that calls this method.
     * @param position position of the entity that calls this method
     * @return nearest Squirrel
     */
    public Squirrel nearestPlayer(XY position) {
        Entity[] squirrelPosition = new Entity[100];
        int counter = 0; // for squirrels
        int boardHeight = settings.getSize().y;
        int boardWidth = settings.getSize().x;
        for (int j = 0; j < boardHeight; j++) { // find all squirrels
            for (int i = 0; i < boardWidth; i++) {
                if (getEntityType(i, j) instanceof Squirrel) {
                    squirrelPosition[counter++] = getEntityType(i, j);
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

    /**
     * This method kills and replaces a entity from the game.
     * The entity gets deleted from the game and a new entity of the
     * same class gets initialised with the same id and a new random position.
     * @param entity which should be deleted and added
     * @throws Exception
     */
    public void killAndReplace(Entity entity) throws Exception {
        if (!(entity instanceof Wall)) {
            int randomX = 0;
            int randomY = 0;
            while (getEntityType(randomX, randomY) != null) {
                //as long as field is not null
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
            logger.log(Level.WARNING,
                    "a new " + entity.getClass().toString() + " at: " + temp.toString() + " has just appeared");
        }
    }

    /**
     * This method kills a entity by deleting it out of the game.
     * @param entity which should be deleted
     * @throws Exception
     */
    public void kill(Entity entity) throws Exception {
        if (!(entity instanceof Wall)) {
            logger.log(Level.WARNING,
                    entity.getClass().toString() + " at: " + entity.getPosition().toString() + " has just died");
            board.getList().remove(entity);
            refresh();
        }
    }

    /**
     * This method moves the entity to the new position.
     * Positions which are outside of the board are ignored.
     * @param entity entity that is tired of its position
     * @param position desired position of the entity
     * @throws Exception
     */
    public void move(Entity entity, XY position) throws Exception {
        if (position.x < 0 | position.y < 0 | position.x > settings.getSize().x | position.y > settings.getSize().y) {
            return;
        }
        logger.log(Level.INFO, entity.getClass().toString() + " moved:" + entity.getPosition().toString() + "->"
                + position.toString());
        entity.setPosition(position);
        refresh();
    }

    /**
     * This method refreshes the 2-dimensional board visualisation after the movement of an entity.
     * @throws Exception
     */
    public void refresh() throws Exception {
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
