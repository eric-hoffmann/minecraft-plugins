package random0ne.mc;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AntiInvis extends JavaPlugin implements Listener
{
	@EventHandler
	public void onSplash(PotionSplashEvent e)
	{
		ArrayList<LivingEntity> targets = new ArrayList<LivingEntity>(e.getAffectedEntities());
		Player thrower = (Player) e.getPotion().getShooter();
		ArrayList<PotionEffect> potion = new ArrayList<PotionEffect>(e.getPotion().getEffects());
		if(isDamage(potion)&&(thrower.getGameMode().compareTo(GameMode.CREATIVE) != 0))
		{
			for (int i = 0; i < targets.size(); i++) 
			{
				if(targets.get(i) instanceof Player && !targets.get(i).equals(thrower))
				{
					thrower.removePotionEffect(PotionEffectType.INVISIBILITY);
					return;
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
	
	@Override
	public void onEnable()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {}
}