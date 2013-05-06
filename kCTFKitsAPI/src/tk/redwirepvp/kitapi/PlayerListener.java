package tk.redwirepvp.kitapi;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {
	private Main plugin;

	public PlayerListener(Main instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (plugin.players.containsKey(event.getEntity().getName())) {
			event.getDrops().clear();
			event.setDeathMessage(null);
		}
	}

	@EventHandler
	public void onPlayerEatSteak(PlayerInteractEvent event) {
		if (plugin.players.containsKey(event.getPlayer().getName())) {
			if (event.getAction() == Action.RIGHT_CLICK_AIR
					|| event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (event.getPlayer().getItemInHand().getType() == Material.COOKED_BEEF) {
					this.plugin
							.getServer()
							.getScheduler()
							.scheduleSyncDelayedTask(
									this.plugin,
									new ItemRemoveThread(event.getPlayer(),
											new ItemStack(Material.COOKED_BEEF,
													1)), 1L);
					event.getPlayer().setHealth(
							event.getPlayer().getHealth() + 8 <= 20 ? event
									.getPlayer().getHealth() + 8 : 20);

				}
			}
		}
	}

	@EventHandler
	public void regenFood(EntityRegainHealthEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (plugin.players.containsKey(p.getName())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void pickUpItem(PlayerPickupItemEvent event) {
		if (plugin.players.containsKey(event.getPlayer().getName()))
			event.setCancelled(true);

	}

	@EventHandler
	public void onXPChange(PlayerExpChangeEvent event) {
		if (plugin.players.containsKey(event.getPlayer().getName()))
			event.setAmount(0);
	}

	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (plugin.players.containsKey(p.getName())) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		if (plugin.players.containsKey(event.getPlayer().getName()))
			plugin.cm.giveKit(event.getPlayer());

	}
}
