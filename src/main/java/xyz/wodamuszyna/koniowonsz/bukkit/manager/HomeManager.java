package xyz.wodamuszyna.koniowonsz.bukkit.manager;

import xyz.wodamuszyna.koniowonsz.bukkit.objects.Home;

import java.util.ArrayList;
import java.util.List;

public class HomeManager {
    public List<Home> homes = new ArrayList<>();

    public void addHome(Home h){
        if(!homes.contains(h)){
            homes.add(h);
        }
    }

    public List<Home> getHomes(){
        return homes;
    }

    public Home getHome(String owner){
        for(Home h : homes){
            if(h.getOwner().equalsIgnoreCase(owner)){
                return h;
            }
        }
        return null;
    }
}
