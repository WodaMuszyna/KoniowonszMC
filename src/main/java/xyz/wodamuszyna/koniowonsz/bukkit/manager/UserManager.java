package xyz.wodamuszyna.koniowonsz.bukkit.manager;

import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.bukkit.objects.Home;
import xyz.wodamuszyna.koniowonsz.bukkit.objects.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    public List<User> users = new ArrayList<>();

    public void addUser(User u){
        if(!users.contains(u)){
            users.add(u);
        }
    }

    public List<User> getUsers(){
        return users;
    }

    public User get(String name){
        for(User u : users){
            if(u.getName().equalsIgnoreCase(name)){
                return u;
            }
        }
        return null;
    }

    public User initUser(String username){
        try {
            ResultSet rs = Main.getConnection().query("SELECT * FROM koniowonsz_users u JOIN koniowonsz_homes h ON h.owner=u.username WHERE u.username='" + username + "'");
            if(rs.next()){
                new Home(rs);
                return new User(rs, true);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public User getUserFromDB(String username){
        try {
            ResultSet rs = Main.getConnection().query("SELECT * FROM koniowonsz_users u WHERE u.username='" + username + "'");
            if(rs.next()){
                return new User(rs, false);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public void removeUser(User u){
        users.remove(u);
        Main.getHomeManager().getHomes().remove(Main.getHomeManager().getHome(u.getName()));
    }
}
