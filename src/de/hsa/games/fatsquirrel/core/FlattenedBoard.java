package de.hsa.games.fatsquirrel.core;

import java.awt.List;

public class FlattenedBoard implements BoardView, EntityContext {
    private Board board;
    private Entity[][] flatBoard;
    private BoardConfig settings;

    public FlattenedBoard(Board board) throws Exception { //2dim.array
        this.board = board;
        this.settings = board.getConfig();
        this.flatBoard = new Entity[settings.getSize().getX()][settings.getSize().getY()];

        EntitySet list = board.getEntitySet();
        int x;
        int y;
        for (int i = 0; i < list.length(); i++) {            //check all entities in the EntitySet
            if (list.getEntity(i) == null) {
                break;
            }
            x = list.getEntity(i).getPosition().getX();    //get coordinates from the entity
            y = list.getEntity(i).getPosition().getY();
            flatBoard[x][y] = list.getEntity(i);    //add Entities from EntitySet to flatBoard


            //DBG
            if(flatBoard[x][y] instanceof Character) {
                System.out.println("flatboard constructor");
                System.out.println(list.getEntity(i).getClass());
                System.out.println("flatboard" + x + "/" + y);
                System.out.println("entity" + list.getEntity(i).getPosition().getX() + "/" + list.getEntity(i).getPosition().getY());
            }
        }
    }

    public Entity[][] getFlattenedBoard() {
        return flatBoard;
    }

    public Entity getEntityType(int x, int y) {
        return flatBoard[x][y];
    }

    public XY getSize() {
        return new XY(settings.getSize().getX(), settings.getSize().getY());
    }

    public Entity getEntityType(XY xy) {
        return flatBoard[xy.getX()][xy.getY()];
    }


    public void tryMove(MiniSquirrel mini, XY moveDirection) throws Exception {
        int x = mini.getPosition().getX() + moveDirection.getX();    //calc new pos
        int y = mini.getPosition().getY() + moveDirection.getY();
        if (flatBoard[x][y] != null) {    //if null no check
            int deltaEnergy = flatBoard[x][y].getEnergy();
            if (flatBoard[x][y] instanceof Wall) {
                mini.updateEnergy(deltaEnergy);
                mini.stun();
                System.out.println("Minisquirrel just got stunned!");
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
                flatBoard[x][y] = temp;
                if (mini.getEnergy() <= 0) {
                    kill(mini);
                    return;
                }
            } else {
                killAndReplace(flatBoard[x][y]);
            }
            mini.updateEnergy(deltaEnergy);
        }
        mini.updateEnergy(-1);    //loses 1energy
        if (mini.getEnergy() <= 0) {
            kill(mini);
            return;
        }
        move(mini,new XY(x,y));
    }

    public void tryMove(GoodBeast good, XY moveDirection) throws Exception {
        int x = good.getPosition().getX() + moveDirection.getX();
        int y = good.getPosition().getY() + moveDirection.getY();
        if (flatBoard[x][y] != null) {
            return;
        }
        move(good, new XY(x, y));
    }

