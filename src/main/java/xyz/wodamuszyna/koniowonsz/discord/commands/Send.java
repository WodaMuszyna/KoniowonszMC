package xyz.wodamuszyna.koniowonsz.discord.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.wodamuszyna.koniowonsz.discord.Config;
import xyz.wodamuszyna.koniowonsz.discord.GuildListener;
import xyz.wodamuszyna.koniowonsz.Main;

import java.util.List;
import java.util.Random;

public class Send extends Command {

    public Send(){
        super.name = "send";
        super.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
    }

    Guild g;

    @Override
    protected void execute(CommandEvent e){
        e.getMessage().delete().complete();
        g = Main.getJDA().getGuildById(Config.serverId);
        if(g != null){
            net.dv8tion.jda.api.entities.Category ct = g.getCategoryById("847548487121633290");
            if(ct != null){
                List<TextChannel> channels = ct.getTextChannels();
                Random rand = new Random();
                //TextChannel c = channels.get(rand.nextInt(channels.size()));
                TextChannel c = g.getTextChannelById("913416650978623509");
                if(c != null){
                    c.sendMessage("siemka").queue(msg -> GuildListener.setMessageId(msg.getId()));
                }
            }
        }
    }
}
