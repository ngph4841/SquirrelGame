package de.hsa.games.fatsquirrel.core;
import java.awt.List;

public class FlattenedBoard implements BoardView, EntityContext {
	private Board board;
	private Entity[][] flatBoard;
	private BoardConfig settings;

	public FlattenedBoard(Board board)throws Exception{ //2dim.array
		this.board = board;
		settings = board.getConfig();
		this.flatBoard = new Entity [settings.getSize().getY()][settings.getSize().getX()];

		EntitySet list = board.getEntitySet();
		int x; int y;
		for(int i = 0; i < list.length(); i++){			//check all entities in the EntitySet
			if(list.getEntity(i) == null){
				break;
			}
			x = list.getEntity(i).getPosition().getX();	//get coordinates from the entity
			y = list.getEntity(i).getPosition().getY();
			flatBoard[y][x] = list.getEntity(i);	//add Entities from EntitySet to flatBoard
		}
	}

	public Entity[][] getFlattenedBoard(){
		return flatBoard;
	}

	public Entity getEntityType(int x, int y){
		return flatBoard[y][x];
	}

	public XY getSize(){
		return new XY(flatBoard[0].length, flatBoard.length);
	}

	public Entity getEntityType(XY xy){
		return flatBoard[xy.getY()][xy.getX()] ;
	}


	public void tryMove(MiniSquirrel mini, XY moveDirection)throws Exception{
		int x = mini.getPosition().getX()+moveDirection.getX();	//calc new pos
		int y = mini.getPosition().getY()+moveDirection.getY();
		int deltaEnergy = flatBoard[y][x].getEnergy();
		if(flatBoard[y][x] != null){	//if null no check
			if(flatBoard[y][x] instanceof Wall){
				mini.updateEnergy(deltaEnergy);
				mini.stun();
				return;
			}
			if(flatBoard[y][x] instanceof MasterSquirrel){
				MasterSquirrel temp = (MasterSquirrel) flatBoard[y][x];
				if(temp.checkIfChild(mini)){
					flatBoard[y][x].updateEnergy(mini.getEnergy());
					kill(mini);
					return;
				}else{
					flatBoard[y][x].updateEnergy(150);
					kill(mini);
					return;
				}
			}
			if(flatBoard[y][x] instanceof MiniSquirrel){
				MiniSquirrel temp = (MiniSquirrel) flatBoard[y][x];
				if(mini.getParentId() != temp.getParentId()){
					kill(mini);
					kill(flatBoard[y][x]);
					return;
				}
			}
			if(flatBoard[y][x] instanceof BadBeast){
				mini.updateEnergy(deltaEnergy);
				BadBeast temp = (BadBeast) flatBoard[y][x];
				temp.bite();
				flatBoard[y][x] = temp;
				if(mini.getEnergy() <= 0){
					kill(mini);
					return;
				}
			}
				mini.updateEnergy(deltaEnergy);
		}
			mini.updateEnergy(-1);	//loses 1energy
			mini.setPosition(new XY(x,y));
	}

	public void tryMove(GoodBeast good, XY moveDirection)throws Exception{
        int x = good.getPosition().getX() + moveDirection.getX();
        int y = good.getPosition().getY() + moveDirection.getY();
		if(flatBoard[y][x] != null){
			return;
		}
		good.setPosition(new XY(x,y));
	}

	public void tryMove(BadBeast bad, XY moveDirection)throws Exception{
		int x = bad.getPosition().getX() + moveDirection.getX();
		int y = bad.getPosition().getY() + moveDirection.getY();
		if(flatBoard[y][x] != null){
			if(flatBoard[y][x] instanceof Squirrel){
				bad.bite();
				System.out.println("A BadBeast just attacked!");
				flatBoard[y][x].updateEnergy(bad.getEnergy());
				if(bad.getBite() == 0){
				killAndReplace(bad);
				}
			}
			return;
		}
		bad.setPosition(new XY(x,y));
	}

