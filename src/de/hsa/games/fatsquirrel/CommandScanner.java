package de.hsa.games.fatsquirrel;

import de.hsa.games.fatsquirrel.Command;
import de.hsa.games.fatsquirrel.console.CommandTypeInfo;
import de.hsa.games.fatsquirrel.console.GameCommandType;
import de.hsa.games.fatsquirrel.console.ScanException;
import de.hsa.games.fatsquirrel.core.XY;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Scanner;

public class CommandScanner {
    CommandTypeInfo[] commandTypeInfos;
    BufferedReader inputReader;
    PrintStream outputStream;
    //CommandTypeInfo command = new MyFavouriteCommandType();

    public CommandScanner(CommandTypeInfo[] commandTypeInfos, BufferedReader inputReader) {
        this.commandTypeInfos = commandTypeInfos;
        this.inputReader = inputReader;
    }


    public Command next() throws IOException, ScanException {
        String line = inputReader.readLine();
        String[] str = line.split(" ");

        for (int i = 0; i < commandTypeInfos.length; i++) {
            if (str[0].equals(commandTypeInfos[i].getName())) {
//                System.out.println("DBG: cmd = " + commandTypeInfos[i].getName());

                Class<?>[] paramsClass = commandTypeInfos[i].getParamTypes();
                //load class, init object[] to save the class obkk later
                Object[] params = new Object[paramsClass.length];

//                System.out.println("DBG: lenght = " + paramsClass.length);
                if (paramsClass.length > 0) {
//                    if (paramsClass.length != str.length - 1) {
//                        System.out.println("ERROR: not enoght params");
//                    }

                    for (int j = 0; j < paramsClass.length; j++) {
                        Class<?> c = paramsClass[j];

                        if (c.equals(int.class)) {
                            params[j] = new Integer(str[j + 1]);
                        } else if (c.equals(float.class)) {
                            params[j] = new Float(str[j + 1]);
                        } else if (c.equals(String.class)) {
                            params[j] = new String(str[j + 1]);
                        } else if (c.equals(XY.class)){
                            switch(commandTypeInfos[i].getName()){
                                case "1":
                                    params[j] = new XY(0,-1);
                                    break;
                                case "2":
                                    params[j] = new XY(1, 0);
                                    break;
                                case "3":
                                    params[j] = new XY(0,1);
                                    break;
                                case "5":
                                    params[j] = new XY(-1,0);
                                    break;
                            }
                        }
                    }
                }
                Command command = new Command(commandTypeInfos[i], params);
                return command;
            }
        }
        throw new ScanException();
    }
}