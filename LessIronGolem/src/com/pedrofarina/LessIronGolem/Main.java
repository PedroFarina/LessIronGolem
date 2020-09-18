package com.pedrofarina.LessIronGolem;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;


public class Main extends JavaPlugin implements Listener {
	public void onEnable() {
		getLogger().info("LessIronGolem is up.");
		getServer().getPluginManager().registerEvents(this, this);
		
		FileConfiguration config = getConfig();
		if (config.getString("min") == null || config.getString("max") == null) {
			config.set("min", "1");
			config.set("max", "3");
			saveConfig();
		}
		min = toInt(config.getString("min"));
		max = toInt(config.getString("max"));
	}
	
	public void onDisable() {
		getLogger().info("LessIronGolem is down.");
	}
	
	public int toInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch(NumberFormatException nfe) {
			return -1;
		}
	}
	
	private int min = 1;
	private int max = 3;
	@EventHandler
	public void onGolemDeath(EntityDeathEvent e) {
		// No need to do anything
		if (e.getEntity().getType() != EntityType.IRON_GOLEM)
			return;

		java.util.List<org.bukkit.inventory.ItemStack> items = e.getDrops();
		for(org.bukkit.inventory.ItemStack item: items) {
			if (item.getType() == Material.IRON_INGOT) {
				item.setAmount(ThreadLocalRandom.current().nextInt(min, max));
			}
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		String message = "Sorry, wrong input.";
		if (sender instanceof Player) {
			String lowerCMD = cmd.getName().toLowerCase();
			int value = toInt(args[0]);
			if (value > 0) {
				switch(lowerCMD) {
				case "igmin":
					min = value;
					message = "Successfully set a new minimum.";
					getConfig().set("min", String.valueOf(min));
					saveConfig();
					break;
				case "igmax":
					max = value;
					getConfig().set("max", String.valueOf(max));
					saveConfig();
					message = "Successfully set a new maximum.";
					break;
					default:
						message = "Sorry, couldn't understand that.";
						break;
				}
			}
		} else {
			message = "Sorry, couldn't understand that.";
		}
		player.sendMessage(message);
		return true;
	}
}