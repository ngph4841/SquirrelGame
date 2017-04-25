

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


	public void tryMove(MiniSquirrel mini, XY moveDirection){
		int x = mini.getPosition().getX()+moveDirection.getX();	//calc new pos
		int y = mini.getPosition().getY()+moveDirection.getY();
		int deltaEnergy = flatBoard[y][x].getEnergy();
		if(flatBoard[y][x] != null){	//if null no check
			if(flatBoard[y][x] instanceof Wall){
				mini.updateEnergy(deltaEnergy);
				return;
			}
		}
			mini.updateEnergy(-1);	//loses 1energy
			mini.setPosition(new XY(y,x));
	}
	
	public void tryMove(GoodBeast good, XY moveDirection){
		int x = good.getPosition().getX()+moveDirection.getX();
		int y = good.getPosition().getY()+moveDirection.getY();
		if(flatBoard[y][x] != null){
			if(flatBoard[y][x] instanceof Wall){
				return;
			}
		}
		good.setPosition(new XY(y,x));
	}
	
	public void tryMove(BadBeast bad, XY moveDirection){
		int x = bad.getPosition().getX()+moveDirection.getX();
		int y = bad.getPosition().getY()+moveDirection.getY();
		if(flatBoard[y][x] != null){
			if(flatBoard[y][x] instanceof Wall){
				return;
			}
		}
		bad.setPosition(new XY(y,x));
	}
	
	public void tryMove(MasterSquirrel master, XY moveDirection)throws Exception{
		int x = master.getPosition().getX()+moveDirection.getX();
		int y = master.getPosition().getY()+moveDirection.getY();
		if(flatBoard[y][x] != null){
			int deltaEnergy = flatBoard[y][x].getEnergy();
			master.updateEnergy(deltaEnergy);
			if(flatBoard[y][x] instanceof Wall){
				master.stun();
				return;
			}
			if(!(flatBoard[y][x] instanceof BadBeast)){			
					killAndReplace(flatBoard[y][x]);		//kill&replace
			}else{
				BadBeast temp = (BadBeast) flatBoard[y][x];
				temp.bite();
				flatBoard[y][x] = temp;
			}
		}
		master.setPosition(new XY(y,x));
	}
	
	public Squirrel nearestPlayer(XY position){
		return null;
	}
	
	public void killAndReplace(Entity entity)throws Exception{
		if(!(entity instanceof Wall)){
			kill(entity);
			int randomX = 0;
			int randomY = 0;
			while(!(flatBoard[randomY][randomX] == null)){ //as long as the field isnt null
				randomX = 1 + (int) (Math.random() * (settings.getSize().getX() - 2));
				randomY = 1 + (int) (Math.random() * (settings.getSize().getY() - 2));
			}
			XY temp = new XY(randomX, randomY);
			entity.setPosition(temp);				//new Pos for entity
			flatBoard[randomX][randomY] = entity;	//add entity in flatBoard&set
			board.getEntitySet().plus(entity);
		}
	}
	
	public void kill(Entity entity)throws Exception{
		if(!(entity instanceof Wall)){
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
