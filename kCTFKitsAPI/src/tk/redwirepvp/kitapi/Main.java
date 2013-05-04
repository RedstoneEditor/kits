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
	private HashMap<String, String> players = new HashMap<String, String>();// Player
																			// name,
																			// Kit
																			// name
	public void registerCommand(String s){
        CCommand cmd = new CCommand(s);
        cmap.register("", cmd);
        cmd.setExecutor(this);
	}
	public void onEnable() {
		plugin = this;
		log = getLogger();
		cm = new ClassManager(this);
	       try{
	            if(Bukkit.getServer() instanceof CraftServer){
	                final Field f = CraftServer.class.getDeclaredField("commandMap");
	                f.setAccessible(true);
	                cmap = (CommandMap)f.get(Bukkit.getServer());
	            }
	        } catch (Exception e){
	            e.printStackTrace();
	        }
	        
	    }
	    public CommandMap getCommandMap(){
	        return cmap;
	    }
	    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
	        cm.setKit((Player)sender, label);
	        return true;
	    }
	    
	    public class CCommand extends Command{
	        
	        private CommandExecutor exe = null;
	 
	        protected CCommand(String name) {
	            super(name);
	        }
	 
	        public boolean execute(CommandSender sender, String commandLabel,String[] args) {
	            if(exe != null){
	                exe.onCommand(sender, this, commandLabel,args);
	            }
	            return false;
	        }
	        
	        public void setExecutor(CommandExecutor exe){
	            this.exe = exe;
	        }
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
}
