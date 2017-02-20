package random0ne.mc;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BalancePotion extends JavaPlugin implements Listener
{
	public int resist = 20;
	@EventHandler
	public void onSplash(PotionSplashEvent e)
	{
		ArrayList<LivingEntity> targets = new ArrayList<LivingEntity>(e.getAffectedEntities());
		ArrayList<PotionEffect> potion = new ArrayList<PotionEffect>(e.getPotion().getEffects());
		if(isDamage(potion))
		{
			for (int i = 0; i < targets.size(); i++) 
			{
				if(targets.get(i) instanceof Player)
				{
					Player target = (Player) targets.get(i);
					LivingEntity thrower = e.getPotion().getShooter();
					if(thrower instanceof Player)
					{
						Player attacker = (Player) thrower;
						e.setIntensity(target, calcResist(attacker,target,e));
					}
					else
					{
						e.setIntensity(target, calcResist(target,e));
					}
				}
			}
		}
	}
	
	public boolean isDamage(ArrayList<PotionEffect> effect)
	{
		for (int i = 0; i < effect.size(); i++) 
		{
			if(effect.get(i).getType().equals(PotionEffectType.HARM)||effect.get(i).getType().equals(PotionEffectType.POISON))
			{
				return true;
			}
		}
		return false;
	}
	public double calcResist(Player attacker, Player victim,PotionSplashEvent e)
	{
		double temp;
		
		if(isInvis(attacker))
		{
			temp = getDamageReduced(victim)*1.25*(resist*.01*2);
		}
		else
		{
			temp = getDamageReduced(victim)*1.25*(resist*.01);
		}
		return e.getIntensity(victim)- e.getIntensity(victim)*temp;
	}
	public double calcResist(Player victim,PotionSplashEvent e)
	{
		double temp;
		temp = getDamageReduced(victim)*1.25*(resist*.01);
		return e.getIntensity(victim)- e.getIntensity(victim)*temp;
	}
	public boolean isInvis(Player player)
	{
		ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>(player.getActivePotionEffects());
		for (int i = 0; i < effects.size(); i++) 
		{
			if(effects.get(i).getType().equals(PotionEffectType.INVISIBILITY))
			{
				return true;
			}
		}
		return false;
	}
	
    public double getDamageReduced(Player player)
    {
	    PlayerInventory inv = player.getInventory();
	    ItemStack boots = inv.getBoots();
	    ItemStack helmet = inv.getHelmet();
	    ItemStack chest = inv.getChestplate();
	    ItemStack pants = inv.getLeggings();
	    double red = 0.0;
	    if (helmet != null)
	    {
	    	switch (helmet.getType())
	    	{
		    case LEATHER_HELMET: 
		    	red = red + 0.04;
		    	break;
		    case GOLD_HELMET:
		    	red = red + 0.08;
		    	break;
		    case CHAINMAIL_HELMET:
		    	red = red + 0.08;
		    	break;
		    case IRON_HELMET:
		    	red = red + 0.08;
		    	break;
		    case DIAMOND_HELMET:
		    	red = red + 0.12;
		    	break;
			default:
				break;	
	    	}
	    }
	    
	    if (chest != null)
	    {
	    	switch (chest.getType())
	    	{
		    case LEATHER_CHESTPLATE: 
		    	red = red + 0.12;
		    	break;
		    case GOLD_CHESTPLATE:
		    	red = red + 0.20;
		    	break;
		    case CHAINMAIL_CHESTPLATE:
		    	red = red + 0.20;
		    	break;
		    case IRON_CHESTPLATE:
		    	red = red + 0.24;
		    	break;
		    case DIAMOND_CHESTPLATE:
		    	red = red + 0.32;
		    	break;
			default:
				break;	
	    	}
	    }
	    
	    if (boots != null)
	    {
	    	switch (boots.getType())
	    	{
		    case LEATHER_BOOTS: 
		    	red = red + 0.04;
		    	break;
		    case GOLD_BOOTS:
		    	red = red + 0.04;
		    	break;
		    case CHAINMAIL_BOOTS:
		    	red = red + 0.04;
		    	break;
		    case IRON_BOOTS:
		    	red = red + 0.08;
		    	break;
		    case DIAMOND_BOOTS:
		    	red = red + 0.12;
		    	break;
			default:
				break;	
	    	}
	    }
	    
	    if (pants != null)
	    {
	    	switch (pants.getType())
	    	{
		    case LEATHER_LEGGINGS: 
		    	red = red + 0.08;
		    	break;
		    case GOLD_LEGGINGS:
		    	red = red + 0.12;
		    	break;
		    case CHAINMAIL_LEGGINGS:
		    	red = red + 0.16;
		    	break;
		    case IRON_LEGGINGS:
		    	red = red + 0.20;
		    	break;
		    case DIAMOND_LEGGINGS:
		    	red = red + 0.24;
		    	break;
			default:
				break;	
	    	}
	    }   
	    
	    return red;
    }
    
    
	
	@Override
	public void onEnable()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {}
}