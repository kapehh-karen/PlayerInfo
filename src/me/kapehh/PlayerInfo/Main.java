package me.kapehh.PlayerInfo;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Karen on 11.10.2014.
 */
public class Main extends JavaPlugin implements CommandExecutor {
    String format = "=*======================\n" +
                    "[{name}]\n" +
                    "\n" +
                    "Звание: {rank}\n" +
                    "Здоровье: {hp}\n" +
                    "\n" +
                    "Динарии: {money}\n" +
                    "\n" +
                    "Убито мобов: {mobs}\n" +
                    "Убито игроков: {players}\n" +
                    "Смертей: {deaths}\n" +
                    "\n" +
                    "=*======================\n";

    private String processFormat(Player player) {
        return format.replace("{name}", player.getName());
    }

    @Override
    public void onEnable() {
        getCommand("playerinfo").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        player.sendMessage(processFormat(player));
        return true;
    }

    @Override
    public void onDisable() {

    }
}
