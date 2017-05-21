package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.console.NotEnoughEnergyException;

public class MasterSquirrel extends Squirrel {

    private static int startEnergy = 1000;
    private int childrenCounter = 0;
    private XY childStartPos;
    private int[] childrenId;

    public MasterSquirrel(int id, int energy, XY position) {
        super(id, energy, position);
    }

    public MasterSquirrel(int id, XY position) {
        super(id, startEnergy, position);
    }

    public void nextStep(EntityContext context) throws Exception {
            //context.tryMove(this, new XY(0, 0));
    }

    public boolean checkIfChild(Entity entity) { // checks in childrenId array for
        // the correct Id
        for (int i = 0; i < childrenId.length; i++) {
            if (childrenId[i] == entity.getId()) {
                return true;
            }
        }
        return false;
    }

    public XY moveMaster() {
        return position;
    }

    public MiniSquirrel spawnChild(int energy) throws NotEnoughEnergyException { // save id in array
        if (this.energy <= energy) {
            throw new NotEnoughEnergyException();
        }
        XY childStartPos = new XY(position.getX() + 1, position.getY()); // spawn next to mother
        childrenCounter++;                            // new child & id of child is mothersId + child#
        MiniSquirrel child = new MiniSquirrel(this.id + childrenCounter, energy, childStartPos, this.id);
        int[] temp = new int[childrenCounter];                            // new array for childrenId
        for (int i = 0; i < childrenCounter - 2; i++) {                // transfer id of children
            temp[i] = childrenId[i];
        }
        temp[childrenCounter - 1] = child.getId();                        // save id of this new child
        childrenId = temp;
        updateEnergy(-energy);
        return child;
    }

}
