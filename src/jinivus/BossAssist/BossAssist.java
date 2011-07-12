package jinivus.BossAssist;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.floyd.bukkit.petition.PetitionPlugin;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class BossAssist extends JavaPlugin
{
	public static PermissionHandler permissionHandler;
	
	@Override
	public void onDisable() {
		System.out.println("Disabling BossAssist");

	}

	@Override
	public void onEnable() {
		setupPermissions();
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
	
	public PetitionPlugin getPetition()
	{
		Plugin pe = getServer().getPluginManager().getPlugin("PetitionPlugin");
		if (pe == null || !(pe instanceof PetitionPlugin)) 
		{
			return null; // Maybe you want throw an exception instead
		}
		return (PetitionPlugin) pe;
	}
	
	private void setupPermissions() {
	    if (permissionHandler != null) {
	        return;
	    }
	    
	    Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
	    
	    if (permissionsPlugin == null) {
	        //log("Permission system not detected, defaulting to OP");
	        return;
	    }
	    
	    permissionHandler = ((Permissions) permissionsPlugin).getHandler();
	    //log("Found and will use plugin "+((Permissions)permissionsPlugin).getDescription().getFullName());
	}

}
