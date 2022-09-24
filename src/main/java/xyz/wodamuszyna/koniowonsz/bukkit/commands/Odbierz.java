package xyz.wodamuszyna.koniowonsz.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.bukkit.config.RewardsConfig;
import xyz.wodamuszyna.koniowonsz.bukkit.objects.User;
import xyz.wodamuszyna.koniowonsz.utils.TimeUtil;
import xyz.wodamuszyna.koniowonsz.utils.Utils;

public class Odbierz implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String l, @NotNull String[] a) {
        if(s instanceof Player){
            Player p = Bukkit.getPlayer(s.getName());
            User u = Main.getUserManager().get(s.getName());
            if(u.getLastDailyRewardClaim() + 1000 * 10 < System.currentTimeMillis()){
                RewardsConfig.Reward r = Main.rewardsConfig.nagrody.get(u.getLastDailyRewardDay());
                for (String komenda : r.komendy) {
                    Main.getInstance().getServer().dispatchCommand(
                            Main.getInstance().getServer().getConsoleSender(),
                            komenda.replace("{NICK}", p.getName()));
                }
                u.setLastDailyRewardClaim(System.currentTimeMillis());
                if(u.getLastDailyRewardDay() + 1 >= Main.rewardsConfig.nagrody.size()) {
                    u.setLastDailyRewardDay(0);
                }else {
                    u.setLastDailyRewardDay(u.getLastDailyRewardDay() + 1);
                }
                s.sendMessage(Utils.fixColor(Main.rewardsConfig.odebranoNagrode.replace("{NAGRODA}", r.nagroda)));
            }else {
                s.sendMessage(Utils.fixColor(Main.rewardsConfig.nieMoznaOdebrac.replace("{CZAS}", TimeUtil.getTimeLeft(u.getLastDailyRewardClaim() + 1000 * 10 - System.currentTimeMillis()))));
            }
            return true;
        }
        return false;
    }
}
