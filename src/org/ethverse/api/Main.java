package org.ethverse.api;

import org.bukkit.plugin.java.JavaPlugin;
import org.ethverse.api.commands.ApiCommand;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		new ApiCommand(this);
		
		this.getServer().getPluginManager().registerEvents(new ethvEvents(), this);
	}
}
