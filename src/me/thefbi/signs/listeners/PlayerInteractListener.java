package me.thefbi.signs.listeners;

import me.thefbi.signs.Signs;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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

public class PlayerInteractListener implements Listener{
		
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
				
		if(event.getClickedBlock().getType().equals(Material.SIGN_POST) || event.getClickedBlock().getType().equals(Material.SIGN) && event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Sign sign = (Sign) event.getClickedBlock().getState();
			
			if(!(sign.getLine(0).contains(ChatColor.BLUE + "Sell")))
			{
				return;
			}
			
			int diamondPrice = Signs.INSTANCE.diamondPrice.get(sign.getLine(1));
			int ironPrice = Signs.INSTANCE.ironPrice.get(sign.getLine(1));
			int iron = 0;
			int dia = 0;
			
			if(diamondPrice == 0 || ironPrice == 0)
			{
				player.sendMessage(Signs.INSTANCE.prefix + "Prices not found...");
				return;
			}
			
			for(Material sell : Signs.INSTANCE.sellable)
			{	
				for(ItemStack items : player.getInventory().getContents())
				{
					if(items != null && items.getType() == sell)
					{
						if(sell == Material.IRON_INGOT)
						{
							iron += 64;
						}
						
						if(sell == Material.DIAMOND)
						{
							dia += 64;
						}
						
						player.getInventory().remove(items);
						player.updateInventory();
						continue;
						
					} else {
						
						continue;
					}
				}
			}
			
			player.sendMessage(Signs.INSTANCE.prefix + "You sold " + dia + " Diamonds for $" + ChatColor.GREEN + dia * diamondPrice);
			player.sendMessage(Signs.INSTANCE.prefix + "You sold " + iron + " Iron for $" + ChatColor.GREEN + iron * ironPrice);

			return;
			
		}
	}

}
