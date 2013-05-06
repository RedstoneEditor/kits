package tk.redwirepvp.ctfkits.Mage;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import tk.redwirepvp.ctfkits.Main;

public class MageListener implements Listener {
	private Main p;

	public MageListener(Main i) {
		p = i;
	}

	public int xpid;

	public HashMap<Arrow, Boolean> arrows = new HashMap<Arrow, Boolean>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void playerDamage(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			if (p.players.get(((Player) event.getEntity()).getName()) == "mage") {
				event.getDrops().clear();
				((Player) event.getEntity()).getInventory().clear();
				((Player) event.getEntity()).getInventory().setArmorContents(
						null);
				((Player)event.getEntity()).updateInventory();
				((Player) event.getEntity()).setHealth(20);
				((Player) event.getEntity()).setExhaustion(16);
				event.getEntity().teleport(
						event.getEntity().getWorld().getSpawnLocation());
				event.setDroppedExp(0);
				p.getServer().getPluginManager().callEvent(new PlayerRespawnEvent((Player)event.getEntity(), event.getEntity().getWorld().getSpawnLocation(), false));
			}
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event){
		if(p.players.get(((Player) event.getPlayer()).getName()) == "mage"){
			p.mage.giveKit((Player) event.getPlayer());
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			event.getPlayer().setExp(1F);
			System.out.println(event.getPlayer().getExp());
		}
	}
	@EventHandler
	public void onProjectileLaunch(final ProjectileLaunchEvent event) {
		System.out.println(event.getEntity().getShooter());
		if (p.players.get(((Player) event.getEntity()).getName()) == "mage") {
			arrows.put((Arrow) event.getEntity(), false);
			((Player) event.getEntity().getShooter()).setExp(0F);
			xpid = Bukkit.getScheduler().scheduleSyncRepeatingTask(p,
					new Runnable() {
						public void run() {
							if ((((Player) event.getEntity().getShooter())
									.getExp() < 1F)) {
								((Player) event.getEntity().getShooter()).setExp(((Player) event
										.getEntity().getShooter()).getExp() + 0.05F);
							} else {
								Bukkit.getScheduler().cancelTask(xpid);
								((Player) event.getEntity().getShooter())
										.setExp(1F);
							}
						}
					}, 0L, 1L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(p, new Runnable() {
				public void run() {
					if (arrows.containsKey(event.getEntity())
							&& arrows.get(event.getEntity()) == false) {
						event.getEntity().remove();
						playFirework(event.getEntity().getWorld(), event
								.getEntity().getLocation());
					}
				}
			}, 5L);
		}
	}

	@EventHandler
	public void rightClick(PlayerInteractEvent event) {
		System.out.println("debug");
		System.out.println(p.players);
		if(p.players.get(event.getPlayer().getName()) == "mage")
		if (event.getAction() == Action.RIGHT_CLICK_AIR
				|| event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			System.out.println("debug2");
			System.out.println(p.players);
			if (event.getPlayer().getItemInHand().getType() == Material.DIAMOND_HOE) {
				System.out.println("debug3");
				System.out.println(event.getPlayer().getExp());
				if (event.getPlayer().getExp() >= 1F) {
					System.out.println("debug4");
					event.setCancelled(true);
					this.p.mage.fireArrows(event.getPlayer().getWorld(),
							event.getPlayer());
				}
			}
		}
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Entity entity = event.getEntity();
		System.out.println(((Arrow) entity).getShooter());
		if (entity instanceof Arrow) {
			if (((Arrow) entity).getShooter() instanceof Player) {
				Player player = (Player) ((Arrow) entity).getShooter();
				if (p.players.get(((Player) event.getEntity().getShooter()).getName()) == "mage") {
					System.out.println(player.getName());
					Arrow arrow = (Arrow) event.getEntity();
					arrow.remove();
					arrows.remove(arrow);
					playFirework(event.getEntity().getWorld(), event
							.getEntity().getLocation());
				}
			}
		}
	}

	private void playFirework(World world, Location location) {
		try {
			p.mage.fep.playFirework(world, location, getRandomEffect());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static FireworkEffect getRandomEffect() {
		return FireworkEffect.builder().with(Type.BALL).withColor(Color.RED)
				.withColor(Color.PURPLE).build();
	}

}
