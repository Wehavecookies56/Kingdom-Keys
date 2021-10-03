package online.kingdomkeys.kingdomkeys.entity;

import static online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType.HEARTLESS_EMBLEM;
import static online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType.HEARTLESS_PUREBLOOD;
import static online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType.NOBODY;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.client.render.block.BlastBloxRenderer;
import online.kingdomkeys.kingdomkeys.client.render.block.MoogleProjectorRenderer;
import online.kingdomkeys.kingdomkeys.client.render.block.PairBloxRenderer;
import online.kingdomkeys.kingdomkeys.client.render.block.PedestalRenderer;
import online.kingdomkeys.kingdomkeys.client.render.block.SoAPlatformRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.ArrowgunShotEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.AssassinRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.BombRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.DarkballRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.DirePlantRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.DuskRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.ElementalMusicalHeartlessRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.GigaShadowRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.GummiShipEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.LargeBodyRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.MegaShadowRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.MoogleRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.NobodyCreeperRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.OrgPortalEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.SeedBulletRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.ShadowGlobRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.ShadowRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.drops.DriveOrbRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.drops.FocusOrbRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.drops.HPOrbRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.drops.MPOrbRenderer;
import online.kingdomkeys.kingdomkeys.client.render.entity.drops.MunnyRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.HeartEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.InvisibleEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.magic.ThunderBoltEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.org.ArrowRainCoreEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.org.ChakramEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.org.LanceEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.org.LaserCircleEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.org.LaserDomeEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.org.LaserDomeShotEntityRenderer;
import online.kingdomkeys.kingdomkeys.client.render.shotlock.VolleyShotlockShotEntityRenderer;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.block.BlastBloxEntity;
import online.kingdomkeys.kingdomkeys.entity.block.GummiEditorTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.MagicalChestTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.MagnetBloxTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.MoogleProjectorTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.OrgPortalTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.PairBloxEntity;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.SoAPlatformTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.SoRCoreTileEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzazaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FiraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FiragaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FirazaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.GravigaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.GraviraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.GravityEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnegaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagneraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnetEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThundagaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThundaraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThundazaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderBoltEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WaterEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WateraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WatergaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WaterzaEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.AssassinEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.BlueRhapsodyEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.DarkballEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.DetonatorEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.DirePlantEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.DuskEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.GigaShadowEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.GreenRequiemEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.LargeBodyEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.MegaShadowEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.MinuteBombEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.NobodyCreeperEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.RedNocturneEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowGlobEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.SkaterBombEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.StormBombEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.YellowOperaEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.ArrowRainCoreEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.ArrowgunShotEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.ChakramEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.LanceEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.LaserCircleCoreEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.LaserDomeCoreEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.LaserDomeShotEntity;
import online.kingdomkeys.kingdomkeys.entity.shotlock.BaseShotlockShotEntity;
import online.kingdomkeys.kingdomkeys.entity.shotlock.DarkVolleyCoreEntity;
import online.kingdomkeys.kingdomkeys.entity.shotlock.PrismRainCoreEntity;
import online.kingdomkeys.kingdomkeys.entity.shotlock.RagnarokCoreEntity;
import online.kingdomkeys.kingdomkeys.entity.shotlock.RagnarokShotEntity;
import online.kingdomkeys.kingdomkeys.entity.shotlock.VolleyShotEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.proxy.ProxyClient;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, KingdomKeys.MODID);

    public static Set<EntityType<? extends Entity>> pureblood = new HashSet<EntityType<?>>();
    public static Set<EntityType<? extends Entity>> emblem = new HashSet<EntityType<?>>();
    public static Set<EntityType<? extends Entity>> nobody = new HashSet<EntityType<?>>();
    public static Set<EntityType<? extends Entity>> npc = new HashSet<EntityType<?>>();

    public static final RegistryObject<EntityType<BlastBloxEntity>> TYPE_BLAST_BLOX = createEntityType(BlastBloxEntity::new, BlastBloxEntity::new, EntityClassification.MISC,"blast_blox_primed", 0.98F, 0.98F);

    public static final RegistryObject<EntityType<PairBloxEntity>> TYPE_PAIR_BLOX = createEntityType(PairBloxEntity::new, PairBloxEntity::new, EntityClassification.MISC,"pair_blox", 1F, 1F);
    public static final RegistryObject<EntityType<MunnyEntity>> TYPE_MUNNY = createEntityType(MunnyEntity::new, MunnyEntity::new, EntityClassification.MISC,"entity_munny", 0.25F, 0.25F);
    public static final RegistryObject<EntityType<HPOrbEntity>> TYPE_HPORB = createEntityType(HPOrbEntity::new, HPOrbEntity::new, EntityClassification.MISC,"entity_hp_orb", 0.25F, 0.25F);
    public static final RegistryObject<EntityType<MPOrbEntity>> TYPE_MPORB = createEntityType(MPOrbEntity::new, MPOrbEntity::new, EntityClassification.MISC,"entity_mp_orb", 0.25F, 0.25F);
    public static final RegistryObject<EntityType<DriveOrbEntity>> TYPE_DRIVEORB = createEntityType(DriveOrbEntity::new, DriveOrbEntity::new, EntityClassification.MISC,"entity_drive_orb", 0.25F, 0.25F);
    public static final RegistryObject<EntityType<FocusOrbEntity>> TYPE_FOCUSORB = createEntityType(FocusOrbEntity::new, FocusOrbEntity::new, EntityClassification.MISC,"entity_focus_orb", 0.25F, 0.25F);

    public static final RegistryObject<EntityType<FireEntity>> TYPE_FIRE = createEntityType(FireEntity::new, FireEntity::new, EntityClassification.MISC,"entity_fire", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<FiraEntity>> TYPE_FIRA = createEntityType(FiraEntity::new, FiraEntity::new, EntityClassification.MISC,"entity_fira", 0.8F, 0.8F);
    public static final RegistryObject<EntityType<FiragaEntity>> TYPE_FIRAGA = createEntityType(FiragaEntity::new, FiragaEntity::new, EntityClassification.MISC,"entity_firaga", 1.2F, 1.2F);
    public static final RegistryObject<EntityType<FirazaEntity>> TYPE_FIRAZA = createEntityType(FirazaEntity::new, FirazaEntity::new, EntityClassification.MISC,"entity_firaza", 1.2F, 1.2F);
    
    public static final RegistryObject<EntityType<BlizzardEntity>> TYPE_BLIZZARD = createEntityType(BlizzardEntity::new, BlizzardEntity::new, EntityClassification.MISC,"entity_blizzard", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<BlizzazaEntity>> TYPE_BLIZZAZA = createEntityType(BlizzazaEntity::new, BlizzazaEntity::new, EntityClassification.MISC,"entity_blizzaza", 0.5F, 0.5F);

    public static final RegistryObject<EntityType<WaterEntity>> TYPE_WATER = createEntityType(WaterEntity::new, WaterEntity::new, EntityClassification.MISC,"entity_water", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<WateraEntity>> TYPE_WATERA = createEntityType(WateraEntity::new, WateraEntity::new, EntityClassification.MISC,"entity_watera", 0.8F, 0.8F);
    public static final RegistryObject<EntityType<WatergaEntity>> TYPE_WATERGA = createEntityType(WatergaEntity::new, WatergaEntity::new, EntityClassification.MISC,"entity_waterga", 1F, 1F);
    public static final RegistryObject<EntityType<WaterzaEntity>> TYPE_WATERZA = createEntityType(WaterzaEntity::new, WaterzaEntity::new, EntityClassification.MISC,"entity_waterza", 1F, 1F);
    
    public static final RegistryObject<EntityType<ThunderEntity>> TYPE_THUNDER = createEntityType(ThunderEntity::new, ThunderEntity::new, EntityClassification.MISC,"entity_thunder", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ThundaraEntity>> TYPE_THUNDARA = createEntityType(ThundaraEntity::new, ThundaraEntity::new, EntityClassification.MISC,"entity_thundara", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ThundagaEntity>> TYPE_THUNDAGA = createEntityType(ThundagaEntity::new, ThundagaEntity::new, EntityClassification.MISC,"entity_thundaga", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ThundazaEntity>> TYPE_THUNDAZA = createEntityType(ThundazaEntity::new, ThundazaEntity::new, EntityClassification.MISC,"entity_thundaza", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ThunderBoltEntity>> TYPE_THUNDERBOLT = createEntityType(ThunderBoltEntity::new, ThunderBoltEntity::new, EntityClassification.MISC,"entity_thunderbolt", 0.5F, 0.5F);
    
    public static final RegistryObject<EntityType<MagnetEntity>> TYPE_MAGNET = createEntityType(MagnetEntity::new, MagnetEntity::new, EntityClassification.MISC,"entity_magnet", 1F, 1F);
    public static final RegistryObject<EntityType<MagneraEntity>> TYPE_MAGNERA = createEntityType(MagneraEntity::new, MagneraEntity::new, EntityClassification.MISC,"entity_magnera", 1.5F, 1.5F);
    public static final RegistryObject<EntityType<MagnegaEntity>> TYPE_MAGNEGA = createEntityType(MagnegaEntity::new, MagnegaEntity::new, EntityClassification.MISC,"entity_magnega", 2F, 2F);
    
    public static final RegistryObject<EntityType<GravityEntity>> TYPE_GRAVITY = createEntityType(GravityEntity::new, GravityEntity::new, EntityClassification.MISC,"entity_gravity", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<GraviraEntity>> TYPE_GRAVIRA = createEntityType(GraviraEntity::new, GraviraEntity::new, EntityClassification.MISC,"entity_gravira", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<GravigaEntity>> TYPE_GRAVIGA = createEntityType(GravigaEntity::new, GravigaEntity::new, EntityClassification.MISC,"entity_graviga", 0.5F, 0.5F);

    public static final RegistryObject<EntityType<SeedBulletEntity>> TYPE_SEED_BULLET = createEntityType(SeedBulletEntity::new, SeedBulletEntity::new, EntityClassification.MISC,"seed_bullet", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ArrowgunShotEntity>> TYPE_ARROWGUN_SHOT = createEntityType(ArrowgunShotEntity::new, ArrowgunShotEntity::new, EntityClassification.MISC,"arrowgun_shot", 0.1F, 0.1F);

    public static final RegistryObject<EntityType<OrgPortalEntity>> TYPE_ORG_PORTAL = createEntityType(OrgPortalEntity::new, OrgPortalEntity::new, EntityClassification.MISC,"entity_org_portal", 1F, 3.5F);
    
    public static final RegistryObject<EntityType<ChakramEntity>> TYPE_CHAKRAM = createEntityType(ChakramEntity::new, ChakramEntity::new, EntityClassification.MISC,"entity_chakram", 1.3F, 0.5F);
    public static final RegistryObject<EntityType<LanceEntity>> TYPE_LANCE = createEntityType(LanceEntity::new, LanceEntity::new, EntityClassification.MISC,"entity_lance", 0.5F, 0.5F);
    
    public static final RegistryObject<EntityType<HeartEntity>> TYPE_HEART = createEntityType(HeartEntity::new, HeartEntity::new, EntityClassification.MISC, "heart", 1F, 1F);

    //Mobs
    public static final RegistryObject<EntityType<MoogleEntity>> TYPE_MOOGLE = createEntityType(MoogleEntity::new, MoogleEntity::new, EntityClassification.AMBIENT, "moogle", 0.6F, 1.5F, MobType.NPC, 0xDACAB0, 0xC50033);
    
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_SHADOW = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.MONSTER, "shadow", 0.5F, 0.7F, HEARTLESS_PUREBLOOD, 0x000000, 0xFFFF00);
    public static final RegistryObject<EntityType<MegaShadowEntity>> TYPE_MEGA_SHADOW = createEntityType(MegaShadowEntity::new, MegaShadowEntity::new, EntityClassification.MONSTER, "mega_shadow", 1.5F, 1.7F, HEARTLESS_PUREBLOOD, 0x000000, 0xAAAA00);
    public static final RegistryObject<EntityType<GigaShadowEntity>> TYPE_GIGA_SHADOW = createEntityType(GigaShadowEntity::new, GigaShadowEntity::new, EntityClassification.MONSTER, "giga_shadow", 2.5F, 2.7F, HEARTLESS_PUREBLOOD, 0x000000, 0x666600);
    public static final RegistryObject<EntityType<DarkballEntity>> TYPE_DARKBALL = createEntityType(DarkballEntity::new, DarkballEntity::new, EntityClassification.MONSTER, "darkball", 1.5F, 2F, HEARTLESS_PUREBLOOD, 0x000000, 0x6600FF);
    public static final RegistryObject<EntityType<ShadowGlobEntity>> TYPE_SHADOW_GLOB = createEntityType(ShadowGlobEntity::new, ShadowGlobEntity::new, EntityClassification.MONSTER, "shadow_glob", 1F, 1F, HEARTLESS_PUREBLOOD, Color.BLACK.getRGB(), 0x312ba0);
    
    //Emblems
    public static final RegistryObject<EntityType<MinuteBombEntity>> TYPE_MINUTE_BOMB = createEntityType(MinuteBombEntity::new, MinuteBombEntity::new, EntityClassification.MONSTER, "minute_bomb", 0.6F, 1.3F, HEARTLESS_EMBLEM, 0x020030, 0x8B4513);
    public static final RegistryObject<EntityType<SkaterBombEntity>> TYPE_SKATER_BOMB = createEntityType(SkaterBombEntity::new, SkaterBombEntity::new, EntityClassification.MONSTER, "skater_bomb", 0.6F, 1.3F, HEARTLESS_EMBLEM, 0x020030, 0xAAAAFF);
    public static final RegistryObject<EntityType<StormBombEntity>> TYPE_STORM_BOMB = createEntityType(StormBombEntity::new, StormBombEntity::new, EntityClassification.MONSTER, "storm_bomb", 0.6F, 1.3F, HEARTLESS_EMBLEM, 0x020030, Color.CYAN.getRGB());
    public static final RegistryObject<EntityType<DetonatorEntity>> TYPE_DETONATOR = createEntityType(DetonatorEntity::new, DetonatorEntity::new, EntityClassification.MONSTER, "detonator", 0.6F, 1.3F, HEARTLESS_EMBLEM, 0x020030, Color.RED.getRGB());
    public static final RegistryObject<EntityType<RedNocturneEntity>> TYPE_RED_NOCTURNE = createEntityTypeImmuneToFire(RedNocturneEntity::new, RedNocturneEntity::new, EntityClassification.MONSTER, "red_nocturne", 1.0F,  2.0F, HEARTLESS_EMBLEM, Color.RED.getRGB(), Color.YELLOW.getRGB());
    public static final RegistryObject<EntityType<BlueRhapsodyEntity>> TYPE_BLUE_RHAPSODY = createEntityType(BlueRhapsodyEntity::new, BlueRhapsodyEntity::new, EntityClassification.MONSTER, "blue_rhapsody", 1.0F, 2.0F, HEARTLESS_EMBLEM, Color.BLUE.getRGB(), Color.YELLOW.getRGB());
    public static final RegistryObject<EntityType<YellowOperaEntity>> TYPE_YELLOW_OPERA = createEntityType(YellowOperaEntity::new, YellowOperaEntity::new, EntityClassification.MONSTER, "yellow_opera", 1.0F, 2.0F, HEARTLESS_EMBLEM, Color.YELLOW.getRGB(), Color.YELLOW.getRGB());
    public static final RegistryObject<EntityType<GreenRequiemEntity>> TYPE_GREEN_REQUIEM = createEntityType(GreenRequiemEntity::new, GreenRequiemEntity::new, EntityClassification.MONSTER, "green_requiem", 1.0F, 2.0F, HEARTLESS_EMBLEM, Color.GREEN.getRGB(), Color.YELLOW.getRGB());
    public static final RegistryObject<EntityType<GummiShipEntity>> TYPE_GUMMI_SHIP = createEntityType(GummiShipEntity::new, GummiShipEntity::new, EntityClassification.MISC, "gummi_ship", 5.0F, 5.0F);

    //TODO update textures to work with newer model, make magic for
    /*
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_SILVER_ROCK = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.MONSTER, "silver_rock", 1.0F, 1.0F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_CRIMSON_JAZZ = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.MONSTER, "crimson_jazz", 1.0F, 1.0F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_EMERALD_BLUES = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.MONSTER, "emerald_blues", 1.0F, 1.0F);
    */

    public static final RegistryObject<EntityType<LargeBodyEntity>> TYPE_LARGE_BODY = createEntityType(LargeBodyEntity::new, LargeBodyEntity::new, EntityClassification.MONSTER, "large_body", 1.3F, 1.6F, HEARTLESS_EMBLEM, 0x4d177c, 0x29014c);
    //TODO make AI
    //public static final RegistryObject<EntityType<ShadowEntity>> TYPE_WHITE_MUSHROOM = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.MONSTER, "white_mushroom", 0.5F, 0.5F, HEARTLESS_EMBLEM, 0xe3e5e8, 0xffffff);
    public static final RegistryObject<EntityType<DirePlantEntity>> TYPE_DIRE_PLANT = createEntityType(DirePlantEntity::new, DirePlantEntity::new, EntityClassification.MONSTER, "dire_plant", 0.75F, 1.5F, HEARTLESS_EMBLEM, 0x4ba04e, 0xedc2c2);

    //Nobodies
    public static final RegistryObject<EntityType<NobodyCreeperEntity>> TYPE_NOBODY_CREEPER = createEntityType(NobodyCreeperEntity::new, NobodyCreeperEntity::new, EntityClassification.MONSTER, "nobody_creeper", 1F, 1.5F, NOBODY, 0xb8bdc4, 0xfcfcfc);
    public static final RegistryObject<EntityType<DuskEntity>> TYPE_DUSK = createEntityType(DuskEntity::new, DuskEntity::new, EntityClassification.MONSTER, "dusk", 1F, 1.5F, NOBODY, 0xb8bdc4, 0xfcfcfc);
    public static final RegistryObject<EntityType<AssassinEntity>> TYPE_ASSASSIN = createEntityType(AssassinEntity::new, AssassinEntity::new, EntityClassification.MONSTER, "assassin", 1F, 1.5F, NOBODY, 0xc9c9c9, 0xd4ccff);

    //Limits
    public static final RegistryObject<EntityType<LaserCircleCoreEntity>> TYPE_LASER_CIRCLE = createEntityType(LaserCircleCoreEntity::new, LaserCircleCoreEntity::new, EntityClassification.MISC,"entity_laser_circle_core", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<LaserDomeCoreEntity>> TYPE_LASER_DOME = createEntityType(LaserDomeCoreEntity::new, LaserDomeCoreEntity::new, EntityClassification.MISC,"entity_laser_dome_core", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<LaserDomeShotEntity>> TYPE_LASER_SHOT = createEntityType(LaserDomeShotEntity::new, LaserDomeShotEntity::new, EntityClassification.MISC,"entity_laser_dome_shot", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ArrowRainCoreEntity>> TYPE_ARROW_RAIN = createEntityType(ArrowRainCoreEntity::new, ArrowRainCoreEntity::new, EntityClassification.MISC,"entity_arrow_rain_core", 0.5F, 0.5F);
    
	public static final RegistryObject<EntityType<DarkVolleyCoreEntity>> TYPE_SHOTLOCK_DARK_VOLLEY = createEntityType(DarkVolleyCoreEntity::new, DarkVolleyCoreEntity::new, EntityClassification.MISC, "entity_shotlock_volley_core", 0.5F, 0.5F);
	public static final RegistryObject<EntityType<RagnarokCoreEntity>> TYPE_SHOTLOCK_CIRCULAR = createEntityType(RagnarokCoreEntity::new, RagnarokCoreEntity::new, EntityClassification.MISC, "entity_shotlock_circular_core", 0.5F, 0.5F);
	public static final RegistryObject<EntityType<PrismRainCoreEntity>> TYPE_PRISM_RAIN = createEntityType(PrismRainCoreEntity::new, PrismRainCoreEntity::new, EntityClassification.MISC, "entity_shotlock_prism_rain", 0.5F, 0.5F);

	public static final RegistryObject<EntityType<BaseShotlockShotEntity>> TYPE_VOLLEY_SHOTLOCK_SHOT = createEntityType(VolleyShotEntity::new, VolleyShotEntity::new, EntityClassification.MISC, "entity_volley_shotlock_shot", 0.5F, 0.5F);
	public static final RegistryObject<EntityType<BaseShotlockShotEntity>> TYPE_RAGNAROK_SHOTLOCK_SHOT = createEntityType(RagnarokShotEntity::new, RagnarokShotEntity::new, EntityClassification.MISC,"entity_ragnarok_shotlock_shot", 0.5F, 0.5F);

    /**
     * Helper method to create a new EntityType and set the registry name
     * @param factory The entity type factory
     * @param clientFactory The client factory
     * @param classification The classification of the entity
     * @param name The registry name of the entity
     * @param sizeX The X size of the entity
     * @param sizeY The Y size of the entity
     * @param <T> The entity type
     * @return The EntityType created
     */
    public static <T extends Entity, M extends EntityType<T>>RegistryObject<EntityType<T>> createEntityType(EntityType.IFactory<T> factory, BiFunction<FMLPlayMessages.SpawnEntity, World, T> clientFactory, EntityClassification classification, String name, float sizeX, float sizeY, EntityHelper.MobType group, int color1, int color2) {
        EntityType<T> type = EntityType.Builder.create(factory, classification)
                .setCustomClientFactory(clientFactory)
                .setShouldReceiveVelocityUpdates(true)
                .setUpdateInterval(1)
                .setTrackingRange(128)
                .size(sizeX, sizeY)
                .build(name);
        if (group != null) {
        	addToGroup(group, type);
            ModItems.ITEMS.register(name+"_spawn_egg", () -> new SpawnEggItem(type, color1, color2, new Item.Properties().group(ItemGroup.MISC)));
        }
        
        return ENTITIES.register(name, () -> type);
    }
    
    public static <T extends Entity, M extends EntityType<T>>RegistryObject<EntityType<T>> createEntityType(EntityType.IFactory<T> factory, BiFunction<FMLPlayMessages.SpawnEntity, World, T> clientFactory, EntityClassification classification, String name, float sizeX, float sizeY) {
        return createEntityType(factory,clientFactory,classification,name,sizeX,sizeY, null, 0,0);
    }

    public static <T extends Entity, M extends EntityType<T>>RegistryObject<EntityType<T>> createEntityTypeImmuneToFire(EntityType.IFactory<T> factory, BiFunction<FMLPlayMessages.SpawnEntity, World, T> clientFactory, EntityClassification classification, String name, float sizeX, float sizeY, EntityHelper.MobType group, int color1, int color2) {
        EntityType<T> type = EntityType.Builder.create(factory, classification)
                .setCustomClientFactory(clientFactory)
                .setShouldReceiveVelocityUpdates(true)
                .setUpdateInterval(1)
                .setTrackingRange(128)
                .size(sizeX, sizeY)
                .immuneToFire()
                .build(name);
        if (group != null) {
        	addToGroup(group, type);
            ModItems.ITEMS.register(name+"_spawn_egg", () -> new SpawnEggItem(type, color1, color2, new Item.Properties().group(ItemGroup.MISC)));
        }

        return ENTITIES.register(name, () -> type);
    }
    
    public static <T extends Entity, M extends EntityType<T>>RegistryObject<EntityType<T>> createEntityTypeImmuneToFire(EntityType.IFactory<T> factory, BiFunction<FMLPlayMessages.SpawnEntity, World, T> clientFactory, EntityClassification classification, String name, float sizeX, float sizeY) {
    	RegistryObject<EntityType<T>> entity = createEntityTypeImmuneToFire(factory,clientFactory,classification,name,sizeX,sizeY, null, 0, 0);
        return entity;
    }

    public static void addToGroup(EntityHelper.MobType group, EntityType<?> type) {
        switch(group) {
            case HEARTLESS_PUREBLOOD:
                pureblood.add(type);
                break;
            case HEARTLESS_EMBLEM:
                emblem.add(type);
                break;
            case NOBODY:
                nobody.add(type);
                break;
            case NPC:
            	npc.add(type);
        }
    }

    /**
     * Register the render classes for the entities, called in {@link ProxyClient#registerModels(ModelRegistryEvent)}  }
     */
    @OnlyIn(Dist.CLIENT)
    public static void registerModels() {
        //Entities
        RenderingRegistry.registerEntityRenderingHandler(TYPE_BLAST_BLOX.get(), BlastBloxRenderer.FACTORY);

        RenderingRegistry.registerEntityRenderingHandler(TYPE_PAIR_BLOX.get(), PairBloxRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MUNNY.get(), MunnyRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_HPORB.get(), HPOrbRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MPORB.get(), MPOrbRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_DRIVEORB.get(), DriveOrbRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_FOCUSORB.get(), FocusOrbRenderer.FACTORY);

        
        RenderingRegistry.registerEntityRenderingHandler(TYPE_FIRE.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_FIRA.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_FIRAGA.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_FIRAZA.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_BLIZZARD.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_BLIZZAZA.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_THUNDER.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_THUNDARA.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_THUNDAGA.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_THUNDAZA.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_THUNDERBOLT.get(), ThunderBoltEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MAGNET.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MAGNERA.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MAGNEGA.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_WATER.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_WATERA.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_WATERGA.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_WATERZA.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_GRAVITY.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_GRAVIRA.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_GRAVIGA.get(), InvisibleEntityRenderer.FACTORY);
        
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MOOGLE.get(), MoogleRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_SHADOW.get(), ShadowRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MEGA_SHADOW.get(), MegaShadowRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_GIGA_SHADOW.get(), GigaShadowRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_DARKBALL.get(), DarkballRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_SHADOW_GLOB.get(), ShadowGlobRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_LARGE_BODY.get(), LargeBodyRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_MINUTE_BOMB.get(), BombRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_SKATER_BOMB.get(), BombRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_STORM_BOMB.get(), BombRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_DETONATOR.get(), BombRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_NOBODY_CREEPER.get(), NobodyCreeperRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_RED_NOCTURNE.get(), ElementalMusicalHeartlessRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_BLUE_RHAPSODY.get(), ElementalMusicalHeartlessRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_YELLOW_OPERA.get(), ElementalMusicalHeartlessRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_GREEN_REQUIEM.get(), ElementalMusicalHeartlessRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_DUSK.get(), DuskRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_ASSASSIN.get(), AssassinRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_DIRE_PLANT.get(), DirePlantRenderer.FACTORY);

        RenderingRegistry.registerEntityRenderingHandler(TYPE_ORG_PORTAL.get(), OrgPortalEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_HEART.get(), HeartEntityRenderer.FACTORY);

        RenderingRegistry.registerEntityRenderingHandler(TYPE_CHAKRAM.get(), ChakramEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_LANCE.get(), LanceEntityRenderer.FACTORY);

        RenderingRegistry.registerEntityRenderingHandler(TYPE_SEED_BULLET.get(), SeedBulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_ARROWGUN_SHOT.get(), ArrowgunShotEntityRenderer.FACTORY);

        RenderingRegistry.registerEntityRenderingHandler(TYPE_LASER_CIRCLE.get(), LaserCircleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_LASER_DOME.get(), LaserDomeEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_LASER_SHOT.get(), LaserDomeShotEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_ARROW_RAIN.get(), ArrowRainCoreEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_SHOTLOCK_DARK_VOLLEY.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_SHOTLOCK_CIRCULAR.get(), InvisibleEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_VOLLEY_SHOTLOCK_SHOT.get(), VolleyShotlockShotEntityRenderer.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_RAGNAROK_SHOTLOCK_SHOT.get(), VolleyShotlockShotEntityRenderer.FACTORY);
        
        RenderingRegistry.registerEntityRenderingHandler(TYPE_GUMMI_SHIP.get(), GummiShipEntityRenderer::new);
        
        //Tile Entities
        
        ClientRegistry.bindTileEntityRenderer(TYPE_PEDESTAL.get(), PedestalRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TYPE_MOOGLE_PROJECTOR.get(), MoogleProjectorRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TYPE_SOA_PLATFORM.get(), SoAPlatformRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TYPE_PEDESTAL.get(), PedestalRenderer::new);

    }

    public static void registerAttributes() {
        GlobalEntityTypeAttributes.put(TYPE_ASSASSIN.get(), AssassinEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_BLUE_RHAPSODY.get(), BlueRhapsodyEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_DARKBALL.get(), DarkballEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_DETONATOR.get(), DetonatorEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_DIRE_PLANT.get(), DirePlantEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_DUSK.get(), DuskEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_GIGA_SHADOW.get(), GigaShadowEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_GREEN_REQUIEM.get(), GreenRequiemEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_LARGE_BODY.get(), LargeBodyEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_MEGA_SHADOW.get(), MegaShadowEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_MINUTE_BOMB.get(), MinuteBombEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_MOOGLE.get(), MoogleEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_NOBODY_CREEPER.get(), NobodyCreeperEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_RED_NOCTURNE.get(), RedNocturneEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_SHADOW.get(), ShadowEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_SHADOW_GLOB.get(), ShadowGlobEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_SKATER_BOMB.get(), SkaterBombEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_STORM_BOMB.get(), StormBombEntity.registerAttributes().create());
        GlobalEntityTypeAttributes.put(TYPE_YELLOW_OPERA.get(), YellowOperaEntity.registerAttributes().create());
        //GlobalEntityTypeAttributes.put(TYPE_GUMMI_SHIP.get(), GummiShipEntity.registerAttributes().create());

    }
    

    public static void registerPlacements() {
    	EntitySpawnPlacementRegistry.register(TYPE_ASSASSIN.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_BLUE_RHAPSODY.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        EntitySpawnPlacementRegistry.register(TYPE_DARKBALL.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        EntitySpawnPlacementRegistry.register(TYPE_DETONATOR.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        EntitySpawnPlacementRegistry.register(TYPE_DIRE_PLANT.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        EntitySpawnPlacementRegistry.register(TYPE_DUSK.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_GIGA_SHADOW.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        EntitySpawnPlacementRegistry.register(TYPE_GREEN_REQUIEM.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        EntitySpawnPlacementRegistry.register(TYPE_LARGE_BODY.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        EntitySpawnPlacementRegistry.register(TYPE_MEGA_SHADOW.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        EntitySpawnPlacementRegistry.register(TYPE_MINUTE_BOMB.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        EntitySpawnPlacementRegistry.register(TYPE_MOOGLE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CreatureEntity::canSpawnOn);
        EntitySpawnPlacementRegistry.register(TYPE_NOBODY_CREEPER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        EntitySpawnPlacementRegistry.register(TYPE_RED_NOCTURNE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        EntitySpawnPlacementRegistry.register(TYPE_SHADOW.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        EntitySpawnPlacementRegistry.register(TYPE_SHADOW_GLOB.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        EntitySpawnPlacementRegistry.register(TYPE_SKATER_BOMB.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        EntitySpawnPlacementRegistry.register(TYPE_STORM_BOMB.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
        EntitySpawnPlacementRegistry.register(TYPE_YELLOW_OPERA.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
    }

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, KingdomKeys.MODID);

    public static final RegistryObject<TileEntityType<MagnetBloxTileEntity>> TYPE_MAGNET_BLOX = TILE_ENTITIES.register("magnet_blox", () -> TileEntityType.Builder.create(MagnetBloxTileEntity::new, ModBlocks.magnetBlox.get()).build(null));
    public static final RegistryObject<TileEntityType<SavepointTileEntity>> TYPE_SAVEPOINT_BLOX = TILE_ENTITIES.register("savepoint", () -> TileEntityType.Builder.create(SavepointTileEntity::new, ModBlocks.savepoint.get()).build(null));
    public static final RegistryObject<TileEntityType<PedestalTileEntity>> TYPE_PEDESTAL = TILE_ENTITIES.register("pedestal", () -> TileEntityType.Builder.create(PedestalTileEntity::new, ModBlocks.pedestal.get()).build(null));
    public static final RegistryObject<TileEntityType<MagicalChestTileEntity>> TYPE_MAGICAL_CHEST = TILE_ENTITIES.register("magical_chest", () -> TileEntityType.Builder.create(MagicalChestTileEntity::new, ModBlocks.magicalChest.get()).build(null));
    public static final RegistryObject<TileEntityType<OrgPortalTileEntity>> TYPE_ORG_PORTAL_TE = TILE_ENTITIES.register("org_portal", () -> TileEntityType.Builder.create(OrgPortalTileEntity::new, ModBlocks.orgPortal.get()).build(null));
    public static final RegistryObject<TileEntityType<MoogleProjectorTileEntity>> TYPE_MOOGLE_PROJECTOR = TILE_ENTITIES.register("moogle_projector", () -> TileEntityType.Builder.create(MoogleProjectorTileEntity::new, ModBlocks.moogleProjector.get()).build(null));
    public static final RegistryObject<TileEntityType<SoAPlatformTileEntity>> TYPE_SOA_PLATFORM = TILE_ENTITIES.register("soa_platform", () -> TileEntityType.Builder.create(SoAPlatformTileEntity::new, ModBlocks.station_of_awakening_core.get()).build(null));
    public static final RegistryObject<TileEntityType<GummiEditorTileEntity>> TYPE_GUMMI_EDITOR = TILE_ENTITIES.register("gummi_editor", () -> TileEntityType.Builder.create(GummiEditorTileEntity::new, ModBlocks.gummiEditor.get()).build(null));
    public static final RegistryObject<TileEntityType<SoRCoreTileEntity>> TYPE_SOR_CORE_TE = TILE_ENTITIES.register("sor_core", () -> TileEntityType.Builder.create(SoRCoreTileEntity::new, ModBlocks.sorCore.get()).build(null));
}
