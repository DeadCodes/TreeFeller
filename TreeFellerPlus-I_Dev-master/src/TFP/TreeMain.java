package TFP;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TreeMain extends JavaPlugin implements Listener{
	
	@Override
	public void onEnable() {
		super.onEnable();
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	/*
	 * Triggering block-cutter
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void a(BlockBreakEvent e) {
		if(e.isCancelled()) return;
		if(e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
		if(!this.isBlockCuttable(e.getBlock())) return;
		
		cutter(e.getBlock().getLocation(), e.getPlayer());
	}

	
	/*
	 * Cutting a block every tick (recursively)
	 */
	private void cutter(Location blocklocation, Player player) {
		for(Location woodlocation : checker(blocklocation)) {
			
			Bukkit.getScheduler().runTaskLater(this, new Runnable() {
				@Override
				public void run() {
					woodlocation.getBlock().breakNaturally();
					cutter(woodlocation, player);
				}
			}, 1L);
		}
	}

	
	/*
	 * Checking in a 3 block diameter if there is a block with the name "LOG" in it
	 */
	private ArrayList<Location> checker(Location loc) {
		ArrayList<Location> locs = new ArrayList<>();
		for(int x = -1; x <= 1; x = x + 1) {
			for(int y = 0; y <= 1; y = y + 1) {
				for(int z = -1; z <= 1; z = z + 1) {
					if(this.isBlockCuttable(loc.clone().add(x, y, z).getBlock())) {
						locs.add(loc.clone().add(x, y, z));
					}
				}
			}
		}
		return locs;
	}

	/*
	 * Check if block is allowed to be cut by cutter
	 */
	private boolean isBlockCuttable(Block block) {
		return block.getType().toSTring().matches("(LOG)|(WOOD)|(STEM)|(HYPHAE)");
	}
}
