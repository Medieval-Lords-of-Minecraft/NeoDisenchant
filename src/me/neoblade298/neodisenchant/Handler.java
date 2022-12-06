package me.neoblade298.neodisenchant;

import java.util.ArrayList;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class Handler implements CommandExecutor {
	private int levelCost;
	private Config config;

	public Handler(Config config, String levelCostStr) {
		this.levelCost = Integer.parseInt(levelCostStr);
		this.config = config;
	}

	public boolean isQuestGear(ItemStack item) {
		return item.hasItemMeta() && item.getItemMeta().hasLore()
				&& item.getItemMeta().getLore().get(0).contains("Tier");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		ItemStack itemInHand = player.getInventory().getItemInMainHand();
		if (cmd.getName().equalsIgnoreCase("disreload")) {
			if (player.hasPermission("disenchant.reload")) {
				player.sendMessage("§3[Disenchant]§f Reloaded Config File!");
				this.config.reloadCustomConfig();
				this.levelCost = Integer.parseInt(this.config.getCustomConfig().getString("levelCost"));
				return true;
			}
			player.sendMessage("§3[Disenchant]§f You don't have permission for this");

			return true;
		}
		if ((cmd.getName().equals("disenchant")) && (!itemInHand.getEnchantments().isEmpty())
				&& (itemInHand.getType() != Material.BOOK)) {
			if (!isQuestGear(itemInHand)) {
				if (player.hasPermission("disenchant.true")) {
					if (player.getLevel() < this.levelCost) {
						player.sendMessage(
								"§3[Disenchant]§f You don't have enough levels for this, you need " + this.levelCost);
					}
					else {

						Map<Enchantment, Integer> enchantments = itemInHand.getEnchantments();

						player.setLevel(player.getLevel() - this.levelCost);
						ArrayList<Enchantment> toRemove = new ArrayList<Enchantment>();
						for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
							toRemove.add(entry.getKey());
							ItemStack enchantBook = new ItemStack(Material.ENCHANTED_BOOK, 1);
							Enchantment enchant = (Enchantment) entry.getKey();
							int level = ((Integer) entry.getValue()).intValue();

							player.getInventory()
									.addItem(new ItemStack[] { addBookEnchantment(enchantBook, enchant, level) });
						}
						
						for (Enchantment ench : toRemove) {
							itemInHand.removeEnchantment(ench);
						}
						player.sendMessage("§3[Disenchant]§f Your item has been disenchanted!");
					}
				}
				else {
					player.sendMessage("§3[Disenchant]§f You do not have the correct permissions for this");
				}
			}
			else {
				player.sendMessage("§3[Disenchant]§f You cannot disenchant quest gear");
			}
			return true;
		}
		player.sendMessage("§3[Disenchant]§f Please only disenchant armour or weapons");

		return false;
	}

	public ItemStack addBookEnchantment(ItemStack item, Enchantment enchantment, int level) {
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(enchantment, level, true);
		item.setItemMeta(meta);
		return item;
	}
}
