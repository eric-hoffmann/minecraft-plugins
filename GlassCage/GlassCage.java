package random0ne.mc;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class GlassCage extends JavaPlugin implements Listener
{
	WorldEditPlugin worldedit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
	
	Map<String,ArrayList<Integer>> percent = new Hashtable<String,ArrayList<Integer>>();

	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("glasscagefill") && args.length == 0)
		{
			if(sender instanceof Player)
			{
				Player user = (Player) sender;
				if(sender.hasPermission("glasscage.fill"))
				{
					Selection sel = worldedit.getSelection(user);
					if (sel == null)
					{
						sender.sendMessage("You must make a selection first!");
						return true;
					}
					else if (sel instanceof CuboidSelection)
					{
						Location max = sel.getMaximumPoint();
						Location min = sel.getMinimumPoint();
						ArrayList<ItemStack> row1 = new ArrayList<ItemStack>();
						ArrayList<ItemStack> row2 = new ArrayList<ItemStack>();
						ArrayList<ItemStack> row3 = new ArrayList<ItemStack>();
						ArrayList<ItemStack> row4 = new ArrayList<ItemStack>();
						setItems(user, row1, row2, row3, row4);
						for (int x = min.getBlockX(); x <= max.getBlockX(); x++) 
						{
							for (int y = min.getBlockY(); y <= max.getBlockY(); y++) 
							{
								for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) 
								{
									Block block = user.getWorld().getBlockAt(x, y, z);
									if(isValid(block))
									{
										Chest chest = (Chest) block.getState();
										fillChests(chest,selectItem(user,row1,row2,row3,row4));
									}
								}
							}
						}
						sender.sendMessage(ChatColor.AQUA + "[GlassCage] "+ ChatColor.YELLOW + "Chests filled!");
						return true;
					}
					else
					{
						sender.sendMessage("Must be a cuboid selection!");
						return true;
					}
				}
				else
				{
					return false;
				}
			}
			else
			{
				sender.sendMessage("You must be a player to use this command!");
				return true;
			}
		}
		if(cmd.getName().equalsIgnoreCase("glasscageinfo"))
		{
			if(sender instanceof Player)
			{
				Player user = Bukkit.getPlayerExact(sender.getName());
				if(sender.hasPermission("glasscage.fill"))
				{
					if(args.length > 0)
					{
						if(args.length == 3 && args[0].equalsIgnoreCase("set"))
						{
							int row = Integer.parseInt(args[1]);
							int percentage = Integer.parseInt(args[2]);
							setProb(row,percentage,user);
							return true;
						}
						else if(args.length == 1 && args[0].equalsIgnoreCase("reset"))
						{
							
							for (int i = 1; i < 5; i++) 
							{
								setProb(i,15,user);
							}
							return true;
						}
						else
						{
							user.sendMessage(ChatColor.RED + "Usage: /glasscageinfo \n    OR /glasscageinfo set [row] [percentage] \n    OR /glasscageinfo reset");
							return true;
						}
					}
					else
					{
						sendProbInfo(user);
						return true;
					}
				}
				else
				{
					return false;
				}
			}
			else
			{
				sender.sendMessage("You must be a player to use this command!");
				return true;
			}
		}
		
    	return false;
	}
	public void fillChests(Chest chest, ItemStack item)
	{
		chest.getBlockInventory().setItem(13, item);
	}
	public void setItems(Player player, ArrayList<ItemStack> row1,ArrayList<ItemStack> row2,ArrayList<ItemStack> row3,ArrayList<ItemStack> row4)
	{
		ItemStack item = new ItemStack(Material.AIR);
		for (int i = 0; i < 9; i++) 
		{
			item = player.getInventory().getItem(i);
			if(item != null && item.getType() != Material.AIR)
			{
				row4.add(item);
			}
		}
		for (int i = 9; i < 18; i++) 
		{
			item = player.getInventory().getItem(i);
			if(item != null && item.getType() != Material.AIR)
			{
				row1.add(item);
			}
		}
		for (int i = 18; i < 27; i++) 
		{
			item = player.getInventory().getItem(i);
			if(item != null && item.getType() != Material.AIR)
			{
				row2.add(item);
			}
		}
		for (int i = 27; i < 35; i++) 
		{
			item = player.getInventory().getItem(i);
			if(item != null && item.getType() != Material.AIR)
			{
				row3.add(item);
			}
		}
	}
	public void setProb(int row, int prob, Player player)
	{
		if (!percent.containsKey(player.getName()))
		{
			addPlayer(player);
		}
		if (row >= 1 && row <= 4)
		{
			percent.get(player.getName()).set(row - 1, prob);
			player.sendMessage(ChatColor.AQUA + "[GlassCage] "+ ChatColor.YELLOW + "Row "+ (row) +" set to "+ prob + "%");
		}
		else
		{
			player.sendMessage(ChatColor.RED+"Invalid row number!");
		}
		
	}
	public void addPlayer(Player player)
	{
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++) 
		{
			temp.add(15);
		}
		percent.put(player.getName(), temp);
	}
	public ItemStack selectItem(Player player, ArrayList<ItemStack> row1,ArrayList<ItemStack> row2,ArrayList<ItemStack> row3,ArrayList<ItemStack> row4)
	{
		if (!percent.containsKey(player.getName()))
		{
			addPlayer(player);
		}
		int rand = (int) (Math.random() *100 +1);
		int per1 = percent.get(player.getName()).get(0);
		int per2 = percent.get(player.getName()).get(1);
		int per3 = percent.get(player.getName()).get(2);
		int per4 = percent.get(player.getName()).get(3);
		
		if(rand <= per1)
		{
			if(row1.size()>0)
			{
				return row1.get(getSlot(row1));
			}
			else
			{
				return new ItemStack(Material.AIR);
			}
			
			
		}
		else if(rand <= per1+per2)
		{
			if(row2.size()>0)
			{
				return row2.get(getSlot(row2));
			}
			else
			{
				return new ItemStack(Material.AIR);
			}
			
		}
		else if (rand <= per1+per2+per3)
		{
			if(row3.size()>0)
			{
				return row3.get(getSlot(row3));
			}
			else
			{
				return new ItemStack(Material.AIR);
			}
			
		}
		else if (rand <= per1+per2+per3+per4)
		{
			if(row4.size()>0)
			{
				return row4.get(getSlot(row4));
			}
			else
			{
				return new ItemStack(Material.AIR);
			}
			
		}
		else
		{
			return new ItemStack(Material.AIR);	
		}
	}
	public int getSlot(ArrayList<ItemStack> row)
	{
		return (int) (Math.random() * row.size());
	}
	public void sendProbInfo(Player player)
	{
		if (!percent.containsKey(player.getName()))
		{
			addPlayer(player);
		}
		ArrayList<Integer> info = percent.get(player.getName());
		player.sendMessage(ChatColor.AQUA + "[GlassCage]");
		player.sendMessage(ChatColor.YELLOW + "Row 1 Percentage: "+ info.get(0));
		player.sendMessage(ChatColor.YELLOW + "Row 2 Percentage: "+ info.get(1));
		player.sendMessage(ChatColor.YELLOW + "Row 3 Percentage: "+ info.get(2));
		player.sendMessage(ChatColor.YELLOW + "Row 4 Percentage: "+ info.get(3));
		player.sendMessage(ChatColor.YELLOW + "Total Percentage: "+ (info.get(0)+info.get(1)+info.get(2)+info.get(3)));
		
	}
	
	public boolean isValid(Block block)
	{
		
		Block upBlock = block.getRelative(BlockFace.UP);
		Block downBlock = block.getRelative(BlockFace.DOWN);
		Block northBlock = block.getRelative(BlockFace.NORTH);
		Block eastBlock = block.getRelative(BlockFace.EAST);
		Block southBlock = block.getRelative(BlockFace.SOUTH);
		Block westBlock = block.getRelative(BlockFace.WEST);
		Block[] blocks = {upBlock,downBlock,northBlock,eastBlock,southBlock,westBlock};
		if(block.getType().equals(Material.CHEST))
		{
			for (int i = 0; i < blocks.length; i++) 
			{
				if(blocks[i].getType().equals(Material.CHEST))
				{
					Chest chest = (Chest) blocks[i].getState();
					for(ItemStack item : chest.getInventory().getContents())
					{
						if(item != null)
						{
							return false;
						}
					}
				}
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void onEnable()
	{
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {}
}