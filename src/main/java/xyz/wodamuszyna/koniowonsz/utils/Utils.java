package xyz.wodamuszyna.koniowonsz.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
    public static Component fixColor(Component component){
        String plain = PlainTextComponentSerializer.plainText().serialize(component);
        ChatColor.translateAlternateColorCodes('&', plain);
        return Component.text(plain);
    }
}
