package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.integration.epicfight.EpicFightEvents;
import online.kingdomkeys.kingdomkeys.lib.KKRegistryObject;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionMagic;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.Optional;

public abstract class Magic implements KKRegistryObject {
	int tier;
	ResourceLocation name;
	boolean hasTargetSelector;
	String translationKey;
	ResourceLocation gmAbility;

	private MagicData data;

	public Magic(ResourceLocation registryName, boolean hasToSelect, ResourceLocation gmAbility) {
		this.name = registryName;
		this.hasTargetSelector = hasToSelect;
		this.gmAbility = gmAbility;
		translationKey = "magic." + registryName.getNamespace() + "." + registryName.getPath() + ".name";
	}

	public int getCasttimeTicks() {
		return data.getCasttime();
	}

	public String getTranslationKey() {
		return translationKey;//.replace(".name", 0 + ".name");
	}

	public double getCost(Player player) {
		PlayerData playerData = PlayerData.get(player);
		double cost = data.getCost();
		if (cost != 300)
			cost -= cost * playerData.getNumberOfAbilitiesEquipped(ModAbilities.MP_THRIFT) * 0.2;
		return Math.max(1, cost);
	}

	public float getDamageMult() {
		return data.getDmgMult();
	}

	public float getDamageMultMax() {
		return data.getDmgMultMax();
	}

	public float getRealDamageMult(Player player) {
		if (getMaxLevel() <= 1) {
			return getDamageMult();
		}

		PlayerData playerData = PlayerData.get(player);
		int localLevel = Utils.getMagicHighestLocalLevel(playerData.getEquippedMagics(), getRegistryName());

		float t = (float) (localLevel - 1) / (getMaxLevel() - 1);
		float base = getDamageMult();
		float max = getDamageMultMax();

		return base + (max - base) * t;
	}

	public boolean getHasToSelect() {
		return hasTargetSelector;
	}

	public boolean getMagicLockOn() {
		return data.getMagicLockOn();
	}

	public int getMaxExp() {
		return data.getMaxExp();
	}

	public int getMaxLevel() {
		return data.getMaxLevel();
	}

	public ResourceLocation getNextTier() {
		return data.getNextTier();
	}

	public ResourceLocation getMagicRC() {
		return data.getMagicRC();
	}

	public MagicData.SpellType getSpellType() {
		return data.getSpellType();
	}

	public boolean canInteract(MagicData.Interaction interaction) {
		return data != null && data.canInteract(interaction);
	}

	public Optional<Ability> getGMAbility() {
		if (gmAbility == null) return Optional.empty();
		return Optional.ofNullable(ModAbilities.registry.get(gmAbility));
	}

	public int getCooldownTicks(PlayerData casterData) {
		if (data == null) {
			return 0;
		}

		double cd = data.getCooldown() * (1 - casterData.getNumberOfAbilitiesEquipped(ModAbilities.ENDLESS_MAGIC) * 0.2);

		if (Utils.perMagicCooldown()) {
			cd *= ModConfigs.SERVER.perMagicCooldownMultiplier.get();
		}

		return Math.max((int) cd, 5);
	}

	public MagicData getMagicData() {
		return data;
	}

	public void setMagicData(MagicData data) {
		this.data = data;
	}

	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnEntity) {

	}

	public int getMagicLocalLevel(Player player) {
		PlayerData playerData = PlayerData.get(player);
		return Utils.getMagicHighestLocalLevel(playerData.getEquippedMagics(), getRegistryName());
	}

	/**
	 * If player and caster are different it means the magic was casted from a target selector to another player in the party
	 *
	 * @param player
	 * @param caster
	 * @param isRC
	 */
	public final void onUse(LivingEntity player, Player caster, LivingEntity lockOnEntity, boolean isRC) {
		PlayerData casterData = PlayerData.get(caster);
		float fullMPBlastMult = 1F;

		if (casterData.isAbilityEquipped(ModAbilities.FULL_MP_BLAST) && casterData.getMP() >= casterData.getMaxMP()) {
			int stacks = casterData.getNumberOfAbilitiesEquipped(ModAbilities.FULL_MP_BLAST);
			fullMPBlastMult = (float) (2F - Math.pow(0.5F, stacks));
		}
		if (!isRC) {
			casterData.addMagicUses(name, 1);
			casterData.remMP(getCost(caster));

			if (getMagicData() != null && getRCProb(casterData)) {
				if (getGMAbility().isPresent()) {
					ReactionMagic reactionCommand = (ReactionMagic) ModReactionCommands.registry.get(getMagicRC());

					if (reactionCommand != null) {
						int duration = (int) (reactionCommand.getDuration() + reactionCommand.getDuration() * (casterData.getNumberOfAbilitiesEquipped(ModAbilities.GRAND_MAGIC_EXTENDER) * 0.25F));

						reactionCommand.setMagic(getRegistryName());
						casterData.addReactionCommand(getMagicRC(), caster, duration);
					}
				}

				casterData.setMagicUses(name, 0);
				PacketHandler.sendTo(new SCSyncPlayerData(caster), (ServerPlayer) caster);
			}
		}

		int cd = getCooldownTicks(casterData);

		// Per-magic mode locks only this spell; the shared timer is left alone so the two modes cannot
		// leak into each other if the option is flipped mid-game.
		if (Utils.perMagicCooldown()) {
			casterData.setMagicCooldownTicks(getRegistryName(), cd);
		} else {
			casterData.setMagicCooldownTicks(cd);
		}

		if (casterData.isAbilityEquipped(ModAbilities.WIZARDS_RUSE)) { //Wizard's Ruse has a chance to heal the player based on the amount of stacked abilities and amount healed based on the cost of the ability
			double num = player.level().random.nextDouble();
			if (num < (0.25 + (0.125 * (casterData.getNumberOfAbilitiesEquipped(ModAbilities.WIZARDS_RUSE) - 1)))) {
				caster.heal((int) getCost(caster) / 2F);
			}
		}

		if (!casterData.isAbilityEquipped(ModAbilities.MAGIC_LOCK_ON)) {
			lockOnEntity = null;
		}

		playMagicCastSound(player, caster);
		Utils.castMagic cast = new Utils.castMagic(player, caster, fullMPBlastMult, lockOnEntity, this);
		casterData.setCastedMagic(cast);

		if (KingdomKeys.efmLoaded) {
			EpicFightEvents.playCastAnimation(caster, isProjectile());
		}

		PacketHandler.sendTo(new SCSyncPlayerData(caster), (ServerPlayer) caster);
	}

	public final void onUse(LivingEntity player, Player caster, LivingEntity lockOnEntity) {
		onUse(player, caster, lockOnEntity, false);
	}


	public Magic getNextTierMagic() {
		ResourceLocation next = getNextTier();
		if (next == null)
			return this;

		return ModMagic.registry.get(next);
	}

	public boolean isProjectile() {
		return false;
	}

	public abstract void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity);

	public abstract void playMagicCastSound(LivingEntity player, Player caster);

	private boolean getRCProb(PlayerData casterData) {
		int prob = casterData.getNumberOfAbilitiesEquipped(ModAbilities.GRAND_MAGIC_HASTE) * 10;

		if (gmAbility != null && casterData.isAbilityEquipped(gmAbility)) {
			prob += casterData.getNumberOfAbilitiesEquipped(gmAbility) * 10;
		}
		prob += (casterData.getMagicUses(name) - 1) * 5;
		double num = Math.random() * 100;
		return num <= prob;
	}

	@Override
	public ResourceLocation getRegistryName() {
		return name;
	}

}