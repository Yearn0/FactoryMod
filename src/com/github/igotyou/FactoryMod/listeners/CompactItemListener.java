package com.github.igotyou.FactoryMod.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class CompactItemListener implements Listener{
	private String compactLore;	
	
	public CompactItemListener(String compactLore) {
		this.compactLore = compactLore;
	}
	
	@EventHandler
	public void blockPlaceEvent(BlockPlaceEvent e) {
		if (!e.getItemInHand().hasItemMeta()) {
			return;
		}
		if (!e.getItemInHand().getItemMeta().hasLore()) {
			return;
		}
		if (e.getItemInHand().getItemMeta().getLore().get(0).equals(compactLore)) {
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void craftingEvent(CraftItemEvent e) {
		CraftingInventory ci = e.getInventory();
		for(ItemStack is:ci.getMatrix()) {
			if (is == null) {
				continue;
			}
			if (!is.hasItemMeta()) {
				continue;
			}
			if (!is.getItemMeta().hasLore()) {
				continue;
			}
			if (is.getItemMeta().getLore().get(0).equals(compactLore)) {
				e.setCancelled(true);
				break;
			}
		}
	}

}
