package me.kapehh.PlayerInfo;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Karen on 11.10.2014.
 */
public class PlayerStat {
    private static JSONParser parser = new JSONParser();

    // stats
    private int mobKills = 0;
    private int playerKills = 0;
    private int deaths = 0;

    public PlayerStat(Player player) {
        try {
            File fileStat = new File(
                    new File(Bukkit.getWorlds().get(0).getWorldFolder(), "stats"),
                    player.getUniqueId().toString()
            );
            JSONObject jsonObject =  (JSONObject) parser.parse(new FileReader(fileStat));
            if (jsonObject.containsKey("stat.mobKills")) mobKills = (Integer) jsonObject.get("stat.mobKills");
            if (jsonObject.containsKey("stat.playerKills")) playerKills = (Integer) jsonObject.get("stat.playerKills");
            if (jsonObject.containsKey("stat.deaths")) deaths = (Integer) jsonObject.get("stat.deaths");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getMobKills() {
        return mobKills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getPlayerKills() {
        return playerKills;
    }
}
