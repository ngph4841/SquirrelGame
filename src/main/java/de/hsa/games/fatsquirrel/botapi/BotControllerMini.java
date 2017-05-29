package de.hsa.games.fatsquirrel.botapi;

import de.hsa.games.fatsquirrel.core.Entity;
import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.MasterSquirrelBot;
import de.hsa.games.fatsquirrel.core.XY;
import java.util.Random;

import de.hsa.games.fatsquirrel.core.*;

/**
 * Created by Freya on 19.05.2017.
 */
public class BotControllerMini implements BotController {

    BotControllerMini(){
    }

    @Override
    public void nextStep(ControllerContext view) throws Exception {
        EntityContext entityContext = view.getEntityContext();

        XY viewLowerLeft = view.getViewLowerLeft();
        XY viewUpperRight = view.getViewUpperRight();
        Entity[] entities = new Entity[999]; //puffer für pawel 1/2
        int counter = 0;
        MiniSquirrelBot mini = (MiniSquirrelBot) view.getEntity();
        XY position = mini.getPosition();

        for(int i = viewLowerLeft.x; i <= viewUpperRight.x; i++){
            for(int j = viewLowerLeft.y; j >= viewUpperRight.y; j--){
                if(entityContext.getEntityType(new XY(i,j)) != null) {
                    if(entityContext.getEntityType(new XY (i,j)).equals(mini)){
                        continue;
                    }
                    entities[counter++] = entityContext.getEntityType(new XY(i, j));
                }
            }
        }

        int distanceY = Math.abs(entities[0].getPosition().y - position.y);
        int distanceX = Math.abs(entities[0].getPosition().x - position.x);
        int index = 0;
        for (int i = 1; i < counter; i++) {
            int distanceX2 = Math.abs(entities[i].getPosition().x - position.x);
            int distanceY2 = Math.abs(entities[i].getPosition().y - position.y);
            if (distanceX2 <= distanceX && distanceY2 <= distanceY) {
                index = i;
                distanceX = distanceX2;
                distanceY = distanceY2;
            }
        }
        //nearest Entity at entites[index]
        XY moveDirection = new XY(0,0);
        // TODO botbrain
        switch (view.getEntityAt(entities[index].getPosition())){
            case WALL:
                moveDirection =  position.minus(entities[index].getPosition());
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
                moveDirection =  position.minus(entities[index].getPosition());;
                break;
            case GOOD_PLANT:
            case GOOD_BEAST:
                moveDirection =  position.minus(entities[index].getPosition()).times(-1);
                break;
        }

        // GB move dir : entities[index].getPosition().getX() - master.getPosition().getX(), entities[index].getPosition().getY() - master.getPosition().getY()

        //move normalisieren
        int x = 0;
        int y = 0;
        if(moveDirection.x != 0){
            if(moveDirection.x < 0){
                x = -1;
            }else{
                x = 1;
            }
        }
        if (moveDirection.y != 0){
            if (moveDirection.y < 0) {
                y = -1;
            }else{
                y = 1;
            }
        }
        moveDirection = new XY(x,y);
        view.move(moveDirection);

        Random rn = new Random(); //random for now
        if(rn.nextInt(50) < 10) {
            int radius = rn.nextInt(9) + 1;
            view.implode(radius);
        }
    }
}