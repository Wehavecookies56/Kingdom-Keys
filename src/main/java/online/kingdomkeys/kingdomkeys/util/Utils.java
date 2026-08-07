package online.kingdomkeys.kingdomkeys.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategoryRegistry;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.gummi.*;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.creativetab.CreativeFilter;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.datagen.init.BlockTagsGen;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.entity.block.GummiHangarTileEntity;
import online.kingdomkeys.kingdomkeys.item.*;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.*;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.limit.ModLimits;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.MagicData;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.menu.PauldronInventory;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.*;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static online.kingdomkeys.kingdomkeys.item.ICreativeTab.Tab.*;

public class Utils {
	public static List<ItemStack> getItemsForCategory(ICreativeTab.Tab category) {
		if (category == null) {
			List<ItemStack> list = new ArrayList<>();
			list.addAll(KingdomKeys.kkItems.get());
			list.addAll(KingdomKeys.kkBlocks.get());
			return list;
		}

		return switch (category) {
			case KEYBLADES -> KingdomKeys.kkItems.get().stream().filter(stack -> stack.getItem() instanceof ICreativeTab tab && tab.getTab() == KEYBLADES).toList();
			case KEYCHAINS -> KingdomKeys.kkItems.get().stream().filter(stack -> stack.getItem() instanceof ICreativeTab tab && tab.getTab() == KEYCHAINS).toList();
			case ORGANIZATION -> KingdomKeys.kkItems.get().stream().filter(stack -> stack.getItem() instanceof ICreativeTab tab && tab.getTab() == ORGANIZATION).toList();
			case EQUIPABLES -> KingdomKeys.kkItems.get().stream().filter(stack -> stack.getItem() instanceof ICreativeTab tab && tab.getTab() == EQUIPABLES).toList();
			case CARDS -> KingdomKeys.kkItems.get().stream().filter(stack -> stack.getItem() instanceof ICreativeTab tab && tab.getTab() == CARDS).toList();
			case ARMORS -> KingdomKeys.kkItems.get().stream().filter(stack -> stack.getItem() instanceof ArmorItem).toList();
			case MATS -> KingdomKeys.kkItems.get().stream().filter(stack -> stack.getItem() instanceof SynthesisItem).toList();
			case GUMMI -> KingdomKeys.kkBlocks.get().stream().filter(stack -> stack.getItem() instanceof BlockItem block && block.getBlock() instanceof ICreativeTab tab && tab.getTab() == ICreativeTab.Tab.GUMMI).toList();
			case MISC -> getMiscItems();
			case NONE -> List.of();
		};
	}

	private static List<ItemStack> getMiscItems() {
		Set<Item> categorized = Arrays.stream(ICreativeTab.Tab.values())
				.filter(tab -> tab != ICreativeTab.Tab.MISC && tab != ICreativeTab.Tab.NONE)
				.flatMap(tab -> getItemsForCategory(tab).stream())
				.map(ItemStack::getItem)
				.collect(Collectors.toSet());

		List<ItemStack> misc = new ArrayList<>();
		misc.addAll(KingdomKeys.kkItems.get());
		misc.addAll(KingdomKeys.kkBlocks.get());

		return misc.stream().filter(stack -> !categorized.contains(stack.getItem())).toList();
	}

	public static List<ItemStack> getCurrentItems() {
		if (CreativeFilter.currentCategory == ICreativeTab.Tab.MISC) {
			return getMiscItems();
		}

		if (CreativeFilter.currentCategory == ICreativeTab.Tab.NONE) {
			return List.of();
		}

		return getItemsForCategory(CreativeFilter.currentCategory);
	}

	public static int getRedstoneFromMagic(String type){
		return switch(type){
			case "fire" -> 1;
			case "ice"-> 2;
			case "water"-> 3;
			case "lightning"-> 4;
			case "air"-> 5;
			case "stop"-> 6;
			case "darkness"-> 7;
			case "light"-> 8;
			default -> 0;
		};
	}

	public static void removeNegativeEffects(Player player) {
		if (player.level().isClientSide)
			return;

		//Copy to avoid ConcurrentModification
		for (MobEffectInstance effect : List.copyOf(player.getActiveEffects())) {
			if (!effect.getEffect().value().isBeneficial()) {
				player.removeEffect(effect.getEffect());
			}
		}
	}

	public static ItemStack getItemInInventory(Player player, Item item) {
		ItemStack itemStack = ItemStack.EMPTY;

		for (ItemStack stack : player.getInventory().items) {
			if (stack.getItem() == item) {
				itemStack = stack;
				break;
			}
		}
		return itemStack;
	}

	public static ItemStack getItemInAnyHand(Player player, Item item) {
		if(!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() == item) {
			return player.getMainHandItem();
		} else if (!player.getOffhandItem().isEmpty() && player.getOffhandItem().getItem() == item){
			return player.getOffhandItem();
		}
		return null;
	}

	public static int getMagicBagSlot(Player player) {
		NonNullList<ItemStack> items = player.getInventory().items;
		for (int i = 0, itemsSize = items.size(); i < itemsSize; i++) {
			ItemStack stack = items.get(i);
			if (stack.is(ModItems.magicsBag.get())) {
				return i;
			}
		}
		return -1;
	}

	public static int getCardsBagSlot(Player player, BagItem.Type type) {
		NonNullList<ItemStack> items = player.getInventory().items;
		for (int i = 0, itemsSize = items.size(); i < itemsSize; i++) {
			ItemStack stack = items.get(i);
			Item item = null;
			if(type == BagItem.Type.MAGICS_BAG) {
				item = ModItems.magicsBag.get();
			} else if(type == BagItem.Type.CARDS_BAG){
				item = ModItems.cardsBag.get();
			} else if(type == BagItem.Type.SHOTLOCKS_BAG){
				item = ModItems.shotlocksBag.get();
			}

			if (stack.is(item)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Whether magic cooldowns are tracked per magic instead of as one shared timer.
	 *
	 * <p>Server config, so it is read through {@code isLoaded} - it is queried from client rendering
	 * too, where the spec may not be up yet on the first frames after joining.</p>
	 */
	/**
	 * The magic bound to a shortcut slot, or null if that slot is empty or out of range. Needed because
	 * the cooldown check has to know which magic is about to be cast.
	 */
	public static ResourceLocation getShortcutMagic(PlayerData playerData, int index) {
		if (playerData == null || !playerData.getShortcutsMap().containsKey(index)) {
			return null;
		}

		int slot = playerData.getShortcutsMap().get(index);
		if (slot >= playerData.getMaxMagics()) {
			return null;
		}

		ItemStack stack = playerData.getEquippedMagics().get(slot);
		return stack != null && stack.getItem() instanceof MagicSpellItem spell ? spell.getMagic() : null;
	}

	public static boolean perMagicCooldown() {
		return ModConfigs.SERVER_SPEC.isLoaded() && ModConfigs.SERVER.perMagicCooldown.get();
	}

	public static boolean hasOnlyOneBag(Player player, BagItem.Type type) {
		boolean found = false;
		for (ItemStack stack : player.getInventory().items) {
			Item item = null;
			if(type == BagItem.Type.MAGICS_BAG) {
				item = ModItems.magicsBag.get();
			} else if(type == BagItem.Type.CARDS_BAG){
				item = ModItems.cardsBag.get();
			} else if(type == BagItem.Type.SHOTLOCKS_BAG){
				item = ModItems.shotlocksBag.get();
			}

			if (stack.is(item)) {
				if (found) {
					return false;
				} else {
					found = true;
				}
			}
		}
		return found;
	}

	public static int getSavepointPercent(int ticks) {
		int res = Math.round(100 - (((ticks-1) /(20F-1F)) * 100F));
		if(res == 0)
			res = 1;
		return res;
	}

	public static final ResourceLocation mobLevelHPModifier = KingdomKeys.rl("mob_level_hp");
	public static final ResourceLocation mobLevelAttackModifier = KingdomKeys.rl("mob_level_attack");

	public static ItemStack getWhiteMushroomReward(ServerLevel level, BlockPos pos) {
		LootTable table = level.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, KingdomKeys.rl("entities/white_mushroom_reward")));
		LootParams params = new LootParams.Builder(level).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).create(LootContextParamSets.CHEST);
		List<ItemStack> items = table.getRandomItems(params);
		return items.isEmpty() ? ItemStack.EMPTY : items.get(0);
	}

	public static int getCheapestDriveCost(PlayerData playerData, List<DriveForm> driveFormMap) {
		int min = playerData.isAbilityEquipped(ModAbilities.DARK_DOMINATION) ? ModDriveForms.ANTI.get().getDriveCost() : 1000;
		for(DriveForm form : driveFormMap){
			if(form != null && form.getDriveFormData() != null && form != ModDriveForms.ANTI.get()) {
				min = Math.min(form.getDriveCost(), min);
			}
		}
		return min;
	}

	public static double getCheapestMagicCost(Map<Integer, ItemStack> magicsMap, Player player, MagicData.SpellType type) {
		double min = 1000;

		PlayerData playerData = PlayerData.get(player);
		if (playerData == null) {
			return 0;
		}

		for (Entry<Integer, ItemStack> magic : magicsMap.entrySet()) {
			if (magic.getKey() >= playerData.getMaxMagics())
				break;

			ItemStack stack = playerData.getEquippedMagic(magic.getKey());

			if (stack != null && stack.getItem() instanceof MagicSpellItem spell) {
				Magic m = ModMagic.registry.get(spell.getMagic());

				if (m == null)
					continue;

				// Ignorar magias de otro tipo
				if (m.getSpellType() != type)
					continue;

				double cost = m.getCost(player);

				// Mantener el comportamiento especial de Cura
				if (cost == 300) {
					return cost;
				}

				min = Math.min(cost, min);
			}
		}

		return min;
	}
	public static List<Component> getResistancesStats(ItemStack selectedItemStack) {
		List<Component> stats = new ArrayList<>();

		int str=0, mag=0, ap = 0, def = 0, fireRes = 0, iceRes = 0, thunderRes = 0, lightRes = 0, darkRes = 0;
		switch (selectedItemStack.getItem()) {
			case KeybladeItem kb -> {
				str = kb.getStrength(0);
				mag = kb.getMagic(0);
			}
			case KKAccessoryItem accessory -> {
				str = accessory.getStr();
				mag = accessory.getMag();
				ap = accessory.getAp();
			}
			case KKArmorItem armor -> {
				def = armor.getDefense();
				for (Entry<KKResistanceType, Integer> resistanceType : armor.getResList().entrySet()) {
					switch (resistanceType.getKey()) {
						case fire -> fireRes = resistanceType.getValue();
						case ice -> iceRes = resistanceType.getValue();
						case lightning -> thunderRes = resistanceType.getValue();
						case light -> lightRes = resistanceType.getValue();
						case darkness -> darkRes = resistanceType.getValue();
					}
				}
			}
			default -> {
			}
		}

		if(ap != 0)
			stats.add(Component.literal(Utils.translateToLocal(Strings.Gui_Menu_Status_AP)+": "+ap).withStyle(ChatFormatting.YELLOW));
		if(str != 0 || selectedItemStack.getItem() instanceof KeybladeItem)
			stats.add(Component.literal(Utils.translateToLocal(Strings.Gui_Menu_Status_Strength)+": "+str).withStyle(ChatFormatting.DARK_RED));
		if(mag != 0 || selectedItemStack.getItem() instanceof KeybladeItem)
			stats.add(Component.literal(Utils.translateToLocal(Strings.Gui_Menu_Status_Magic)+": "+mag).withStyle(ChatFormatting.BLUE));
		if(def != 0)
			stats.add(Component.literal(Utils.translateToLocal(Strings.Gui_Menu_Status_Defense)+": "+def).withStyle(ChatFormatting.WHITE));
		if(fireRes != 0)
			stats.add(Component.literal(Utils.translateToLocal(Strings.Gui_Menu_Status_FireResShort)+": "+fireRes+"%").withStyle(ChatFormatting.RED));
		if(iceRes != 0)
			stats.add(Component.literal(Utils.translateToLocal(Strings.Gui_Menu_Status_BlizzardResShort)+": "+iceRes+"%").withStyle(ChatFormatting.AQUA));
		if(thunderRes != 0)
			stats.add(Component.literal(Utils.translateToLocal(Strings.Gui_Menu_Status_ThunderResShort)+": "+thunderRes+"%").withStyle(ChatFormatting.YELLOW));
		if(lightRes != 0)
			stats.add(Component.literal(Utils.translateToLocal(Strings.Gui_Menu_Status_LightResShort)+": "+lightRes+"%").withStyle(ChatFormatting.GRAY));
		if(darkRes != 0)
			stats.add(Component.literal(Utils.translateToLocal(Strings.Gui_Menu_Status_DarkResShort)+": "+darkRes+"%").withStyle(ChatFormatting.DARK_GRAY));

		return stats;
	}

