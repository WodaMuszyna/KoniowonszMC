package xyz.wodamuszyna.koniowonsz.discord;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.wodamuszyna.koniowonsz.Main;

public class GuildListener extends ListenerAdapter {

    static Guild g;
    public static String messageId;
    Logger logger = LoggerFactory.getLogger(GuildListener.class);

    public void onReady(ReadyEvent e){
        g = Main.getJDA().getGuildById(Config.serverId);
        if(g != null){
//            TextChannel c = g.getTextChannelById("913416650978623509");
//            if(c != null) {
//            }
        }
    }

    public void onMessageReceived(MessageReceivedEvent e){
        if(Main.getInstance().isMaintenance()) return;
        if(g != null){
            TextChannel c = g.getTextChannelById("924702832802824192");
            if(e.getMessage().getAuthor().getId().equals("913394985880154152")) return;
            if(c != null && e.getMessage().getTextChannel().equals(c)) {
                TextComponent msg = Component.text("DISCORD ").color(NamedTextColor.DARK_PURPLE)
                        .append(Component.text(e.getMessage().getAuthor().getName(), NamedTextColor.WHITE))
                        .append(Component.text(" >> ", NamedTextColor.DARK_GRAY)).append(Component.text(e.getMessage().getContentDisplay(), NamedTextColor.WHITE));
                Main.getInstance().getServer().broadcast(msg);
            }
        }
    }

    public static String getMessageId(){ return messageId; }
    public static void setMessageId(String id){ messageId = id; }
    public static Guild getGuild(){ return g; }
}
