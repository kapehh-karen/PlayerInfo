package me.kapehh.PlayerInfo;

import me.kapehh.main.pluginmanager.config.EventPluginConfig;
import me.kapehh.main.pluginmanager.config.EventType;
import me.kapehh.main.pluginmanager.config.PluginConfig;
import me.kapehh.main.pluginmanager.vault.PluginVault;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

/**
 * Created by Karen on 11.10.2014.
 */
public class Main extends JavaPlugin implements CommandExecutor {
    public static Main instance;

    private boolean isForce;
    private Economy economy;
    private PluginConfig pluginConfig;
    private DBHelper dbHelper;
    private RankList rankList;
    private String format;

    private String processFormat(Player player, PlayerStat playerStat) {
        String res = format.replace("{name}", player.getName())
            .replace("{lvl}", String.valueOf(playerStat.getLvl()))
            .replace("{hp}", String.valueOf(playerStat.getHp()))
            .replace("{money}", String.valueOf(playerStat.getMoney()))
            .replace("{rank}", playerStat.getRank())
            .replace("{mobs}", String.valueOf(playerStat.getMobKills()))
            .replace("{players}", String.valueOf(playerStat.getPlayerKills()))
            .replace("{deaths}", String.valueOf(playerStat.getDeaths()));

        RankList.RankListItem item = rankList.getNextRankListItem(playerStat.getRank());
        if (item == null) {
            return res;
        }

        return res.replace("{nextrank}", item.getRankName())
            .replace("{lastmobs}", String.valueOf(RankList.getLastMobs(playerStat, item)))
            .replace("{lastplayers}", String.valueOf(RankList.getLastKills(playerStat, item)));
    }

    public Economy getEconomy() {
        return economy;
    }

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    @EventPluginConfig(EventType.LOAD)
    public void onConfigLoad() {
        FileConfiguration cfg = pluginConfig.getConfig();

        if (dbHelper != null) {
            try {
                dbHelper.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        dbHelper = new DBHelper(
            cfg.getString("connect.ip", ""),
            cfg.getString("connect.db", ""),
            cfg.getString("connect.login", ""),
            cfg.getString("connect.password", "")
        );
        try {
            dbHelper.connect();
            rankList = new RankList();
            getLogger().info("Success connect to MySQL!");
        } catch (SQLException e) {
            dbHelper = null;
            e.printStackTrace();
        }

        format = cfg.getString("message");

        getLogger().info("Complete!");
    }

    @Override
    public void onEnable() {
        instance = this;

        if (getServer().getPluginManager().getPlugin("PluginManager") == null) {
            getLogger().info("PluginManager not found!!!");
            getServer().getPluginManager().disablePlugin(this);
            isForce = true;
            return;
        }
        isForce = false;

        economy = PluginVault.setupEconomy();

        getCommand("playerinfo").setExecutor(this);

        pluginConfig = new PluginConfig(this);
        pluginConfig.addEventClasses(this).setup().loadData();
    }

    private Player getPlayer(String name) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { // если это консоль

            // reload
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                pluginConfig.loadData();
                return true;
            }

            return false; // другого консоли не дано
        }

        if (args.length == 0) {
            Player player = (Player) sender;
            sender.sendMessage(processFormat(player, new PlayerStat(player)));
            return true;
        } else {
            Player player = getPlayer(args[0]);
            if (player != null) {
                sender.sendMessage(processFormat(player, new PlayerStat(player)));
            } else {
                sender.sendMessage("Player not found!");
            }
            return true;
        }
    }

    @Override
    public void onDisable() {
        if (isForce) {
            return;
        }

        if (dbHelper != null) {
            try {
                dbHelper.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
