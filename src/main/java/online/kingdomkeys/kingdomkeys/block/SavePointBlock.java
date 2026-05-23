package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCDeleteSavePointScreenshot;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.network.stc.SCUpdateSavePoints;
import online.kingdomkeys.kingdomkeys.savepoint.ModSavePoints;
import online.kingdomkeys.kingdomkeys.savepoint.SavePoint;
import online.kingdomkeys.kingdomkeys.savepoint.SavePointData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

import javax.annotation.Nullable;
import java.util.List;

public class SavePointBlock extends BaseBlock implements EntityBlock, INoDataGen {
	private static final VoxelShape collisionShape = Block.box(1.0D, 0.0D, 1.0D, 16.0D, 1.0D, 16.0D);

	public static final EnumProperty<SavePointStorage.SavePointType> TIER = EnumProperty.create("tier", SavePointStorage.SavePointType.class);

	public SavePointBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(TIER, SavePointStorage.SavePointType.NORMAL));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		ItemStack held = pContext.getItemInHand();
		if (held.getItem() instanceof BlockItem blockItem) {
			if (blockItem.getBlock() == ModBlocks.savepoint.get()) {
				if (held.has(ModComponents.SAVE_POINT_TIER)) {
					return this.defaultBlockState().setValue(TIER, SavePointStorage.SavePointType.valueOf(held.get(ModComponents.SAVE_POINT_TIER)));
				}
			}
		}
		return this.defaultBlockState().setValue(TIER, SavePointStorage.SavePointType.NORMAL);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		if (stack.has(ModComponents.SAVE_POINT_TIER)) {
			if (tooltipComponents.getFirst() != null) {
				if (stack.get(ModComponents.SAVE_POINT_TIER).equals(SavePointStorage.SavePointType.LINKED.getSerializedName().toUpperCase())) {
					tooltipComponents.set(0, Component.translatable("block." + KingdomKeys.MODID + ".linked_savepoint"));
				} else if (stack.get(ModComponents.SAVE_POINT_TIER).equals(SavePointStorage.SavePointType.WARP.getSerializedName().toUpperCase())) {
					tooltipComponents.set(0, Component.translatable("block." + KingdomKeys.MODID + ".warp_point"));
				}

				addToList(tooltipComponents, Utils.translateToLocal("savepoint.healing"), stack.get(ModComponents.SAVE_POINT_HEAL));
				addToList(tooltipComponents, Utils.translateToLocal("savepoint.magic"), stack.get(ModComponents.SAVE_POINT_MAGIC));
				addToList(tooltipComponents, Utils.translateToLocal("savepoint.feed"), stack.get(ModComponents.SAVE_POINT_HUNGER));
				addToList(tooltipComponents, Utils.translateToLocal("savepoint.drive"), stack.get(ModComponents.SAVE_POINT_DRIVE));
				addToList(tooltipComponents, Utils.translateToLocal("savepoint.focus"), stack.get(ModComponents.SAVE_POINT_FOCUS));

			}
		}
		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
	}

	private void addToList(List<Component> tooltipComponents, String s, Integer val) {
		if(val != null)
			tooltipComponents.add(Component.translatable(Character.toUpperCase(s.charAt(0))+s.substring(1)+": "+val+"%"));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder);
		pBuilder.add(TIER);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return collisionShape;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return collisionShape;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return Block.box(0D, 0D, 0D, 16.0D, 2.0D, 16.0D);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
		if (pNewState.getBlock() != this) {
			if (!pLevel.isClientSide()) {
				SavepointTileEntity te = (SavepointTileEntity) pLevel.getBlockEntity(pPos);
				ItemStack stack = new ItemStack(this);
				stack.set(ModComponents.SAVE_POINT_TIER, pState.getValue(TIER).name());
				stack.set(ModComponents.SAVE_POINT_HEAL, te.getHeal());
				stack.set(ModComponents.SAVE_POINT_MAGIC, te.getMagic());
				stack.set(ModComponents.SAVE_POINT_HUNGER, te.getHunger());
				stack.set(ModComponents.SAVE_POINT_FOCUS, te.getFocus());
				stack.set(ModComponents.SAVE_POINT_DRIVE, te.getDrive());
				popResource(pLevel, pPos, stack);

				SavePointStorage storage = SavePointStorage.getStorage(pLevel.getServer());
				if (storage.savePointRegistered(te.getID())) {
					SavePointStorage.SavePoint removed = storage.getSavePoint(te.getID());
					storage.removeSavePoint(te.getID());
					for (Level level : pLevel.getServer().getAllLevels()) {
						for (Player playerFromList : level.players()) {
							PacketHandler.sendTo(new SCUpdateSavePoints(storage.getDiscoveredSavePoints(playerFromList)), (ServerPlayer) playerFromList);
							PacketHandler.sendTo(new SCDeleteSavePointScreenshot(removed.name(), removed.id()), (ServerPlayer) playerFromList);
						}
					}
				}
			}
		}
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (worldIn.isClientSide)
			return;

		BlockEntity be = worldIn.getBlockEntity(pos);
		if (!(be instanceof SavepointTileEntity savepoint))
			return;

		String tier = stack.get(ModComponents.SAVE_POINT_TIER);
		if (tier == null)
			return;

		worldIn.setBlockAndUpdate(pos, state.setValue(TIER, SavePointStorage.SavePointType.valueOf(tier)));

		savepoint.setHeal(stack.get(ModComponents.SAVE_POINT_HEAL));
		savepoint.setMagic(stack.get(ModComponents.SAVE_POINT_MAGIC));
		savepoint.setHunger(stack.get(ModComponents.SAVE_POINT_HUNGER));
		savepoint.setDrive(stack.get(ModComponents.SAVE_POINT_DRIVE));
		savepoint.setFocus(stack.get(ModComponents.SAVE_POINT_FOCUS));
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide)
			return ItemInteractionResult.SUCCESS;

		if (stack.isEmpty())
			return ItemInteractionResult.CONSUME;

		if (!(world.getBlockEntity(pos) instanceof SavepointTileEntity savepoint))
			return ItemInteractionResult.CONSUME;

		SavePointData data = getSavePointData(state);

		if (data == null) {
			player.displayClientMessage(Component.literal("Savepoint data not loaded"), true);
			return ItemInteractionResult.FAIL;
		}

		// Upgrade stats
		for (SavePointData.SavePointStat stat : SavePointData.SavePointStat.values()) {
			Item upgradeItem = getItemToUpgrade(data, stat);
			if (upgradeItem != null && stack.getItem() == upgradeItem) {
				if (stat == SavePointData.SavePointStat.TIER) {
					upgradeTier(world, pos, state, stack, player);
				} else {
					upgradeStat(player, stack, savepoint, data, stat);
				}

				return ItemInteractionResult.SUCCESS;
			}
		}

		player.displayClientMessage(Component.translatable("This item cannot be used to upgrade anything"), true);
		return ItemInteractionResult.CONSUME;
	}

	/**
	 * Gets the savepoint data associated with the current block tier
	 */
	@Nullable
	private SavePointData getSavePointData(BlockState state) {
		SavePoint savePoint = getSavePoint(state.getValue(TIER));
		if (savePoint == null)
			return null;
		return savePoint.getData();
	}

	/**
	 * Gets the item used to upgrade a specific savepoint stat
	 */
	@Nullable
	public static Item getItemToUpgrade(SavePointData data, SavePointData.SavePointStat stat) {
		if (data == null)
			return null;

		ResourceLocation rl = data.getMaterials().get(stat);

		if (rl == null)
			return null;

		return BuiltInRegistries.ITEM.get(rl);
	}

	/**
	 * Upgrades the specified savepoint stat if it is available for the current tier
	 */
	private void upgradeStat(Player player, ItemStack stack, SavepointTileEntity savepoint, SavePointData data, SavePointData.SavePointStat stat) {
		if (!data.restores(stat)) {
			String translated = Utils.translateToLocal(getTranslationKey(stat));
			String capitalized = Character.toUpperCase(translated.charAt(0)) + translated.substring(1);
			player.displayClientMessage(Component.translatable("savepoint.unavailable", capitalized), true);
			return;
		}

		int current = getSavepointValue(savepoint, stat);
		if (current <= 1) {
			player.displayClientMessage(Component.translatable("savepoint.maxed", Utils.translateToLocal(getTranslationKey(stat))), true);
			return;
		}

		stack.shrink(1);
		int newValue = Math.max(current - 4, 1);

		setSavepointValue(savepoint, stat, newValue);
		player.displayClientMessage(Component.translatable("savepoint.upgrade", Utils.translateToLocal(getTranslationKey(stat)), Utils.getSavepointPercent(newValue)), true);
	}

	/**
	 * Returns the translation key associated with a savepoint stat
	 */
	private String getTranslationKey(SavePointData.SavePointStat stat) {
		return switch (stat) {
			case HP -> "savepoint.healing";
			case MP -> "savepoint.magic";
			case HUNGER -> "savepoint.feed";
			case FOCUS -> "savepoint.focus";
			case DRIVE -> "savepoint.drive";
			case TIER -> "";
		};
	}

	private int getSavepointValue(SavepointTileEntity savepoint, SavePointData.SavePointStat stat) {
		return switch (stat) {
			case HP -> savepoint.getHeal();
			case MP -> savepoint.getMagic();
			case HUNGER -> savepoint.getHunger();
			case FOCUS -> savepoint.getFocus();
			case DRIVE -> savepoint.getDrive();
			case TIER -> 0;
		};
	}
	private void setSavepointValue(SavepointTileEntity savepoint, SavePointData.SavePointStat stat, int value) {
		switch (stat) {
			case HP -> savepoint.setHeal(value);
			case MP -> savepoint.setMagic(value);
			case HUNGER -> savepoint.setHunger(value);
			case FOCUS -> savepoint.setFocus(value);
			case DRIVE -> savepoint.setDrive(value);
		}
	}

	/**
	 * Upgrades the savepoint tier to the next available level.
	 */
	private void upgradeTier(Level world, BlockPos pos, BlockState state, ItemStack stack, Player player) {
		if (state.getValue(TIER) == SavePointStorage.SavePointType.WARP) {
			player.displayClientMessage(Component.translatable("savepoint.max_upgrade"), true);
			return;
		}

		stack.shrink(1);
		SavePointStorage.SavePointType nextTier = state.getValue(TIER) == SavePointStorage.SavePointType.NORMAL ? SavePointStorage.SavePointType.LINKED : SavePointStorage.SavePointType.WARP;
		BlockState newState = state.setValue(TIER, nextTier);

		world.setBlockAndUpdate(pos, newState);
		player.displayClientMessage(Component.translatable("savepoint.upgrade_type", nextTier.getSerializedName()), true);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (world.isClientSide)
			return;

		if (!(entity instanceof Player player))
			return;

		if (!(world.getBlockEntity(pos) instanceof SavepointTileEntity savepoint))
			return;

		SavePoint savePoint = getSavePoint(state.getValue(TIER));

		if (savePoint == null || savePoint.getData() == null) {
			player.displayClientMessage(Component.literal("Savepoint data not loaded"), true);
			return;
		}

		SavePointData data = savePoint.getData();
		if (hasInvalidValues(savepoint)) {
			player.displayClientMessage(Component.translatable("ERROR, this is probably an old savepoint, break and place it again to correct it"), true);
			return;
		}

		handleRestore(player, savepoint, data, world, pos, entity.tickCount);
		super.entityInside(state, world, pos, entity);
	}

	/**
	 * Checks if the savepoint contains invalid values
	 */
	private boolean hasInvalidValues(SavepointTileEntity savepoint) {
		return savepoint.getHeal() == 0 || savepoint.getHunger() == 0 || savepoint.getFocus() == 0 || savepoint.getMagic() == 0 || savepoint.getDrive() == 0;
	}

	/**
	 * Handles periodic stat restoration while the player stands on it
	 */
	private void handleRestore(Player player, SavepointTileEntity savepoint, SavePointData data, Level world, BlockPos pos, int tickCount) {
		for (SavePointData.SavePointStat stat : SavePointData.SavePointStat.values()) {
			if (!data.restores(stat))
				continue;

			int interval = getSavepointValue(savepoint, stat);
			if (interval <= 0)
				continue;

			if (tickCount % interval != 0)
				continue;

			if (restore(player, stat))
				showParticles(player, world, pos);
		}
	}

	/**
	 * Restores the specified stat for the player.
	 */
	private boolean restore(Player player, SavePointData.SavePointStat stat) {
		PlayerData playerData = PlayerData.get(player);
		if(playerData == null)
			return false;

		switch (stat) {
			case HP -> {
				if (player.getHealth() >= player.getMaxHealth())
					return false;

				player.heal(1);
				return true;
			}

			case HUNGER -> {
				if (player.getFoodData().getFoodLevel() >= 20)
					return false;

				player.getFoodData().eat(1, 1);
				return true;
			}

			case MP -> {
				if (playerData.getMP() >= playerData.getMaxMP())
					return false;

				playerData.addMP(1);
				PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				return true;
			}

			case FOCUS -> {
				if (playerData.getFocus() >= playerData.getMaxFocus())
					return false;

				playerData.addFocus(1);
				PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				return true;
			}

			case DRIVE -> {
				if (playerData.getDP() >= playerData.getMaxDP())
					return false;

				playerData.addDP(5);
				PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
				return true;
			}

			case TIER -> {
				return false;
			}
		}
		return false;
	}

	public void showParticles(Player player, Level world, BlockPos pos){
		if (player.tickCount % 5 == 0) {
			player.playSound(ModSounds.savepoint.get(), 1F, 1F);
		}
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.2, pos.getY() + 2.5, pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 2.5, pos.getZ() + 0.2, 0.0D, 0.0D, 0.0D);
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.8, pos.getY() + 2.5, pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
		world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 2.5, pos.getZ() + 0.8, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return type == ModEntities.TYPE_SAVEPOINT.get() ? SavepointTileEntity::tick : null;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return ModEntities.TYPE_SAVEPOINT.get().create(pPos, pState);
	}

	public static SavePoint getSavePoint(SavePointStorage.SavePointType type) {
		return switch(type) {
			case NORMAL -> ModSavePoints.NORMAL;
			case LINKED -> ModSavePoints.LINKED;
			case WARP -> ModSavePoints.WARP;
		};
	}
}
