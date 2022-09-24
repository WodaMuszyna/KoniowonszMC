package xyz.wodamuszyna.koniowonsz.discord.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.wodamuszyna.koniowonsz.discord.Config;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.utils.Utils;

import java.util.concurrent.TimeUnit;

public class Say extends Command {

    Logger logger = LoggerFactory.getLogger(Say.class);

    public Say(){
        super.name = "say";
        super.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
        super.arguments = "<channel> <message>";
    }

    @Override
    protected void execute(CommandEvent e) {
        Guild g = Main.getJDA().getGuildById(Config.serverId);
        String[] a = e.getArgs().split(" ");
        Message msg = e.getMessage();
        msg.delete().complete();
        if(g != null){
            if(a.length < 2){
                e.getMessage().reply("Usage: $say <channel> <message>").complete().delete().queueAfter(5, TimeUnit.SECONDS);
                return;
            }
            TextChannel c = g.getTextChannelById(Utils.getIDFromMention(a[0]));
            if(c != null){
                StringBuilder sb = new StringBuilder(a[1]);
                for (int i = 2; i < a.length; ++i) {
                    sb.append(" ").append(a[i]);
                }
                c.sendMessage(sb.toString()).queue();
            }
        }
    }
}
