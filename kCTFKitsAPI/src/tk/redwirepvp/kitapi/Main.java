package tk.redwirepvp.kitapi;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_5_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private static Main plugin;
	public static Logger log;
	public ClassManager cm;
	private static CommandMap cmap;
	public HashMap<String, String> players = new HashMap<String, String>();// Player
																			// name,
																			// Kit
																			// name
	public static CommandMap commandMap = null;
	static {
		try {
			if (Bukkit.getServer() instanceof CraftServer) {
				final Field f = CraftServer.class
						.getDeclaredField("commandMap");
				f.setAccessible(true);
				commandMap = (CommandMap) f.get(Bukkit.getServer());
			}
		} catch (final SecurityException e) {
			System.out.println("Please disable the security manager");
		} catch (final Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void registerCommand(String s) {

		Command c = new Command(s) {

			@Override
			public boolean execute(CommandSender arg0, String arg1,
					String[] arg2) {

			Main.this.cm.setKit((Player)arg0, this.getName());

				return false;
			}
		};
		c.setDescription("Your command description");
		commandMap.register("/", c);

	}

	public void onEnable() {
		plugin = this;
		log = getLogger();
		cm = new ClassManager(this);
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
	}

	public void onDisable() {

	}

	public static Main getInstance() {
		return plugin;
	}

	public String getPClass(Player p) {
		if (this.players.containsKey(p.getName()))
			return (String) this.players.get(p.getName());
		return "";
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String tag, String[] args){
		if(cmd.getName().equalsIgnoreCase("quitkits")){
			if(sender instanceof Player){
				if(players.containsKey(sender.getName())){
					players.remove(sender.getName());
					
				}
			}
		}
		return false;
	}
}
