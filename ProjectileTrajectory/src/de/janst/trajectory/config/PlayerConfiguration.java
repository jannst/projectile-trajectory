package de.janst.trajectory.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.darkblade12.particleeffect.ParticleEffect;
import com.darkblade12.particleeffect.ParticleEffect.OrdinaryColor;

import de.janst.trajectory.calculator.CalculatorType;
import de.janst.trajectory.util.RGBColor;

public class PlayerConfiguration extends Configuration {

	public static boolean SAVE_INSTANT;
	private final Map<CalculatorType, ParticleEffect> particles = new HashMap<CalculatorType, ParticleEffect>();
	private final Map<CalculatorType, OrdinaryColor> particleColors = new HashMap<CalculatorType, OrdinaryColor>();
	
	public PlayerConfiguration(UUID uuid) {
		super("/players/" + uuid.toString() + ".yml" , false);
		
		config.addDefaults(PlayerConfigurationDefaults.DEFAULTS.getDefaults());
		if(changes) {
			save();
		}
	}
	
	public boolean isEnabled() {
		return config.getBoolean("enabled");
	}
	
	public void setEnabled(boolean enabled) {
		config.set("enabled", enabled);
		setChanges(true);
	}
	
	public boolean isTrajectoryEnabled(CalculatorType type) {
		return config.getBoolean(type.getName()+".enabled");
	}
	
	public void setTrajectoryEnabled(CalculatorType type, boolean enabled) {
		config.set(type.getName()+".enabled", enabled);
		setChanges(true);
	}
	
	public void setTrajectoryParticle(ParticleEffect particle, CalculatorType type) {
		particles.put(type, particle);
		config.set(type.getName()+".particle", particle.getName());
		setChanges(true);
	}
	
	public ParticleEffect getTrajectoryParticle(CalculatorType type) {
		if(particles.containsKey(type)) {
			return particles.get(type);
		}
		else {
			ParticleEffect effect = ParticleEffect.fromName(config.getString(type.getName()+".particle"));
			particles.put(type, effect);
			return effect;
		}
	}
	
	public void setParticleColor(RGBColor color, CalculatorType type) {
		particleColors.put(type, new OrdinaryColor(color.getRed(), color.getGreen(), color.getBlue()));
		config.set(type.getName()+".color", color.getName());
		setChanges(true);
	}
	
	public OrdinaryColor getOrdinaryParticleColor(CalculatorType type) {
		if(particleColors.containsKey(type)) {
			return particleColors.get(type);
		}
		else {
			RGBColor color = RGBColor.fromName(config.getString(type.getName()+".color"));
			OrdinaryColor ordinaryColor = new OrdinaryColor(color.getRed(), color.getGreen(), color.getBlue());
			particleColors.put(type, ordinaryColor);
			return ordinaryColor;
		}
	}
	
	public RGBColor getParticleColor(CalculatorType type) {
		return RGBColor.fromName(config.getString(type.getName()+".color"));
	}
	
	public void updateDistanceLevel(CalculatorType type) {
		int level = getDistanceLevel(type);
		if(level+1 > 10) 
			level = 0;
		else 
			level++;
		config.set(type.getName()+".distance-level", level);
		setChanges(true);
	}
	
	public int getDistanceLevel(CalculatorType type) {
		return config.getInt(type.getName()+".distance-level");
	}
	
	public void setChanges(boolean b) {
		changes = b;
		if(changes && SAVE_INSTANT) {
			save();
		}
	}
}