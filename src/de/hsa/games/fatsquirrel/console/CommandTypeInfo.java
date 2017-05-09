package de.hsa.games.fatsquirrel.console;

public interface CommandTypeInfo {

    public String getName();

    public String getHelpText();

    public Class<?>[] getParamTypes();

    public String getMethodName();
}
