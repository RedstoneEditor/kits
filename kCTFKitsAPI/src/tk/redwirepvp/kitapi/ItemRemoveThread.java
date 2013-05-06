package tk.redwirepvp.kitapi;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemRemoveThread
  implements Runnable
{
  private Player p;
  private ItemStack is;

  public ItemRemoveThread(Player player, ItemStack items)
  {
    this.p = player;
    this.is = items;
  }

  public void run()
  {
    this.p.getInventory().removeItem(new ItemStack[] { this.is });
  }
}