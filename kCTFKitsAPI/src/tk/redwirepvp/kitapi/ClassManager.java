package tk.redwirepvp.kitapi;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import tk.redwirepvp.kitapi.api.ExClassLoader;
import tk.redwirepvp.kitapi.api.Kit;

public class ClassManager {
	public Main plugin;

	public List<Kit> Classes = new ArrayList<Kit>();

	public ClassManager(Main pl) {
		this.plugin = pl;
		this.Classes.addAll(new ExClassLoader(this.plugin).load(this.plugin
				.getDataFolder() + File.separator + "classes"));
	}

	public void setKit(Player p, String kitname) {
		if (!p.isOnline()) {
			this.plugin.log.info("Player not online!!!");
			return;
		}

		String kit = "";
		Iterator<Kit> it = this.Classes.iterator();
		Kit a = null;
		while (it.hasNext()) {
			a = (Kit) it.next();
			if ((a != null) && (a.getName().equalsIgnoreCase(kitname))) {
				kit = kitname;
				if (plugin.players.containsKey(p.getName())) {
					plugin.players.remove(p.getName());
				}
				plugin.players.put(p.getName(), kit);
				giveKit(p);
				p.sendMessage(ChatColor.GREEN + "You are now the kit: " + kit);
			}

		}

		if (kit == "") {
			p.sendMessage(ChatColor.RED + "There's no kit named " + kitname);
			return;
		}

		if (!hasperm(p, kit)) {
			notClassYet(p, kit);
			return;
		}

	}

	private void notClassYet(Player p, String kit) {
		String s = "You don't have permission to use &k!";
		s = s.replace("&k", kit);
		p.sendMessage(s);
	}

	public void giveKit(Player p) {
		String cn = this.plugin.getPClass(p).toLowerCase();

		p.getInventory().clear();

		boolean done = false;

		for (Kit a : this.Classes) {
			if (a.getName().equalsIgnoreCase(cn)) {
				a.giveKit(p);
				done = true;
			}
		}

		if (!done) {
			p.sendMessage("Error: You are in no class!");
			this.plugin.log.warning("|Player " + p.getName()
					+ " should be in class " + cn + ", but he isn't!!!|");
			setKit(p, cn);
		}
		p.setGameMode(GameMode.SURVIVAL);

	}

	public boolean hasperm(Player p, String kit) {
		String perm = "kCTFKits." + kit;
		if (p.hasPermission(perm))
			return true;
		return false;
	}
}