	public void tryMove(MasterSquirrel master, XY moveDirection)throws Exception{
		int x = master.getPosition().getX()+moveDirection.getX();
		int y = master.getPosition().getY()+moveDirection.getY();
		if(flatBoard[y][x] != null){
			int deltaEnergy = flatBoard[y][x].getEnergy();
			if(flatBoard[y][x] instanceof Wall){	//wall stun
				master.stun();
				master.updateEnergy(deltaEnergy);
				return;
			}
			if(!(flatBoard[y][x] instanceof BadBeast)){
				if (flatBoard[y][x] instanceof MasterSquirrel){
					return;
				}
				if(flatBoard[y][x] instanceof MiniSquirrel){
					if(master.checkIfChild(flatBoard[y][x])){
						master.updateEnergy(deltaEnergy);	//child gives mama gift
					}else{
						master.updateEnergy(150);	//takes other child
					}
					kill(flatBoard[y][x]);
				}
					master.updateEnergy(deltaEnergy);		//plants gb
					killAndReplace(flatBoard[y][x]);		//kill&replace
			}else{
				//BadBeast
				master.updateEnergy(deltaEnergy);
				tryMove((BadBeast)flatBoard[y][x], new XY(moveDirection.getX()*-1,moveDirection.getY()*-1));
				return;
			}
		}
		if(master.getEnergy() < 0){
			master.updateEnergy(Math.abs(master.getEnergy()));
		}
		master.setPosition(new XY(x,y));
	}

	public Squirrel nearestPlayer(XY position){
		Entity[] squirrelPosition = new Entity[50];
		int counter = 0; //for squirrels
		for(int i = 0; i < settings.getSize().getY();i++){ //find all squirrels
			for(int j = 0; j < settings.getSize().getX(); j++){
				if(flatBoard[i][j] instanceof Squirrel){
					squirrelPosition[counter++] = flatBoard[i][j];
				}
			}
		}
		//calc abs near
		int distanceY = Math.abs(squirrelPosition[0].getPosition().getY() - position.getY());
		int distanceX = Math.abs(squirrelPosition[0].getPosition().getX() - position.getX());
		int idx = 0;
		for(int i = 1; i < counter; i++){
		    int distanceX2 = Math.abs(squirrelPosition[i].getPosition().getX() - position.getX());
		    int distanceY2 = Math.abs(squirrelPosition[i].getPosition().getY() - position.getY());

		    if(distanceX2 <= distanceX && distanceY2 <= distanceY){
		        idx = i;
		        distanceX = distanceX2;
		        distanceY = distanceY2;
		    }
		}
		return (Squirrel) squirrelPosition[idx];
	}

    public void killAndReplace(Entity entity) throws Exception {

        if(!(entity instanceof Wall)){
			int randomX = 0;
			int randomY = 0;
			while(!(flatBoard[randomY][randomX] == null)){ //as long as the field isnt null
				randomX = 1 + (int) (Math.random() * (settings.getSize().getX() - 2));
				randomY = 1 + (int) (Math.random() * (settings.getSize().getY() - 2));
			}
			XY temp = new XY(randomX, randomY);
			if(!(entity instanceof BadBeast)){
				entity.setPosition(temp);				//new Pos for entity
	            board.getEntitySet().plus(entity);
				flatBoard[randomX][randomY] = entity;	//add entity in flatBoard&set
			}else{
				BadBeast bad = new BadBeast(entity.getId(),temp);
				board.getEntitySet().plus(bad);
				flatBoard[randomX][randomY] = bad;
			}
            kill(entity);
			System.out.println("a new " + entity.getClass() + " has just appeared");
		}
	}

	public void kill(Entity entity)throws Exception{
		if(!(entity instanceof Wall)){
			System.out.println("a " + entity.getClass() + " has just been killed");
			board.getEntitySet().remove(entity);

			EntitySet list = board.getEntitySet();
			int x; int y;
			for(int i = 0; i < list.length(); i++){			//refresh the flatboard
				if(list.getEntity(i) == null){				//w/o del entity
					break;
				}
				x = list.getEntity(i).getPosition().getX();
				y = list.getEntity(i).getPosition().getY();
				flatBoard[y][x] = list.getEntity(i);
			}
		}
	}
}
