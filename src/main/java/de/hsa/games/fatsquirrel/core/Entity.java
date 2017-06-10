package de.hsa.games.fatsquirrel.core;

/**The entity class is abstract and has different inherited classes.
 * (Squirrel,Character,Wall,GoodPlant,BadPlant)
 * Contains ID, Energy, StartEnergy and Position.
 */
public abstract class Entity {
    protected int id;
    protected int startEnergy;
    protected int energy;
    protected XY position;

    /**
     *
     * @param id id of the object
     * @param energy energy of the object
     * @param position position in the Board
     */
    Entity(int id, int energy, XY position) {
        this.id = id;
        this.energy = energy;
        this.position = position;
    }

    /**
     * This returns the objects id.
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * This returns the objects energy.
     * @return the energy the object
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * This returns the objects position.
     * @return the position of the object
     */
    public XY getPosition() {
        return position;
    }

    /**
     * Replaces the old position of the object
     * @param position new Position for the object
     */
    public void setPosition(XY position) {
        this.position = position;
    }

    /**
     * This method modifies the energy of the object.
     * @param deltaEnergy energy that changes the energy of the object
     */
    public void updateEnergy(int deltaEnergy) {
        energy += deltaEnergy;
    }

    /**
     * @param context EntityContext contains the logic of the entity
     * @throws Exception
     */
    public abstract void nextStep(EntityContext context) throws Exception;

    /**
     * This method returns the objects values in a string format.
     * @return the objects values in a string format
     */
    public String toString() {
        String s = "";
        s += "Type: " + this.getClass() + "\nid: " + id + "\nenergy: " + energy + "\nposition: " + position.x + "|"
                + position.y + "\n";
        return s;
    }

    /**
     * This method checks if two Entities are the same Entity.
     * @param x Entity to be compared.
     * @return true if its the same entity, else false.
     */
    public boolean equals(Entity x) { // id check
        return this.id == x.getId();
    }
}