    public void tryMove(BadBeast bad, XY moveDirection) throws Exception {
        int x = bad.getPosition().getX() + moveDirection.getX();
        int y = bad.getPosition().getY() + moveDirection.getY();
        if (flatBoard[x][y] != null) {
            if (flatBoard[x][y] instanceof Squirrel) {
                bad.bite();
                System.out.println("A BadBeast just attacked!");
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
            int x = master.getPosition().getX() + moveDirection.getX();
            int y = master.getPosition().getY() + moveDirection.getY();
            if (flatBoard[x][y] == null) {
                master.setPosition(new XY(x, y));
            } else {

                //DBG
                System.out.println("DBG:" + flatBoard[x][y].getClass());
                System.out.println("entitypos:" + flatBoard[x][y].getPosition().getX() + "/" + flatBoard[x][y].getPosition().getY());
                System.out.println("flatboardpos:" + x + "/" + y);

                int deltaEnergy = flatBoard[x][y].getEnergy(); //how much eng
                if (flatBoard[x][y] instanceof Wall) {    //wall stun
                    master.stun();
                    master.updateEnergy(deltaEnergy);
                } else if (flatBoard[x][y] instanceof MasterSquirrel) { //nothing
                    return;
                } else if (flatBoard[x][y] instanceof MiniSquirrel) {
                    if (master.checkIfChild(flatBoard[x][y])) {
                        master.updateEnergy(deltaEnergy);    //child gives mama gift
                    } else {
                        master.updateEnergy(150);    //takes eng from rndm child
                    }
                    kill(flatBoard[x][y]); //child dies :(
                } else if (flatBoard[x][y] instanceof BadBeast) {
                    master.updateEnergy(deltaEnergy);
                    tryMove((BadBeast) flatBoard[x][y], new XY(moveDirection.getX() * -1, moveDirection.getY() * -1));
                } else if (flatBoard[x][y] instanceof GoodBeast | flatBoard[x][y] instanceof GoodPlant | flatBoard[x][y] instanceof BadPlant){
                    master.updateEnergy(deltaEnergy);        //plants&goodbeast
                    killAndReplace(flatBoard[x][y]);
                    move(master, new XY (x, y));
                }
            }
            if (master.getEnergy() < 0) {
                master.updateEnergy(Math.abs(master.getEnergy()));
            }

        } else { //stun impl
            master.increaseStunCounter();
            if (master.getStunCounter() >= 3) {
                master.resetStunCounter();
                master.cleanse();
            }
        }
    }

    public Squirrel nearestPlayer(XY position) {
        Entity[] squirrelPosition = new Entity[100];
        int counter = 0; //for squirrels
        int boardHeight = settings.getSize().getY();
        int boardWidth = settings.getSize().getX();
        for (int j = 0; j < boardHeight; j++) { //find all squirrels
            for (int i = 0; i < boardWidth; i++) {
                if (flatBoard[i][j] instanceof Squirrel) {
                    squirrelPosition[counter++] = flatBoard[i][j];
                }
            }
        }
        //calc abs near
        int distanceY = Math.abs(squirrelPosition[0].getPosition().getY() - position.getY());
        int distanceX = Math.abs(squirrelPosition[0].getPosition().getX() - position.getX());
        int index = 0;
        for (int i = 1; i < counter; i++) {
            int distanceX2 = Math.abs(squirrelPosition[i].getPosition().getX() - position.getX());
            int distanceY2 = Math.abs(squirrelPosition[i].getPosition().getY() - position.getY());
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
            while (flatBoard[randomX][randomY] != null) { //as long as the field isnt null
                randomX = 1 + (int) (Math.random() * (settings.getSize().getX() - 2));
                randomY = 1 + (int) (Math.random() * (settings.getSize().getY() - 2));
            }
            XY temp = new XY(randomX, randomY);
            if (entity instanceof BadBeast) {
                BadBeast bad = new BadBeast(entity.getId(), temp);
                board.getEntitySet().plus(bad);
            } else if ( entity instanceof GoodBeast){
                GoodBeast good = new GoodBeast(entity.getId(), temp);
                board.getEntitySet().plus(good);
            } else if( entity instanceof BadPlant){
                BadPlant bad = new BadPlant(entity.getId(), temp);
                board.getEntitySet().plus(bad);
            } else if( entity instanceof GoodPlant){
                GoodPlant good = new GoodPlant(entity.getId(), temp);
                board.getEntitySet().plus(good);
            }
            kill(entity); //kill & update flatboard
            System.out.println("a new " + entity.getClass() + " has just appeared");
        }
    }

    public void kill(Entity entity) throws Exception {
        if (!(entity instanceof Wall)) {
            System.out.println("a " + entity.getClass() + " has just been killed");
            board.getEntitySet().remove(entity);

            EntitySet list = board.getEntitySet();
            int x;
            int y;
            for (int i = 0; i < list.length(); i++) {            //refresh the flatboard
                if (list.getEntity(i) == null) {                //w/o del entity
                    break;
                }
                x = list.getEntity(i).getPosition().getX();
                y = list.getEntity(i).getPosition().getY();
                flatBoard[x][y] = list.getEntity(i);
            }
        }
    }

    public void  move(Entity entity, XY position){
        int oldX = entity.getPosition().getX();
        int oldY = entity.getPosition().getY();

        //DBG
        System.out.println(entity.getClass());
        System.out.println("old position:" + oldX + "/" + oldY);
        System.out.println("new position:" + position.getX() + "/" + position.getY());

        flatBoard[oldX][oldY] = null;
        entity.setPosition(position);
        flatBoard[entity.getPosition().getX()][entity.getPosition().getY()] = entity;

        //DBG
        System.out.println(flatBoard[position.getX()][position.getY()].getClass());
        System.out.println(flatBoard[oldX][oldY]);
    }
}
