package random0ne.mc;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionBlocker extends JavaPlugin implements Listener
{
	@EventHandler
	public void onSplash(PotionSplashEvent e)
	{
		ArrayList<LivingEntity> targets = new ArrayList<LivingEntity>(e.getAffectedEntities());
		ArrayList<PotionEffect> potion = new ArrayList<PotionEffect>(e.getPotion().getEffects());
		Player thrower = (Player) e.getPotion().getShooter();
		ItemStack[] armor = thrower.getInventory().getArmorContents();
		if(isDamage(potion)&&!hasArmor(armor))
		{
			for (int i = 0; i < targets.size(); i++) 
			{
				if(targets.get(i) instanceof Player)
				{
					e.setIntensity(targets.get(i), 0.0);
				}
			}
		}
	}
	
	public boolean hasArmor(ItemStack[] armor)
	{
		for (int i = 0; i < armor.length; i++) 
		{
			if(armor[i].getType() == Material.AIR)
			{
				return false;
			}
		}
		return true;
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
	
	@Override
	public void onEnable()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {}
}