	public static GummiShipEntity.ShipStats getShipStats(GummiStructure structure) {
		float speed = 0;
		int mobility = 0;
		LinkedList<Vec3> passengers = new LinkedList<>();
		LinkedList<Vec3> weapons = new LinkedList<>();
		int weight = 0;
		int armour = 0;
		HashMap<GummiWeaponBlock.ShotType, Integer> impact = new HashMap<>();

		int sizeX = structure.getBlocks().length;
		int sizeY = structure.getBlocks()[0].length;
		int sizeZ = structure.getBlocks()[0][0].length;

		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				for (int z = 0; z < sizeZ; z++) {
					BlockState state = structure.getBlocks()[x][y][z];
					if (state != null && !state.isAir()) {
						weight++;//TODO make heavier blocks maybe?
						armour++;
						if (state.getBlock() instanceof GummiCockpitBlock cockpit) {
							if (!cockpit.isMultiBlock() || state.getValue(GummiCockpitBlock.X) == 0 && state.getValue(GummiCockpitBlock.Y) == 0 && state.getValue(GummiCockpitBlock.Z) == 0) {
								if (cockpit.getMaxSeats() > 0) {
									for (Vec3 s : cockpit.getSeats()) {
										Vec3 finalPos = new Vec3(x - s.x(), y + s.y() + 0.18F, z + s.z());
										passengers.add(finalPos);
									}
								}
							}
						}

						if (state.getBlock() instanceof GummiBlockBase gummi) {
							armour += gummi.getArmour();
							weight += gummi.getWeight()-1;// we already add one by default
						}

						if (state.getBlock() == Blocks.OBSIDIAN) {
							weight++;
						}
						if(state.getBlock() instanceof GummiWeaponBlock wpn && wpn.isMultiBlock()){
							if(state.getValue(GummiWeaponBlock.X) == 0 && state.getValue(GummiWeaponBlock.Z) == 0) {
								if(wpn.shotType.getRootType() == GummiWeaponBlock.ShotType.WATER){
									//int power = wpn.shotType == GummiWeaponBlock.ShotType.WATER ? 1 : wpn.shotType == GummiWeaponBlock.ShotType.WATERA ? 2 : 3;
									impact.put(wpn.shotType, wpn.getFirepower());
								} else {
									weapons.add(new Vec3(x, y, z));
								}
							}
						} else if (state.getBlock() instanceof GummiWeaponBlock wpn) {
							if(wpn.shotType.getRootType().equals(GummiWeaponBlock.ShotType.WATER)){
								impact.put(wpn.shotType, wpn.getFirepower());
							} else {
								weapons.add(new Vec3(x, y, z));
							}
						} else if (state.getBlock() instanceof GummiAeroBlock aero) {
							mobility += aero.getMobility();
						} else if (state.getBlock() instanceof GummiEngineBlock engine) {
							speed += engine.getSpeed();
						}
					}
				}
			}
		}
		return new GummiShipEntity.ShipStats(speed,weight,armour,weapons,impact,passengers,mobility);
	}

	public static GummiStructure resizeStructure(GummiStructure original, int newSize) {
		int oldSize = original.getWidth();

		if (oldSize > newSize) {
			return null;
		}

		GummiStructure resized = new GummiStructure(original.getOwnerID(), original.getName(), newSize, newSize, newSize);
		BlockState[][][] oldBlocks = original.getBlocks();
		BlockState[][][] newBlocks = resized.getBlocks();

		int offset = (newSize - oldSize) / 2;

		for (int x = 0; x < oldSize; x++) {
			for (int y = 0; y < oldSize; y++) {
				for (int z = 0; z < oldSize; z++) {
					newBlocks[x + offset][y][z + offset] = oldBlocks[x][y][z];
				}
			}
		}

		return resized;
	}

	public static Vec3i getRealGummiStructureSize(GummiStructure structure){
		int sizeX = structure.getBlocks().length;
		int sizeY = structure.getBlocks()[0].length;
		int sizeZ = structure.getBlocks()[0][0].length;

		int minX = sizeX, maxX = -1;
		int minY = sizeY, maxY = -1;
		int minZ = sizeZ, maxZ = -1;

		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				for (int z = 0; z < sizeZ; z++) {
					BlockState state = structure.getBlocks()[x][y][z];
					if (state != null && !state.isAir()) {
						if (x < minX) minX = x;
						if (x > maxX) maxX = x;
						if (y < minY) minY = y;
						if (y > maxY) maxY = y;
						if (z < minZ) minZ = z;
						if (z > maxZ) maxZ = z;
					}
				}
			}
		}

		if (maxX == -1) {
			return new Vec3i(0, 0, 0);
		}

		int realWidth  = (maxX - minX) + 1;
		int realHeight = (maxY - minY) + 1;
		int realDepth  = (maxZ - minZ) + 1;

		return new Vec3i(realWidth, realHeight, realDepth);
	}

	public static boolean[] isStructureEven(GummiStructure structure){
		Vec3i realDim = Utils.getRealGummiStructureSize(structure);
		boolean xEven = realDim.getX() % 2 == 0;
		boolean zEven = realDim.getX() % 2 == 0;
		return new boolean[]{ xEven, zEven };
	}

	public static void moveShip(Level level, BlockPos origin, Direction facing, int size, String moveDirStr) {
		Direction realDir = switch (moveDirStr.toUpperCase()) {
			case "FORWARD" -> facing.getOpposite();
			case "BACKWARD" -> facing;
			case "LEFT" -> switch (facing) {
				case NORTH -> Direction.EAST;
				case SOUTH -> Direction.WEST;
				case EAST -> Direction.SOUTH;
				case WEST -> Direction.NORTH;
				default -> facing;
			};
			case "RIGHT" -> switch (facing) {
				case NORTH -> Direction.WEST;
				case SOUTH -> Direction.EAST;
				case EAST -> Direction.NORTH;
				case WEST -> Direction.SOUTH;
				default -> facing;
			};
			case "UP" -> Direction.UP;
			case "DOWN" -> Direction.DOWN;
			default -> throw new IllegalStateException("Unexpected value: " + moveDirStr.toUpperCase());
		};

		int dx = realDir.getStepX();
		int dy = realDir.getStepY();
		int dz = realDir.getStepZ();

		int[] offsets = Utils.getShipOffset(facing, size);
		if (offsets == null)
			return;

		BlockPos minCorner = origin.offset(offsets[0], 0, offsets[1]);
		BlockPos maxCorner = minCorner.offset(size - 1, size - 1, size - 1);

		List<BlockPos> positions = new ArrayList<>();
		Map<BlockPos, BlockState> blocks = new HashMap<>();
		// Everything a block knows that isn't in its state lives in its block entity: the core's fuel and damage, for instance. Carrying the states alone would move an empty shell.
		Map<BlockPos, CompoundTag> blockEntities = new HashMap<>();

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				for (int z = 0; z < size; z++) {
					int rx = x, rz = z;

					switch (facing) {
						case SOUTH -> { rx = x; rz = z; }
						case NORTH -> { rx = size - 1 - x; rz = size - 1 - z; }
						case EAST -> { rx = z; rz = size - 1 - x; }
						case WEST -> { rx = size - 1 - z; rz = x; }
					}

					BlockPos pos = origin.offset(offsets[0] + rx, y, offsets[1] + rz);
					BlockState state = level.getBlockState(pos);
					if (state.isAir())
						continue;

					positions.add(pos);
					blocks.put(pos, state);

					BlockEntity blockEntity = level.getBlockEntity(pos);
					if (blockEntity != null) {
						blockEntities.put(pos, blockEntity.saveCustomOnly(level.registryAccess()));
					}
				}
			}
		}

		//To prevent moving outside the building area
		boolean canMove = true;
		for (BlockPos pos : positions) {
			BlockPos newPos = pos.offset(dx, dy, dz);
			if (newPos.getX() < minCorner.getX() || newPos.getX() > maxCorner.getX() || newPos.getY() < minCorner.getY() || newPos.getY() > maxCorner.getY() || newPos.getZ() < minCorner.getZ() || newPos.getZ() > maxCorner.getZ()) {
				canMove = false;
				break;
			}
		}

		if (!canMove) {
			KingdomKeys.LOGGER.debug("Can't move, out of hangar");
			return;
		}

		for (BlockPos pos : positions) {
			setBlockWithoutUpdate(level, pos, Blocks.AIR.defaultBlockState());
		}

		for (BlockPos pos : positions) {
			BlockState state = blocks.get(pos);
			BlockPos newPos = pos.offset(dx, dy, dz);
			level.setBlock(newPos, state, 3);

			CompoundTag data = blockEntities.get(pos);
			if (data == null) {
				continue;
			}

			// setBlock has just made a fresh block entity, so its saved contents have to be poured back in.
			BlockEntity moved = level.getBlockEntity(newPos);
			if (moved != null) {
				moved.loadCustomOnly(data, level.registryAccess());
				moved.setChanged();
			}
		}
	}

	public static GummiStructure getGummiStructureWithFacing(UUID ownerID, String nameInHangar, Level level, BlockPos origin, Direction facing, int size) {
		GummiStructure struct = new GummiStructure(ownerID, nameInHangar,size, size, size);

		int max = size - 1;

		int[] offsets = Utils.getShipOffset(facing,size);
		if(offsets == null)
			return null;

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				for (int z = 0; z < size; z++) {
					int rx = x;
					int rz = z;

					switch (facing) {
						case SOUTH -> { rx = x; rz = z; }
						case NORTH -> { rx = max - x; rz = max - z; }
						case EAST  -> { rx = z; rz = max - x; }
						case WEST  -> { rx = max - z; rz = x; }
					}

					BlockPos target = origin.offset(offsets[0] + rx, y, offsets[1] + rz);
					BlockState original = level.getBlockState(target);

					if (original.getBlock() != Blocks.AIR) {
						Rotation rotation = switch (facing) {
							case SOUTH -> Rotation.NONE;
							case NORTH -> Rotation.CLOCKWISE_180;
							case WEST  -> Rotation.COUNTERCLOCKWISE_90;
							case EAST  -> Rotation.CLOCKWISE_90;
							default -> Rotation.NONE;
						};
						BlockState rotated = Utils.rotateBlock(original,rotation);
						struct.getBlocks()[x][y][z] = rotated;

					}

				}
			}
		}
		return struct;
	}

	public static int getAmountOfGummiShipsInBuildPlate(Level level, BlockPos origin, Direction facing, int size) {
		int[] offsets = Utils.getShipOffset(facing,size);
		if(offsets == null)
			return 0;

		AABB box = new AABB(origin.getX()+offsets[0], origin.getY(), origin.getZ()+offsets[1], origin.getX()+offsets[0]+size, origin.getY() + size, origin.getZ()+offsets[1]+size);
		List<GummiShipEntity> entities = level.getEntitiesOfClass(GummiShipEntity.class, box);

		return entities.size();
	}

	public static GummiShipEntity getGummiShipInBuildPlate(Level level, BlockPos origin, Direction facing, int size) {
		int[] offsets = Utils.getShipOffset(facing,size);
		if(offsets == null)
			return null;

		AABB box = new AABB(origin.getX()+offsets[0], origin.getY(), origin.getZ()+offsets[1], origin.getX()+offsets[0]+size, origin.getY() + size, origin.getZ()+offsets[1]+size);
		List<GummiShipEntity> entities = level.getEntitiesOfClass(GummiShipEntity.class, box);

		if(entities.size() == 1){
			return entities.getFirst();
		}

		return null;
	}

	public static List<GummiShipEntity> getAllGummiShipsInBuildPlate(Level level, BlockPos origin, Direction facing, int size) {
		int[] offsets = Utils.getShipOffset(facing,size);
		if(offsets == null)
			return null;

		AABB box = new AABB(origin.getX()+offsets[0], origin.getY(), origin.getZ()+offsets[1], origin.getX()+offsets[0]+size, origin.getY() + size, origin.getZ()+offsets[1]+size);
		return level.getEntitiesOfClass(GummiShipEntity.class, box);
	}

	public static void removeBlocks(Level level, BlockPos origin, Direction facing, int size) {
		int max = size - 1;

		int[] offsets = Utils.getShipOffset(facing,size);
		if(offsets == null)
			return;

		for (int x = 0; x < size; x++) {
			for (int y = size-1; y >= 0; y--) {
				for (int z = 0; z < size; z++) {
					int rx = x;
					int rz = z;

					switch (facing) {
						case NORTH -> { rx = x; rz = z; }
						case SOUTH -> { rx = max - x; rz = max - z; }
						case EAST  -> { rx = z; rz = max - x; }
						case WEST  -> { rx = max - z; rz = x; }
					}

					BlockPos target = origin.offset(offsets[0] + rx, y, offsets[1] + rz);
					if (level.getBlockState(target).getBlock() != Blocks.AIR) {
						setBlockWithoutUpdate(level, target, Blocks.AIR.defaultBlockState());
					}
				}
			}
		}
	}



	public static boolean setBlockWithoutUpdate(Level level, BlockPos pos, BlockState state) {
		int flags = 3;
		if (level.isOutsideBuildHeight(pos)) {
			return false;
		} else if (!level.isClientSide && level.isDebug()) {
			return false;
		} else {
			LevelChunk levelchunk = level.getChunkAt(pos);
			//Block block = state.getBlock();

			pos = pos.immutable(); // Forge - prevent mutable BlockPos leaks
			BlockSnapshot blockSnapshot = null;
			if (level.captureBlockSnapshots && !level.isClientSide) {
				blockSnapshot = BlockSnapshot.create(level.dimension(), level, pos, flags);
				level.capturedBlockSnapshots.add(blockSnapshot);
			}

			//BlockState old = level.getBlockState(pos);
			//int oldLight = old.getLightEmission(level, pos);
			//int oldOpacity = old.getLightBlock(level, pos);

			BlockState blockstate = ((IKKLevelChunkExtension)levelchunk).kingdom_Keys$setBlockState(pos, state, (flags & 64) != 0);
			if (blockstate == null) {
				if (blockSnapshot != null) level.capturedBlockSnapshots.remove(blockSnapshot);
				return false;
			} else {
				//BlockState blockstate1 = level.getBlockState(pos);

				if (blockSnapshot == null) { // Don't notify clients or update physics while capturing blockstates
					markAndNotifyBlockNoNeighbourUpdate(level, pos, levelchunk, blockstate, state, flags, 512);
				}

				return true;
			}
		}
	}

	public static void markAndNotifyBlockNoNeighbourUpdate(Level level, BlockPos p_46605_, @Nullable LevelChunk levelchunk, BlockState blockstate, BlockState p_46606_, int p_46607_, int p_46608_) {
		Block block = p_46606_.getBlock();
		BlockState blockstate1 = level.getBlockState(p_46605_);
		{
			{
				if (blockstate1 == p_46606_) {
					if (blockstate != blockstate1) {
						level.setBlocksDirty(p_46605_, blockstate, blockstate1);
					}

					if ((p_46607_ & 2) != 0
							&& (!level.isClientSide || (p_46607_ & 4) == 0)
							&& (level.isClientSide || levelchunk.getFullStatus() != null && levelchunk.getFullStatus().isOrAfter(FullChunkStatus.BLOCK_TICKING))) {
						level.sendBlockUpdated(p_46605_, blockstate, p_46606_, p_46607_);
					}

					if ((p_46607_ & 1) != 0) {
						level.blockUpdated(p_46605_, blockstate.getBlock());
						if (!level.isClientSide && p_46606_.hasAnalogOutputSignal()) {
							level.updateNeighbourForOutputSignal(p_46605_, block);
						}
					}

					if ((p_46607_ & 16) == 0 && p_46608_ > 0) {
						int i = p_46607_ & -34;
						//blockstate.updateIndirectNeighbourShapes(level, p_46605_, i, p_46608_ - 1);
						//p_46606_.updateNeighbourShapes(level, p_46605_, i, p_46608_ - 1);
						//p_46606_.updateIndirectNeighbourShapes(level, p_46605_, i, p_46608_ - 1);
					}

					level.onBlockStateChange(p_46605_, blockstate, blockstate1);
					p_46606_.onBlockStateChange(level, p_46605_, blockstate);
				}
			}
		}
	}

	public static BlockPos getCorePos(Level level, BlockPos origin, Direction facing, int size) {
		int max = size - 1;

		int[] offsets = Utils.getShipOffset(facing,size);
		if(offsets == null)
			return null;

		for (int x = 0; x < size; x++) {
			for (int y = size-1; y >= 0; y--) {
				for (int z = 0; z < size; z++) {
					int rx = x;
					int rz = z;

					switch (facing) {
						case NORTH -> { rx = x; rz = z; }
						case SOUTH -> { rx = max - x; rz = max - z; }
						case EAST  -> { rx = z; rz = max - x; }
						case WEST  -> { rx = max - z; rz = x; }
					}

					BlockPos target = origin.offset(offsets[0] + rx, y, offsets[1] + rz);
					if (level.getBlockState(target).getBlock() == ModBlocks.gummiCore.get()) {
						return target;
					}
				}
			}
		}
		return null;
	}

	public static int getCorePosCount(Level level, BlockPos origin, Direction facing, int size) {
		int max = size - 1;
		int cores = 0;

		int[] offsets = Utils.getShipOffset(facing,size);
		if(offsets == null)
			return 0;

		for (int x = 0; x < size; x++) {
			for (int y = size-1; y >= 0; y--) {
				for (int z = 0; z < size; z++) {
					int rx = x;
					int rz = z;

					switch (facing) {
						case NORTH -> { rx = x; rz = z; }
						case SOUTH -> { rx = max - x; rz = max - z; }
						case EAST  -> { rx = z; rz = max - x; }
						case WEST  -> { rx = max - z; rz = x; }
					}

					BlockPos target = origin.offset(offsets[0] + rx, y, offsets[1] + rz);
					if (level.getBlockState(target).getBlock() == ModBlocks.gummiCore.get()) {
						cores++;
					}
				}
			}
		}
		return cores;
	}

	public static boolean hasBlocks(Level level, BlockPos origin, Direction facing, int size) {
		int max = size - 1;

		int[] offsets = Utils.getShipOffset(facing,size);
		if(offsets == null)
			return false;

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				for (int z = 0; z < size; z++) {
					int rx = x;
					int rz = z;

					switch (facing) {
						case NORTH -> { rx = x; rz = z; }
						case SOUTH -> { rx = max - x; rz = max - z; }
						case EAST  -> { rx = z; rz = max - x; }
						case WEST  -> { rx = max - z; rz = x; }
					}

					BlockPos target = origin.offset(offsets[0] + rx, y, offsets[1] + rz);
					if (level.getBlockState(target).getBlock() != Blocks.AIR) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static ArrayList<Block> getBannedBlocks(Level level, BlockPos origin, Direction facing, int size) {
		ArrayList<Block> blocks = new ArrayList<>();
		int max = size - 1;

		int[] offsets = Utils.getShipOffset(facing,size);
		if(offsets == null)
			return null;

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				for (int z = 0; z < size; z++) {
					int rx = x;
					int rz = z;

					switch (facing) {
						case NORTH -> { rx = x; rz = z; }
						case SOUTH -> { rx = max - x; rz = max - z; }
						case EAST  -> { rx = z; rz = max - x; }
						case WEST  -> { rx = max - z; rz = x; }
					}

					BlockPos target = origin.offset(offsets[0] + rx, y, offsets[1] + rz);
					if (level.getBlockState(target).getBlock().builtInRegistryHolder().is(BlockTagsGen.BANNED_GUMMI_BLOCKS)) {
						blocks.add(level.getBlockState(target).getBlock());
					}
				}
			}
		}
		return blocks;
	}

	public static boolean isVanillaCrit(Player player) {
		return player.fallDistance > 0.0F && !player.onGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger();
	}

	public static BlockState rotateBlock(BlockState state, Rotation rotation) {
		// If block has custom rotate implementatio
		BlockState rotated = state.rotate(rotation);
		if (!rotated.equals(state))
			return rotated;

		// If it doesn't change we force it
		for (Property<?> property : state.getProperties()) { //Get all properties
			if (property.getName().equalsIgnoreCase("facing") && property instanceof DirectionProperty dirProp) { //Get facing
				Direction dir = state.getValue(dirProp);
				return state.setValue(dirProp, rotation.rotate(dir));
			}
		}

		return state; // if none of the above work we just return the same block
	}
	public static int[] getShipOffset(Direction facing, int size) {
		switch (facing) {
			case NORTH -> {
				return new int[]{-(size / 2), 1};
			}
			case SOUTH -> {
				return new int[]{-(size / 2), -size};
			}
			case EAST -> {
				return new int[]{-size, -(size / 2)};
			}
			case WEST -> {
				return new int[]{1, -(size / 2)};
			}
		}
		return null;
	}

	public static GummiHangarTileEntity.HangarEnergyStorage getEnergyStoragePerLevel(int lvl) {
		int[] stats = getFEStatsPerLevel(lvl);
		return new GummiHangarTileEntity.HangarEnergyStorage(stats[0], stats[1], stats[2]);
	}

	public static int[] getFEStatsPerLevel(int lvl){
		return switch (lvl){
			case 0 -> new int[]{ 20000, 100, 50 };
			case 1 -> new int[]{ 60000, 120, 60 };
			case 2 -> new int[]{ 120000, 180, 90 };
			case 3 -> new int[]{ 240000, 260, 130 };
			case 4 -> new int[]{ 400000, 350, 175 };
			case 5,6,7,8,9,10 -> new int[]{ 1000000, 1000, 500 };
			default -> throw new IllegalStateException("Unexpected value for Utils#getFEPerLevel: " + lvl);
		};
	}

	public static String getFormattedNumber(int num) {
		return String.format("%,d", num);
	}

	public static String getFormattedNumber(float num) {
		return String.format("%,.2f", num);
	}

	public static boolean isKBArmor(ItemStack stack) {
		if (!(stack.getItem() instanceof ArmorItem armor))
			return false;
		return armor.getMaterial().value().equipSound().value() == ModSounds.keyblade_armor.get();
	}

	public static ResourceLocation getRCNameFromIndex(Player player, int reactionSelected) {
		int index = 0;
		for (Entry<ResourceLocation, Integer> entry : PlayerData.get(player).getReactionCommands().entrySet()) {
			if(index == reactionSelected) {
				return entry.getKey();
			}
			index++;
		}
		return null;
	}

	public static List<ResourceLocation> getSpellsList(PlayerData playerData, MagicData.SpellType type) {
		Map<Integer, ItemStack> equippedMagics = playerData.getEquippedMagics();
		int maxMagics = playerData.getMaxMagics();

		List<ResourceLocation> result = new ArrayList<>();

		if (equippedMagics.isEmpty())
			return result;

		for (Entry<Integer, ItemStack> entry : equippedMagics.entrySet()) {
			if (entry.getKey() >= maxMagics)
				break;

			if (entry.getValue().getItem() instanceof MagicSpellItem spell) {
				Magic magic = ModMagic.registry.get(spell.getMagic());

				if (magic != null && magic.getSpellType() == type) {
					result.add(spell.getMagic());
				}
			}
		}

		return result;
	}

	public static int getMagicSlotFromNameAndLevel(Map<Integer, ItemStack> equippedMagics, ResourceLocation commandMagicName) {
		if (equippedMagics.isEmpty()) return -1;

		for (Entry<Integer, ItemStack> entry : equippedMagics.entrySet()) {
			ItemStack stack = entry.getValue();
			if (!stack.isEmpty() && stack.getItem() instanceof MagicSpellItem spell) {
				if (spell.getMagic().equals(commandMagicName)) {
					return entry.getKey();
				}
			}
		}
		return -1;
	}

	public static int getMagicHighestLocalLevel(Map<Integer, ItemStack> equippedMagics, ResourceLocation commandMagicName) {
		if (equippedMagics.isEmpty()) return -1;

		int level = -1;
		for (Entry<Integer, ItemStack> entry : equippedMagics.entrySet()) {
			ItemStack stack = entry.getValue();
			if (!stack.isEmpty() && stack.getItem() instanceof MagicSpellItem spell) {
				if (spell.getMagic().equals(commandMagicName)) {
					level = Math.max(spell.getLocalLevel(stack), level);
				}
			}
		}

		return level;
	}

	public static void addMagicExperience(Player player, int amount) {
		PlayerData playerData = PlayerData.get(player);
		if(playerData == null)
			return;

		ArrayList<String> leveledMagics = new ArrayList<>();
		for (ItemStack stack : playerData.getEquippedMagics().values()) {
			if (stack.isEmpty())
				continue;

			if (!(stack.getItem() instanceof MagicSpellItem magic))
				continue;

			int oldLevel = magic.getLocalLevel(stack);
			magic.addExp(stack, amount);

			if(magic.getLocalLevel(stack) != oldLevel) { //If the level is different show notif
				leveledMagics.add("M_" + stack.getHoverName().getString() + " " + Utils.translateToLocal("gui.magicspell.lvl_short", magic.isMaxed(stack) ? "MAX" : magic.getLocalLevel(stack)));
			}
		}

		if(!leveledMagics.isEmpty()){
			player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.levelup.get(), SoundSource.MASTER, 0.5f, 1.0f);
			PacketHandler.sendTo(new SCShowOverlayPacket("levelup", player.getUUID(), player.getGameProfile().getName(), playerData.getLevel(), playerData.getNotifColor(), leveledMagics), (ServerPlayer) player);
		}
	}


	public static void addShotlockExperience(Player player, int amount) {
		PlayerData playerData = PlayerData.get(player);
		if (playerData == null)
			return;

		ItemStack equipped = playerData.getEquippedShotlock();
		if (equipped == null || equipped.isEmpty() || !(equipped.getItem() instanceof online.kingdomkeys.kingdomkeys.item.ShotlockItem shotlockItem))
			return;

		int oldLevel = shotlockItem.getLocalLevel(equipped);
		shotlockItem.addExp(equipped, amount);

		if (shotlockItem.getLocalLevel(equipped) != oldLevel) {
			ArrayList<String> leveledShotlocks = new ArrayList<>();
			leveledShotlocks.add("S_" + equipped.getHoverName().getString() + " " + Utils.translateToLocal("gui.magicspell.lvl_short", shotlockItem.isMaxed(equipped) ? "MAX" : shotlockItem.getLocalLevel(equipped)));

			player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.levelup.get(), SoundSource.MASTER, 0.5f, 1.0f);
			PacketHandler.sendTo(new SCShowOverlayPacket("levelup", player.getUUID(), player.getGameProfile().getName(), playerData.getLevel(), playerData.getNotifColor(), leveledShotlocks), (ServerPlayer) player);
		}
	}

	public static final Map<ResourceKey<Biome>, Item> MEMORY_BY_BIOME = new HashMap<>();

	public static Item getMemoryFromBiome(Holder<Biome> biome) {
		return biome.unwrapKey().map(MEMORY_BY_BIOME::get).orElse(null);
	}

	public static void showTutorial(ServerPlayer player, List<Title> tutorial) {
		PlayerData playerData = PlayerData.get(player);
		if(!playerData.hasSeenTutorial(Constants.TUTORIAL_CO_LOBBY)) {
			PacketHandler.sendTo(new SCShowMessagesPacket(tutorial), player);

			playerData.setSeenTutorial(Constants.TUTORIAL_CO_LOBBY);
			PacketHandler.sendTo(new SCSyncPlayerData(player, playerData), player);
		}
	}

	public static BlockPos getBlockPosYHeight(Level level, int posX, int posZ) {
		int yPos = level.getHeight(Heightmap.Types.WORLD_SURFACE, posX, posZ);
		BlockPos pos = new BlockPos(posX, yPos, posZ);

		while (level.getBlockState(pos).is(ModBlocks.structureWall.get()) || level.getBlockState(pos).is(Blocks.AIR)) {
			pos = pos.below();
		}
		return pos;
	}

	public static int getYHeight(Level level, int posX, int posZ) {
		int yPos = level.getHeight(Heightmap.Types.WORLD_SURFACE, posX, posZ);
		BlockPos pos = new BlockPos(posX, yPos, posZ);

		while (level.getBlockState(pos).is(ModBlocks.structureWall.get()) || level.getBlockState(pos).is(Blocks.AIR)) {
			pos = pos.below();
		}
		return pos.getY();
	}

	public static class Title {
		public String title, subtitle;
		public int fadeIn = 10, fadeOut = 20, displayTime = 70;
		public boolean titleFont;

		public Title(String title, String subtitle, int fadeIn, int displayTime, int fadeOut) {
			this.title = title;
			this.subtitle = subtitle;
			this.fadeIn = fadeIn;
			this.fadeOut = fadeOut;
			this.displayTime = displayTime;
		}

		public Title(String title, String subtitle) {
			this.title = title;
			this.subtitle = subtitle;
		}

		public Title(CompoundTag compound) {
			read(compound);
		}

		public Title setKHFont(){
			this.titleFont = true;
			return this;
		}

		public CompoundTag write() {
			CompoundTag compound = new CompoundTag();
			compound.putString("title", title);
			compound.putString("subtitle", subtitle);
			compound.putInt("fadein", fadeIn);
			compound.putInt("fadeout", fadeOut);
			compound.putInt("displaytime", displayTime);
			compound.putBoolean("titlefont", titleFont);
			return compound;
		}

		public void read(CompoundTag tag) {
			this.title = tag.getString("title");
			this.subtitle = tag.getString("subtitle");
			this.fadeIn = tag.getInt("fadein");
			this.fadeOut = tag.getInt("fadeout");
			this.displayTime = tag.getInt("displaytime");
			this.titleFont = tag.getBoolean("titlefont");
		}

		public static CompoundTag writeList(List<Title> titles) {
			CompoundTag compound = new CompoundTag();
			for (int i = 0; i < titles.size(); i++) {
				Title t = titles.get(i);
				compound.put("m" + i, t.write());
			}
			compound.putInt("size", titles.size());
			return compound;
		}

		public static List<Title> readList(CompoundTag compound) {
			int size = compound.getInt("size");
			List<Title> titles = new ArrayList<Title>();
			for (int i = 0; i < size; i++) {
				titles.add(new Title(compound.getCompound("m" + i)));
			}
			return titles;
		}

		public static StreamCodec<FriendlyByteBuf, Title> STREAM_CODEC = new StreamCodec<>() {
			@Override
			public Title decode(FriendlyByteBuf pBuffer) {
				return new Title(pBuffer.readNbt());
			}

			@Override
			public void encode(FriendlyByteBuf pBuffer, Title pValue) {
				pBuffer.writeNbt(pValue.write());
			}
		};
	}

	public record ShotlockPosition(int id,float x,float y, float z){
		public static final StreamCodec<FriendlyByteBuf, ShotlockPosition> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.INT,
				ShotlockPosition::id,
				ByteBufCodecs.FLOAT,
				ShotlockPosition::x,
				ByteBufCodecs.FLOAT,
				ShotlockPosition::y,
				ByteBufCodecs.FLOAT,
				ShotlockPosition::z,
				ShotlockPosition::new
		);
	}

	public record castMagic(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity, Magic magic) {}

	public static ResourceLocation getItemRegistryName(Item item) {
		return BuiltInRegistries.ITEM.getKey(item);
	}

	public static ResourceLocation getBlockRegistryName(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block);
	}

	public static float map(float x, float in_min, float in_max, float out_min, float out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

	public static int getSlotFor(Inventory inv, ItemStack stack) {
		for (int i = 0; i < inv.getContainerSize(); ++i) {
			if (!inv.getItem(i).isEmpty() && ItemStack.matches(stack, inv.getItem(i))) {
				return i;
			}
		}
		return -1;
	}

	public static boolean isNumber(char c) {
		try {
			Integer.parseInt(String.valueOf(c));
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static int getInt(String num) {
		int number;
		try {
			number = Integer.parseInt(num);
			return number;
		} catch (Exception e) {
			return 0;
		}
	}

	public static double getDouble(String num) {
		double number;
		try {
			number = Double.parseDouble(num);
			return number;
		} catch (Exception e) {
			return 0;
		}
	}

	public static int clamp(int value, int min, int max) {
		return Math.min(Math.max(value, min), max);
	}

	public static float clamp(float value, float min, float max) {
		return Math.min(Math.max(value, min), max);
	}

	public static double clamp(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}

	/**
	 * Method for generating random integer between the 2 parameters, The order of
	 * min and max do not matter.
	 *
	 * @param min minimum value that the random integer can be
	 * @param max maximum value that the random integer can be
	 * @return a random integer
	 */
	public static int randomWithRange(int min, int max) {
		int range = Math.abs(max - min) + 1;
		return (int) (Math.random() * range) + (Math.min(min, max));
	}

	/**
	 * Method for generating random doubles between the 2 parameters, The order of
	 * min and max do not matter.
	 *
	 * @param min minimum value that the random double can be
	 * @param max maximum value that the random double can be
	 * @return a random double
	 */
	public static double randomWithRange(double min, double max) {
		double range = Math.abs(max - min);
		return (Math.random() * range) + (Math.min(min, max));
	}

	/**
	 * Method for generating random floats between the 2 parameters, The order of
	 * min and max do not matter.
	 *
	 * @param min minimum value that the random float can be
	 * @param max maximum value that the random float can be
	 * @return a random float
	 */
	public static float randomWithRange(float min, float max) {
		float range = Math.abs(max - min) + 1;
		return (float) (Math.random() * range) + (Math.min(min, max));
	}

	/**
	 * Replacement for the old i8n format method
	 *
	 * @param name   the unlocalized string to translate
	 * @param format the format of the string
	 * @return the translated string
	 */
	public static String translateToLocalFormatted(String name, Object... format) {
		MutableComponent translation = Component.translatable(name, format);
		return translation.getString();
	}

	/**
	 * Replacement for the old i8n translate to local method
	 *
	 * @param name the unlocalized string to translate
	 * @return the translated string
	 */
	public static String translateToLocal(String name, Object... args) {
		MutableComponent translation = Component.translatable(name, args);
		return translation.getString();
	}

	/**
	 * Get the ItemStack of the item that made the DamageSource
	 *
	 * @param damageSource
	 * @param player
	 * @return
	 */
	public static ItemStack getWeaponDamageStack(DamageSource damageSource, Player player) {
		switch (damageSource.getMsgId()) {
			case "player":
				if (player.getMainHandItem() != null && player.getMainHandItem().getItem() instanceof KeybladeItem || player.getMainHandItem().getItem() instanceof IOrgWeapon) {
					return player.getMainHandItem();
				}
				break;
			case "offhand":
				if (player.getOffhandItem() != null && player.getOffhandItem().getItem() instanceof KeybladeItem || player.getOffhandItem().getItem() instanceof IOrgWeapon) {
					return player.getOffhandItem();
				}
		}
		return null;

	}

	public enum OrgMember {
		NONE, XEMNAS, XIGBAR, XALDIN, VEXEN, LEXAEUS, ZEXION, SAIX, AXEL, DEMYX, LUXORD, MARLUXIA, LARXENE, ROXAS;

		public static final StreamCodec<FriendlyByteBuf, OrgMember> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.INT,
				Enum::ordinal,
				integer -> OrgMember.values()[integer]
		);
	}

	public static int getDriveFormLevel(Map<ResourceLocation, int[]> map, ResourceLocation driveForm) {
		if(map.get(driveForm) == null) {
			KingdomKeys.LOGGER.error("The drive form map doesn't contain " + driveForm);
			return 0;
		}
		if (driveForm.equals(ModDriveForms.ANTI.get().getRegistryName()))
			return 7;
		return map.get(driveForm)[0];
	}

	public static LinkedHashMap<Item, Integer> getSortedMaterials(Map<Item, Integer> materials) {

		ArrayList<Item> list = new ArrayList<>(materials.keySet());
		list.sort(Comparator.comparing(BuiltInRegistries.ITEM::getKey));

		LinkedHashMap<Item, Integer> map = new LinkedHashMap<>();
		for (Item k : list) {
			map.put(k, materials.get(k));
		}
		return map;
	}

	public static LinkedHashMap<ResourceLocation, int[]> getSortedAbilities(LinkedHashMap<ResourceLocation, int[]> abilities) {
		return abilities.entrySet().stream().sorted((entry, entry2) -> {
			Ability ability = ModAbilities.registry.get(entry.getKey());
			Ability ability2 = ModAbilities.registry.get(entry2.getKey());
			if (ability != null && ability2 != null) {
				return ability.compareTo(ability2);
			}
			return entry.getKey().compareTo(entry2.getKey());
		}).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (value, value2) -> value, LinkedHashMap::new));
	}

	public static LinkedHashMap<ResourceLocation, int[]> getSortedDriveForms(LinkedHashMap<ResourceLocation, int[]> driveFormsMap, List<DriveForm> visibleForms) {
		List<DriveForm> list = new ArrayList<>();

		for (ResourceLocation entry : driveFormsMap.keySet()) {
			DriveForm form = ModDriveForms.registry.get(entry);
			if (visibleForms.contains(form)) { // Should only add the form if it is visible
				list.add(form);
			}
		}

		list.sort(Comparator.comparingInt(DriveForm::getOrder));

		LinkedHashMap<ResourceLocation, int[]> map = new LinkedHashMap<>();
		for (DriveForm driveForm : list) {
			map.put(driveForm.getRegistryName(), driveFormsMap.get(driveForm.getRegistryName()));
		}

		return map;
	}

	public static List<Limit> getPlayerLimitAttacks(Player player) {
		if(ModConfigs.getServerConfig().allowAllOrgLimits.getAsBoolean())
			return new ArrayList<>(ModLimits.registry.stream().toList());

		return ModLimits.registry.stream().filter(limit -> limit.getOwner() == PlayerData.get(player).getAlignment()).collect(Collectors.toList());
	}

	public static List<Limit> getSortedLimits(List<Limit> list) {
		List<Limit> newList = new ArrayList<>(list);
		newList.sort(Comparator.comparingInt(Limit::getOrder));
		return newList;
	}

	public static Player getPlayerByName(Level world, String name) {
		List<? extends Player> players = world.getServer() == null ? world.players() : getAllPlayers(world.getServer());
		for (Player p : players) {
			if (p.getGameProfile().getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}

	public static Player getClosestPlayer(Entity e, Level world) {
		Player nearest = null;
		if (e.getServer() == null) {
			return null;
		}
		List<? extends Player> players = world == null ? getAllPlayers(e.getServer()) : world.players();
		for (Player p : players) {
			if (nearest == null) {
				nearest = p;
			}

			if (p.distanceTo(e) < nearest.distanceTo(e)) {
				nearest = p;
			}
		}
		return nearest;
	}

	public static Player getClosestPlayer(Entity e) {
		return getClosestPlayer(e, null);
	}

	public static List<Player> getAllPlayers(MinecraftServer ms) {
		List<Player> list = new ArrayList<Player>();
		for (ServerLevel world : ms.getAllLevels()) {
			for (Player p : world.players()) {
				list.add(p);
			}
		}
		return list;
	}

	public static List<Entity> getEntitiesInRadius(Entity entity, float radius) {
		return entity.level().getEntities(entity, entity.getBoundingBox().inflate(radius), Entity::isAlive);
	}

	public static List<LivingEntity> getLivingEntitiesInRadius(Entity entity, float radius) {
		List<Entity> list = entity.level().getEntities(entity, entity.getBoundingBox().inflate(radius), Entity::isAlive);
		List<LivingEntity> elList = new ArrayList<LivingEntity>();
		for (Entity e : list) {
			if (e instanceof LivingEntity) {
				elList.add((LivingEntity) e);
			}
		}

		return elList;
	}

	public static List<Entity> removePartyMembersFromList(Player player, List<Entity> list) {
		Party casterParty = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());

		if (casterParty != null && !casterParty.getFriendlyFire()) {
			for (Member m : casterParty.getMembers()) {
				list.remove(player.level().getPlayerByUUID(m.getUUID()));
			}
		} else {
			list.remove(player);
		}
		return list;
	}

	/**
	 * Used to check if there's anyone else online in the party for KO effect
	 *
	 * @param player
	 * @param p
	 * @param level
	 * @return
	 */
	public static boolean anyPartyMemberOnExcept(Player player, Party p, ServerLevel level) {
		boolean membersOn = false;
		for (Member member : p.getMembers()) {
			if (Utils.getPlayerByName(level, member.getUsername().toLowerCase()) != null) {
				if (Utils.getPlayerByName(level, member.getUsername().toLowerCase()) != player) {
					membersOn = true;
				}
			}
		}
		return membersOn;
	}

	public static List<LivingEntity> getLivingEntitiesInRadiusExcludingParty(LivingEntity player, float radius) {
		List<Entity> list = player.level().getEntities(player, player.getBoundingBox().inflate(radius), Entity::isAlive);
		Party casterParty = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());

		if (casterParty != null && !casterParty.getFriendlyFire()) {
			for (Member m : casterParty.getMembers()) {
				list.remove(player.level().getPlayerByUUID(m.getUUID()));
			}
		} else {
			list.remove(player);
		}

		List<LivingEntity> elList = new ArrayList<LivingEntity>();
		for (Entity e : list) {
			if (e instanceof LivingEntity) {
				elList.add((LivingEntity) e);
			}
		}

		return elList;
	}

	/**
	 * Gets entities in radius from the entity param
	 *
	 * @param player  to ignore from the list
	 * @param entity  where to check with radius
	 * @param radiusX
	 * @param radiusY
	 * @param radiusZ
	 * @return
	 */
	public static List<LivingEntity> getLivingEntitiesInRadiusExcludingParty(Player player, Entity entity, float radiusX, float radiusY, float radiusZ) {
		List<Entity> list = player.level().getEntities(player, entity.getBoundingBox().inflate(radiusX, radiusY, radiusZ), Entity::isAlive);
		Party casterParty = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());

		if (casterParty != null && !casterParty.getFriendlyFire()) {
			for (Member m : casterParty.getMembers()) {
				list.remove(player.level().getPlayerByUUID(m.getUUID()));
			}
		} else {
			list.remove(player);
		}

		list.remove(entity);

		List<LivingEntity> elList = new ArrayList<LivingEntity>();
		for (Entity e : list) {
			if (e instanceof LivingEntity) {
				elList.add((LivingEntity) e);
			}
		}

		return elList;
	}

	public static String getResourceName(String text) {
		return text.replaceAll("[ \\t]+$", "").replaceAll("\\s+", "_").replaceAll("[\\'\\:\\-\\,\\#]", "").replaceAll("\\&", "and").toLowerCase();
	}

	public static void createKeybladeID(ItemStack stack) {
		if (!hasKeybladeID(stack)) {
			UUID uuid = UUID.randomUUID();
			stack.set(ModComponents.KEYBLADE_ID, uuid);
			KingdomKeys.LOGGER.debug("Created new keybladeID:{} for {}", uuid, stack.getDisplayName().getString());
		}
	}

	public static void copyKeybladeID(ItemStack source, ItemStack destination) {
		if (hasKeybladeID(source)) {
			destination.set(ModComponents.KEYBLADE_ID, source.get(ModComponents.KEYBLADE_ID));
		}
	}

	public static boolean hasKeybladeID(ItemStack stack) {
		return stack.has(ModComponents.KEYBLADE_ID) && !stack.is(Items.AIR);
	}

	public static UUID getKeybladeID(ItemStack stack) {
		if (hasKeybladeID(stack)) {
			return stack.getOrDefault(ModComponents.KEYBLADE_ID, Util.NIL_UUID);
		}
		return null;
	}

	public static void armourTick(ItemStack stack, Entity entity, Level level, int slot) {
		if (entity instanceof Player player && !level.isClientSide) {
			PlayerData playerData = PlayerData.get(player);
			if(playerData != null) {
				UUID armorUUID = playerData.getEquippedKBArmor(0).getItem() != null ? Utils.getArmorID(playerData.getEquippedKBArmor(0)) : null;

				if (Utils.hasArmorID(stack)) {
					if (Utils.getArmorID(stack).equals(armorUUID)) { //If UUID is the same check slots
						//If the armor item is ticking outside an armor slot
						if (!(player.getInventory().getItem(36) == stack || player.getInventory().getItem(37) == stack || player.getInventory().getItem(38) == stack || player.getInventory().getItem(39) == stack)) {
							Utils.desummonArmour(playerData, player, stack, slot, true, true);
						}
					} else {//If UUID is different remove
						Utils.desummonArmour(playerData, player, stack, slot, false, true);
					}
				}
			}
		}
	}

	public static void desummonArmour(PlayerData playerData, Player player, ItemStack stack, int slot, boolean sameUUID, boolean playSound) {
		if (sameUUID) {
			PauldronInventory pauldronInventory = (PauldronInventory) playerData.getEquippedKBArmor(0).getCapability(Capabilities.ItemHandler.ITEM);
			if (stack.getItem() instanceof ArmorItem armorItem) {
				stack.remove(ModComponents.ARMOR_ID);
				pauldronInventory.setStackInSlot(armorItem.getType().ordinal(), stack);
			}
		}
		player.getInventory().setItem(slot, ItemStack.EMPTY);
		if (playSound) {
			player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
		}
	}

	public static boolean hasArmorID(ItemStack stack) {
		if (stack.getItem() instanceof PauldronItem || stack.getItem() instanceof ArmorItem) {
			return stack.has(ModComponents.ARMOR_ID);
		}
		return false;
	}

	public static UUID getArmorID(ItemStack stack) {
		if (hasArmorID(stack)) {
			return stack.get(ModComponents.ARMOR_ID);
		}
		return null;
	}

	// Returns the inv slot if summoned keychain is found
	public static int findSummoned(Inventory inv, ItemStack chain) {
		List<ItemStack> list = new ArrayList<>(inv.items);
		list.addAll(inv.armor);
		list.addAll(inv.offhand);
		return findSummoned(list, chain);
	}

	public static int findSummoned(List<ItemStack> inv, ItemStack chain) {
		if (!ItemStack.matches(chain, ItemStack.EMPTY)) {
			for (int i = 0; i < inv.size(); i++) {
				ItemStack slotStack = inv.get(i);
				// Make sure it has a tag
				if (hasKeybladeID(slotStack)) {
					// Compare the ID with the chain's
					if (hasKeybladeID(chain) && getKeybladeID(slotStack).equals(getKeybladeID(chain))) {
						return i;
					}
				}
			}
		}
		return -1;
	}

	public static int findSummoned(Inventory inv){
		List<ItemStack> list = new ArrayList<>(inv.items);
		list.addAll(inv.armor);
		list.addAll(inv.offhand);

		for (int i = 0; i < list.size(); i++) {
			ItemStack slotStack = list.get(i);
			// Make sure it has a tag
			if (hasKeybladeID(slotStack) && slotStack.getItem() instanceof KeybladeItem) {
				return i;
			}
		}
		return -1;
	}

	public static void swapStack(Inventory inv, int stack1, int stack2) {
		ItemStack tempStack = inv.getItem(stack2);
		inv.setItem(stack2, inv.getItem(stack1));
		inv.setItem(stack1, tempStack);
	}

	// Returns the category for the stack from the IItemCategory interface, the
	// registry, else it returns MISC
	public static ItemCategory getCategoryForStack(ItemStack stack) {
		ItemCategory category = ItemCategory.MISC;
		if (stack.getItem() instanceof IItemCategory) {
			category = ((IItemCategory) stack.getItem()).getCategory();
		} else if (ItemCategoryRegistry.hasCategory(stack.getItem())) {
			category = ItemCategoryRegistry.getCategory(stack.getItem());
		}
		return category;
	}

	public static ItemCategory getCategoryForRecipe(ResourceLocation location) {
		if (RecipeRegistry.getInstance().containsKey(location)) {
			return getCategoryForStack(new ItemStack(RecipeRegistry.getInstance().getValue(location).getResult()));
		} else {
			return ItemCategory.MISC;
		}
	}

	public static ItemCategory getCategoryForShop(ResourceLocation stackRL) {
		return getCategoryForStack(new ItemStack(BuiltInRegistries.ITEM.get(stackRL)));
	}

	public static int getAccessoriesStat(PlayerData playerData, String type) {
		int res = 0;
		int c = 1;
		for (Entry<Integer, ItemStack> entry : playerData.getEquippedAccessories().entrySet()) {
			if (c > playerData.getMaxAccessories())
				break;
			if (!ItemStack.matches(entry.getValue(), ItemStack.EMPTY)) {
				KKAccessoryItem accessory = (KKAccessoryItem) entry.getValue().getItem();
				switch (type) {
					case "ap":
						res += accessory.getAp();
						break;
					case "str":
						res += accessory.getStr();
						break;
					case "mag":
						res += accessory.getMag();
						break;
				}
			}
		}
		return res;
	}

	public static List<ResourceLocation> getAccessoriesAbilities(PlayerData playerData) {
		List<ResourceLocation> res = new ArrayList<>();
		int c = 1;
		for (Entry<Integer, ItemStack> entry : playerData.getEquippedAccessories().entrySet()) {
			if (c > playerData.getMaxAccessories())
				break;
			if (!ItemStack.matches(entry.getValue(), ItemStack.EMPTY)) {
				KKAccessoryItem accessory = (KKAccessoryItem) entry.getValue().getItem();
				res.addAll(accessory.getAbilities());
			}
			c++;
		}
		return res;
	}

	public static int getArmorsStat(Map<Integer, ItemStack> equipped, String type) {
		int res = 0;
		for (Entry<Integer, ItemStack> entry : equipped.entrySet()) {
			if (!ItemStack.matches(entry.getValue(), ItemStack.EMPTY)) {
				KKArmorItem kkArmorItem = (KKArmorItem) entry.getValue().getItem();
				switch (type) {
					case "def":
						res += kkArmorItem.getDefense();
						break;
					case "darkness":
						if (kkArmorItem.CheckKey(KKResistanceType.darkness))
							res += kkArmorItem.GetResValue(KKResistanceType.darkness, res == 0 ? 100 : 100 - res);
						break;
					case "light":
						if (kkArmorItem.CheckKey(KKResistanceType.light))
							res += kkArmorItem.GetResValue(KKResistanceType.light, res == 0 ? 100 : 100 - res);
						break;
					case "ice":
						if (kkArmorItem.CheckKey(KKResistanceType.ice))
							res += kkArmorItem.GetResValue(KKResistanceType.ice, res == 0 ? 100 : 100 - res);
						break;
					case "air":
						if (kkArmorItem.CheckKey(KKResistanceType.air))
							res += kkArmorItem.GetResValue(KKResistanceType.air, res == 0 ? 100 : 100 - res);
						break;
					case "lightning":
						if (kkArmorItem.CheckKey(KKResistanceType.lightning))
							res += kkArmorItem.GetResValue(KKResistanceType.lightning, res == 0 ? 100 : 100 - res);
						break;
					case "water":
						if (kkArmorItem.CheckKey(KKResistanceType.water))
							res += kkArmorItem.GetResValue(KKResistanceType.water, res == 0 ? 100 : 100 - res);
						break;
					case "fire":
						if (kkArmorItem.CheckKey(KKResistanceType.fire))
							res += kkArmorItem.GetResValue(KKResistanceType.fire, res == 0 ? 100 : 100 - res);
						break;
				}
			}
		}
		return res;
	}

	public static int getArmorsStat(PlayerData playerData, String type) {
		return getArmorsStat(playerData.getEquippedArmors(), type);
	}

	public static int getConsumedAP(PlayerData playerData) {
		int ap = 0;
		LinkedHashMap<ResourceLocation, int[]> map = playerData.getAbilityMap();
		for (Entry<ResourceLocation, int[]> entry : map.entrySet()) {
			Ability a = ModAbilities.registry.get(entry.getKey());
			ap += a.getAPCost() * Integer.bitCount(entry.getValue()[1]);
		}
		return ap;
	}

	public static double getMPHasteValue(PlayerData playerData) {
		int val = 0;
		val += (2 * playerData.getNumberOfAbilitiesEquipped(ModAbilities.MP_HASTE));
		val += (4 * playerData.getNumberOfAbilitiesEquipped(ModAbilities.MP_HASTERA));
		val += (6 * playerData.getNumberOfAbilitiesEquipped(ModAbilities.MP_HASTEGA));
		return val;
	}

	public static void RefreshAbilityAttributes(Player player, PlayerData playerData) {
		if (player.level().isClientSide)
			return;

		Multimap<Holder<Attribute>, AttributeModifier> map = HashMultimap.create();

		// Luck - affects things like chest loot, separate from looting or fortune.
		AttributeModifier attributemodifier = new AttributeModifier(ModAbilities.LUCKY_STRIKE.location(), playerData.getNumberOfAbilitiesEquipped(ModAbilities.LUCKY_STRIKE), AttributeModifier.Operation.ADD_VALUE);
		map.put(Attributes.LUCK, attributemodifier);

		player.getAttributes().addTransientAttributeModifiers(map);
	}

	private static final String ORG_TEAM_ID = "kk_orgrobes";

	public static PlayerTeam getOrCreateTeam(ServerLevel level) {
		Scoreboard sb = level.getScoreboard();
		PlayerTeam team = sb.getPlayerTeam(ORG_TEAM_ID);

		if (team == null) {
			team = sb.addPlayerTeam(ORG_TEAM_ID);
			team.setNameTagVisibility(Team.Visibility.NEVER);
			team.setCollisionRule(Team.CollisionRule.ALWAYS);
		}
		return team;
	}

	public static void updateOrgRobesTeam(ServerPlayer player) {
		ServerLevel level = player.serverLevel();
		Scoreboard sb = level.getScoreboard();
		PlayerTeam team = getOrCreateTeam(level);

		String name = player.getScoreboardName();
		if(ModConfigs.hideOrgNames){
			if (Utils.isWearingOrgRobes(player)) {
				if (sb.getPlayersTeam(name) != team) {
					sb.addPlayerToTeam(name, team);
				}
			} else {
				if (sb.getPlayersTeam(name) == team) {
					sb.removePlayerFromTeam(name, team);
				}
			}
		} else {
			//If config is false make sure everyone invisible is visible again
			if (sb.getPlayersTeam(name) == team) {
				sb.removePlayerFromTeam(name, team);
			}
		}
	}

	public static boolean isWearingOrgRobes(Player player) {
		if (!ModConfigs.SERVER.orgEnabled.get())
			return false;

		boolean wearingOrgCloak = true;
		for (int i = 0; i < player.getInventory().armor.size(); ++i) {
			ItemStack itemStack = player.getInventory().armor.get(i);
			if (itemStack.isEmpty() || !BuiltInRegistries.ITEM.getKey(itemStack.getItem()).getPath().startsWith("organization_") && !BuiltInRegistries.ITEM.getKey(itemStack.getItem()).getPath().startsWith("xemnas_") && !BuiltInRegistries.ITEM.getKey(itemStack.getItem()).getPath().startsWith("anticoat_")) {
				wearingOrgCloak = false;
				break;
			}
		}
		return wearingOrgCloak;
	}

	final static int[] bagCosts = {10000,20000,40000,80000};
	final static int[] hangarCosts = {15000,30000,70000,130000,9999999};

	public static int getBagCosts(int bagLevel) {
		return bagCosts[bagLevel];
	}
	public static int getHangarCosts(int hangarLevel) {
		return hangarCosts[hangarLevel];
	}

	public static String getHangarSizeFromLevel(int level) {
		return switch(level){
			case 0 -> "XS";
			case 1 -> "S";
			case 2 -> "M";
			case 3 -> "L";
			case 4 -> "XL";
			case 5,6,7,8,9,10 -> level+"";
			default -> "Unsuported value: " + level;
		};
	}


	public static String snakeToCamel(String str) {
		// Capitalize first letter of string
		str = str.substring(0, 1).toUpperCase() + str.substring(1);

		// Run a loop till string contains underscore
		while (str.contains("_")) {
			// Replace the first occurrence of letter that present after the underscore, to capitalize form of next letter of underscore
			str = str.replaceFirst("_[a-z]", String.valueOf(Character.toUpperCase(str.charAt(str.indexOf("_") + 1))));
		}
		str = str.substring(0, 1).toLowerCase() + str.substring(1);
		return str;
	}

	public static Shotlock getPlayerShotlock(Player player) {
		PlayerData playerData = PlayerData.get(player);
		net.minecraft.world.item.ItemStack equipped = playerData.getEquippedShotlock();
		if (equipped != null && equipped.getItem() instanceof online.kingdomkeys.kingdomkeys.item.ShotlockItem shotlockItem) {
			return ModShotlocks.registry.get(shotlockItem.getShotlock());
		} else {
			return null;
		}
	}

	public static boolean isPlayerLowHP(Player player) {
		return isLowHP(player.getHealth(), player.getMaxHealth());
	}

	public static boolean isLowHP(float hp, float maxHP) {
		return hp < maxHP / 4;
	}

	// Gets items excluding AIR
	public static Map<Integer, ItemStack> getEquippedItems(Map<Integer, ItemStack> equippedItems) {
		Map<Integer, ItemStack> finalMap = new HashMap<>(equippedItems);
		for (Entry<Integer, ItemStack> entry : equippedItems.entrySet()) {
			ItemStack stack = entry.getValue();
			if (ItemStack.matches(stack, ItemStack.EMPTY)) {
				finalMap.remove(entry.getKey());
			}
		}

		return finalMap;
	}

	public static boolean isEntityInParty(Party party, Entity e) {
		if (party == null)
			return false;
		List<Member> list = party.getMembers();
		for (Member m : list) {
			if (m.getUUID().equals(e.getUUID())) {
				return true;
			}
		}
		return false;
	}

	public static List<Entity> removeFriendlyEntities(List<Entity> list) {
		List<Entity> list2 = new ArrayList<>();
		for (Entity e : list) {
			if (e instanceof Monster || e instanceof Player) {
				list2.add(e);
			}
		}
		return list2;
	}

	public static boolean isHostile(Entity e) {
		return e instanceof Monster || e instanceof Player || e instanceof Slime;
	}

	public static List<ResourceLocation> getKeybladeAbilitiesAtLevel(Item item, int level) {
		ArrayList<ResourceLocation> abilities = new ArrayList<>();
		KeybladeItem keyblade = null;
		if (item instanceof IKeychain) {
			keyblade = ((IKeychain) item).toSummon();
		} else if (item instanceof KeybladeItem) {
			keyblade = ((KeybladeItem) item);
		}

		if (keyblade != null) {
			for (int i = 0; i <= level; i++) {
				ResourceLocation a = keyblade.data.getLevelAbility(i);
				if (a != null) {
					abilities.add(a);
				}
			}
		}
		return abilities;
	}

	public static List<ResourceLocation> getOrgWeaponAbilities(Item item) {
		ArrayList<ResourceLocation> abilities = new ArrayList<>();
		if (item instanceof IOrgWeapon org) {
			ResourceLocation[] a = org.getOrganizationData().getAbilities();
			if (a != null) {
				abilities.addAll(Arrays.asList(a));
			}

		}
		return abilities;
	}

	/**
	 * Set to level 1
	 *
	 * @param playerData
	 * @param player
	 */
	public static void restartLevel(PlayerData playerData, Player player) { // sets player level to base
		playerData.setLevel(1);
		playerData.setExperience(0);
		playerData.setMaxHP(20);
		player.setHealth(playerData.getMaxHP());
		player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(playerData.getMaxHP());
		playerData.setMaxMP(0);
		playerData.setMP(playerData.getMaxMP());

		playerData.setStrength(1);
		playerData.setMagic(1);
		playerData.setDefense(1);
		playerData.setMaxAP(0);
		playerData.setMaxAccessories(0);
		playerData.setMaxArmors(0);
		playerData.setMaxMagics(0);

		playerData.clearAbilities();
		SoAState.applyStatsForChoices(player, playerData, false);

		playerData.equipShotlock(net.minecraft.world.item.ItemStack.EMPTY);

		// playerData.addAbility(Strings.zeroExp, false);
	}

	/**
	 * Recalculate drive form levels and permanent abilities and shotlocks
	 *
	 * @param playerData
	 * @param player
	 */
	public static void restartLevel2(PlayerData playerData, Player player) { // calculates drive forms
		LinkedHashMap<ResourceLocation, int[]> driveForms = playerData.getDriveFormMap();
		for (Entry<ResourceLocation, int[]> entry : driveForms.entrySet()) {
			int dfLevel = entry.getValue()[0];
			DriveForm form = ModDriveForms.registry.get(entry.getKey());
			if (!Utils.getFakeForms().contains(form.getRegistryName())) {
				for (int i = 1; i <= dfLevel; i++) {
					form.getBaseAbilityForLevel(i).ifPresent(baseAbility -> {
						playerData.addAbility(baseAbility, false);
					});
				}
			}
		}

		playerData.getPAbilitiesList().forEach(a -> {
			playerData.addAbility(a,false);
		});

		player.heal(playerData.getMaxHP());
		playerData.setMP(playerData.getMaxMP());
	}

	public static List<ResourceLocation> getFakeForms(){
		return ModDriveForms.registry.stream().filter(DriveForm::isFakeForm).map(DriveForm::getRegistryName).toList();
	}

	public static String getTierFromInt(int tier) {
		return switch (tier) {
			case 1 -> "D";
			case 2 -> "C";
			case 3 -> "B";
			case 4 -> "A";
			case 5 -> "S";
			case 6 -> "SS";
			case 7 -> "SSS";
			default -> "Unknown: " + tier;
		};
	}

	public static int getFreeSlotsForPlayer(Player player) {
		int free = 0;
		for (ItemStack stack : player.getInventory().items) {
			if (ItemStack.matches(ItemStack.EMPTY, stack)) {
				free++;
			}
		}
		return free;
	}

	public static int stacksForItemAmount(ItemStack item, int amount) {
		return (int) Math.round(Math.ceil((double) amount / (double) item.getMaxStackSize()));
	}

	public static int getLootingLevel(Player player) {
		int lvl = 0;
		if (!ItemStack.isSameItem(player.getMainHandItem(), ItemStack.EMPTY) && player.getMainHandItem().isEnchanted()) {
			lvl += EnchantmentHelper.getTagEnchantmentLevel(player.registryAccess().holderOrThrow(Enchantments.LOOTING), player.getMainHandItem());
		}
		if (!ItemStack.isSameItem(player.getOffhandItem(), ItemStack.EMPTY) && player.getOffhandItem().isEnchanted()) {
			lvl += EnchantmentHelper.getTagEnchantmentLevel(player.registryAccess().holderOrThrow(Enchantments.LOOTING), player.getOffhandItem());
		}
		lvl += PlayerData.get(player).getNumberOfAbilitiesEquipped(ModAbilities.LUCKY_STRIKE);
		return lvl;
	}

	public static List<Component> appendEnchantmentNames(String text, ItemEnchantments enchantments) {
		List<Component> arrayList = new ArrayList<>();
		arrayList.add(Component.translatable(text));
		enchantments.keySet().forEach(enchantmentHolder -> {
			enchantmentHolder.value();
			arrayList.add(Component.literal(ChatFormatting.GRAY + "- " + Enchantment.getFullname(enchantmentHolder, enchantments.getLevel(enchantmentHolder)).getString()));
		});
		return arrayList;
	}

	public static int[] getRGBFromDec(int color) {
		int[] colors = new int[3];
		colors[0] = ((color >> 16) & 0xff);
		colors[1] = ((color >> 8) & 0xff);
		colors[2] = (color & 0xff);
		return colors;
	}

	public static int getDecFromRGB(int r, int g, int b) {
		return (256 * 256 * r + 256 * g + b);
	}

	public static boolean shouldRenderOverlay(Player player) {
		if (ModConfigs.showGuiToggle == ModConfigs.ShowType.HIDE) {
			return false;
		} else if (ModConfigs.showGuiToggle == ModConfigs.ShowType.WEAPON) {
			if (!(player.getMainHandItem().getItem() instanceof KeybladeItem || player.getOffhandItem().getItem() instanceof KeybladeItem || player.getMainHandItem().getItem() instanceof IOrgWeapon || player.getOffhandItem().getItem() instanceof IOrgWeapon)) {
				return false;
			}
		}
		if(player instanceof LocalPlayer lp){
			return !Minecraft.getInstance().options.hideGui;
		}
		return !player.hasEffect(ModMobEffects.KO);
	}

	public static BlockPos stringArrayToBlockPos(String[] temp) {
		return new BlockPos(getInt(temp[0]), getInt(temp[1]), getInt(temp[2]));
	}

	public static void reviveFromKO(LivingEntity entity) {
		entity.removeEffect(ModMobEffects.KO);
	}

	public static int getRandomMobLevel(Player player) {
		if (ModConfigs.mobLevelingUp) {
			PlayerData playerData = PlayerData.get(player);
			if (playerData == null)
				return 0;

			int avgLevel = playerData.getLevel();

			if (WorldData.get(player.getServer()).getPartyFromMember(player.getUUID()) != null) {
				Party p = WorldData.get(player.getServer()).getPartyFromMember(player.getUUID());
				int total = 0;
				int membersOnline = 0;
				for (Member m : p.getMembers()) {
					if (Utils.getPlayerByName(player.level(), m.getUsername().toLowerCase()) != null) {
						total += PlayerData.get(Utils.getPlayerByName(player.level(), m.getUsername().toLowerCase())).getLevel();
						membersOnline++;
					}
				}
				if (membersOnline == 0) {
					avgLevel = 1;
					KingdomKeys.LOGGER.warn("Party {} with 0 online members. Player={}, PartyMembers={}, Dimension={}", p.getName(), player.getGameProfile().getName(), p.getMembers().size(), player.level().dimension().location());
				} else {
					avgLevel = total / membersOnline;
				}
			}

			int level = avgLevel - player.level().random.nextInt(6) + 2;
			level = Utils.clamp(level, 1, 100);

			return level;
		}
		return 0;
	}

	public static ChatFormatting getLevelColor(Player player, int lvl) {
		PlayerData playerData = PlayerData.get(player);
		if (playerData == null)
			return ChatFormatting.WHITE;

		if (playerData.getLevel() > lvl) {
			return ChatFormatting.GREEN;
		} else if (playerData.getLevel() == lvl) {
			return ChatFormatting.YELLOW;
		} else {
			return ChatFormatting.RED;
		}
	}

	public static void playSoundToEveryone(ServerLevel level, SoundEvent sound, float vol, float pitch) {
		for (Player p : getAllPlayers(level.getServer())) {
			p.level().playSound(null, p.blockPosition(), sound, SoundSource.PLAYERS, vol, pitch);
		}

	}

	public static void summonKeyblade(Player player, boolean forceDesummon, ResourceLocation formToSummonFrom) {
		PlayerData playerData = PlayerData.get(player);

		if(playerData.isFormActive(ModDriveForms.ANTI))
			return;

		ItemStack heldStack = player.getMainHandItem();
		ItemStack offHeldStack = player.getOffhandItem();
		ItemStack chain = playerData.getEquippedKeychain(DriveForm.NONE);
		boolean useOrg = false;
		if (playerData.getAlignment() != OrgMember.NONE) {
			chain = playerData.getEquippedWeapon().copy();
			useOrg = true;
		}
		ItemStack extraChain = null;
		if (!formToSummonFrom.equals(DriveForm.NONE)) {
			if (playerData.getEquippedKeychains().containsKey(formToSummonFrom)) {
				extraChain = playerData.getEquippedKeychain(formToSummonFrom);
			}
		} else {
			if(playerData.isAbilityEquipped(ModAbilities.SYNCH_BLADE)) {
				if(playerData.getAlignment() == OrgMember.NONE || playerData.getEquippedWeapon() != null && playerData.getEquippedWeapon().getItem() instanceof KeybladeItem && playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE) != null) {
					extraChain = playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE);
				} else {
					extraChain = chain.copy();
					for(ItemStack weapon : playerData.getWeaponsUnlocked()) {
						if(ItemStack.isSameItem(weapon, extraChain)) {
							extraChain.applyComponents(weapon.getComponents());
							break;
						}
					}
				}
			}

		}
		//list of items created so first found keyblade can be removed in order to prevent it from finding the same keyblade twice
		List<ItemStack> potentialKeyblades = new ArrayList<>(player.getInventory().items); //items = 0-35
		potentialKeyblades.addAll(player.getInventory().armor); //armor added to keep indexing consistent 36-39
		potentialKeyblades.addAll(player.getInventory().offhand); //40
		int slotSummoned = -1;
		slotSummoned = Utils.findSummoned(potentialKeyblades, chain);

		if (slotSummoned != -1) {
			potentialKeyblades.set(slotSummoned, ItemStack.EMPTY); //set to empty instead of remove to keep indexing consistent
		}
		int extraSlotSummoned = -1;
		if (extraChain != null)
			extraSlotSummoned = Utils.findSummoned(potentialKeyblades, extraChain);
		ItemStack summonedStack = slotSummoned > -1 ? player.getInventory().getItem(slotSummoned) : ItemStack.EMPTY;
		ItemStack summonedExtraStack = extraSlotSummoned > -1 ? player.getInventory().getItem(extraSlotSummoned) : ItemStack.EMPTY;
		if (forceDesummon) {
			heldStack = summonedStack;
			if (!heldStack.isEmpty()) {
				offHeldStack = summonedExtraStack;
			}
		}
		if ((forceDesummon) || (!offHeldStack.isEmpty() && ItemStack.matches(offHeldStack, summonedExtraStack) && (Utils.hasKeybladeID(offHeldStack)))) {
			if (forceDesummon || (!heldStack.isEmpty() && (ItemStack.matches(heldStack, summonedStack)))) {
				if (hasKeybladeID(offHeldStack) && getKeybladeID(offHeldStack).equals(getKeybladeID(extraChain))) {
					extraChain.applyComponents(offHeldStack.getComponents());
					playerData.equipKeychain(formToSummonFrom, extraChain);
					player.getInventory().setItem(extraSlotSummoned, ItemStack.EMPTY);
					player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
				}
			}
		} else if (extraSlotSummoned > -1) {
			//SUMMON FROM ANOTHER SLOT
			Utils.swapStack(player.getInventory(), 40, extraSlotSummoned);
			player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);

		} else {
			if (extraChain != null) {
				if (!extraChain.isEmpty()) {
					if (offHeldStack.isEmpty()) {
						ItemStack keyblade;
						if(extraChain.getItem() instanceof IKeychain) {
							keyblade = new ItemStack(((IKeychain) extraChain.getItem()).toSummon());
						} else {
							keyblade = new ItemStack(extraChain.getItem());
						}
						keyblade.applyComponents(extraChain.getComponents());
						player.getInventory().setItem(40, keyblade);
						player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						spawnKeybladeParticles(player, InteractionHand.OFF_HAND);

					} else if (player.getInventory().getFreeSlot() > -1) {
						ItemStack keyblade;
						if(extraChain.getItem() instanceof IKeychain) {
							keyblade = new ItemStack(((IKeychain) extraChain.getItem()).toSummon());
						} else {
							keyblade = new ItemStack(extraChain.getItem());
						}
						keyblade.applyComponents(extraChain.getComponents());
						Utils.swapStack(player.getInventory(), player.getInventory().getFreeSlot(), 40);
						player.getInventory().setItem(40, keyblade);
						player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);
					}
				}
			}
		}
		if ((forceDesummon) || (!heldStack.isEmpty() && (Utils.hasKeybladeID(heldStack)))) {
			//DESUMMON
			if (Utils.hasKeybladeID(heldStack)) {
				if (heldStack.has(ModComponents.KEYBLADE_ID) && heldStack.get(ModComponents.KEYBLADE_ID).equals(chain.get(ModComponents.KEYBLADE_ID))) { //Keyblade user
					chain.set(ModComponents.KEYBLADE_ID, heldStack.get(ModComponents.KEYBLADE_ID));
					chain.applyComponents(heldStack.getComponents()); //Set enchantments from keyblade to keychain
					if (useOrg) {
						Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
						for(ItemStack weapon : weapons) {
							if(ItemStack.isSameItem(weapon, heldStack)) {
								weapon.applyComponents(heldStack.getComponents());
								break;
							}
						}
						playerData.setWeaponsUnlocked(weapons);
					} else {
						playerData.equipKeychain(DriveForm.NONE, chain);
					}
					if(playerData.isAbilityEquipped(ModAbilities.SYNCH_BLADE) && extraChain != null && !extraChain.is(Items.AIR)) {
						player.getInventory().setItem(40, ItemStack.EMPTY);
					}
					player.getInventory().setItem(slotSummoned, ItemStack.EMPTY);
					player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
				}
			}
		} else if (slotSummoned > -1) {
			//SUMMON FROM ANOTHER SLOT
			Utils.swapStack(player.getInventory(), player.getInventory().selected, slotSummoned);
			player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);
			spawnKeybladeParticles(player, InteractionHand.MAIN_HAND);

		} else {
			if (!chain.isEmpty()) {
				if (heldStack.isEmpty()) {
					ItemStack keyblade;
					if (!useOrg) {
						keyblade = new ItemStack(((IKeychain) chain.getItem()).toSummon());
						keyblade.applyComponents(chain.getComponents());
					} else {
						//Summon org
						keyblade = chain;
						Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
						for(ItemStack weapon : weapons) {
							if(ItemStack.isSameItem(weapon, keyblade)) {
								keyblade.applyComponents(weapon.getComponents());
								break;
							}
						}

					}
					//Summon when keyblade is unsummoned
					Utils.swapStack(player.getInventory(), player.getInventory().selected, player.getInventory().getFreeSlot());
					player.getInventory().setItem(player.getInventory().selected, keyblade);
					player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);
					spawnKeybladeParticles(player, InteractionHand.MAIN_HAND);

				} else if (player.getInventory().getFreeSlot() > -1) {
					ItemStack keyblade;
					if (!useOrg) {
						keyblade = new ItemStack(((IKeychain) chain.getItem()).toSummon());
						keyblade.applyComponents(chain.getComponents());
					} else { //Summon org weapon
						keyblade = chain;
						Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
						for(ItemStack weapon : weapons) {
							if(ItemStack.isSameItem(weapon, keyblade)) {
								keyblade.applyComponents(weapon.getComponents());
								break;
							}
						}
					}
					//When does it happen?
					Utils.swapStack(player.getInventory(), player.getInventory().getFreeSlot(), player.getInventory().selected);
					player.getInventory().setItem(player.getInventory().selected, keyblade);
					player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);

				}
			}
		}
	}

	private static void spawnKeybladeParticles(Player summoner, InteractionHand hand) {
		Vec3 userPos = new Vec3(summoner.getX(), summoner.getY(), summoner.getZ());
		Vec3 lHandCenter = new Vec3(-0.4, -1.3D, -0.38D);
		lHandCenter = lHandCenter.yRot((float) Math.toRadians(-summoner.yBodyRot));

		Vec3 rHandCenter = new Vec3(0.4, -1.3D, -0.38D);
		rHandCenter = rHandCenter.yRot((float) Math.toRadians(-summoner.yBodyRot));
		Vec3 v = null;
		if(hand == InteractionHand.MAIN_HAND) {
			v = userPos.add(-rHandCenter.x, rHandCenter.y, -rHandCenter.z);
		} else {
			v = userPos.add(-lHandCenter.x,lHandCenter.y, -lHandCenter.z);
		}
		((ServerLevel)summoner.level()).sendParticles(ParticleTypes.FIREWORK, v.x, summoner.getY() + 1, v.z, 80, 0,0,0, 0.2);

	}

	public record BlockPosBounds(BlockPos min, BlockPos max) {
		public BlockPosBounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
			this(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ));
		}

		public boolean isPlayerWithin(Player player) {
			return (int) player.getX() >= min.getX() && (int) player.getX() <= max.getX() && (int) player.getY() >= min.getY() && (int) player.getY() <= max.getY() && (int) player.getZ() >= min.getZ() && (int) player.getZ() <= max.getZ();
		}
	}

	public static boolean isTouchingWall(Player player) {
		if (player.onGround())
			return false;

		AABB box = player.getBoundingBox();
		double yShrink = 0.2; // ignore 20% of the block's top and bottom to avoid false positives
		AABB sideBox = new AABB(box.minX, box.minY + yShrink, box.minZ, box.maxX, box.maxY - yShrink, box.maxZ).inflate(0.05);

		Level level = player.level();

		return hasCollision(level, player, sideBox.move(0.1, 0, 0)) ||
				hasCollision(level, player, sideBox.move(-0.1, 0, 0)) ||
				hasCollision(level, player, sideBox.move(0, 0, 0.1)) ||
				hasCollision(level, player, sideBox.move(0, 0, -0.1));
	}

	private static boolean hasCollision(Level level, Player player, AABB box) {
		return level.getBlockCollisions(player, box).iterator().hasNext();
	}

	public static boolean isAprilFools() {
		if (!ModConfigs.seasonalEvents) return false;
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH) == Calendar.APRIL && calendar.get(Calendar.DAY_OF_MONTH) == 1;
	}

	public static void applyMobLevel(LivingEntity mob, int level) {
		if (level != 0) {
			AttributeInstance attack = mob.getAttribute(Attributes.ATTACK_DAMAGE);
			if (attack != null) {
				AttributeModifier attackModifier = attack.getModifier(Utils.mobLevelAttackModifier);
				if (attackModifier != null) {
					attack.removeModifier(attackModifier);
				}
				attack.addPermanentModifier(new AttributeModifier(Utils.mobLevelAttackModifier, level * ModConfigs.mobLevelStats / 500F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
			}
			AttributeInstance hp = mob.getAttribute(Attributes.MAX_HEALTH);
			if (hp != null) {
				AttributeModifier hpModifier = hp.getModifier(Utils.mobLevelHPModifier);
				if (hpModifier != null) {
					hp.removeModifier(hpModifier);
				}
				hp.addPermanentModifier(new AttributeModifier(Utils.mobLevelHPModifier, level * ModConfigs.mobLevelStats / 500F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
			}
		}
	}

	public static List<DriveForm> getVisibleDriveForms(Player player) {
		return ModDriveForms.registry.stream().filter(driveForm -> driveForm.displayInCommandMenu(player)).toList();
	}

	public static void removeEffects(Holder<MobEffect> effect, LivingEntity entity) {
		if(effect.is(ModMobEffects.GRAVITY)) {
			if (entity instanceof ServerPlayer player) {
				PacketHandler.sendTo(new SCRecalculateEyeHeight(), player);
				if (player.getForcedPose() != null && !PlayerData.get(player).getIsGliding()) {
					player.setForcedPose(null);
				}
			}
		}

		if(effect.is(ModMobEffects.STOP)){
			GlobalData globalData = GlobalData.get(entity);
			if (entity instanceof Mob) {
				((Mob) entity).setNoAi(false);
			}

			//Damage portion is handled in online/kingdomkeys/kingdomkeys/handler/EntityEvents.java:658
			//We iterate over the list and remove duplicates since for some reason the hit event fires twice
			ArrayList<Float> realDamage = new ArrayList<>();
			for(int i = 0; i < globalData.getStopDamage().size(); i++){
				if(i % 2 == 0){
					realDamage.add(globalData.getStopDamage().get(i));
				}
			}

			globalData.setStopDamage(realDamage);

			if (entity instanceof ServerPlayer)
				PacketHandler.sendTo(new SCSyncGlobalData(entity), (ServerPlayer) entity);
		}

		if(effect.is(ModMobEffects.ZERO_GRAVITY)){
			entity.setNoGravity(false);
		}

		if(effect != null && !entity.level().isClientSide()) {
			entity.level().getServer().getPlayerList().getPlayers().forEach(player -> {
				player.connection.send(new ClientboundRemoveMobEffectPacket(entity.getId(), effect));
			});
		}
	}

	public static void giveItems(ServerPlayer player, boolean showBig, List<ItemStack> itemStacks) {
		giveItems(player, showBig, itemStacks.toArray(new ItemStack[0]));
	}

	public static void giveItems(ServerPlayer player, boolean showBig, ItemStack... items) {
		Arrays.stream(items).forEach(stack -> {
			// tryToAddItem shrinks it, when it fails it returns the stack that was left
			ItemStack remaining = stack.copy();

			if (!tryToAddItem(player, remaining, false)) {
				//no space so add to overflow
				PlayerData playerData = PlayerData.get(player);
				if (playerData != null) {
					//you could say this is a stack overflow
					remaining = playerData.addToOverflow(remaining);
				}

				//overflow is full drop items
				if (!remaining.isEmpty()) {
					player.drop(remaining, true, false);
				}
			}
		});
		//send to client
		PacketHandler.sendTo(new SCDisplayGivenItems(Arrays.stream(items).toList(), showBig), player);
	}

	public static boolean tryToAddItem(Player player, ItemStack item, boolean simulate) {
		//first pass try to find any stackable slots
		if (simulate) {
			item = item.copy();
		}
		for (ItemStack stack : player.getInventory().items) {
			if (ItemStack.isSameItemSameComponents(item, stack)) {
				int remaining = stack.getMaxStackSize() - stack.getCount();
				if (remaining != 0) {
					//stack as much as possible
					int toAdd = Math.min(remaining, item.getCount());
					item.shrink(toAdd);
					if (!simulate) {
						stack.grow(toAdd);
					}
					if (item.getCount() == 0) {
						//no items left in stack
						return true;
					}
				}
			}
		}
		//second pass try to find any empty slots
		for (int i = 0; i < player.getInventory().items.size(); ++i) {
			if (player.getInventory().getItem(i).isEmpty()) {
				//free slot found
				if (!simulate) {
					player.getInventory().setItem(i, item);
				}
				return true;
			}
		}
		//no free space
		return false;
	}

	public static String createDescriptionKey(ItemStack stack) {
		ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
		return "item." + key.getNamespace() + "." + key.getPath() + ".desc";
	}

}