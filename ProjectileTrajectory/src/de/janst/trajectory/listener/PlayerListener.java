package de.janst.trajectory.listener;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.janst.trajectory.TrajectorySimulator;
import de.janst.trajectory.calculator.TrajectoryCalculator;
import de.janst.trajectory.util.ItemChecker;
import de.janst.trajectory.util.Permission;

public class PlayerListener implements Listener {

	private final TrajectorySimulator trajectorySimulator;

	public PlayerListener(TrajectorySimulator trajectorySimulator) {
		this.trajectorySimulator = trajectorySimulator;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onItemChange(PlayerItemHeldEvent event) {
		if(!event.isCancelled() && event.getPlayer().hasPermission(Permission.USE.getString())) {
			TrajectoryCalculator calculator = ItemChecker.ITEM_CHECKER.checkItem(event.getNewSlot(), event.getPlayer().getInventory());
			
			if(calculator != null) {
				trajectorySimulator.getTrajectoryScheduler().addCalculator(event.getPlayer().getUniqueId(), calculator);
			}
			else if(trajectorySimulator.getTrajectoryScheduler().hasCalculator(event.getPlayer().getUniqueId())) {
				trajectorySimulator.getTrajectoryScheduler().removeCalculator(event.getPlayer().getUniqueId());
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getWhoClicked().hasPermission(Permission.USE.getString())) {
			trajectorySimulator.getInventoryScheduler().addPlayer(event.getWhoClicked().getUniqueId());
		}
	}

	@EventHandler
	public void onDrag(InventoryDragEvent event) {
		if(event.getWhoClicked().hasPermission(Permission.USE.getString())) {
			trajectorySimulator.getInventoryScheduler().addPlayer(event.getWhoClicked().getUniqueId());
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		try {
			trajectorySimulator.getPlayerHandler().addPlayer(event.getPlayer());
		} catch (IOException e) {
			trajectorySimulator.getLogger().log(Level.SEVERE, "Could not add player " + event.getPlayer().getName());
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		trajectorySimulator.getPlayerHandler().removePlayer(event.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if(event.getPlayer().hasPermission(Permission.USE.getString())) {
			trajectorySimulator.getInventoryScheduler().addPlayer(event.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onThrow(ProjectileLaunchEvent event) {
		if(event.getEntity().getShooter() instanceof Player && !event.isCancelled()) {
			Player player = (Player) event.getEntity().getShooter();
			if(player.hasPermission(Permission.USE.getString())) {
				if(player.getItemInHand() == null || player.getItemInHand().getAmount() > 0)
					return;
				Material material = player.getItemInHand().getType();
				if(material != null && (material == Material.SNOW_BALL || material == Material.ENDER_PEARL || material == Material.EGG || material == Material.POTION)) {
					trajectorySimulator.getTrajectoryScheduler().removeCalculator(player.getUniqueId());
				}
			}
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event) {
		trajectorySimulator.getPlayerHandler().removePlayer(event.getPlayer().getUniqueId());
	}
}
