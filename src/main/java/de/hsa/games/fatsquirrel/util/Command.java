package de.hsa.games.fatsquirrel.util;

import de.hsa.games.fatsquirrel.console.CommandTypeInfo;

public class Command {
	private final CommandTypeInfo commandType;
	private final Object[] params;
	
	public Command(CommandTypeInfo commandType, Object[] params){
		this.commandType = commandType;
		this.params = params;
	}
	
	public CommandTypeInfo getCommandType(){
		return commandType;
	}
	
	public Object[] getParams(){
		return params;
	}
	
	public Object getParam(int index){
		return params[index];
	}

}
