package de.janst.trajectory.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import de.janst.trajectory.TrajectorySimulator;
import de.janst.trajectory.calculator.CalculatorType;
import de.janst.trajectory.menu.api.ItemCreator;
import de.janst.trajectory.menu.api.MenuSheet;
import de.janst.trajectory.menu.api.SlotListener;
import de.janst.trajectory.playerhandling.PlayerObject;

public class MainMenu extends MenuSheet {

	private final PlayerObject playerObject;

	public MainMenu(TrajectorySimulator plugin, Player player) {
		super(plugin, "�2�lTrajectory Menu", 9, player);
		this.playerObject = plugin.getPlayerHandler().getPlayerObject(player.getUniqueId());
		registerListener("base", new MainListener());
		initContents();
	}

	@Override
	public void initContents() {
		setEnabledItem();
		setContent(0, new ItemCreator("�4�lClose menu", Material.BUCKET, 1).toItem());
		setContent(4, new ItemCreator("�6�lCustomize Arrow trajectory", Material.BOW, 1).toItem());
		ItemStack stack = new ItemCreator("�6�lCustomize Potion trajectory", Material.POTION, 1).toItem();
		Potion potion = new Potion(PotionType.FIRE_RESISTANCE);
		potion.setSplash(true);
		potion.apply(stack);
		setContent(5, stack);
		setContent(6, new ItemCreator("�6�lCustomize Snowball trajectory", Material.SNOW_BALL, 1).toItem());
		setContent(7, new ItemCreator("�6�lCustomize Egg trajectory", Material.EGG, 1).toItem());
		setContent(8, new ItemCreator("�6�lCustomize Enderpearl trajectory", Material.ENDER_PEARL, 1).toItem());
		updateInventory();
	}
	
	private void setEnabledItem() {
		String title = playerObject.getConfig().isEnabled() ? "�4�lDisable trajectory" : "�2�lEnable trajectory";
		byte data = playerObject.getConfig().isEnabled() ? (byte)14 : (byte)13;
		setContent(1, new ItemCreator(title, Material.WOOL, 1, data, (short) 0).toItem());
	}
	
	private class MainListener implements SlotListener {

		@Override
		public void clickSlot(InventoryClickEvent event) {
			int slot = event.getSlot();
			if(event.getSlot() == 0) {
				shutMenuConstruct();
			}
			else if(event.getSlot() == 1) {
				playerObject.getConfig().setEnabled(!playerObject.getConfig().isEnabled());
				setEnabledItem();
				updateInventory();
			}
			else if(slot == 4) {
				standby();
				new TrajectoryCustomizeMenu(MainMenu.this, playerObject, CalculatorType.ARROW).show();
			}
			else if(slot == 5) {
				standby();
				new TrajectoryCustomizeMenu(MainMenu.this, playerObject, CalculatorType.POTION).show();
			}
			else if(slot == 6) {
				standby();
				new TrajectoryCustomizeMenu(MainMenu.this, playerObject, CalculatorType.SNOWBALL).show();
			}
			else if(slot == 7) {
				standby();
				new TrajectoryCustomizeMenu(MainMenu.this, playerObject, CalculatorType.EGG).show();
			}
			else if(slot == 8) {
				standby();
				new TrajectoryCustomizeMenu(MainMenu.this, playerObject, CalculatorType.ENDERPEARL).show();
			}
		}

		@Override
		public void leftClick(InventoryClickEvent event) {
		}

		@Override
		public void rightClick(InventoryClickEvent event) {
		}

		@Override
		public void shiftClick(InventoryClickEvent event) {
		}
		
	}

}