package de.hsa.games.fatsquirrel.console;

/**
 * This Interface is used to process the user inputs into commands for the game engine.
 * This Interface is implemented by the GameCommandType Enum.
 */
public interface CommandTypeInfo {

    /**
     * This method returns the name of the command.
     * @return the name of the command
     */
    public String getName();

    /**
     * This method returns the help text for the command.
     * @return the help text of the command
     */
    public String getHelpText();

    /**
     * This method returns a class array of param types for reflection
     * @return an array of parameter types
     */
    public Class<?>[] getParamTypes();

    /**
     * This method returns the
     * @return
     */
    public String getMethodName();
}
