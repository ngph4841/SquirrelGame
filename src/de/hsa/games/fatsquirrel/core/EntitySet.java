package de.hsa.games.fatsquirrel.core;

public class EntitySet {
	private Entity[] set;
	private int counter = 0;

	public EntitySet() {
		set = new Entity[50];
	}

	public EntitySet(int cap) {
		set = new Entity[cap];
	}
	
	public Entity[] getSet(){
		return set;
	}

	public Entity getEntity(int index) {
		return set[index];
	}

	public int length() {
		return set.length;
	}
	
	public void setEntitySet(Entity[] set){
		this.set = set;
	}

	public void add(Entity o) {
		if (counter < set.length) {
			for (int i = 0; i < counter; i++) {
				if (set[i].equals(o)) {
					return;
				}
			}
			set[counter++] = o;
		}
	}
	
	public void plus(Entity o){
		EntitySet temp = new EntitySet(set.length +1);
		for(int i = 0; i < set.length; i++){
			temp.add(set[i]);
		}
		temp.add(o);
		setEntitySet(temp.getSet());
	}

	public void remove(Entity o) {
		if (counter == 0) {
			return;
		}
		for (int i = 0; i < counter; i++) {
			if (set[i].equals(o)) {
				set[i] = null;
				counter--;
			}
		}
		int a = 0;
		for (int i = 0; i < counter; i++) {
			if (set[i] == null) {
				a++;
			}
			set[i] = set[a++];
		}
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
			if(set[i] instanceof Character){
				set[i].nextStep(context);
			}
		}
	}
}