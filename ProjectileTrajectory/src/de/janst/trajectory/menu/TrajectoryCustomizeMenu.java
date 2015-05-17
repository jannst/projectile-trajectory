package de.janst.trajectory.menu;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.darkblade12.particleeffect.ParticleEffect.ParticleProperty;

import de.janst.trajectory.calculator.CalculatorType;
import de.janst.trajectory.menu.api.ItemCreator;
import de.janst.trajectory.menu.api.MenuSheet;
import de.janst.trajectory.menu.api.SlotListener;
import de.janst.trajectory.playerhandling.PlayerObject;
import de.janst.trajectory.util.ParticleItems;
import de.janst.trajectory.util.RGBColor;

public class TrajectoryCustomizeMenu extends MenuSheet {

	private final PlayerObject playerObject;
	private final CalculatorType type;
	private boolean colorable = false;

	public TrajectoryCustomizeMenu(MenuSheet parent, PlayerObject playerObject, CalculatorType type) {
		super(parent.getPlugin(), "�6�l"+type.getName()+" trajectory", 9, parent);
		registerListener("base", new MainListener());
		this.playerObject = playerObject;
		this.type = type;
		initContents();
		updateInventory();
	}

	@Override
	public void initContents() {
		setContent(0, new ItemCreator("�c�lback", Material.BUCKET, 1).toItem());
		setContent(5, new ItemCreator("�6�lChoose particle", Material.REDSTONE, 1).toItem());
		setParticleItem();
		setEnabledItem();
		setColorItems();
		setDistanceItem();
	}

	private void setEnabledItem() {
		String title = playerObject.getConfig().isTrajectoryEnabled(type) ? "�4�lDisable "+type.getName().toLowerCase()+" trajectory" : "�2�lEnable "+type.getName().toLowerCase()+" trajectory";
		byte data = playerObject.getConfig().isTrajectoryEnabled(type) ? (byte)14 : (byte)13;
		setContent(1, new ItemCreator(title, Material.WOOL, 1, data, (short) 0).toItem());
	}
	
	public void setParticleItem() {
		ItemStack item = new ItemStack(ParticleItems.fromEffect(playerObject.getConfig().getTrajectoryParticle(type)).getItem());
		ItemMeta meta = item.getItemMeta();
		String name = meta.getDisplayName();
		meta.setDisplayName("�6�lSelected: " + name);
		item.setItemMeta(meta);
		setContent(4, item);
	}
	
	private void setDistanceItem() {
		ItemCreator creator = new ItemCreator("�6�lUpdate distance", Material.COMPASS, playerObject.getConfig().getDistanceLevel(type));
		creator.addLore("�aActual distance: �e" + playerObject.getConfig().getDistanceLevel(type)*2 + " Blocks");
		creator.addLore("�eInfo: with this option you can set the minimal distance");
		creator.addLore("�ebetween you and the displayed particles");
		setContent(2, creator.toItem());
	}
	
	public void setColorItems() {
		if(playerObject.getConfig().getTrajectoryParticle(type).hasProperty(ParticleProperty.COLORABLE)) {
			colorable = true;
			RGBColor color = playerObject.getConfig().getParticleColor(type);
			setContent(7, new ItemCreator("�6�lSelected: �a�l" + color.getDisplayName(), Material.WOOL, 1, color.getData(), (short)0).toItem());
			setContent(8, new ItemCreator("�a�lChoose particle color", Material.REDSTONE, 1).toItem());
		}
		else {
			colorable = false;
			setContent(7, null);
			setContent(8, null);
		}
			
	}
	
	
	
	private class MainListener implements SlotListener {

		@Override
		public void clickSlot(InventoryClickEvent event) {
			if(event.getSlot() == 0) {
				shutMenu();
				getParent().show();
			}
			else if(event.getSlot() == 1) {
				playerObject.getConfig().setTrajectoryEnabled(type, !playerObject.getConfig().isTrajectoryEnabled(type));
				System.out.println(playerObject.getConfig().isTrajectoryEnabled(type));
				setEnabledItem();
				updateInventory();
			}
			else if(event.getSlot() == 2) {
				playerObject.getConfig().updateDistanceLevel(type);
				setDistanceItem();
				updateInventory();
			}
			else if(event.getSlot() == 5) {
				standby();
				new ParticleSelectMenu(TrajectoryCustomizeMenu.this, playerObject, type).show();
			}
			else if(event.getSlot() == 8 && colorable) {
				standby();
				new ColorSelectMenu(TrajectoryCustomizeMenu.this, playerObject, type).show();
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