package me.neoblade298.neodisenchant;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public void onEnable() {
		getLogger().info("Disenchanter has been Enabled");
		Config myConfig = new Config(this);
		myConfig.reloadCustomConfig();
		myConfig.saveCustomConfig();

		myConfig.getCustomConfig();
		String levelCost = myConfig.getCustomConfig().getString("levelCost");
		Handler myHandler = new Handler(myConfig, levelCost);
		getCommand("disenchant").setExecutor(myHandler);
		getCommand("disreload").setExecutor(myHandler);
	}

	public void onDisable() {
		getLogger().info("Disenchanter has been Disabled");
	}
}
