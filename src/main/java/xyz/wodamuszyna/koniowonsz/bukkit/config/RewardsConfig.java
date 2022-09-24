package xyz.wodamuszyna.koniowonsz.bukkit.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import xyz.wodamuszyna.koniowonsz.utils.ConfigUtils;

import java.util.List;
import java.util.Map;

@SerializableAs("RewardsConfig")
public class RewardsConfig implements ConfigurationSerializable {
    public String odebranoNagrode;
    public String nieMoznaOdebrac;
    public List<Reward> nagrody;


    public RewardsConfig(Map<String, Object> m){
        ConfigUtils.deserialize(m, this);
    }

    public Map<String, Object> serialize() { return ConfigUtils.serialize(this); }

    @SerializableAs("Reward")
    public static class Reward implements ConfigurationSerializable{
        public List<String> komendy;
        public String nagroda;

        public Reward(Map<String, Object> m){
            ConfigUtils.deserialize(m, this);
        }

        public Map<String, Object> serialize() { return ConfigUtils.serialize(this); }
    }
}
