package de.hsa.games.fatsquirrel.consoletest;

import de.hsa.games.fatsquirrel.console.CommandTypeInfo;


public enum MyFavouriteCommandType implements CommandTypeInfo {


    HELP("help", "  * list all commands"),
    EXIT("exit", "  * exit program"),
    ADDI("addi", "<param1>  <param2>   * simple integer add ", int.class, int.class),
    ADDF("addf", "<param1>  <param2>   * simple float add ", float.class, float.class),
    ECHO("echo", "<param1>  <param2>   * echos param1 string param2 times ", String.class, int.class),
    UP("5", "  * move up"),
    DOWN("2", "  * move down"),
    LEFT("1", "  * move left"),
    RIGHT("3", "  * move right");


    private String name = "";
    private String info = "";
    private Class<?>[] paramsClasses = new Class[0];


    private MyFavouriteCommandType(String name, String info) {
        this.name = name;
        this.info = info;
    }

    MyFavouriteCommandType(String name, String info, Class<?> a, Class<?> b) {
        this.name = name;
        this.info = info;
        this.paramsClasses = new Class<?>[]{a, b};
    }

    public String getName() {
        return name;
    }

    public String getHelpText() {
        return info;
    }

    public Class<?>[] getParamTypes() {
        return paramsClasses;
    }
}
