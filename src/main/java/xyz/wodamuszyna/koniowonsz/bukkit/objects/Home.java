package xyz.wodamuszyna.koniowonsz.bukkit.objects;

import org.bukkit.Location;
import xyz.wodamuszyna.koniowonsz.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Home {
    public String owner;
    public String world;
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;

    public Home(ResultSet rs){
        try {
            this.owner = rs.getString("owner");
            this.world = rs.getString("world");
            this.x = rs.getDouble("x");
            this.y = rs.getDouble("y");
            this.z = rs.getDouble("z");
            this.yaw = rs.getFloat("yaw");
            this.pitch = rs.getFloat("pitch");
            Main.getHomeManager().addHome(this);
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public Home(String owner, Location location){
        this.owner = owner;
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        insert();
        Main.getHomeManager().addHome(this);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Location getLocation(){
        return new Location(Main.getInstance().getServer().getWorld(world), x, y, z, yaw, pitch);
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    private void insert(){
        try {
            PreparedStatement ps = Main.getConnection().getConnection().prepareStatement("INSERT INTO koniowonsz_homes (owner, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, owner);
            ps.setString(2, world);
            ps.setDouble(3, x);
            ps.setDouble(4, y);
            ps.setDouble(5, z);
            ps.setFloat(6, yaw);
            ps.setFloat(7, pitch);
            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void update(){
        try {
            PreparedStatement ps = Main.getConnection().getConnection().prepareStatement("UPDATE koniowonsz_homes SET world = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ? WHERE owner = ?");
            ps.setString(1, world);
            ps.setDouble(2, x);
            ps.setDouble(3, y);
            ps.setDouble(4, z);
            ps.setFloat(5, yaw);
            ps.setFloat(6, pitch);
            ps.setString(7, owner);
            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}