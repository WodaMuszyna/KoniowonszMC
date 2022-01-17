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
import xyz.wodamuszyna.koniowonsz.Utils;

import java.util.concurrent.TimeUnit;

public class Reply extends Command {

    Logger logger = LoggerFactory.getLogger(Reply.class);

    public Reply(){
        super.name = "reply";
        super.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
        super.arguments = "<channel> <id> <message>";
    }

    @Override
    protected void execute(CommandEvent e) {
        Guild g = Main.getJDA().getGuildById(Config.serverId);
        String[] a = e.getArgs().split(" ");
        Message msg = e.getMessage();
        msg.delete().complete();
        if(g != null){
            if(a.length < 3){
                e.getMessage().reply("Usage: $reply <channel> <id> <message>").complete().delete().queueAfter(5, TimeUnit.SECONDS);
                return;
            }
            TextChannel c = g.getTextChannelById(Utils.getIDFromMention(a[0]));
            if(c != null){
                Message m = c.retrieveMessageById(a[1]).complete();
                if(m != null){
                    logger.debug("Message content: "+m.getContentRaw());
                    StringBuilder sb = new StringBuilder(a[2]);
                    for (int i = 3; i < a.length; ++i) {
                        sb.append(" ").append(a[i]);
                    }
                    logger.debug("Message reply: "+ sb);
                    m.reply(sb.toString()).queue();
                }
            }
        }
    }
}
