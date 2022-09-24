package xyz.wodamuszyna.koniowonsz.discord.commands;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.wodamuszyna.koniowonsz.discord.Config;
import xyz.wodamuszyna.koniowonsz.Main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Run extends Command {

    Logger logger = LoggerFactory.getLogger(Run.class);

    public Run(){
        super.name = "run";
        super.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
    }

    @Override
    protected void execute(CommandEvent e) {
        String[] a = e.getArgs().split(" ");
        Message msg = e.getMessage();
        Guild g = Main.getJDA().getGuildById(Config.serverId);
        if(g != null){
            msg.delete().complete();
            Pattern pattern = Pattern.compile("(?s)\\$(?:edit_last_)?run(?: +(\\S*)\\s*|\\s*)(?:\\n((?:[^\\n\\r\\f\\v]*\\n)*?)\\s*|\\s*)```(?:(\\S+)\\n\\s*|\\s*)(.*)```(?:\\n?((?:[^\\n\\r\\f\\v]\\n?)+)+|)");
            Matcher matcher = pattern.matcher(msg.getContentRaw());
            if(matcher.find()){
                if(matcher.group(1) == null){
                    e.reply("Please specify language");
                    return;
                }
                try {
                    RequestConfig requestConfig = RequestConfig.custom()
                            .setCookieSpec(CookieSpecs.STANDARD)
                            .build();
                    HttpClient httpClient = HttpClientBuilder.create()
                            .setDefaultRequestConfig(requestConfig)
                            .build();

                    JsonObject o = new JsonObject();
                    o.addProperty("language", matcher.group(1));
                    o.addProperty("source", matcher.group(4));
                    String payload = new Gson().toJson(o);
                    StringEntity requestEntity = new StringEntity(
                            payload,
                            ContentType.APPLICATION_JSON);

                    HttpPost postMethod = new HttpPost("https://emkc.org/api/v1/piston/execute");
                    postMethod.setEntity(requestEntity);
                    HttpResponse response = httpClient.execute(postMethod);
                    JsonObject out = new Gson().fromJson(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8), JsonObject.class);
                    logger.debug(out.toString());
                    e.reply("Your output: \n```\n"+out.get("output").getAsString()+"\n```");
                } catch (IOException malformedURLException) {
                    logger.debug(malformedURLException.getMessage());
                    malformedURLException.printStackTrace();
                }
            }
        }
    }
}
