package me.kapehh.PlayerInfo;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Karen on 11.10.2014.
 */
public class PlayerStat {
    private int hp = 0;
    private int lvl = 0;
    private double money = 0;
    private int mobKills = 0;
    private int playerKills = 0;
    private int deaths = 0;
    private int playtime = 0;
    private String rank;

    public PlayerStat(Player player) {
        DBHelper dbHelper = Main.instance.getDbHelper();
        if (dbHelper != null) {
            try {
                DBHelper.DBHelperResult result = dbHelper.prepareQueryStart("SELECT prefix FROM player WHERE name=?", player.getName());
                if (result.getResultSet().next()) {
                    rank = result.getResultSet().getString("prefix");
                } else {
                    rank = "Unknown#1";
                }
                dbHelper.queryEnd(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            rank = "Unknown#2";
        }

        hp = (int) player.getHealth();
        lvl = player.getLevel();
        money = Main.instance.getEconomy().getBalance(player.getName());
        mobKills = player.getStatistic(Statistic.MOB_KILLS);
        playerKills = player.getStatistic(Statistic.PLAYER_KILLS);
        deaths = player.getStatistic(Statistic.DEATHS);
        playtime = player.getStatistic(Statistic.PLAY_ONE_TICK) / 20; // to seconds
    }

    public String getRank() {
        return rank;
    }

    public int getLvl() {
        return lvl;
    }

    public double getMoney() {
        return money;
    }

    public int getHp() {
        return hp;
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

    public int getPlaytime() {
        return playtime;
    }
}
