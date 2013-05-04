package tk.redwirepvp.kitapi.api;

import org.bukkit.entity.Player;

import tk.redwirepvp.kitapi.Main;

public abstract class Kit {
	public Main plugin = Main.getInstance();
	public abstract void onEnable();
	public abstract String getName();
	public abstract String getVersion();
	public abstract String getAuthor();
	public abstract void giveKit(Player player);
}
