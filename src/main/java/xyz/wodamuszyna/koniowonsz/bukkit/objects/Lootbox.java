package xyz.wodamuszyna.koniowonsz.bukkit.objects;

import xyz.wodamuszyna.koniowonsz.bukkit.config.LootboxConfig;
import xyz.wodamuszyna.koniowonsz.bukkit.manager.LootboxManager;

public class Lootbox {

    private String name;
    private int wagi;
    private LootboxConfig config;

    public Lootbox(String name, LootboxConfig config){
        this.name = name;
        this.config = config;
        this.wagi = 0;
        LootboxManager.addLootbox(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LootboxConfig getConfig() {
        return config;
    }

    public void setConfig(LootboxConfig config) {
        this.config = config;
    }

    public int getWagi() {
        return wagi;
    }

    public void setWagi(int wagi) {
        this.wagi = wagi;
    }
}
