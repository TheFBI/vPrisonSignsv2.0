package me.thefbi.signs.listeners;

import me.thefbi.signs.Signs;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

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

public class SignCreateListener implements Listener{
	
	@EventHandler
	public void onSignCreate(SignChangeEvent event)
	{	
		 if(Signs.INSTANCE.players.contains(event.getPlayer().getUniqueId()))
		 {
			 
			 if(event.getLine(0).equalsIgnoreCase("sell all"))
			 {
				 
				 event.setLine(0, ChatColor.BLUE + "Sell");
			 }
			 
			 for(String m : Signs.INSTANCE.mines)
			 {
				 if(m.contains(event.getLine(1)))
				 { 
					 
					 event.setLine(2, Signs.INSTANCE.sellData.getString("Mine." + event.getLine(1) + ".Diamond_Price"));
					 event.setLine(3, Signs.INSTANCE.sellData.getString("Mine." + event.getLine(1) + ".Iron_Price"));
					 
					 event.getPlayer().sendMessage(Signs.INSTANCE.prefix + "You have created a sell sign for mine " + ChatColor.RED + event.getLine(1));
					 break;
					 
				 } else {
					 
					 if("Mine." + event.getLine(1) + ".Diamond_Price" != null || "Mine." + event.getLine(1) + ".Iron_Price" != null)
					 {
						 event.getPlayer().sendMessage(Signs.INSTANCE.prefix + "Prices not found... create them.");
						 event.setCancelled(true);
						 return;
					 }
				 }
			 }
		 }
	}
}
