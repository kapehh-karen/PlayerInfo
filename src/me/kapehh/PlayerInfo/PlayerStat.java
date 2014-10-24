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
        // TODO: Добавить оффлайн загрузку статистики игрока из файла (если он оффлайн)
        DBHelper dbHelper = Main.instance.getDbHelper();
        if (dbHelper != null) {
            try {
                DBHelper.DBHelperResult result = dbHelper.prepareQueryStart("SELECT prefix FROM player WHERE name=?", player.getName());
                if (result.getResultSet().next()) {
                    rank = result.getResultSet().getString("prefix");
                } else {
                    rank = "Recruit";
                }
                dbHelper.queryEnd(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            rank = "Unknown";
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

    // convert

    private final static int ONE_SECOND = 1;
    private final static int SECONDS_IN_A_MINUTE = 60;
    private final static int ONE_MINUTE = ONE_SECOND * 60;
    private final static int MINUTES_IN_AN_HOUR = 60;
    private final static int ONE_HOUR = ONE_MINUTE * 60;
    private final static int HOURS_IN_A_DAY = 24;
    private final static int ONE_DAY = ONE_HOUR * 24;
    private final static int DAYS_IN_A_YEAR = 365;

    public static String formatHMSM(int duration) {
        duration /= ONE_SECOND;
        int seconds = duration % SECONDS_IN_A_MINUTE;
        duration /= SECONDS_IN_A_MINUTE;
        int minutes = (int) (duration % MINUTES_IN_AN_HOUR);
        duration /= MINUTES_IN_AN_HOUR;
        int hours = duration % HOURS_IN_A_DAY;
        duration /= HOURS_IN_A_DAY;
        int days = duration % DAYS_IN_A_YEAR;
        duration /= DAYS_IN_A_YEAR;
        int years = duration;
        if (days == 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else if (years == 0) {
            return String.format("%d день %02d:%02d:%02d", days, hours, minutes, seconds);
        } else {
            return String.format("%d год %d день %02dh%02dm%02ds", years, days, hours, minutes, seconds);
        }
    }
}
