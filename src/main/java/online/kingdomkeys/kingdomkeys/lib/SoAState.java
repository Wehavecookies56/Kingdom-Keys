package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.leveling.Level;
import online.kingdomkeys.kingdomkeys.leveling.ModLevels;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;

public enum SoAState implements StringRepresentable {
    NONE("none", (byte)0), CHOICE("choice", (byte)1), SACRIFICE("sacrifice", (byte)2), CONFIRM("confirm", (byte)3), COMPLETE("complete", (byte)4), WARRIOR("warrior", (byte)5), GUARDIAN("guardian", (byte)6), MYSTIC("mystic", (byte)7);

    private final String name;
    private final byte b;
    SoAState(String name, byte b) {
        this.name = name;
        this.b = b;
    }
    public byte get() {
        return b;
    }

    private boolean Compare(byte b) { return this.b == b; }

    public static SoAState fromByte(byte b) {
        SoAState[] values = SoAState.values();
	    for (SoAState value : values) {
		    if (value.Compare(b)) {
			    return value;
		    }
	    }
        return NONE;
    }

	public static final StreamCodec<FriendlyByteBuf, SoAState> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BYTE,
			SoAState::get,
			SoAState::fromByte
	);

    public static void applyStatsForChoices(Player player, PlayerData playerData, boolean remove) {
        if (playerData.getSoAState() == COMPLETE) {
            SoAState choice = !remove ? playerData.getChosen() : playerData.getSacrificed();
            SoAState sacrifice = !remove ? playerData.getSacrificed() : playerData.getChosen();
            
            if (remove) {
				KingdomKeys.LOGGER.info("Removing old choice? " + sacrifice);
				removeNonStatsData(ModLevels.registry.get(KingdomKeys.rl(sacrifice.getSerializedName())), playerData);
				KingdomKeys.LOGGER.info(playerData.getAbilityMap());
				playerData.getStrengthStat().removeModifier("choice");
				playerData.getMagicStat().removeModifier("choice");
				playerData.getDefenseStat().removeModifier("choice");
				playerData.getMaxAPStat().removeModifier("choice");

				playerData.getStrengthStat().removeModifier("sacrifice");
				playerData.getMagicStat().removeModifier("sacrifice");
				playerData.getDefenseStat().removeModifier("sacrifice");
				playerData.getMaxAPStat().removeModifier("sacrifice");
				playerData.setSoAState(NONE);
			} else {
				Level choiceData = ModLevels.registry.get(KingdomKeys.rl(choice.getSerializedName()));
				Level sacrificeData = ModLevels.registry.get(KingdomKeys.rl(sacrifice.getSerializedName()));
				addForChoice(1, choiceData, playerData);
				addForChoice(0, sacrificeData, playerData);
			}
		}
    }

	public static void addForChoice(int choiceLevel, Level choice, PlayerData playerData) {
		String modifier = "choice";
		if (choiceLevel == 0) {
			modifier = "sacrifice";
		}
		if (choice.getStr(choiceLevel) != 0) {
			playerData.getStrengthStat().addModifier(modifier, choice.getStr(choiceLevel), false, false);
		}
		if (choice.getMag(choiceLevel) != 0) {
			playerData.getMagicStat().addModifier(modifier, choice.getMag(choiceLevel), false, false);
		}
		if (choice.getDef(choiceLevel) != 0) {
			playerData.getDefenseStat().addModifier(modifier, choice.getDef(choiceLevel), false, false);
		}
		if (choice.getMaxAP(choiceLevel) != 0) {
			playerData.getMaxAPStat().addModifier(modifier, choice.getMaxAP(choiceLevel), false, false);
		}
		if (choice.getMaxHp(choiceLevel) > 0) {
			playerData.addMaxHP(choice.getMaxHp(choiceLevel));
		}
		if (choice.getMaxMp(choiceLevel) > 0) {
			playerData.addMaxMP(choice.getMaxMp(choiceLevel));
		}

        for (ResourceLocation ability : choice.getAbilities(choiceLevel)) {
            if (ability != null) {
                Ability a = ModAbilities.registry.get(ability);
                if (a != null) {
                    playerData.addAbility(ability, true);
                }
            }
        }

        for (ResourceLocation shotlock : choice.getShotlocks(choiceLevel)) {
            if (shotlock != null) {
                Shotlock a = ModShotlocks.registry.get(shotlock);
                if (a != null) {
                    playerData.addShotlockToList(shotlock, true);
                }
            }
        }

		// TODO magic spell earning from level up
        /*for (String magic : choice.getSpells(choiceLevel)) {
            if (magic != null) {
                Magic magicInstance = ModMagic.registry.get(KingdomKeys.rl(magic));
                if (magicInstance != null) {
                    if (playerData != null && playerData.getMagicsCastMap() != null) {
                        if (!playerData.getMagicsCastMap().containsKey(magic)) {
                            playerData.setMagicLevel(KingdomKeys.rl(magic), playerData.getMagicLevel(KingdomKeys.rl(magic)), true);
                        } else {
                            playerData.setMagicLevel(KingdomKeys.rl(magic), playerData.getMagicLevel(KingdomKeys.rl(magic)) + 1, true);
                        }
                    }
                }
            }
        }*/

        if (choice.getMaxAccessories(choiceLevel) != 0) {
			playerData.addMaxAccessories(choice.getMaxAccessories(choiceLevel));
		}
		
		if (choice.getMaxArmors(choiceLevel) != 0) {
			playerData.addMaxArmors(choice.getMaxArmors(choiceLevel));
		}

		if (choice.getMaxMagics(choiceLevel) != 0) {
			playerData.addMaxMagics(choice.getMaxMagics(choiceLevel));
		}
	}
    
    public static void removeNonStatsData(Level levelData, PlayerData playerData) {
        //levelData.getAbilities(0);
        for (ResourceLocation ability : levelData.getAbilities(0)) {
            if (ability != null) {
                Ability a = ModAbilities.registry.get(ability);
                if (a != null) {
                    playerData.removeAbility(ability);
                }
            }
        }
        //levelData.getShotlocks(0);
        for (ResourceLocation shotlock : levelData.getShotlocks(0)) {
            if (shotlock != null) {
                Shotlock a = ModShotlocks.registry.get(shotlock);
                if (a != null) {
                    playerData.removeShotlockFromList(shotlock);
                }
            }
        }
        /*levelData.getSpells(0); //TODO spells to remove
        for (String magic : levelData.getSpells(0)) {
            if (magic != null) {
                Magic magicInstance = ModMagic.registry.get(KingdomKeys.rl(magic));
                if (magicInstance != null) {
                    if (playerData != null && playerData.getMagicsCastMap() != null) {
                        if (playerData.getMagicsCastMap().containsKey(magic)) {
                            playerData.setMagicLevel(KingdomKeys.rl(magic), playerData.getMagicLevel(KingdomKeys.rl(magic)) - 1, true);
                        }
                    }
                }
            }
        }*/

       // levelData.getAbilities(1);
        for (ResourceLocation ability : levelData.getAbilities(1)) {
            if (ability != null) {
                Ability a = ModAbilities.registry.get(ability);
                if (a != null) {
                    playerData.removeAbility(ability);
                }
            }
        }
        //levelData.getShotlocks(1);
        for (ResourceLocation shotlock : levelData.getShotlocks(1)) {
            if (shotlock != null) {
                Shotlock a = ModShotlocks.registry.get(shotlock);
                if (a != null) {
                    playerData.removeShotlockFromList(shotlock);
                }
            }
        }
        //levelData.getSpells(1); //TODO initial spells
        /*for (String magic : levelData.getSpells(1)) {
            if (magic != null) {
                Magic magicInstance = ModMagic.registry.get(KingdomKeys.rl(magic));
                if (magicInstance != null) {
                    if (playerData != null && playerData.getMagicsCastMap() != null) {
                        if (playerData.getMagicsCastMap().containsKey(magic)) {
                            playerData.setMagicLevel(KingdomKeys.rl(magic), playerData.getMagicLevel(KingdomKeys.rl(magic)) - 1, true);
                        }
                    }
                }
            }
        }*/
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
