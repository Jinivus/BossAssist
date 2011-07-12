package jinivus.BossAssist;

import java.io.IOException;
import java.sql.Connection;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.floyd.bukkit.petition.PetitionPlugin;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class BACommand implements CommandExecutor
{
	private final BossAssist plugin;
	public BACommand(BossAssist plugin)
	{
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] split)
	{
		WorldGuardPlugin wg = plugin.getWorldGuard();
		WorldEditPlugin we = plugin.getWorldEdit();
		PetitionPlugin pe = plugin.getPetition();
		Connection dbh = null;
		//Make sure the command is being executed by a player, not console
		if (!(sender instanceof Player)) 
		{
			sender.sendMessage(ChatColor.RED + "You are not a player!");
			return false;
		}
		Player player = (Player) sender;
		if(!plugin.permissionHandler.has(player, "bossassist.define"))
		{
			player.sendMessage(ChatColor.RED + "You do not have permission to do that.");
			return true;
		}
		//if they just used /ba, we want it to tell them the arguments
		if(split.length == 0)
		{
			sender.sendMessage(ChatColor.RED + "Usage:");
			return false;
		}
		else if (split.length == 2) //if has the right amount of arguments
		{
			String name = split[0];
			String type = split[1];
			String[] names = name.split(",");
			String id = names[0]+"-"+type;

			Selection sel = we.getSelection(player);
			if (sel == null) {
				player.sendMessage(ChatColor.RED + "Make a selection with WorldEdit first.");
			}

			ProtectedRegion region;

			if (sel instanceof CuboidSelection) { //If the WorldGuard selection is a cuboid
				BlockVector min = sel.getNativeMinimumPoint().toBlockVector();
				BlockVector max = sel.getNativeMaximumPoint().toBlockVector();
								
				RegionManager mgr = wg.getGlobalRegionManager().get(sel.getWorld()); //gets WorldGuard Region Manager for world of the selection
				
				// Checking if a region with the same ID already exists, if so append a number starting with 2
				// Also checks whether the region with appended numbers counting onward exist.
				if(mgr.hasRegion(id))
				{
					int suffixID = 1;
					String OriginalID = id;
					while (mgr.hasRegion(id))
					{
						suffixID++;
						id = OriginalID+Integer.toString(suffixID);
					}
				}
				
				//Create the region holder, not saving it yet
				region = new ProtectedCuboidRegion(id, min, max);
				
				//Add owners specified in "names" array
				for (int i =0;i < names.length;i++)
				{
					region.getOwners().addPlayer(names[i]);
				}
				
				//add region to region manager
				mgr.addRegion(region);
				//attempt to save regionmanager to file.
				try {
					mgr.save();
					sender.sendMessage(ChatColor.YELLOW + "Region saved as " + id + ".");
					String[] herp = ("create Review region "+id).split(" ");
					if (pe.dbpool != null) { dbh = pe.dbpool.getConnection(); }
					pe.performOpen(player, herp, dbh);
					if (pe.dbpool != null) { pe.dbpool.releaseConnection(dbh); }
					return true;
				} catch (IOException e) {
					throw new CommandException("Failed to write regions file: "
							+ e.getMessage());
				}
			} 


		}	
		//wg.getGlobalRegionManager().get(player.getWorld());
		return false;

	}
}

