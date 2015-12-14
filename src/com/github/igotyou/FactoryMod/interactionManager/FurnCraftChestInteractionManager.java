package com.github.igotyou.FactoryMod.interactionManager;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;

import com.github.igotyou.FactoryMod.factories.FurnCraftChestFactory;
import com.github.igotyou.FactoryMod.multiBlockStructures.FurnCraftChestStructure;
import com.github.igotyou.FactoryMod.recipes.IRecipe;
import com.github.igotyou.FactoryMod.recipes.InputRecipe;

public class FurnCraftChestInteractionManager implements IInteractionManager {
	private FurnCraftChestFactory fccf;
	private HashMap<Clickable, InputRecipe> recipes = new HashMap<Clickable, InputRecipe>();

	public FurnCraftChestInteractionManager(FurnCraftChestFactory fccf) {
		this.fccf = fccf;
	}

	public FurnCraftChestInteractionManager() {
	}

	public void setFactory(FurnCraftChestFactory fccf) {
		this.fccf = fccf;
	}

	public void redStoneEvent(BlockRedstoneEvent e) {
		// TODO
	}

	public void blockBreak(Player p, Block b) {
		fccf.getRepairManager().breakIt();
		if (p != null) {
			p.sendMessage(ChatColor.DARK_RED
					+ "You broke the factory, it is in disrepair now");
		}
		if (fccf.isActive()) {
			fccf.deactivate();
		}
	}

	public void leftClick(Player p, Block b) {
		if (b.equals(((FurnCraftChestStructure) fccf.getMultiBlockStructure())
				.getChest())) { // chest interaction
			if (p.isSneaking()) { // sneaking, so showing detailed recipe stuff
				ClickableInventory ci = new ClickableInventory(
						new ArrayList<Clickable>(), 54, fccf.getCurrentRecipe()
								.getRecipeName());
				int index = 4;
				for (ItemStack is : ((InputRecipe) fccf.getCurrentRecipe())
						.getInputRepresentation(fccf.getInventory())) {
					Clickable c = new Clickable(is) {
						@Override
						public void clicked(Player arg0) {
							// nothing, just supposed to look nice
						}
					};
					ci.setSlot(c, index);
					// weird math to fill up the gui nicely
					if ((index % 9) == 4) {
						index++;
						continue;
					}
					if ((index % 9) > 4) {
						index -= (((index % 9) - 4) * 2);
					} else {
						if ((index % 9) == 0) {
							index += 9;
						} else {
							index += (((4 - (index % 9)) * 2) + 1);
						}
					}

				}
				index = 49;
				for (ItemStack is : ((InputRecipe) fccf.getCurrentRecipe())
						.getOutputRepresentation(fccf.getInventory())) {
					Clickable c = new Clickable(is) {
						@Override
						public void clicked(Player arg0) {
							// nothing, just supposed to look nice
						}
					};
					ci.setSlot(c, index);
					if ((index % 9) == 4) {
						index++;
						continue;
					}
					if ((index % 9) > 4) {
						index -= (((index % 9) - 4) * 2);
					} else {
						if ((index % 9) == 0) {
							index -= 9;
						} else {
							index += (((4 - (index % 9)) * 2) + 1);
						}
					}

				}
				ci.showInventory(p);

			} else { // not sneaking, so just a short sumup
				p.sendMessage(ChatColor.GOLD + fccf.getName()
						+ " currently turned "
						+ (fccf.isActive() ? "on" : "off"));
				if (fccf.isActive()) {
					p.sendMessage(ChatColor.GOLD
							+ String.valueOf((fccf.getCurrentRecipe()
									.getProductionTime() - fccf
									.getRunningTime()) / 20)
							+ " seconds remaining until current run is complete");
				}
				p.sendMessage(ChatColor.GOLD + "Currently selected recipe: "
						+ fccf.getCurrentRecipe().getRecipeName());
				p.sendMessage(ChatColor.GOLD + "Currently at "
						+ fccf.getRepairManager().getHealth() + " health");
			}

			return;
		}
		if (b.equals(((FurnCraftChestStructure) fccf.getMultiBlockStructure())
				.getCraftingTable())) { // crafting table interaction
			ArrayList<Clickable> clickables = new ArrayList<Clickable>();
			for (IRecipe rec : fccf.getRecipes()) {
				InputRecipe recipe = (InputRecipe) (rec);
				Clickable c = new Clickable(recipe.getRecipeRepresentation()) {

					@Override
					public void clicked(Player p) {
						if (fccf.isActive()) {
							p.sendMessage(ChatColor.RED
									+ "You can't switch recipes while the factory is running");
						} else {
							fccf.setRecipe(recipes.get(this));
							p.sendMessage(ChatColor.GREEN
									+ "Switched recipe to "
									+ recipes.get(this).getRecipeName());
						}

					}
				};
				recipes.put(c, recipe);
				clickables.add(c);
			}
			ClickableInventory ci = new ClickableInventory(clickables,
					InventoryType.CHEST, "Select a recipe");
			ci.showInventory(p);
			return;
		}
		if (b.equals(((FurnCraftChestStructure) fccf.getMultiBlockStructure())
				.getFurnace())) { // furnace interaction
			if (fccf.isActive()) {
				fccf.deactivate();
			} else {
				fccf.attemptToActivate(p);
			}
		}
	}

	public void rightClick(Player p, Block b) {
		// Nothing to do here, every block already has a right click
		// functionality
	}

}