package de.hsa.games.fatsquirrel.consoletest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import de.hsa.games.fatsquirrel.util.Command;
import de.hsa.games.fatsquirrel.util.CommandScanner;
import de.hsa.games.fatsquirrel.console.ScanException;

import java.io.InputStreamReader;

public class MyFavouriteCommandProcessor {
    PrintStream outputStream = System.out;
    BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

    public void process() throws IOException, ScanException {
        CommandScanner commandScanner = new CommandScanner(MyFavouriteCommandType.values(), inputReader);
        Command command;

        while (true) {
            command = commandScanner.next();

            Object[] params = command.getParams();
            MyFavouriteCommandType commandType = (MyFavouriteCommandType) command.getCommandType();

            switch (commandType) {

                case EXIT:
                    System.exit(0);
                case HELP:
                    help();
                    break;
                case ADDI:
                    int aI = (Integer) params[0];
                    int bI = (Integer) params[1];
                    outputStream.println(aI + " + " + bI + " = " + (aI + bI));
                    break;
                case ADDF:
                    float aF = (Float) params[0];
                    float bF = (Float) params[1];
                    outputStream.println(aF + " + " + bF + " = " + (aF + bF));
                    break;
                case ECHO:
                    String s = (String) params[0];
                    int echo = (Integer) params[1];
                    for (int i = 0; i < echo; i++){
                        outputStream.println(s);
                    }
                    break;
                case UP:
                    outputStream.println("moved up");
                    break;
                case DOWN:
                    outputStream.println("moved down");
                    break;
                case LEFT:
                    outputStream.println("moved left");
                    break;
                case RIGHT:
                    outputStream.println("moved right");
                    break;
            }
        }
    }

    private void help() {
        for (MyFavouriteCommandType e : MyFavouriteCommandType.values()) {
            outputStream.println("enter: " + e.getName() + " for " + e.getHelpText());
        }
    }

    public static void main(String[] args) throws IOException, ScanException {
        MyFavouriteCommandProcessor test = new MyFavouriteCommandProcessor();
        test.process();
    }

}
