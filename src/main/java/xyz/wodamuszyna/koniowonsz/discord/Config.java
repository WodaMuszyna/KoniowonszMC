package xyz.wodamuszyna.koniowonsz.discord;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.wodamuszyna.koniowonsz.Main;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {
    public static String token;
    public static String ownerId;
    public static String serverId;

    public static void init(){
        String path = null;
        try {
            path = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        }catch (URISyntaxException ex){
            ex.printStackTrace();
        }
        if(path != null){
            //path = path.substring(0, path.lastIndexOf("/"));
            File file = new File(Paths.get(Main.getInstance().getDataFolder() + File.separator + "config.json").toString());
            if(file.exists()){
                try {
                    Reader reader = Files.newBufferedReader(file.toPath());
                    JsonObject object = new Gson().fromJson(reader, JsonObject.class);
                    token = object.get("token").getAsString();
                    ownerId = object.get("ownerId").getAsString();
                    serverId = object.get("serverId").getAsString();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }else{
            Logger l = LoggerFactory.getLogger(Config.class);
            l.error("CONFIG FILE NOT FOUND");
            System.exit(0);
        }
    }

}
