package xyz.wodamuszyna.koniowonsz.bukkit.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.discord.GuildListener;

import java.awt.*;
import java.util.Date;
import java.util.Random;

public class ServerListener implements Listener {
    @EventHandler
    public void onMessage(AsyncChatEvent e){
        if(Main.getInstance().isMaintenance()) return;
        Guild g = GuildListener.getGuild();
        TextChannel c = g.getTextChannelById("924702832802824192");
        if(c != null){
            EmbedBuilder eb = new EmbedBuilder()
                    .setColor(getRandomColor())
                    .setAuthor(e.getPlayer().getName())
                    .setDescription(PlainTextComponentSerializer.plainText().serialize(e.message()))
                    .setTimestamp(new Date().toInstant());
            c.sendMessage(eb.build()).queue();
        }
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent e){
        if(Main.getInstance().isMaintenance()){
            if(!e.getPlayer().isOp()){
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("Przerwa techniczna!"));
            }
        }
    }

    private Color getRandomColor(){
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        return new Color(r, g, b);
    }
}
