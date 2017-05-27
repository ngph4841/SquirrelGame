package de.hsa.games.fatsquirrel.core;

public abstract class Entity {// abstarct damits nur spezifische Entitys gib zb
    // MasterSquireel
    protected int id;
    protected int startEnergy;
    protected int energy;
    protected XY position;


    Entity(int id, int energy, XY position) {
        this.id = id;
        this.energy = energy;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public int getEnergy() {
        return energy;
    }

    public XY getPosition() {
        return position;
    }

    public void setPosition(XY position) {
        this.position = position;
    }

    public void updateEnergy(int deltaEnergy) {
        energy += deltaEnergy;
    }

    public abstract void nextStep(EntityContext context) throws Exception;

    public String toString() {
        String s = "";
        s += "Type: " + this.getClass() + "\nid: " + id + "\nenergy: " + energy + "\nposition: " + position.x + "|"
                + position.y + "\n";
        return s;
    }

    public boolean equals(Entity x) { // id check
        return this.id == x.getId();
    }
}
