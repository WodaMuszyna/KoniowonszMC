package xyz.wodamuszyna.koniowonsz;

import org.bukkit.ChatColor;

public class Utils {

    public static String getIDFromMention(String c){
        return c.substring(0, c.length() - 1).substring(2);
    }
    public static String fixColor(String msg){
        if (msg == null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
