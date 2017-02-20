package random0ne.mc;

import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class VoteReward extends JavaPlugin implements Listener
{
	boolean online = true;
	Hashtable<String, ArrayList<ItemStack>> players = new Hashtable<String, ArrayList<ItemStack>>();
	ItemStack iron = new ItemStack(Material.IRON_INGOT,30);
	ItemStack diamond3 = new ItemStack(Material.DIAMOND,3);
	ItemStack diamond10 = new ItemStack(Material.DIAMOND,10);
	ItemStack horseEgg = new ItemStack(Material.MONSTER_EGG,1,(short)100);
	ItemStack villagerEgg = new ItemStack(Material.MONSTER_EGG , 1,(short) 120);
	ItemStack enchBookProt = new ItemStack(Material.ENCHANTED_BOOK , 1);
	ItemStack enchBookUnbr = new ItemStack(Material.ENCHANTED_BOOK , 1);
	ItemStack skull = new ItemStack(Material.SKULL_ITEM , 1, (short)1);
	ItemStack voucher = new ItemStack(Material.PAPER , 1);
	ItemStack ticket = new ItemStack(Material.PAPER , 1);
	ItemStack beacon = new ItemStack(Material.BEACON , 1);
	ItemStack exp = new ItemStack(Material.EXP_BOTTLE,64);
	EnchantmentStorageMeta prot =  (EnchantmentStorageMeta) enchBookProt.getItemMeta();
	EnchantmentStorageMeta unbr =  (EnchantmentStorageMeta) enchBookUnbr.getItemMeta();
		
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		Player player = e.getPlayer();
		
		if(players.containsKey(player.getName()))
		{
			ArrayList<ItemStack> items = players.get(player.getName());
			for (int i = 0; i < items.size(); i++) 
			{
				if(items.get(i) == null)
				{
					player.giveExpLevels(30);
				}
				else
				{
				player.getInventory().addItem(items.get(i));
				}
			}
			players.remove(player.getName());
			announceOffline(player);
		}
	}
	
	@EventHandler
	public void onVote (VotifierEvent e)
	{
		//use "vote." to access the vote information
		Vote vote = e.getVote();
		//"voter" refers to the player who voted
		Player voter = Bukkit.getServer().getPlayerExact(vote.getUsername());

		//checks to make sure the player is online
		if (voter==null)
		{
			online = false;
		}
		else
		{
			online = true;
		}
		//creates a random number between (inclusive) 1-100
		double chance = (Math.random() * 100 + 1);
		
		//figures out what prize to give
		if(chance <= 15)
		{
			if( (int) (Math.random()*2) == 0)
			{
				giveItem(vote,voter,iron);
			}
			else
			{
				giveItem(vote,voter,null);
			}
		}
		else if(chance <= 25)
		{
			giveItem(vote,voter,diamond3);
		}
		else if (chance <= 29)
		{
			giveItem(vote,voter,diamond10);
		}
		else if (chance <= 31)
		{
			giveItem(vote,voter,horseEgg);
		}
		else if (chance <= 32)
		{
			giveItem(vote,voter,villagerEgg);
		}
		else if (chance <= 33.5)
		{
			if( (int) (Math.random()*2) == 0)
			{
				giveItem(vote,voter,enchBookUnbr);
			}
			else
			{
				giveItem(vote,voter,enchBookProt);
			}
		}
		else if (chance <= 33.9)
		{
			if( (int) (Math.random()*2) == 0)
			{
				giveItem(vote,voter,ticket);
			}
			else
			{
				giveItem(vote,voter,skull);
			}
		}
		else if (chance <= 34)
		{
			if( (int) (Math.random()*2) == 0)
			{
				giveItem(vote,voter,voucher);
			}
			else
			{
				giveItem(vote,voter,beacon);
			}
		}	
	}
	
	//names the ticket and voucher
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
	
	//adds enchantments to the books, and adds the custom data to the ticket/voucher
	public void createItems()
	{
		unbr.addStoredEnchant(Enchantment.DURABILITY, 3, true);
		prot.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
		enchBookUnbr.setItemMeta(unbr);
		enchBookProt.setItemMeta(prot);
		voucher = customItem(voucher,ChatColor.GOLD+"Auction Voucher",ChatColor.AQUA+"Use this at the Sunday auctions",ChatColor.AQUA+"to recieve 50% off of your",ChatColor.AQUA+"chest purchase!");
		ticket = customItem(ticket,ChatColor.DARK_RED+"Legendary Ticket",ChatColor.AQUA+"Give this paper to an admin",ChatColor.AQUA+"to recieve a powerful",ChatColor.AQUA+"legendary item!");
	}
	
	//gives the item to the player if they are online, or stores the item if they aren't
	public void giveItem(Vote vote,Player voter,ItemStack item)
	{
		String name = vote.getUsername();
		if(online)
		{
			if (item == null)
			{
				voter.giveExpLevels(30);
			}
			else
			{
			voter.getInventory().addItem(item);
			}
			announceOnline(vote,item);
		}
		else
		{
			if(players.containsKey(name))
			{
				players.get(name).add(item);
			}
			else
			{
				players.put(name, new ArrayList<ItemStack>());
				players.get(name).add(item);
			}
		}
	}
	
	//announces the jackpot if the player is online
	public void announceOnline(Vote vote, ItemStack item)
	{
		Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "Voting Jackpot! " +ChatColor.AQUA + vote.getUsername() + ChatColor.DARK_PURPLE+" won "+ ChatColor.GOLD + itemName(item)+"!");
	}
	
	//announces the jackpot if the player is offline
	public void announceOffline(Player player)
	{
		Bukkit.getServer().broadcastMessage(ChatColor.AQUA + player.getName() + ChatColor.DARK_PURPLE+" hit the voting jackpot!");
	}
	
	public String itemName(ItemStack item)
	{
		if( item == null)
		{
			return "30 Levels";
		}
		else if(item.equals(iron))
		{
			return "10 Iron";
		}
		else if (item.equals(diamond3))
		{
			return "3 Diamonds";
		}
		else if (item.equals(diamond10))
		{
			return "10 Diamonds";
		}
		else if (item.equals(horseEgg))
		{
			return "A Horse Egg";
		}
		else if (item.equals(villagerEgg))
		{
			return "A Villager Egg";
		}
		else if (item.equals(enchBookProt))
		{
			return "A Protection IV Book";
		}
		else if (item.equals(enchBookUnbr))
		{
			return "An Unbreaking III Book";
		}
		else if (item.equals(skull))
		{
			return "A Wither Skull";
		}
		else if (item.equals(voucher))
		{
			return "An Auction Voucher";
		}
		else if (item.equals(ticket))
		{
			return "A Legendary Ticket";
		}
		else if (item.equals(beacon))
		{
			return "A Beacon";
		}
		else
		{
			return"If you see this, Random messed up his code...";
		}
		
	}
	
	//registers the listener with the server when the plugin is started
	@Override
	public void onEnable()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		createItems();		
	}
	
	//nothing needed here for now :P
	@Override
	public void onDisable() {}
}