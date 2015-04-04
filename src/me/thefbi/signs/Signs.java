package me.thefbi.signs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import me.thefbi.signs.config.MyConfig;
import me.thefbi.signs.config.MyConfigManager;
import me.thefbi.signs.listeners.PlayerInteractListener;
import me.thefbi.signs.listeners.SignCreateListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * *********************************************************************
 * Copyright TheFBI (c) 2014. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with
 * similar branding are the sole property of TheFBI. Distribution,
 * reproduction, taking screenshots, or claiming any content as your own
 * will break the terms of the license I don't have, and will void any
 * agreements made with you, the guy who is reading this.
 * Thanks, and goodbye.
 * ***********************************************************************
 */

public class Signs extends JavaPlugin{
		
	public HashMap<String, Integer> ironPrice = new HashMap<String, Integer>();
	
	public HashMap<String, Integer> diamondPrice= new HashMap<String, Integer>();
		
	public String[] mines = {"aMine", "bMine", "cMine", "dMine", "eMine", "fMine", "gMine", "hMine", "iMine", "jMine", "kMine", "lMine", "mMine", "nMine", "oMine", "pMine", "qMine", "rMine", "sMine", "tMine", "uMine", "vMine", "wMine", "xMine", "yMine", "zMine"};
	
	public Material[] sellable = {Material.IRON_INGOT, Material.DIAMOND};
	
	private static Logger log = Bukkit.getLogger();
		
	public List<UUID> players = new ArrayList<UUID>();
	
	public boolean autoLoadPrices = getConfig().getBoolean("auto-load-prices");
	
	private PluginManager getPluginManager = Bukkit.getServer().getPluginManager();
	
	public String prefix = ChatColor.GREEN + "Venom " + ChatColor.DARK_GRAY + "// " + ChatColor.GRAY;
	
	private String[] commands = {"/prison getpermission", "/vprison set <mine> <diamond price> <iron price>", "/vprison get prices", "/vprison reload"};
	
	public static Signs INSTANCE;
	
	MyConfigManager manager;
	public MyConfig sellData;
	MyConfig settings;
	
	private void registerEvents()
	{
		getPluginManager.registerEvents(new SignCreateListener(), this);
		getPluginManager.registerEvents(new PlayerInteractListener(), this);
	}
	
	private void setPricesEnable()
	{
		for(String s : mines)
		{
			this.diamondPrice.put(s, new Integer(sellData.getInt("Mine." + s + ".Diamond_Price")));
			this.ironPrice.put(s, new Integer(sellData.getInt("Mine." + s + ".Iron_Price")));
			Bukkit.broadcastMessage(prefix + "Set " + ChatColor.RED + "Diamond " + ChatColor.GRAY + "price for " + ChatColor.RED + s + ChatColor.GRAY + " to " + ChatColor.RED + this.diamondPrice.get(s));
			Bukkit.broadcastMessage(prefix + "Set " + ChatColor.RED + "Iron_Ingot " + ChatColor.GRAY + "price for " + ChatColor.RED + s + ChatColor.GRAY + " to " + ChatColor.RED + this.ironPrice.get(s));
		}
	}
	
	private void setPrices(Player player)
	{
		for(String s : mines)
		{
			this.diamondPrice.put(s, new Integer(sellData.getInt("Mine." + s + ".Diamond_Price")));
			this.ironPrice.put(s, new Integer(sellData.getInt("Mine." + s + ".Iron_Price")));
			player.sendMessage(prefix + "Set " + ChatColor.RED + "Diamond " + ChatColor.GRAY + "price for " + ChatColor.RED + s + ChatColor.GRAY + " to " + ChatColor.RED + this.diamondPrice.get(s));
			player.sendMessage(prefix + "Set " + ChatColor.RED + "Iron_Ingot " + ChatColor.GRAY + "price for " + ChatColor.RED + s + ChatColor.GRAY + " to " + ChatColor.RED + this.ironPrice.get(s));
		}
	}
	
	public void onDisable()
	{
		INSTANCE = this;
	}
	
	public void onEnable()
	{
		INSTANCE = this;
		manager = new MyConfigManager(this);
		sellData = manager.getNewConfig("SellData.yml", new String[] {"THIS IS WHERE SELLDATA IS KEPT", "VENOMPRISON"});
		settings = manager.getNewConfig("Settings.yml", new String[] {"WHO NEEDS A REAL CONFIG?", "VENOMPRISON CONFIG"});
		long start = System.currentTimeMillis();
		log.info("Registering Events");
		registerEvents();
		long end = System.currentTimeMillis();
		log.info("Loading vPrison Sell Sign prices...");
		this.setPricesEnable();
		long overall = end - start;
		
		log.info("[vPrisonSigns] Completed in " + overall + " ms");
		
		saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("Only players can spawn these signs.");
			return true;
		}
		
		Player p = (Player) sender;
		
		if(command.getName().equalsIgnoreCase("vprison") && args.length == 0)
		{
			p.sendMessage(prefix + ChatColor.GREEN + "                    vPrison Sell Signs");
			p.sendMessage(prefix + "< -------------------------------- >");
			
			for(String c : commands)
			{
				p.sendMessage(prefix + c);
			}
			
			p.sendMessage(prefix + "< -------------------------------- >");
			
			return true;
			
		} else if (command.getName().equalsIgnoreCase("vprison") && args[0].equalsIgnoreCase("getpermission") && args.length == 1)
		{	
			if(!(p.hasPermission("venom.prison.getpermission")))
			{
				p.sendMessage(prefix + "No permission!");
				return true;
			}
			if (players.contains(p.getUniqueId()))
			{
				players.remove(p.getUniqueId());
				p.sendMessage(prefix + "You have been removed from the SET_SIGN list.");
				return true;
				
			} else {
				
				players.add(p.getUniqueId());
				p.sendMessage(prefix + "You have been added to permission list. // This only allows you to create sell signs.");
				return true;
			}
			
		} else if (command.getName().equalsIgnoreCase("vprison") && args[0].equalsIgnoreCase("set") && args.length == 4)
		{
			if(!(p.hasPermission("venom.prison.set")))
			{
				p.sendMessage(prefix + "No permission!");
				return true;
			}
			String mine = args[1];
			int diamondPrice = Integer.parseInt(args[2]);
			int ironIngotPrice = Integer.parseInt(args[3]);
		
			sellData.set("Mine." + args[1] + ".Diamond_Price", diamondPrice);
			sellData.set("Mine." + args[1] + ".Iron_Price", ironIngotPrice);
			sellData.saveConfig();
			p.sendMessage(prefix + "[" + ChatColor.RED + mine + ChatColor.GRAY + "] " + ChatColor.GRAY + "Diamond Price set to: " + diamondPrice + ", Iron price set to: " + ironIngotPrice);
			return true;
			
		} else if (command.getName().equalsIgnoreCase("vprison") && args[0].equalsIgnoreCase("get") && args[1].equalsIgnoreCase("prices") && args.length == 2)
		{
			if(!(p.hasPermission("venom.prison.getprices")))
			{
				p.sendMessage(prefix + "No permission!");
				return true;
			}
			this.setPrices(p);
			
		} else if (command.getName().equalsIgnoreCase("vprison") && args[0].equalsIgnoreCase("reload"))
		{
			if(!(p.hasPermission("venom.prison.reload")))
			{
				p.sendMessage(prefix + "No permssions!");
				return true;
			}
			sellData.reloadConfig();
			p.sendMessage(prefix + "reloaded config");
			return true;
		}
		
		return false;
	}
	
}
