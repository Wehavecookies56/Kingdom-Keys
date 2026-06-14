package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionMagic;
import online.kingdomkeys.kingdomkeys.util.Utils;

public abstract class Magic {
	int tier;
	ResourceLocation name;
	boolean hasTargetSelector;
	String translationKey;
	String gmAbility;

	private MagicData data;

	public Magic(ResourceLocation registryName, boolean hasToSelect, String gmAbility) {
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
		if (cost != 300) cost -= cost * playerData.getNumberOfAbilitiesEquipped(Strings.mpThrift) * 0.2;
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
		int localLevel = Utils.getMagicHighestLocalLevel(playerData.getEquippedMagics(), getRegistryName().toString());

		float t = (float) (localLevel - 1) / (getMaxLevel() - 1);
		float base = getDamageMult();
		float max = getDamageMultMax();

		float dmg = base + (max - base) * t;
		//System.out.println("localLevel=" + localLevel + " maxLevel=" + getMaxExpLevel(lvl) + " base=" + base + " max=" + max + " dmg=" + dmg);
		return dmg;
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

	public Ability getGMAbility() {
		if (gmAbility == null) return null;
		return ModAbilities.registry.get(ResourceLocation.parse(gmAbility));
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
		return Utils.getMagicHighestLocalLevel(playerData.getEquippedMagics(), getRegistryName().toString());
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

		if (casterData.isAbilityEquipped(Strings.fullMPBlast) && casterData.getMP() >= casterData.getMaxMP()) {
			int stacks = casterData.getNumberOfAbilitiesEquipped(Strings.fullMPBlast);
			fullMPBlastMult = (float) (2F - Math.pow(0.5F, stacks));
		}
		if (!isRC) {
			casterData.addMagicUses(name, 1);
			casterData.remMP(getCost(caster));

			if (getMagicData() != null && getRCProb(casterData)) {
				if (getGMAbility() != null) {
					ReactionMagic reactionCommand = (ReactionMagic) ModReactionCommands.registry.get(getMagicRC());

					if (reactionCommand != null) {
						int duration = (int) (reactionCommand.getDuration() + reactionCommand.getDuration() * (casterData.getNumberOfAbilitiesEquipped(Strings.grandMagicExtender) * 0.25F));

						reactionCommand.setMagic(getRegistryName());
						casterData.addReactionCommand(getMagicRC().toString(), caster, duration);
					}
				}

				casterData.setMagicUses(name, 0);
				PacketHandler.sendTo(new SCSyncPlayerData(caster), (ServerPlayer) caster);
			}
		}

		int cd = (int) (data.getCooldown() * (1 - casterData.getNumberOfAbilitiesEquipped(Strings.endlessMagic) * 0.2));
		casterData.setMagicCooldownTicks(Math.max(cd, 5));

		if (casterData.isAbilityEquipped(Strings.wizardsRuse)) { //Wizard's Ruse has a chance to heal the player based on the amount of stacked abilities and amount healed based on the cost of the ability
			double num = player.level().random.nextDouble();
			if (num < (0.25 + (0.125 * (casterData.getNumberOfAbilitiesEquipped(Strings.wizardsRuse) - 1)))) {
				caster.heal((int) getCost(caster) / 2F);
			}
		}

		if (!casterData.isAbilityEquipped(Strings.magicLockOn)) {
			lockOnEntity = null;
		}

		playMagicCastSound(player, caster);
		Utils.castMagic cast = new Utils.castMagic(player, caster, fullMPBlastMult, lockOnEntity, this);
		casterData.setCastedMagic(cast);

		PacketHandler.sendTo(new SCSyncPlayerData(caster), (ServerPlayer) caster);
	}

	public final void onUse(LivingEntity player, Player caster, LivingEntity lockOnEntity) {
		onUse(player, caster, lockOnEntity, false);
	}


	public Magic getNextTierMagic() {
		ResourceLocation next = getNextTier();

		if (next == null) return this;

		return ModMagic.registry.get(next);
	}

	public abstract void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity);

	/*public void playMagicCastSound2(LivingEntity player, Player caster) {
		playMagicCastSound(player,caster);
	}*/

	public abstract void playMagicCastSound(LivingEntity player, Player caster);

	private boolean getRCProb(PlayerData casterData) {
		int prob = casterData.getNumberOfAbilitiesEquipped(Strings.grandMagicHaste) * 10;

		if (gmAbility != null && casterData.isAbilityEquipped(gmAbility)) {
			prob += casterData.getNumberOfAbilitiesEquipped(gmAbility) * 10;
		}
		prob += (casterData.getMagicUses(name) - 1) * 5;
		double num = Math.random() * 100;
		System.out.println(num + " " + prob + " " + casterData.getMagicUses(name));
		return num <= prob;
	}

	public ResourceLocation getRegistryName() {
		return name;
	}

}