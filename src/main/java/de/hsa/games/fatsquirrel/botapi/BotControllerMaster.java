package de.hsa.games.fatsquirrel.botapi;

import de.hsa.games.fatsquirrel.core.Entity;
import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.XY;

/**
 * Created by Freya on 19.05.2017.
 */
public class BotControllerMaster implements BotController {

    BotControllerMaster() {
    }

    @Override
    public void nextStep(ControllerContext view) throws Exception {
        EntityContext entityContext = view.getEntityContext();

        XY viewLowerLeft = view.getViewLowerLeft();
        XY viewUpperRight = view.getViewUpperRight();
        Entity[] entities = new Entity[999]; //puffer für pawel 1/2
        int counter = 0;
        XY masterPosition = view.locate();

        for (int i = viewLowerLeft.x; i <= viewUpperRight.x; i++) {
            for (int j = viewLowerLeft.y; j >= viewUpperRight.y; j--) {
                if (entityContext.getEntityType(new XY(i, j)) != null) {
                    if (!masterPosition.equals(new XY(i,j))) {
                        entities[counter++] = entityContext.getEntityType(new XY(i, j));
                    }
                }
            }
        }

        //runs between the 2 walls on either side

        int distanceY = Math.abs(entities[0].getPosition().y - masterPosition.y);
        int distanceX = Math.abs(entities[0].getPosition().x - masterPosition.x);

        int index = 0;
        for (int i = 1; i < counter; i++) {
            int distanceX2 = Math.abs(entities[i].getPosition().x - masterPosition.x);
            int distanceY2 = Math.abs(entities[i].getPosition().y - masterPosition.y);
            if (distanceX2 <= distanceX && distanceY2 <= distanceY) {
                index = i;
                distanceX = distanceX2;
                distanceY = distanceY2;
            }
        }
        //nearest Entity at entites[index]
        XY moveDirection = new XY(0, 0);
        // TODO botbrain
        switch (view.getEntityAt(entities[index].getPosition())) {
            case WALL:
                moveDirection =  masterPosition.minus(entities[index].getPosition());
                break;
            case MASTER_SQUIRREL:
                //break;
            case MINI_SQUIRREL:
                // break;
            case NONE:
                //sollte selten vorkommen aber RNG dann
                break;
            case BAD_PLANT:
            case BAD_BEAST:
                moveDirection =  masterPosition.minus(entities[index].getPosition());
                break;
            case GOOD_PLANT:
            case GOOD_BEAST:
                moveDirection =  masterPosition.minus(entities[index].getPosition()).times(-1);
                break;
        }

        // GB move dir : entities[index].getPosition().getX() - master.getPosition().getX(), entities[index].getPosition().getY() - master.getPosition().getY()

        //move normalisieren
        int x = 0;
        int y = 0;
        if (moveDirection.x != 0) {
            if (moveDirection.x < 0) {
                x = -1;
            } else {
                x = 1;
            }
        }
        if (moveDirection.y != 0) {
            if (moveDirection.y < 0) {
                y = -1;
            } else {
                y = 1;
            }
        }
        moveDirection = new XY(x, y);
        view.move(moveDirection);
    }
}