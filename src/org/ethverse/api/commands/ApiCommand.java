package org.ethverse.api.commands;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ethverse.api.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


public class ApiCommand implements CommandExecutor {
	
	public String Host = "http://10.12.96.4:86";
	
	@SuppressWarnings("unused")
	private Main plugin;
	
	public ApiCommand(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("ethv").setExecutor(this);
	}

	
	public String getText(String url) throws IOException {
	    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
	    //add headers to the connection, or check the status if desired..
	    
	    // handle error response code it occurs
	    int responseCode = connection.getResponseCode();
	    InputStream inputStream;
	    if (200 <= responseCode && responseCode <= 299) {
	        inputStream = connection.getInputStream();
	    } else {
	        inputStream = connection.getErrorStream();
	    }

	    BufferedReader in = new BufferedReader(
	        new InputStreamReader(
	            inputStream));

	    StringBuilder response = new StringBuilder();
	    String currentLine;

	    while ((currentLine = in.readLine()) != null) 
	        response.append(currentLine);

	    in.close();

	    return response.toString();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Only Players may execute this command!");
		}
		
		Player p = (Player) sender;
	
		String pid = p.getDisplayName();
		String uuid = p.getUniqueId().toString();
		String argStr = String.join(",", args);
		
		if(args.length > 0 && new String("shop").equals(args[0])) {
			openGUI(p);
		}
		
		try {
			Scanner scanner = new Scanner(new URL(Host+"/?uuid="+uuid+"&pid="+pid+"&comm="+label+"&args="+argStr).openStream());
			String data = scanner.nextLine();
			JSONObject json = (JSONObject) new JSONParser().parse(data);
			
			JSONArray jsonarray = (JSONArray) json.get("data");
			int banListSize = jsonarray.size();
			
			for (int i = 0; i < banListSize; i++) {
				JSONObject json1 = (JSONObject) jsonarray.get(i);
				
				String toMsg = json1.get("to").toString();
				String txt = json1.get("text").toString();
				String hover = json1.get("hover").toString();
				String copy = json1.get("copy").toString();
				String link = json1.get("link").toString();
		        
		        
				TextComponent message = new TextComponent( txt );
				
				if(!new String("").equals(copy)) {
		        	 message.setClickEvent( new ClickEvent( ClickEvent.Action.COPY_TO_CLIPBOARD, copy ) );
				}
				
				if(!new String("").equals(hover)) {
		        	 message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( hover ).create() ) );
				}
				
				if(!new String("").equals(link)) {
		        	 message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, link ) );
				} 
				
				
				if(new String("self").equals(toMsg)) {
					p.spigot().sendMessage(message);
				} 
				else if(new String("all").equals(toMsg)) {
					Bukkit.spigot().broadcast(message);
				} 
				else {
					Player pt = (Player) Bukkit.getServer().getPlayer(toMsg);
					
					if(pt.isOnline()) {
						pt.spigot().sendMessage(message);
					}
				}
			}
			
		} catch (IOException e) {
			p.sendMessage("Something Went Wrong. Try Again Later !");
			e.printStackTrace();
		} catch (ParseException e) {
			p.sendMessage("Something Went Wrong. Try Again Later !");
			e.printStackTrace();
		}
		
		
		
		return true;
	}
	
	
	
	private void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE +  "ETHVerse Shopping");
       
        ItemStack survival = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta survivalMeta = survival.getItemMeta();
        ItemStack creative = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta creativeMeta = creative.getItemMeta();
       
        survivalMeta.setDisplayName("Survival");
        survival.setItemMeta(survivalMeta);
        creativeMeta.setDisplayName("Creative");
        creative.setItemMeta(creativeMeta);
       
        inv.setItem(3, survival);
        inv.setItem(5, creative);
       
        player.openInventory(inv);
    }
	
	
	
}
