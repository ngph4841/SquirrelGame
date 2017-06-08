package de.hsa.games.fatsquirrel.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by Freya on 25.05.2017.
 */
public class LogAdvice implements InvocationHandler {
    private Object target;

    public LogAdvice(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //create logger here
        Logger logger = Logger.getLogger("");
//        FileHandler fileHandler = new FileHandler("Log.txt");
//        SimpleFormatter formatter = new SimpleFormatter();
//        fileHandler.setFormatter(formatter);
//        logger.addHandler(fileHandler);

        //invoke method
        Object result = method.invoke(target, args);

        //log method & result & args
        String resultString = "void";
        if(result != null){
            resultString = result.toString();
        }
        String argsString = "void";
        if(args !=null) {
           argsString = args.toString();
        }
        logger.log(Level.INFO, "method:" + method.toString() + ", args:" + argsString + ", result:" + resultString);

        return result;
    }
}
