package xyz.wodamuszyna.koniowonsz.bukkit.listener;

import com.google.gson.Gson;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.bukkit.objects.User;
import xyz.wodamuszyna.koniowonsz.discord.GuildListener;
import xyz.wodamuszyna.koniowonsz.utils.Utils;

import java.awt.*;
import java.sql.Timestamp;
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
                    .setTimestamp(new Timestamp(new Date().getTime()).toInstant());
            c.sendMessage(new MessageBuilder().setEmbed(eb.build()).build()).queue();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        User u = Main.getUserManager().initUser(e.getPlayer().getName());
        if(u == null){
            u = new User(e.getPlayer());
        }
        User finalU = u;
        (new BukkitRunnable(){
            @Override
            public void run() {
                if (finalU.getLastDailyRewardClaim() + 1000 * 10 < System.currentTimeMillis()) {
                     TextComponent c = Component.text(Utils.fixColor("&8&l>> &bDostepna jest dzienna nagroda &a&lKLIK")).clickEvent(ClickEvent.runCommand("/odbierz")).hoverEvent(HoverEvent.showText(Component.text(Utils.fixColor("&8&l>> &bKliknij aby odebrac dzienna nagrode"))));
                     e.getPlayer().sendMessage(c);
                }
            }
        }).runTaskLater(Main.getInstance(), 50L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        User u = Main.getUserManager().get(e.getPlayer().getName());
        if(u != null){
            u.setIp(e.getPlayer().getAddress().getAddress().getHostAddress());
            u.setLastOnline(new Timestamp(new Date().getTime()));
            Location l = e.getPlayer().getLocation();
            u.setLastWorld(l.getWorld().getName());
            u.setLastX(l.getX());
            u.setLastY(l.getY());
            u.setLastZ(l.getZ());
            u.update();
            Main.getUserManager().removeUser(u);
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
