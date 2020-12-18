package org.ethverse.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
//import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
//import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerRespawnEvent;
//import org.bukkit.event.block.Action;
//import org.bukkit.event.player.PlayerInteractEvent;
//import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
//import org.bukkit.potion.PotionEffect;
//import org.bukkit.potion.PotionEffectType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.md_5.bungee.api.ChatColor;

public class ethvEvents implements Listener {
	
	public String Host = "http://10.12.96.4:86";
	
	
//	private Location spawn;

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
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent ev) {
        Player p = ev.getPlayer();
        Action action = ev.getAction();
        
//        if(ev.getHand() == EquipmentSlot.HAND && action == Action.RIGHT_CLICK_BLOCK) {
        if(action == Action.RIGHT_CLICK_BLOCK) {
        	
            Location blockLocation = ev.getClickedBlock().getLocation();
    		int x = blockLocation.getBlockX();
    		int y = blockLocation.getBlockY();
    		int z = blockLocation.getBlockZ();
        	
	        if(ev.getClickedBlock().getBlockData().getMaterial().equals(Material.STONE_BUTTON)) {
	        	Material mt = p.getLocation().getWorld().getBlockAt(x, y, z+1).getType();
	    		if(!mt.equals(Material.AIR)) {
	    			p.getInventory().addItem(new ItemStack(mt, 1));
	    		}
	        } 
	        else if(ev.getClickedBlock().getBlockData().getMaterial().equals(Material.DARK_OAK_BUTTON)) {
	        	Material mt = p.getLocation().getWorld().getBlockAt(x, y, z-1).getType();
	    		if(!mt.equals(Material.AIR)) {
	    			p.getInventory().addItem(new ItemStack(mt, 1));
	    		}
	        }
        }
	}
	
	
	@EventHandler
	public void onRespawnPVPEVENT(PlayerRespawnEvent ev) {
	    Player p = ev.getPlayer();
	    
		String world = p.getWorld().getName();
		
		if(world.endsWith("_nether")) { world = world.replace("_nether",""); } 
		else if(world.endsWith("_the_end")) { world = world.replace("_the_end",""); }
	    
		Location location = new Location(Bukkit.getWorld(world), -344, 64, 20);
		p.setBedSpawnLocation(location);
	}
	
	
	@EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent ev) {
		Player p = ev.getPlayer();  

			
		String world = p.getWorld().getName();
		
		if(world.endsWith("_nether")) { world = world.replace("_nether",""); } 
		else if(world.endsWith("_the_end")) { world = world.replace("_the_end",""); }
	    
		Location location = new Location(Bukkit.getWorld(world), -344, 64, 20);
		p.setBedSpawnLocation(location);
//	      
//	      p.teleport(location); 
	      
		
//	      p.setHealth(1);
//	      p.setFoodLevel(10);
//	      p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 1));
//	      p.sendMessage(ChatColor.RED + "Durch die Schmerzen bist du Verwirrt!");
	      
	      return;
	}
	
	@EventHandler()
	public void onPlayerDeathDrop(PlayerDeathEvent ev) {
		ev.getDrops().clear();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent ev) {
		Player p = ev.getPlayer();
		
		String world = p.getWorld().getName();
		
		if(world.endsWith("_nether")) { world = world.replace("_nether",""); } 
		else if(world.endsWith("_the_end")) { world = world.replace("_the_end",""); }
		
		Location location = new Location(Bukkit.getWorld(world), -344, 64, 20);
		p.teleport(location);
	}
	
	
	@EventHandler
    public void onPlayerLogin(PlayerLoginEvent ev) {
	    Player p = ev.getPlayer();
	    
	    String pid = p.getDisplayName();
		String uuid = p.getUniqueId().toString();
	    
	    try {
			Scanner scanner = new Scanner(new URL(Host+"/checkWhitelist?uuid="+uuid+"&pid="+pid).openStream());
			String data = scanner.nextLine();
			JSONObject json = (JSONObject) new JSONParser().parse(data);
			
    		String sts = json.get("status").toString();
			String res = json.get("result").toString();
//			
			if(new String("200").equals(sts)) {
//				p.setBedSpawnLocation(new Location(p.getWorld(), -344, 63, 20), true);
				ev.allow();
				
//				-344, 63, 20
				
				
			} else {
				ev.disallow(Result.KICK_WHITELIST, ChatColor.RED + res);
			    return;
			}
			
		} catch (IOException e) {
			ev.disallow(Result.KICK_WHITELIST, ChatColor.RED + "Something Went Wrong. Try Again Later !!");
			
			p.sendMessage("Something Went Wrong. Try Again Later !");
			e.printStackTrace();
			
			return;
		} catch (ParseException e) {
			ev.disallow(Result.KICK_WHITELIST, ChatColor.RED + "Something Went Wrong. Try Again Later !!");
		    
			p.sendMessage("Something Went Wrong. Try Again Later !!");
			e.printStackTrace();
			
			return;
		}
    }


	@EventHandler(priority = EventPriority.HIGHEST)
	public void blockPlace(BlockPlaceEvent ev) {
		Block block = ev.getBlock();
		Material material = block.getType();
		Player p = ev.getPlayer();
		
		String world = p.getWorld().getName();
		
		Location blockLocation = block.getLocation();
		int x = blockLocation.getBlockX();
		int y = blockLocation.getBlockY();
		int z = blockLocation.getBlockZ();
//		
		String pid = p.getDisplayName();
		String uuid = p.getUniqueId().toString();
		String blockMaterial = material.name();
		
		
		
        try {
			Scanner scanner = new Scanner(new URL(Host+"/buildBlock?uuid="+uuid+"&world="+world+"&pid="+pid+"&material="+blockMaterial+"&x="+x+"&y="+y+"&z="+z).openStream());
			String data = scanner.nextLine();
			JSONObject json = (JSONObject) new JSONParser().parse(data);
			
    		String sts = json.get("status").toString();
			String res = json.get("result").toString();
//			
			if(!new String("200").equals(sts)) {
				block.setType(Material.AIR);
				p.sendMessage(ChatColor.RED + res); 
			}
			
		} catch (IOException e) {
			block.setType(Material.AIR);
			
			p.sendMessage("Something Went Wrong. Try Again Later !!");
			e.printStackTrace();
		} catch (ParseException e) {
			block.setType(Material.AIR);
			
			p.sendMessage("Something Went Wrong. Try Again Later !!");
			e.printStackTrace();
		}
        
		return;
		
		
	}
	
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockCanBuild(BlockCanBuildEvent ev) {
		Block block = ev.getBlock();
		Player p = ev.getPlayer();
		Material material = block.getType();
		
		String world = p.getWorld().getName();
		
		Location blockLocation = block.getLocation();
		int x = blockLocation.getBlockX();
		int y = blockLocation.getBlockY();
		int z = blockLocation.getBlockZ();
		
		
		String pid = p.getDisplayName();
		String uuid = p.getUniqueId().toString();
		String blockMaterial = material.name();
        
        try {
			Scanner scanner = new Scanner(new URL(Host+"/canBuild?uuid="+uuid+"&world="+world+"&pid="+pid+"&material="+blockMaterial+"&x="+x+"&y="+y+"&z="+z).openStream());
			String data = scanner.nextLine();
			JSONObject json = (JSONObject) new JSONParser().parse(data);
			
    		String sts = json.get("status").toString();
			String res = json.get("result").toString();
//			
			if(new String("200").equals(sts)) {
				ev.setBuildable(true);
			} else {
				ev.setBuildable(false);
				p.sendMessage(ChatColor.RED + res); 
			}
			
			
		} catch (IOException e) {
			ev.setBuildable(false);
			
			p.sendMessage("Something Went Wrong. Try Again Later !!");
			e.printStackTrace();
		} catch (ParseException e) {
			ev.setBuildable(false);
			
			p.sendMessage("Something Went Wrong. Try Again Later !!");
			e.printStackTrace();
		}
        
		return;
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(BlockDamageEvent ev) {
        Block block = ev.getBlock();
        Player p = ev.getPlayer();
        Material material = block.getType();
		
		String world = p.getWorld().getName();

//        if (block == null) { return; }
//        if (!block.getType().equals(Material.CHEST)) { return; }
        
		
		Location blockLocation = block.getLocation();
		int x = blockLocation.getBlockX();
		int y = blockLocation.getBlockY();
		int z = blockLocation.getBlockZ();
//		
		String pid = p.getDisplayName();
		String uuid = p.getUniqueId().toString();
		String blockMaterial = material.name();
		
		
		
        try {
			Scanner scanner = new Scanner(new URL(Host+"/breakBlock?uuid="+uuid+"&world="+world+"&pid="+pid+"&material="+blockMaterial+"&x="+x+"&y="+y+"&z="+z).openStream());
			String data = scanner.nextLine();
			JSONObject json = (JSONObject) new JSONParser().parse(data);
			
    		String sts = json.get("status").toString();
			String res = json.get("result").toString();
//			
			if(new String("200").equals(sts)) {
				ev.setCancelled(false);
			} else {
				ev.setCancelled(true);
				p.sendMessage(ChatColor.RED + res); 
			}
			
			
		} catch (IOException e) {
			ev.setCancelled(true);
			
			p.sendMessage("Something Went Wrong. Try Again Later !!");
			e.printStackTrace();
		} catch (ParseException e) {
			ev.setCancelled(true);
			
			p.sendMessage("Something Went Wrong. Try Again Later !!");
			e.printStackTrace();
		}
        
		return;
    }
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent ev) {
        Block block = ev.getBlock();
        Player p = ev.getPlayer();
        Material material = block.getType();
		
		String world = p.getWorld().getName();

//        if (block == null) { return; }
//        if (!block.getType().equals(Material.CHEST)) { return; }
        
		
		Location blockLocation = block.getLocation();
		int x = blockLocation.getBlockX();
		int y = blockLocation.getBlockY();
		int z = blockLocation.getBlockZ();
//		
		String pid = p.getDisplayName();
		String uuid = p.getUniqueId().toString();
		String blockMaterial = material.name();
		
		
		
        try {
			Scanner scanner = new Scanner(new URL(Host+"/breakBlock?uuid="+uuid+"&world="+world+"&pid="+pid+"&material="+blockMaterial+"&x="+x+"&y="+y+"&z="+z).openStream());
			String data = scanner.nextLine();
			JSONObject json = (JSONObject) new JSONParser().parse(data);
			
    		String sts = json.get("status").toString();
			String res = json.get("result").toString();
//			
			if(new String("200").equals(sts)) {
				ev.setCancelled(false);
			} else {
				ev.setCancelled(true);
				p.sendMessage(ChatColor.RED + res); 
			}
			
			
		} catch (IOException e) {
			ev.setCancelled(true);
			
			p.sendMessage("Something Went Wrong. Try Again Later !!");
			e.printStackTrace();
		} catch (ParseException e) {
			ev.setCancelled(true);
			
			p.sendMessage("Something Went Wrong. Try Again Later !!");
			e.printStackTrace();
		}
        
		return;
    }
	
	@EventHandler
	public void InventoryClick(InventoryClickEvent ev){
	  Player p = (Player) ev.getWhoClicked();
	  
	  if(p == null) { return; }
	  
	  InventoryView inventory = p.getOpenInventory();
	  if (inventory.getTitle() == null) { return; }
	  
	  if (inventory.getTitle().equals(ChatColor.BLUE +  "ETHVerse Shopping")) {
//		  ev.getCurrentItem().getType().name();
		  
		  ev.setCancelled(true);
		  return;
	  }
	}
	
}
