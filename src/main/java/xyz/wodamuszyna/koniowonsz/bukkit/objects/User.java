package xyz.wodamuszyna.koniowonsz.bukkit.objects;

import org.bukkit.entity.Player;
import xyz.wodamuszyna.koniowonsz.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class User {
    private String name;
    private String ip;
    private Timestamp lastOnline;
    private String lastWorld;
    private double lastX;
    private double lastY;
    private double lastZ;
    private int lastDailyRewardDay;
    private long lastDailyRewardClaim;
    public long lastHomeCommandUsage;

    public User(Player p){
        this.name = p.getName();
        this.ip = p.getAddress().getAddress().getHostAddress();
        this.lastWorld = p.getLocation().getWorld().getName();
        this.lastX = p.getLocation().getX();
        this.lastY = p.getLocation().getY();
        this.lastZ = p.getLocation().getZ();
        this.lastDailyRewardDay = 0;
        insert();
        Main.getUserManager().addUser(this);
    }

    public User(ResultSet rs, boolean addToManager){
        try {
            this.name = rs.getString("username");
            this.ip = rs.getString("ip");
            this.lastOnline = rs.getTimestamp("lastOnline");
            this.lastWorld = rs.getString("lastWorld");
            this.lastX = rs.getDouble("lastX");
            this.lastY = rs.getDouble("lastY");
            this.lastZ = rs.getDouble("lastZ");
            this.lastDailyRewardDay = rs.getInt("lastDailyRewardDay");
            this.lastDailyRewardClaim = rs.getLong("lastDailyRewardClaim");
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        if(addToManager) Main.getUserManager().addUser(this);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Timestamp getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(Timestamp lastOnline) {
        this.lastOnline = lastOnline;
    }

    public String getLastWorld() {
        return lastWorld;
    }

    public void setLastWorld(String lastWorld) {
        this.lastWorld = lastWorld;
    }

    public double getLastX() {
        return lastX;
    }

    public void setLastX(double lastX) {
        this.lastX = lastX;
    }

    public double getLastY() {
        return lastY;
    }

    public void setLastY(double lastY) {
        this.lastY = lastY;
    }

    public double getLastZ() {
        return lastZ;
    }

    public void setLastZ(double lastZ) {
        this.lastZ = lastZ;
    }

    public String getLastLocation(){
        return lastWorld + ":" + (int)lastX + ";" + (int)lastY + ";" + (int)lastZ;
    }

    public int getLastDailyRewardDay() {
        return lastDailyRewardDay;
    }

    public void setLastDailyRewardDay(int lastDailyRewardDay) {
        this.lastDailyRewardDay = lastDailyRewardDay;
    }

    public long getLastDailyRewardClaim() {
        return lastDailyRewardClaim;
    }

    public void setLastDailyRewardClaim(long lastDailyRewardClaim) {
        this.lastDailyRewardClaim = lastDailyRewardClaim;
    }

    private void insert(){
        try {
            PreparedStatement ps = Main.getConnection().getConnection().prepareStatement("INSERT INTO `koniowonsz_users` (`username`, `ip`, `lastWorld`, `lastX`, `lastY`, `lastZ`, `lastDailyRewardDay`, `lastDailyRewardClaim`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, ip);
            ps.setString(3, lastWorld);
            ps.setDouble(4, lastX);
            ps.setDouble(5, lastY);
            ps.setDouble(6, lastZ);
            ps.setInt(7, lastDailyRewardDay);
            ps.setLong(8, lastDailyRewardClaim);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try {
            PreparedStatement ps = Main.getConnection().getConnection().prepareStatement("UPDATE `koniowonsz_users` SET `ip` = ?, `lastOnline` = ?, `lastWorld` = ?, `lastX` = ?, `lastY` = ?, `lastZ` = ?, `lastDailyRewardDay` = ?, `lastDailyRewardClaim` = ? WHERE `username` = ?");
            ps.setString(1, ip);
            ps.setTimestamp(2, lastOnline);
            ps.setString(3, lastWorld);
            ps.setDouble(4, lastX);
            ps.setDouble(5, lastY);
            ps.setDouble(6, lastZ);
            ps.setInt(7, lastDailyRewardDay);
            ps.setLong(8, lastDailyRewardClaim);
            ps.setString(9, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
