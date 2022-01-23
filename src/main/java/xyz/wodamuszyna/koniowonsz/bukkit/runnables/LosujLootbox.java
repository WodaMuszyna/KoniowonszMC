package xyz.wodamuszyna.koniowonsz.bukkit.runnables;

import org.bukkit.entity.Player;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.Utils;
import xyz.wodamuszyna.koniowonsz.bukkit.config.LootboxConfig;

import java.util.Map;

public class LosujLootbox implements Runnable{
    public Player player;
    public LootboxConfig config;
    public boolean pickingRandomLootbox;

    public LosujLootbox(Player p, LootboxConfig c, boolean pickingRandomLootbox){
        this.player = p;
        this.config = c;
        this.pickingRandomLootbox = pickingRandomLootbox;
    }
    public void run() {
        if(pickingRandomLootbox){
            int los = Main.getLootboxManager().R.nextInt(Main.getInstance().wagaLootboxow);
            for (Map.Entry<String, LootboxConfig> m : Main.getInstance().lootboxy.entrySet()) {
                if (m.getValue().waga > los) {
                    Main.getLootboxManager().give(player, m.getKey(), 1);
                    return;
                }
                los -= m.getValue().waga;
            }
        }else {
            int los = Main.getLootboxManager().R.nextInt(Main.getInstance().wagi.get(config));
            for (LootboxConfig.Los l : config.losy) {
                if (l.prawdopodobienstwo > los) {
                    this.player.sendMessage(Utils.fixColor(config.wylosowales.replace("{NAZWA}", l.nagroda)));
                    for (String komendy : l.komendy) {
                        Main.getInstance().getServer().dispatchCommand(
                                Main.getInstance().getServer().getConsoleSender(),
                                komendy.replace("{NICK}", this.player.getName()));
                    }
                    return;
                }
                los -= l.prawdopodobienstwo;
            }
        }
    }
}