package random0ne.mc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public final class CompressedOres extends JavaPlugin implements Listener
{
	ItemStack compressedGold = new ItemStack(customItem(new ItemStack(Material.INK_SACK,1,(short) 11), ChatColor.GOLD + "Compressed Gold Ore",ChatColor.GOLD + "This is compressed gold.", ChatColor.GOLD + "Place it in a crafting table", ChatColor.GOLD +"to recieve 9 gold ore."));
	ItemStack compressedIron = new ItemStack(customItem(new ItemStack(Material.INK_SACK,1,(short)7), ChatColor.GRAY + "Compressed Iron Ore",ChatColor.GRAY + "This is compressed iron.", ChatColor.GRAY + "Place it in a crafting table", ChatColor.GRAY +"to recieve 9 iron ore."));
	ShapedRecipe compressGold = new ShapedRecipe(compressedGold).shape("OOO","OOO","OOO").setIngredient('O', Material.GOLD_ORE);
	ShapedRecipe compressIron = new ShapedRecipe(compressedIron).shape("OOO","OOO","OOO").setIngredient('O', Material.IRON_ORE);
	ShapelessRecipe goldToRegular =  new ShapelessRecipe(new ItemStack(Material.GOLD_ORE,9)).addIngredient(compressedGold.getData());
	ShapelessRecipe ironToRegular =  new ShapelessRecipe(new ItemStack(Material.IRON_ORE,9)).addIngredient(compressedIron.getData());
	public void onEnable() 
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().addRecipe(compressGold);
		getServer().addRecipe(goldToRegular);
		getServer().addRecipe(compressIron);
		getServer().addRecipe(ironToRegular);
	}
	public void onDisable() 
	{
		getServer().clearRecipes();
	}
	public ItemStack customItem(ItemStack item, String disp, String lore1, String lore2, String lore3)
	{
		List<String> lore = new ArrayList<String>();
		ItemMeta meta = item.getItemMeta();
		lore.add(lore1);
		lore.add(lore2);
		lore.add(lore3);
		meta.setDisplayName(disp);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	@EventHandler
	public void onCraft (PrepareItemCraftEvent event)
	{
		CraftingInventory craft = event.getInventory();
		if(craft.getRecipe().getResult().isSimilar(goldToRegular.getResult()))
		{
			if(!craft.containsAtLeast(compressedGold, 1))
			{
				craft.setResult(null);
			}
			getLogger().info("hi!");
		}
		if(craft.getRecipe().getResult().isSimilar(ironToRegular.getResult()))
		{
			if(!craft.containsAtLeast(compressedIron, 1))
			{
				craft.setResult(null);
			}
			getLogger().info("there!");
		}
		
	}
}