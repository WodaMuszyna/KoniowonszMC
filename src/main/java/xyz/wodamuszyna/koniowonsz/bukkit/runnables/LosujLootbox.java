package xyz.wodamuszyna.koniowonsz.bukkit.runnables;

import org.bukkit.entity.Player;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.utils.Utils;
import xyz.wodamuszyna.koniowonsz.bukkit.config.LootboxConfig;
import xyz.wodamuszyna.koniowonsz.bukkit.objects.Lootbox;

public class LosujLootbox implements Runnable{
    public Player player;
    public Lootbox lootbox;
    public boolean pickingRandomLootbox;

    public LosujLootbox(Player p, Lootbox l, boolean pickingRandomLootbox){
        this.player = p;
        this.lootbox = l;
        this.pickingRandomLootbox = pickingRandomLootbox;
    }
    public void run() {
        if(pickingRandomLootbox){
            int los = Main.getLootboxManager().R.nextInt(Main.getInstance().wagaLootboxow);
            for (Lootbox l : Main.getLootboxManager().getLootboxy()) {
                if (l.getConfig().waga > los) {
                    Main.getLootboxManager().give(player, l.getName(), 1);
                    return;
                }
                los -= l.getConfig().waga;
            }
        }else {
            int los = Main.getLootboxManager().R.nextInt(lootbox.getWagi());
            for (LootboxConfig.Los l : lootbox.getConfig().losy) {
                if (l.prawdopodobienstwo > los) {
                    this.player.sendMessage(Utils.fixColor(lootbox.getConfig().wylosowales.replace("{NAZWA}", l.nagroda)));
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