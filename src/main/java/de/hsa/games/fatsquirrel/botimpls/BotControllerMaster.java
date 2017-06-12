package de.hsa.games.fatsquirrel.botimpls;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.core.Entity;
import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.XY;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Freya on 19.05.2017.
 */
public class BotControllerMaster implements BotController {
    private Logger logger = Logger.getLogger("launcherLogger");

    public BotControllerMaster() {}

    @Override
    public void nextStep(ControllerContext view) throws Exception {
        EntityContext entityContext = view.getEntityContext();
        //load the view window
        XY viewLowerLeft = view.getViewLowerLeft();
        XY viewUpperRight = view.getViewUpperRight();
        Entity[] entities = new Entity[999]; //puffer für pawel 1/2
        int counter = 0;
        XY masterPosition = view.locate();

        //look at all the entities in the window
        for (int i = viewLowerLeft.x; i <= viewUpperRight.x; i++) {
            for (int j = viewLowerLeft.y; j >= viewUpperRight.y; j--) {
                if (entityContext.getEntityType(new XY(i, j)) != null) {
                    if (!masterPosition.equals(new XY(i,j))) {
                        entities[counter++] = entityContext.getEntityType(new XY(i, j));
                    }
                }
            }
        }
        if(entities[0] == null){
            logger.log(Level.WARNING,"no entity found in the viewWindow");
            return;
        }

        // calc. nearest Entity to react to
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
        XY moveDirection = new XY(0, 0);
        switch (view.getEntityAt(entities[index].getPosition())) {
            case WALL:
                moveDirection =  masterPosition.minus(entities[index].getPosition());
                break;
            case MASTER_SQUIRREL:
                //break;
            case MINI_SQUIRREL:
                // break;
            case NONE:
                // break;
            case BAD_BEAST:
                moveDirection =  masterPosition.minus(entities[index].getPosition());
                break;
            case BAD_PLANT:
            case GOOD_PLANT:
            case GOOD_BEAST:
                moveDirection =  masterPosition.minus(entities[index].getPosition()).times(-1);
                break;
        }

        //normalise move
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

        //rnd spawning
//        Random rn = new Random();
//        if(rn.nextInt(50) < 10) {
//            view.spawnMiniBot(new XY(1, 0), 100);
//        }
    }

    @Override
    public int getRemainingSteps(ControllerContext view) throws Exception {
        return view.getRemainingSteps();
    }
}