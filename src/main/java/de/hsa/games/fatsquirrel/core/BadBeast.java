package de.hsa.games.fatsquirrel.core;

import java.util.Random;

public class BadBeast extends Character {
    private static int startEnergy = -10;
    private int bite = 7;

    BadBeast(int id, XY position) {
        super(id, startEnergy, position);

    }

    public int getBite() {
        return bite;
    }

    public void bite() {
        bite--;
    }

    public void nextStep(EntityContext context) throws Exception {
        if (turnCounter == 0) {
            XY squirrelPosition = new XY (99999,99999);
            if(context.nearestPlayer(this.position) !=null) {
                squirrelPosition = context.nearestPlayer(this.position).getPosition();
            }
            XY moveDirection = squirrelPosition.minus(position);
            if (Math.abs(moveDirection.x) <= 6 && Math.abs(moveDirection.y) <= 6) {
                int x = moveDirection.x;
                int y = moveDirection.y;
                // vector calc.
                if (x != 0) {
                    if (x > 0) {
                        x = 1;
                    } else {
                        x = -1;
                    }
                }
                if (y != 0) {
                    if (y > 0) {
                        y = 1;
                    } else {
                        y = -1;
                    }
                }
                moveDirection = new XY(x, y);
            } else {
                moveDirection = moveBeast(); // if squirrel too far rng
            }
            context.tryMove(this, moveDirection); // check if vector viable&move
        }
        turnCounter++;
        if (turnCounter >= 4) {
            turnCounter = 0;
        }
    }

    public static XY moveBeast() { // returns moveDircetion Vector
        Random rn = new Random();
        int x = 0;
        int y = 0;
        int range = 3;
        x = rn.nextInt(range) + (-1);
        y = rn.nextInt(range) + (-1);

        return new XY(x, y);
    }
}
