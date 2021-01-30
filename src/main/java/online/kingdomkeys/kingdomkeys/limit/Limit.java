package online.kingdomkeys.kingdomkeys.limit;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public abstract class Limit extends ForgeRegistryEntry<Limit> {

	public static final ResourceLocation NONE = new ResourceLocation(KingdomKeys.MODID + ":none");

	String name;
	int order;
	int cost;
	int cooldown;

	String translationKey;

	OrgMember owner;

	public Limit(ResourceLocation registryName, int order, int cost, int cooldown, OrgMember owner) {
		this.name = registryName.toString();
		this.cost = cost;
		this.cooldown = cooldown;
		setRegistryName(registryName);
		this.order = order;
		this.owner = owner;
		translationKey = "limit." + getRegistryName().getPath() + ".name";
	}

	public Limit(String registryName, int order, int cost, int cooldown, OrgMember owner) {
		this(new ResourceLocation(registryName), order, cost, cooldown, owner);
	}

	public String getName() {
		return name;
	}

	public String getTranslationKey() {
		return translationKey;
	}

	public int getCost() {
		return cost;
	}
	
	public int getCooldown() {
		return cooldown;
	}

	public int getOrder() {
		return order;
	}
	
	public OrgMember getOwner() {
		return owner;
	}
	
	public abstract void onUse(PlayerEntity player, LivingEntity target);


	/*public void initDrive(PlayerEntity player) {
		if (!getRegistryName().equals(NONE)) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.setActiveDriveForm(getName());
			int cost = ModDriveForms.registry.getValue(new ResourceLocation(getName())).getDriveCost();
			playerData.remDP(cost);
			playerData.setFP(300 + playerData.getDriveFormLevel(playerData.getActiveDriveForm()) * 100);
			// Summon Keyblades
			playerData.setAntiPoints(playerData.getAntiPoints() + getFormAntiPoints());
			player.world.playSound(player, player.getPosition(), ModSounds.drive.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			pushEntities(player);
			PacketHandler.syncToAllAround(player, playerData);
		}
	}

	private void pushEntities(PlayerEntity player) {
		List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(4.0D, 3.0D, 4.0D));
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Entity e = (Entity) list.get(i);
				if (e instanceof LivingEntity) {
					double d = e.getPosX() - player.getPosX();
					double d1 = e.getPosZ() - player.getPosZ();
					((LivingEntity) e).knockBack(e, 1, -d, -d1);
					e.setMotion(e.getMotion().x, 0.7F, e.getMotion().z);
				}
			}
		}
	}

	public void updateDrive(PlayerEntity player) {
		double formDecrease = 0.2;
		if (!getRegistryName().equals(NONE)) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if (playerData.isAbilityEquipped(Strings.formBoost))
				formDecrease = .15;
			if (playerData.getFP() > 0) {
				playerData.setFP(playerData.getFP() - formDecrease);
			} else {
				endDrive(player);
			}
		}
		// Consume FP
		// Check if FP <= 0 then end
	}

	public void endDrive(PlayerEntity player) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setActiveDriveForm(Limit.NONE.toString());
		player.world.playSound(player, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
		if (!player.world.isRemote) {
			PacketHandler.syncToAllAround(player, playerData);
		}
	}*/

}