package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.leveling.Level;
import online.kingdomkeys.kingdomkeys.leveling.ModLevels;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;

public enum SoAState {
    NONE((byte)0), CHOICE((byte)1), SACRIFICE((byte)2), CONFIRM((byte)3), COMPLETE((byte)4), WARRIOR((byte)5), GUARDIAN((byte)6), MYSTIC((byte)7);

    private final byte b;
    SoAState(byte b) {
        this.b = b;
    }
    public byte get() {
        return b;
    }

    private boolean Compare(byte b) { return this.b == b; }

    public static SoAState fromByte(byte b) {
        SoAState[] values = SoAState.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].Compare(b)) {
                return values[i];
            }
        }
        return NONE;
    }

	public static final StreamCodec<FriendlyByteBuf, SoAState> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SoAState decode(FriendlyByteBuf pBuffer) {
            return SoAState.fromByte(pBuffer.readByte());
        }

        @Override
        public void encode(FriendlyByteBuf pBuffer, SoAState pValue) {
            pBuffer.writeByte(pValue.get());
        }
    };

    public static void applyStatsForChoices(Player player, PlayerData playerData, boolean remove) {
        if (playerData.getSoAState() == COMPLETE) {
            SoAState choice = !remove ? playerData.getChosen() : playerData.getSacrificed();
            SoAState sacrifice = !remove ? playerData.getSacrificed() : playerData.getChosen();
            
            if (remove) {
				KingdomKeys.LOGGER.info("Removing old choice? " + sacrifice);
				removeNonStatsData(ModLevels.registry.get(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, sacrifice.toString().toLowerCase())), playerData);
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
				Level choiceData = ModLevels.registry.get(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, choice.toString().toLowerCase()));
				Level sacrificeData = ModLevels.registry.get(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, sacrifice.toString().toLowerCase()));
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
		if (choice.getAbilities(choiceLevel).length > 0) {
			for (String ability : choice.getAbilities(choiceLevel)) {
				if (ability != null) {
					Ability a = ModAbilities.registry.get(ResourceLocation.parse(ability));
					if (a != null) {
						playerData.addAbility(ability, true);
					}
				}
			}
		}
		if (choice.getShotlocks(choiceLevel).length > 0) {
			for (String shotlock : choice.getShotlocks(choiceLevel)) {
				if (shotlock != null) {
					Shotlock a = ModShotlocks.registry.get(ResourceLocation.parse(shotlock));
					if (a != null) {
						playerData.addShotlockToList(shotlock, true);
					}
				}
			}
		}
		if (choice.getSpells(choiceLevel).length > 0) {
			for (String magic : choice.getSpells(choiceLevel)) {
				if (magic != null) {
					Magic magicInstance = ModMagic.registry.get(ResourceLocation.parse(magic));
					if (magicInstance != null) {
						if (playerData != null && playerData.getMagicsMap() != null) {
							if (!playerData.getMagicsMap().containsKey(magic)) {
								playerData.setMagicLevel(ResourceLocation.parse(magic), playerData.getMagicLevel(ResourceLocation.parse(magic)), true);
							} else {
								playerData.setMagicLevel(ResourceLocation.parse(magic), playerData.getMagicLevel(ResourceLocation.parse(magic)) + 1, true);
							}
						}
					}
				}
			}
		}
		
		if (choice.getMaxAccessories(choiceLevel) != 0) {
			playerData.addMaxAccessories(choice.getMaxAccessories(choiceLevel));
		}
		
		if (choice.getMaxArmors(choiceLevel) != 0) {
			playerData.addMaxArmors(choice.getMaxArmors(choiceLevel));
		}
	}
    
    public static void removeNonStatsData(Level levelData, PlayerData playerData) {
		if (levelData.getAbilities(0).length > 0) {
			for (String ability : levelData.getAbilities(0)) {
				if (ability != null) {
					Ability a = ModAbilities.registry.get(ResourceLocation.parse(ability));
					if (a != null) {
						playerData.removeAbility(ability);
					}
				}
			}
		}
		if (levelData.getShotlocks(0).length > 0) {
			for (String shotlock : levelData.getShotlocks(0)) {
				if (shotlock != null) {
					Shotlock a = ModShotlocks.registry.get(ResourceLocation.parse(shotlock));
					if (a != null) {
						playerData.removeShotlockFromList(shotlock);
					}
				}
			}
		}
		if (levelData.getSpells(0).length > 0) {
			for (String magic : levelData.getSpells(0)) {
				if (magic != null) {
					Magic magicInstance = ModMagic.registry.get(ResourceLocation.parse(magic));
					if (magicInstance != null) {
						if (playerData != null && playerData.getMagicsMap() != null) {
							if (playerData.getMagicsMap().containsKey(magic)) {
								playerData.setMagicLevel(ResourceLocation.parse(magic), playerData.getMagicLevel(ResourceLocation.parse(magic))-1, true);
							}
						}
					}
				}
			}
		}

    	if (levelData.getAbilities(1).length > 0) {
			for (String ability : levelData.getAbilities(1)) {
				if (ability != null) {
					Ability a = ModAbilities.registry.get(ResourceLocation.parse(ability));
					if (a != null) {
						playerData.removeAbility(ability);
					}
				}
			}
		}    		
		if (levelData.getShotlocks(1).length > 0) {
			for (String shotlock : levelData.getShotlocks(1)) {
				if (shotlock != null) {
					Shotlock a = ModShotlocks.registry.get(ResourceLocation.parse(shotlock));
					if (a != null) {
						playerData.removeShotlockFromList(shotlock);
					}
				}
			}
		}
		if (levelData.getSpells(1).length > 0) {
			for (String magic : levelData.getSpells(1)) {
				if (magic != null) {
					Magic magicInstance = ModMagic.registry.get(ResourceLocation.parse(magic));
					if (magicInstance != null) {
						if (playerData != null && playerData.getMagicsMap() != null) {
							if (playerData.getMagicsMap().containsKey(magic)) {
								playerData.setMagicLevel(ResourceLocation.parse(magic), playerData.getMagicLevel(ResourceLocation.parse(magic))-1, true);
							}
						}
					}
				}
			}
		}
    }
}
