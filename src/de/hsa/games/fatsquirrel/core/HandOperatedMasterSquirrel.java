package de.hsa.games.fatsquirrel.core;

public class HandOperatedMasterSquirrel extends MasterSquirrel {

    HandOperatedMasterSquirrel(int id, int energy, XY position) {
        super(id, energy, position);
    }

    public void nextStep() throws Exception {
        moveHandMaster();
    }

    public void moveHandMaster() throws Exception { // controls numPad
        int c;
        int x = 0;
        int y = 0;
        c = (int) System.in.read() - '0';
        System.in.read();
        System.in.read();
        System.out.println(c);


        switch (c) {
            case 1:
                x = -1;
                y = 0;
                break;

            case 2:
                x = 0;
                y = 1;
                break;

            case 3:
                x = 1;
                y = 0;
                break;

            case 5:
                x = 0;
                y = -1;
                break;
        }
        position = new XY(position.getX() + x, position.getY() + y);
    }
}