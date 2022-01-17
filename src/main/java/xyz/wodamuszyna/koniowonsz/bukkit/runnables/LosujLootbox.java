package xyz.wodamuszyna.koniowonsz.bukkit.runnables;

import org.bukkit.entity.Player;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.Utils;
import xyz.wodamuszyna.koniowonsz.bukkit.config.LootboxConfig;

public class LosujLootbox implements Runnable{
    public Player player;

    public LosujLootbox(Player p){ this.player = p;}
    public void run() {
        int los = Main.getLootboxManager().R.nextInt(Main.getInstance().sumaWag);
        for (LootboxConfig.Los l : Main.getInstance().lootboxConfig.losy) {
            if (l.prawdopodobienstwo > los) {
                this.player.sendMessage(Utils.fixColor(Main.getInstance().lootboxConfig.wylosowales.replace("{NAZWA}", l.nagroda)));
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