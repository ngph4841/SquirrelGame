package de.hsa.games.fatsquirrel.core;

public class EntitySet {
    private Entity[] set;
    private int counter = 0;

    public EntitySet() {
        set = new Entity[150];
    }

    public EntitySet(int cap) {
        set = new Entity[cap];
    }

    public Entity get(int index) {
        return set[index];
    }

    public int size() {
        return set.length;
    }

    public void add(Entity entity) {
        if (counter < set.length) {
            for (int i = 0; i < counter; i++) {
                if (set[i].equals(entity)) {
                    return;
                }
            }
            set[counter++] = entity;
        }else{
            plus(entity);
        }
    }

    public void plus(Entity entity) {
        Entity[] temp = new Entity[++counter];
        for (int i = 0; i < set.length; i++) {
            temp[i] = set[i];
        }
        temp[counter - 1] = entity;
        set = temp;
    }

    public void remove(Entity entity) {
        if (counter <= 0) {
            return;
        }
        for (int i = 0; i < counter; i++) {
            if (set[i].equals(entity)) {
                set[i] = null;
                counter--;
                break;
            }
        }
        int a = 0;
        Entity[] temp = new Entity[counter];
        for (int i = 0; i < counter; i++) {
            if (set[i] == null) {
                a++;
            }
            temp[i] = set[a++];
        }
        set = temp;
    }

    public String toString() {
        String s = "content of EntitySet \n";
        for (int i = counter - 1; i >= 0; i--) {
            s += i + ". entry:\n " + set[i].toString() + "\n";
        }
        return s;
    }

    public void moveAll(EntityContext context) throws Exception {
        for (int i = 0; i < counter; i++) { // all elements in the []list
            if (set[i] instanceof Character | set[i] instanceof Squirrel) {
                set[i].nextStep(context);
            }
        }
    }
}