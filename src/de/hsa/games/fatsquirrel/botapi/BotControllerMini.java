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
        MasterSquirrelBot master = (MasterSquirrelBot) view.getEntity();
        XY position = master.getPosition();

        for(int i = viewLowerLeft.getX(); i <= viewUpperRight.getX(); i++){
            for(int j = viewLowerLeft.getY(); j >= viewUpperRight.getY(); j--){
                if(entityContext.getEntityType(new XY(i,j)) != null) {
                    if(entityContext.getEntityType(new XY (i,j)).equals(master)){
                        continue;
                    }
                    entities[counter++] = entityContext.getEntityType(new XY(i, j));
                }
            }
        }

        int distanceY = Math.abs(entities[0].getPosition().getY() - position.getY());
        int distanceX = Math.abs(entities[0].getPosition().getX() - position.getX());
        int index = 0;
        for (int i = 1; i < counter; i++) {
            int distanceX2 = Math.abs(entities[i].getPosition().getX() - position.getX());
            int distanceY2 = Math.abs(entities[i].getPosition().getY() - position.getY());
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
                System.out.println("WALL");
                moveDirection = new XY(master.getPosition().getX() - entities[index].getPosition().getX(), master.getPosition().getY() - entities[index].getPosition().getY());
                for(int t = 0; t < 15; t++){
                    System.out.println(moveDirection.getX() + "/" +  moveDirection.getY());
                }
                break;
            case MASTER_SQUIRREL:

                //break;
            case MINI_SQUIRREL:
                System.out.println("MINI");
                // break;
            case NONE:
                //sollte selten vorkommen aber RNG dann
                break;
            case BAD_PLANT:
            case BAD_BEAST:
                System.out.println("BAD B");
                moveDirection = new XY(master.getPosition().getX() - entities[index].getPosition().getX(), master.getPosition().getY() - entities[index].getPosition().getY());
                for(int t = 0; t < 15; t++){
                    System.out.println(moveDirection.getX() + "/" +  moveDirection.getY());
                }
                break;
            case GOOD_PLANT:
            case GOOD_BEAST:
                System.out.println("GOOD B");
                moveDirection = new XY(-(master.getPosition().getX() - entities[index].getPosition().getX()), -(master.getPosition().getY() - entities[index].getPosition().getY()));
                for(int t = 0; t < 15; t++){
                    System.out.println(moveDirection.getX() + "/" +  moveDirection.getY());
                }
                break;
        }

        // GB move dir : entities[index].getPosition().getX() - master.getPosition().getX(), entities[index].getPosition().getY() - master.getPosition().getY()

        //move normalisieren
        int x = 0;
        int y = 0;
        if(moveDirection.getX() != 0){
            if(moveDirection.getX() < 0){
                x = -1;
            }else{
                x = 1;
            }
        }
        if (moveDirection.getY() != 0){
            if (moveDirection.getY() < 0) {
                y = -1;
            }else{
                y = 1;
            }
        }
        moveDirection = new XY(x,y);
        view.move(moveDirection);
    }
}