package jinivus.BossAssist;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class BossAssist extends JavaPlugin
{

	@Override
	public void onDisable() {
		System.out.println("Disabling BossAssist");

	}

	@Override
	public void onEnable() {
		getCommand("ba").setExecutor(new BACommand(this));
		System.out.println("Enabled BossAssist");

	}

	public WorldGuardPlugin getWorldGuard()
	{
		Plugin wg = getServer().getPluginManager().getPlugin("WorldGuard");

		// WorldGuard may not be loaded
		if (wg == null || !(wg instanceof WorldGuardPlugin)) 
		{
			return null; // Maybe you want throw an exception instead
		}

		return (WorldGuardPlugin) wg;
	}
	
	public WorldEditPlugin getWorldEdit()
	{
		Plugin we = getServer().getPluginManager().getPlugin("WorldEdit");
		
		if (we == null || !(we instanceof WorldEditPlugin)) 
		{
			return null; // Maybe you want throw an exception instead
		}
		
		return (WorldEditPlugin) we;
	}

}
