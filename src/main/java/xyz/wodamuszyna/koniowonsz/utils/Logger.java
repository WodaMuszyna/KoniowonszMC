package xyz.wodamuszyna.koniowonsz.utils;

import xyz.wodamuszyna.koniowonsz.Main;

import java.util.logging.Level;

public class Logger {
    public static void info(String... logs){
        for(int i=0; i<logs.length; i++){
            log(Level.INFO, logs[i]);
            i++;
        }
    }

    public static void warning(String... logs){
        for(int i=0; i<logs.length; i++){
            log(Level.WARNING, logs[i]);
            i++;
        }
    }

    public static void severe(String... logs){
        for(int i=0; i<logs.length; i++){
            log(Level.SEVERE, logs[i]);
            i++;
        }
    }

    public static void log(Level level, String log){
        Main.getInstance().getLogger().log(level, log);
    }

    public static void exception(Throwable cause){
        cause.printStackTrace();
    }
}
