package de.hsa.games.fatsquirrel.console;

import de.hsa.games.fatsquirrel.core.XY;

public enum GameCommandType implements CommandTypeInfo{

	
	HELP("help", "  * list all commands" , "help"),
	EXIT("exit", "  * exit program", "exit"),
	ALL("all", " * all", "all"),
	MASTER_ENERGY("master", " * master energy", "masterMethod"),
	SPAWN_MINI("mini", " * spawn MiniSquirrel", int.class, "spawn"),
	UP("5", "  * move up" , XY.class),
	DOWN("2", "  * move down" , XY.class),
	LEFT("1", "  * move left", XY.class),
	RIGHT("3", "  * move right", XY.class);
	

	private String name;
	private String info;
	private String method;
	private Class<?>[] paramsClasses = new Class[0];


	private GameCommandType(String name, String info, Class<?> a, String method){
		this.name = name;
		this.info = info;
		this.method = method;
		paramsClasses = new Class[1];
		paramsClasses[0] = a;
	}

	GameCommandType (String name, String info, String method){
		this.name = name;
		this.info = info;
		this.method = method;
	}

	GameCommandType(String name, String info, Class <?> a){
		this.name = name;
		this.info = info;
		paramsClasses = new Class[1];
		paramsClasses[0] = a;
//		paramsClasses[0] = (XY) direction;

	}
	
	public String getName(){
		return name;
	}

	public String getHelpText(){
		return info;
	}
	
	public Class<?>[] getParamTypes(){
		return paramsClasses;
	}



//	public String getMethodName(){
//		return method;
//	}
}
