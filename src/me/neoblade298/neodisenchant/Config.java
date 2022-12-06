package me.neoblade298.neodisenchant;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
	public FileConfiguration customConfig = null;
	public File customConfigFile = null;
	private Plugin plugin;

	public Config(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void reloadCustomConfig() {
		if (this.customConfigFile == null) {
			this.customConfigFile = new File(this.plugin.getDataFolder(), "config.yml");
		}
		this.customConfig = YamlConfiguration.loadConfiguration(this.customConfigFile);

		Reader defConfigStream = new InputStreamReader(this.plugin.getResource("config.yml"));
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			this.customConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getCustomConfig() {
		if (this.customConfig == null) {
			reloadCustomConfig();
		}
		return this.customConfig;
	}

	public void saveCustomConfig() {
		if ((this.customConfig == null) || (this.customConfigFile == null)) {
			return;
		}
		try {
			getCustomConfig().save(this.customConfigFile);
		} catch (IOException ex) {
			this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.customConfigFile, ex);
		}
	}

	public void saveDefaultConfig() {
		if (this.customConfigFile == null) {
			this.customConfigFile = new File(this.plugin.getDataFolder(), "config.yml");
		}
		if (!this.customConfigFile.exists()) {
			this.plugin.saveResource("config.yml", false);
		}
	}
}
