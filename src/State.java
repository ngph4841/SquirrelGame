
public class State {
	private int highscore;
	private Board board;
	private FlattenedBoard flattenedBoard;
	
	public State(Board board1)throws Exception{
		this.highscore = 10000;
		this.board = board1;
		this.flattenedBoard = new FlattenedBoard(board);
	}
	
	public Board getBoard(){
		return board;
	}
	
	public BoardView getBoardView(){
		return flattenedBoard;
	}
	
	public void update(XY direction)throws Exception{
		EntityContext context = (EntityContext) flattenedBoard;
		board.getEntitySet().moveAll(context);		//call nextStep in all entities
		int stunTurnCounter = 0;
		
		//move MasterSquirrel with Vector#
		if(stunTurnCounter == 0){
		XY position = board.getEntitySet().getEntity(0).getPosition();
		position = new XY (position.getX() + direction.getX(), position.getY() + direction.getY());
		board.getEntitySet().getEntity(0).setPosition(position);
		}else{
			stunTurnCounter++;
		}
		if(stunTurnCounter == 3){
			stunTurnCounter = 0;
		}
		flattenedBoard = (FlattenedBoard) board.flatten();	//update Boardview
	}

}
