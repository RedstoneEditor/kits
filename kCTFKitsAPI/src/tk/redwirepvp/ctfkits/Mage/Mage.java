package tk.redwirepvp.ctfkits.Mage;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import tk.redwirepvp.ctfkits.Main;
import tk.redwirepvp.ctfkits.utils.FireworkEffectPlayer;

public class Mage {
	private Main p;

	public Mage(Main i) {
		p = i;
	}

	public FireworkEffectPlayer fep = new FireworkEffectPlayer();

	@SuppressWarnings("deprecation")
	public void giveKit(Player p) {
		PlayerInventory pi = p.getInventory();
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		// chestplate
		ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		this.p.itemUtil.setColor(chest, 0x927AA9);
		this.p.itemUtil.setName(chest, ChatColor.GREEN + "Chesticle Plate");
		this.p.itemUtil.setLore(chest, new String[] { "Covers your",
				"breasticles" });
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		pi.setChestplate(chest);
		// leggings
		ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		this.p.itemUtil.setColor(legs, 0x927AA9);
		this.p.itemUtil.setName(legs, ChatColor.GREEN + "Balloom Pants");
		this.p.itemUtil.setLore(legs, new String[] { "Make room for",
				"your balls" });
		legs.addEnchantment(Enchantment.PROTECTION_FIRE, 1);
		pi.setLeggings(legs);
		// boots
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
		this.p.itemUtil.setColor(boots, 0x927AA9);
		this.p.itemUtil.setName(boots, ChatColor.GREEN + "Shoes BITCH!");
		this.p.itemUtil.setLore(boots, new String[] { "Protect those",
				"lil thangs" });
		boots.addEnchantment(Enchantment.PROTECTION_FALL, 1);
		pi.setBoots(boots);
		// damage spell
		ItemStack damage = new ItemStack(Material.DIAMOND_HOE, 1);
		this.p.itemUtil.setName(damage, ChatColor.DARK_PURPLE + "Damage Spell");
		pi.addItem(damage);
		// fire spell
		ItemStack fire = new ItemStack(Material.WOOD_HOE, 1);
		this.p.itemUtil.setName(fire, ChatColor.RED + "Fire Spell");
		pi.addItem(fire);
		// lightning spell
		ItemStack thor = new ItemStack(Material.STONE_HOE, 1);
		this.p.itemUtil.setName(thor, ChatColor.YELLOW + "Lightning Spell");
		pi.addItem(thor);
		// freeze spell
		ItemStack freeze = new ItemStack(Material.IRON_HOE, 1);
		this.p.itemUtil.setName(freeze, ChatColor.BLUE + "Freeze Spell");
		pi.addItem(freeze);
		// heal spell
		ItemStack heal = new ItemStack(Material.GOLD_HOE, 1);
		this.p.itemUtil.setName(heal, ChatColor.GREEN + "Heal Spell");
		pi.addItem(heal);
		p.updateInventory();
		p.sendMessage(ChatColor.GREEN + "You are now a Mage");
		System.out.println("setting xp");
		p.setExp(1F);
		System.out.println(p.getExp());
	}

	public void fireArrows(World world, Player player) {
		Projectile arrow = player.launchProjectile(Arrow.class);
		Vector vec = player.getLocation().getDirection();
		arrow.setVelocity(new Vector(vec.getX() * 5, vec.getY() * 5,
				vec.getZ() * 5));
	}
}
