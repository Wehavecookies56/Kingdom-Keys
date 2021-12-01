package online.kingdomkeys.kingdomkeys.entity;

import static online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType.HEARTLESS_EMBLEM;
import static online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType.HEARTLESS_PUREBLOOD;
import static online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType.NOBODY;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fmlclient.registry.RenderingRegistry;
import net.minecraftforge.fmllegacy.network.FMLPlayMessages;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
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

    public static final RegistryObject<EntityType<BlastBloxEntity>> TYPE_BLAST_BLOX = createEntityType(BlastBloxEntity::new, BlastBloxEntity::new, MobCategory.MISC,"blast_blox_primed", 0.98F, 0.98F);

    public static final RegistryObject<EntityType<PairBloxEntity>> TYPE_PAIR_BLOX = createEntityType(PairBloxEntity::new, PairBloxEntity::new, MobCategory.MISC,"pair_blox", 1F, 1F);
    public static final RegistryObject<EntityType<MunnyEntity>> TYPE_MUNNY = createEntityType(MunnyEntity::new, MunnyEntity::new, MobCategory.MISC,"entity_munny", 0.25F, 0.25F);
    public static final RegistryObject<EntityType<HPOrbEntity>> TYPE_HPORB = createEntityType(HPOrbEntity::new, HPOrbEntity::new, MobCategory.MISC,"entity_hp_orb", 0.25F, 0.25F);
    public static final RegistryObject<EntityType<MPOrbEntity>> TYPE_MPORB = createEntityType(MPOrbEntity::new, MPOrbEntity::new, MobCategory.MISC,"entity_mp_orb", 0.25F, 0.25F);
    public static final RegistryObject<EntityType<DriveOrbEntity>> TYPE_DRIVEORB = createEntityType(DriveOrbEntity::new, DriveOrbEntity::new, MobCategory.MISC,"entity_drive_orb", 0.25F, 0.25F);
    public static final RegistryObject<EntityType<FocusOrbEntity>> TYPE_FOCUSORB = createEntityType(FocusOrbEntity::new, FocusOrbEntity::new, MobCategory.MISC,"entity_focus_orb", 0.25F, 0.25F);

    public static final RegistryObject<EntityType<FireEntity>> TYPE_FIRE = createEntityType(FireEntity::new, FireEntity::new, MobCategory.MISC,"entity_fire", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<FiraEntity>> TYPE_FIRA = createEntityType(FiraEntity::new, FiraEntity::new, MobCategory.MISC,"entity_fira", 0.8F, 0.8F);
    public static final RegistryObject<EntityType<FiragaEntity>> TYPE_FIRAGA = createEntityType(FiragaEntity::new, FiragaEntity::new, MobCategory.MISC,"entity_firaga", 1.2F, 1.2F);
    public static final RegistryObject<EntityType<FirazaEntity>> TYPE_FIRAZA = createEntityType(FirazaEntity::new, FirazaEntity::new, MobCategory.MISC,"entity_firaza", 1.2F, 1.2F);
    
    public static final RegistryObject<EntityType<BlizzardEntity>> TYPE_BLIZZARD = createEntityType(BlizzardEntity::new, BlizzardEntity::new, MobCategory.MISC,"entity_blizzard", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<BlizzazaEntity>> TYPE_BLIZZAZA = createEntityType(BlizzazaEntity::new, BlizzazaEntity::new, MobCategory.MISC,"entity_blizzaza", 0.5F, 0.5F);

    public static final RegistryObject<EntityType<WaterEntity>> TYPE_WATER = createEntityType(WaterEntity::new, WaterEntity::new, MobCategory.MISC,"entity_water", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<WateraEntity>> TYPE_WATERA = createEntityType(WateraEntity::new, WateraEntity::new, MobCategory.MISC,"entity_watera", 0.8F, 0.8F);
    public static final RegistryObject<EntityType<WatergaEntity>> TYPE_WATERGA = createEntityType(WatergaEntity::new, WatergaEntity::new, MobCategory.MISC,"entity_waterga", 1F, 1F);
    public static final RegistryObject<EntityType<WaterzaEntity>> TYPE_WATERZA = createEntityType(WaterzaEntity::new, WaterzaEntity::new, MobCategory.MISC,"entity_waterza", 1F, 1F);
    
    public static final RegistryObject<EntityType<ThunderEntity>> TYPE_THUNDER = createEntityType(ThunderEntity::new, ThunderEntity::new, MobCategory.MISC,"entity_thunder", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ThundaraEntity>> TYPE_THUNDARA = createEntityType(ThundaraEntity::new, ThundaraEntity::new, MobCategory.MISC,"entity_thundara", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ThundagaEntity>> TYPE_THUNDAGA = createEntityType(ThundagaEntity::new, ThundagaEntity::new, MobCategory.MISC,"entity_thundaga", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ThundazaEntity>> TYPE_THUNDAZA = createEntityType(ThundazaEntity::new, ThundazaEntity::new, MobCategory.MISC,"entity_thundaza", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ThunderBoltEntity>> TYPE_THUNDERBOLT = createEntityType(ThunderBoltEntity::new, ThunderBoltEntity::new, MobCategory.MISC,"entity_thunderbolt", 0.5F, 0.5F);
    
    public static final RegistryObject<EntityType<MagnetEntity>> TYPE_MAGNET = createEntityType(MagnetEntity::new, MagnetEntity::new, MobCategory.MISC,"entity_magnet", 1F, 1F);
    public static final RegistryObject<EntityType<MagneraEntity>> TYPE_MAGNERA = createEntityType(MagneraEntity::new, MagneraEntity::new, MobCategory.MISC,"entity_magnera", 1.5F, 1.5F);
    public static final RegistryObject<EntityType<MagnegaEntity>> TYPE_MAGNEGA = createEntityType(MagnegaEntity::new, MagnegaEntity::new, MobCategory.MISC,"entity_magnega", 2F, 2F);
    
    public static final RegistryObject<EntityType<GravityEntity>> TYPE_GRAVITY = createEntityType(GravityEntity::new, GravityEntity::new, MobCategory.MISC,"entity_gravity", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<GraviraEntity>> TYPE_GRAVIRA = createEntityType(GraviraEntity::new, GraviraEntity::new, MobCategory.MISC,"entity_gravira", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<GravigaEntity>> TYPE_GRAVIGA = createEntityType(GravigaEntity::new, GravigaEntity::new, MobCategory.MISC,"entity_graviga", 0.5F, 0.5F);

    public static final RegistryObject<EntityType<SeedBulletEntity>> TYPE_SEED_BULLET = createEntityType(SeedBulletEntity::new, SeedBulletEntity::new, MobCategory.MISC,"seed_bullet", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ArrowgunShotEntity>> TYPE_ARROWGUN_SHOT = createEntityType(ArrowgunShotEntity::new, ArrowgunShotEntity::new, MobCategory.MISC,"arrowgun_shot", 0.1F, 0.1F);

    public static final RegistryObject<EntityType<OrgPortalEntity>> TYPE_ORG_PORTAL = createEntityType(OrgPortalEntity::new, OrgPortalEntity::new, MobCategory.MISC,"entity_org_portal", 1F, 3.5F);
    
    public static final RegistryObject<EntityType<ChakramEntity>> TYPE_CHAKRAM = createEntityType(ChakramEntity::new, ChakramEntity::new, MobCategory.MISC,"entity_chakram", 1.3F, 0.5F);
    public static final RegistryObject<EntityType<LanceEntity>> TYPE_LANCE = createEntityType(LanceEntity::new, LanceEntity::new, MobCategory.MISC,"entity_lance", 0.5F, 0.5F);
    
    public static final RegistryObject<EntityType<HeartEntity>> TYPE_HEART = createEntityType(HeartEntity::new, HeartEntity::new, MobCategory.MISC, "heart", 1F, 1F);

    //Mobs
    public static final RegistryObject<EntityType<MoogleEntity>> TYPE_MOOGLE = createEntityType(MoogleEntity::new, MoogleEntity::new, MobCategory.AMBIENT, "moogle", 0.6F, 1.5F, MobType.NPC, 0xDACAB0, 0xC50033);
    
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_SHADOW = createEntityType(ShadowEntity::new, ShadowEntity::new, MobCategory.MONSTER, "shadow", 0.5F, 0.7F, HEARTLESS_PUREBLOOD, 0x000000, 0xFFFF00);
    public static final RegistryObject<EntityType<MegaShadowEntity>> TYPE_MEGA_SHADOW = createEntityType(MegaShadowEntity::new, MegaShadowEntity::new, MobCategory.MONSTER, "mega_shadow", 1.5F, 1.7F, HEARTLESS_PUREBLOOD, 0x000000, 0xAAAA00);
    public static final RegistryObject<EntityType<GigaShadowEntity>> TYPE_GIGA_SHADOW = createEntityType(GigaShadowEntity::new, GigaShadowEntity::new, MobCategory.MONSTER, "giga_shadow", 2.5F, 2.7F, HEARTLESS_PUREBLOOD, 0x000000, 0x666600);
    public static final RegistryObject<EntityType<DarkballEntity>> TYPE_DARKBALL = createEntityType(DarkballEntity::new, DarkballEntity::new, MobCategory.MONSTER, "darkball", 1.5F, 2F, HEARTLESS_PUREBLOOD, 0x000000, 0x6600FF);
    public static final RegistryObject<EntityType<ShadowGlobEntity>> TYPE_SHADOW_GLOB = createEntityType(ShadowGlobEntity::new, ShadowGlobEntity::new, MobCategory.MONSTER, "shadow_glob", 1F, 1F, HEARTLESS_PUREBLOOD, Color.BLACK.getRGB(), 0x312ba0);
    
    //Emblems
    public static final RegistryObject<EntityType<MinuteBombEntity>> TYPE_MINUTE_BOMB = createEntityType(MinuteBombEntity::new, MinuteBombEntity::new, MobCategory.MONSTER, "minute_bomb", 0.6F, 1.3F, HEARTLESS_EMBLEM, 0x020030, 0x8B4513);
    public static final RegistryObject<EntityType<SkaterBombEntity>> TYPE_SKATER_BOMB = createEntityType(SkaterBombEntity::new, SkaterBombEntity::new, MobCategory.MONSTER, "skater_bomb", 0.6F, 1.3F, HEARTLESS_EMBLEM, 0x020030, 0xAAAAFF);
    public static final RegistryObject<EntityType<StormBombEntity>> TYPE_STORM_BOMB = createEntityType(StormBombEntity::new, StormBombEntity::new, MobCategory.MONSTER, "storm_bomb", 0.6F, 1.3F, HEARTLESS_EMBLEM, 0x020030, Color.CYAN.getRGB());
    public static final RegistryObject<EntityType<DetonatorEntity>> TYPE_DETONATOR = createEntityType(DetonatorEntity::new, DetonatorEntity::new, MobCategory.MONSTER, "detonator", 0.6F, 1.3F, HEARTLESS_EMBLEM, 0x020030, Color.RED.getRGB());
    public static final RegistryObject<EntityType<RedNocturneEntity>> TYPE_RED_NOCTURNE = createEntityTypeImmuneToFire(RedNocturneEntity::new, RedNocturneEntity::new, MobCategory.MONSTER, "red_nocturne", 1.0F,  2.0F, HEARTLESS_EMBLEM, Color.RED.getRGB(), Color.YELLOW.getRGB());
    public static final RegistryObject<EntityType<BlueRhapsodyEntity>> TYPE_BLUE_RHAPSODY = createEntityType(BlueRhapsodyEntity::new, BlueRhapsodyEntity::new, MobCategory.MONSTER, "blue_rhapsody", 1.0F, 2.0F, HEARTLESS_EMBLEM, Color.BLUE.getRGB(), Color.YELLOW.getRGB());
    public static final RegistryObject<EntityType<YellowOperaEntity>> TYPE_YELLOW_OPERA = createEntityType(YellowOperaEntity::new, YellowOperaEntity::new, MobCategory.MONSTER, "yellow_opera", 1.0F, 2.0F, HEARTLESS_EMBLEM, Color.YELLOW.getRGB(), Color.YELLOW.getRGB());
    public static final RegistryObject<EntityType<GreenRequiemEntity>> TYPE_GREEN_REQUIEM = createEntityType(GreenRequiemEntity::new, GreenRequiemEntity::new, MobCategory.MONSTER, "green_requiem", 1.0F, 2.0F, HEARTLESS_EMBLEM, Color.GREEN.getRGB(), Color.YELLOW.getRGB());
    public static final RegistryObject<EntityType<GummiShipEntity>> TYPE_GUMMI_SHIP = createEntityType(GummiShipEntity::new, GummiShipEntity::new, MobCategory.MISC, "gummi_ship", 5.0F, 5.0F);

    //TODO update textures to work with newer model, make magic for
    /*
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_SILVER_ROCK = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.MONSTER, "silver_rock", 1.0F, 1.0F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_CRIMSON_JAZZ = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.MONSTER, "crimson_jazz", 1.0F, 1.0F);
    public static final RegistryObject<EntityType<ShadowEntity>> TYPE_EMERALD_BLUES = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.MONSTER, "emerald_blues", 1.0F, 1.0F);
    */

    public static final RegistryObject<EntityType<LargeBodyEntity>> TYPE_LARGE_BODY = createEntityType(LargeBodyEntity::new, LargeBodyEntity::new, MobCategory.MONSTER, "large_body", 1.3F, 1.6F, HEARTLESS_EMBLEM, 0x4d177c, 0x29014c);
    //TODO make AI
    //public static final RegistryObject<EntityType<ShadowEntity>> TYPE_WHITE_MUSHROOM = createEntityType(ShadowEntity::new, ShadowEntity::new, EntityClassification.MONSTER, "white_mushroom", 0.5F, 0.5F, HEARTLESS_EMBLEM, 0xe3e5e8, 0xffffff);
    public static final RegistryObject<EntityType<DirePlantEntity>> TYPE_DIRE_PLANT = createEntityType(DirePlantEntity::new, DirePlantEntity::new, MobCategory.MONSTER, "dire_plant", 0.75F, 1.5F, HEARTLESS_EMBLEM, 0x4ba04e, 0xedc2c2);

    //Nobodies
    public static final RegistryObject<EntityType<NobodyCreeperEntity>> TYPE_NOBODY_CREEPER = createEntityType(NobodyCreeperEntity::new, NobodyCreeperEntity::new, MobCategory.MONSTER, "nobody_creeper", 1F, 1.5F, NOBODY, 0xb8bdc4, 0xfcfcfc);
    public static final RegistryObject<EntityType<DuskEntity>> TYPE_DUSK = createEntityType(DuskEntity::new, DuskEntity::new, MobCategory.MONSTER, "dusk", 1F, 1.5F, NOBODY, 0xb8bdc4, 0xfcfcfc);
    public static final RegistryObject<EntityType<AssassinEntity>> TYPE_ASSASSIN = createEntityType(AssassinEntity::new, AssassinEntity::new, MobCategory.MONSTER, "assassin", 1F, 1.5F, NOBODY, 0xc9c9c9, 0xd4ccff);

    //Limits
    public static final RegistryObject<EntityType<LaserCircleCoreEntity>> TYPE_LASER_CIRCLE = createEntityType(LaserCircleCoreEntity::new, LaserCircleCoreEntity::new, MobCategory.MISC,"entity_laser_circle_core", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<LaserDomeCoreEntity>> TYPE_LASER_DOME = createEntityType(LaserDomeCoreEntity::new, LaserDomeCoreEntity::new, MobCategory.MISC,"entity_laser_dome_core", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<LaserDomeShotEntity>> TYPE_LASER_SHOT = createEntityType(LaserDomeShotEntity::new, LaserDomeShotEntity::new, MobCategory.MISC,"entity_laser_dome_shot", 0.5F, 0.5F);
    public static final RegistryObject<EntityType<ArrowRainCoreEntity>> TYPE_ARROW_RAIN = createEntityType(ArrowRainCoreEntity::new, ArrowRainCoreEntity::new, MobCategory.MISC,"entity_arrow_rain_core", 0.5F, 0.5F);
    
	public static final RegistryObject<EntityType<DarkVolleyCoreEntity>> TYPE_SHOTLOCK_DARK_VOLLEY = createEntityType(DarkVolleyCoreEntity::new, DarkVolleyCoreEntity::new, MobCategory.MISC, "entity_shotlock_volley_core", 0.5F, 0.5F);
	public static final RegistryObject<EntityType<RagnarokCoreEntity>> TYPE_SHOTLOCK_CIRCULAR = createEntityType(RagnarokCoreEntity::new, RagnarokCoreEntity::new, MobCategory.MISC, "entity_shotlock_circular_core", 0.5F, 0.5F);
	public static final RegistryObject<EntityType<PrismRainCoreEntity>> TYPE_PRISM_RAIN = createEntityType(PrismRainCoreEntity::new, PrismRainCoreEntity::new, MobCategory.MISC, "entity_shotlock_prism_rain", 0.5F, 0.5F);

	public static final RegistryObject<EntityType<BaseShotlockShotEntity>> TYPE_VOLLEY_SHOTLOCK_SHOT = createEntityType(VolleyShotEntity::new, VolleyShotEntity::new, MobCategory.MISC, "entity_volley_shotlock_shot", 0.5F, 0.5F);
	public static final RegistryObject<EntityType<BaseShotlockShotEntity>> TYPE_RAGNAROK_SHOTLOCK_SHOT = createEntityType(RagnarokShotEntity::new, RagnarokShotEntity::new, MobCategory.MISC,"entity_ragnarok_shotlock_shot", 0.5F, 0.5F);

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
    public static <T extends Entity, M extends EntityType<T>>RegistryObject<EntityType<T>> createEntityType(EntityType.EntityFactory<T> factory, BiFunction<FMLPlayMessages.SpawnEntity, Level, T> clientFactory, MobCategory classification, String name, float sizeX, float sizeY, EntityHelper.MobType group, int color1, int color2) {
        EntityType<T> type = EntityType.Builder.of(factory, classification)
                .setCustomClientFactory(clientFactory)
                .setShouldReceiveVelocityUpdates(true)
                .setUpdateInterval(1)
                .setTrackingRange(128)
                .sized(sizeX, sizeY)
                .build(name);
        if (group != null) {
        	addToGroup(group, type);
            ModItems.ITEMS.register(name+"_spawn_egg", () -> new SpawnEggItem((EntityType<? extends Mob>) type, color1, color2, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        }
        
        return ENTITIES.register(name, () -> type);
    }
    
    public static <T extends Entity, M extends EntityType<T>>RegistryObject<EntityType<T>> createEntityType(EntityType.EntityFactory<T> factory, BiFunction<FMLPlayMessages.SpawnEntity, Level, T> clientFactory, MobCategory classification, String name, float sizeX, float sizeY) {
        return createEntityType(factory,clientFactory,classification,name,sizeX,sizeY, null, 0,0);
    }

    public static <T extends Entity, M extends EntityType<T>>RegistryObject<EntityType<T>> createEntityTypeImmuneToFire(EntityType.EntityFactory<T> factory, BiFunction<FMLPlayMessages.SpawnEntity, Level, T> clientFactory, MobCategory classification, String name, float sizeX, float sizeY, EntityHelper.MobType group, int color1, int color2) {
        EntityType<T> type = EntityType.Builder.of(factory, classification)
                .setCustomClientFactory(clientFactory)
                .setShouldReceiveVelocityUpdates(true)
                .setUpdateInterval(1)
                .setTrackingRange(128)
                .sized(sizeX, sizeY)
                .fireImmune()
                .build(name);
        if (group != null) {
        	addToGroup(group, type);
            ModItems.ITEMS.register(name+"_spawn_egg", () -> new SpawnEggItem((EntityType<? extends Mob>) type, color1, color2, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        }

        return ENTITIES.register(name, () -> type);
    }
    
    public static <T extends Entity, M extends EntityType<T>>RegistryObject<EntityType<T>> createEntityTypeImmuneToFire(EntityType.EntityFactory<T> factory, BiFunction<FMLPlayMessages.SpawnEntity, Level, T> clientFactory, MobCategory classification, String name, float sizeX, float sizeY) {
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
        RenderingRegistry.registerEntityRenderingHandler(TYPE_BLAST_BLOX.get(), BlastBloxRenderer::new);

        EntityRenderers.register(TYPE_PAIR_BLOX.get(), PairBloxRenderer::new);
        EntityRenderers.register(TYPE_MUNNY.get(), MunnyRenderer::new);
        EntityRenderers.register(TYPE_HPORB.get(), HPOrbRenderer::new);
        EntityRenderers.register(TYPE_MPORB.get(), MPOrbRenderer::new);
        EntityRenderers.register(TYPE_DRIVEORB.get(), DriveOrbRenderer::new);
        EntityRenderers.register(TYPE_FOCUSORB.get(), FocusOrbRenderer::new);


        EntityRenderers.register(TYPE_FIRE.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_FIRA.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_FIRAGA.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_FIRAZA.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_BLIZZARD.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_BLIZZAZA.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_THUNDER.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_THUNDARA.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_THUNDAGA.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_THUNDAZA.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_THUNDERBOLT.get(), ThunderBoltEntityRenderer::new);
        EntityRenderers.register(TYPE_MAGNET.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_MAGNERA.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_MAGNEGA.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_WATER.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_WATERA.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_WATERGA.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_WATERZA.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_GRAVITY.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_GRAVIRA.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_GRAVIGA.get(), InvisibleEntityRenderer::new);

        EntityRenderers.register(TYPE_MOOGLE.get(), MoogleRenderer::new);
        EntityRenderers.register(TYPE_SHADOW.get(), ShadowRenderer::new);
        EntityRenderers.register(TYPE_MEGA_SHADOW.get(), MegaShadowRenderer::new);
        EntityRenderers.register(TYPE_GIGA_SHADOW.get(), GigaShadowRenderer::new);
        EntityRenderers.register(TYPE_DARKBALL.get(), DarkballRenderer::new);
        EntityRenderers.register(TYPE_SHADOW_GLOB.get(), ShadowGlobRenderer::new);
        EntityRenderers.register(TYPE_LARGE_BODY.get(), LargeBodyRenderer::new);
        EntityRenderers.register(TYPE_MINUTE_BOMB.get(), BombRenderer::new);
        EntityRenderers.register(TYPE_SKATER_BOMB.get(), BombRenderer::new);
        EntityRenderers.register(TYPE_STORM_BOMB.get(), BombRenderer::new);
        EntityRenderers.register(TYPE_DETONATOR.get(), BombRenderer::new);
        EntityRenderers.register(TYPE_NOBODY_CREEPER.get(), NobodyCreeperRenderer::new);
        EntityRenderers.register(TYPE_RED_NOCTURNE.get(), ElementalMusicalHeartlessRenderer::new);
        EntityRenderers.register(TYPE_BLUE_RHAPSODY.get(), ElementalMusicalHeartlessRenderer::new);
        EntityRenderers.register(TYPE_YELLOW_OPERA.get(), ElementalMusicalHeartlessRenderer::new);
        EntityRenderers.register(TYPE_GREEN_REQUIEM.get(), ElementalMusicalHeartlessRenderer::new);
        EntityRenderers.register(TYPE_DUSK.get(), DuskRenderer::new);
        EntityRenderers.register(TYPE_ASSASSIN.get(), AssassinRenderer::new);
        EntityRenderers.register(TYPE_DIRE_PLANT.get(), DirePlantRenderer::new);

        EntityRenderers.register(TYPE_ORG_PORTAL.get(), OrgPortalEntityRenderer::new);
        EntityRenderers.register(TYPE_HEART.get(), HeartEntityRenderer::new);

        EntityRenderers.register(TYPE_CHAKRAM.get(), ChakramEntityRenderer::new);
        EntityRenderers.register(TYPE_LANCE.get(), LanceEntityRenderer::new);

        EntityRenderers.register(TYPE_SEED_BULLET.get(), SeedBulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(TYPE_ARROWGUN_SHOT.get(), ArrowgunShotEntityRenderer::new);

        EntityRenderers.register(TYPE_LASER_CIRCLE.get(), LaserCircleEntityRenderer::new);
        EntityRenderers.register(TYPE_LASER_DOME.get(), LaserDomeEntityRenderer::new);
        EntityRenderers.register(TYPE_LASER_SHOT.get(), LaserDomeShotEntityRenderer::new);
        EntityRenderers.register(TYPE_ARROW_RAIN.get(), ArrowRainCoreEntityRenderer::new);
        EntityRenderers.register(TYPE_SHOTLOCK_DARK_VOLLEY.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_SHOTLOCK_CIRCULAR.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(TYPE_VOLLEY_SHOTLOCK_SHOT.get(), VolleyShotlockShotEntityRenderer::new);
        EntityRenderers.register(TYPE_RAGNAROK_SHOTLOCK_SHOT.get(), VolleyShotlockShotEntityRenderer::new );
        
        RenderingRegistry.registerEntityRenderingHandler(TYPE_GUMMI_SHIP.get(), GummiShipEntityRenderer::new);
        
        //Tile Entities
        
        ClientRegistry.bindTileEntityRenderer(TYPE_PEDESTAL.get(), PedestalRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TYPE_MOOGLE_PROJECTOR.get(), MoogleProjectorRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TYPE_SOA_PLATFORM.get(), SoAPlatformRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TYPE_PEDESTAL.get(), PedestalRenderer::new);

    }

    public static void registerAttributes() {
        DefaultAttributes.put(TYPE_ASSASSIN.get(), AssassinEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_BLUE_RHAPSODY.get(), BlueRhapsodyEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_DARKBALL.get(), DarkballEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_DETONATOR.get(), DetonatorEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_DIRE_PLANT.get(), DirePlantEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_DUSK.get(), DuskEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_GIGA_SHADOW.get(), GigaShadowEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_GREEN_REQUIEM.get(), GreenRequiemEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_LARGE_BODY.get(), LargeBodyEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_MEGA_SHADOW.get(), MegaShadowEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_MINUTE_BOMB.get(), MinuteBombEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_MOOGLE.get(), MoogleEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_NOBODY_CREEPER.get(), NobodyCreeperEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_RED_NOCTURNE.get(), RedNocturneEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_SHADOW.get(), ShadowEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_SHADOW_GLOB.get(), ShadowGlobEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_SKATER_BOMB.get(), SkaterBombEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_STORM_BOMB.get(), StormBombEntity.registerAttributes().build());
        DefaultAttributes.put(TYPE_YELLOW_OPERA.get(), YellowOperaEntity.registerAttributes().build());
        //GlobalEntityTypeAttributes.put(TYPE_GUMMI_SHIP.get(), GummiShipEntity.registerAttributes().create());

    }
    

    public static void registerPlacements() {
    	SpawnPlacements.register(TYPE_ASSASSIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
        SpawnPlacements.register(TYPE_BLUE_RHAPSODY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(TYPE_DARKBALL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(TYPE_DETONATOR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(TYPE_DIRE_PLANT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(TYPE_DUSK.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
        SpawnPlacements.register(TYPE_GIGA_SHADOW.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(TYPE_GREEN_REQUIEM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(TYPE_LARGE_BODY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(TYPE_MEGA_SHADOW.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(TYPE_MINUTE_BOMB.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(TYPE_MOOGLE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PathfinderMob::checkMobSpawnRules);
        SpawnPlacements.register(TYPE_NOBODY_CREEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
        SpawnPlacements.register(TYPE_RED_NOCTURNE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(TYPE_SHADOW.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(TYPE_SHADOW_GLOB.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(TYPE_SKATER_BOMB.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(TYPE_STORM_BOMB.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacements.register(TYPE_YELLOW_OPERA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
    }

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, KingdomKeys.MODID);

    public static final RegistryObject<BlockEntityType<MagnetBloxTileEntity>> TYPE_MAGNET_BLOX = TILE_ENTITIES.register("magnet_blox", () -> BlockEntityType.Builder.of(MagnetBloxTileEntity::new, ModBlocks.magnetBlox.get()).build(null));
    public static final RegistryObject<BlockEntityType<PedestalTileEntity>> TYPE_PEDESTAL = TILE_ENTITIES.register("pedestal", () -> BlockEntityType.Builder.of(PedestalTileEntity::new, ModBlocks.pedestal.get()).build(null));
    public static final RegistryObject<BlockEntityType<MagicalChestTileEntity>> TYPE_MAGICAL_CHEST = TILE_ENTITIES.register("magical_chest", () -> BlockEntityType.Builder.of(MagicalChestTileEntity::new, ModBlocks.magicalChest.get()).build(null));
    public static final RegistryObject<BlockEntityType<OrgPortalTileEntity>> TYPE_ORG_PORTAL_TE = TILE_ENTITIES.register("org_portal", () -> BlockEntityType.Builder.of(OrgPortalTileEntity::new, ModBlocks.orgPortal.get()).build(null));
    public static final RegistryObject<BlockEntityType<MoogleProjectorTileEntity>> TYPE_MOOGLE_PROJECTOR = TILE_ENTITIES.register("moogle_projector", () -> BlockEntityType.Builder.of(MoogleProjectorTileEntity::new, ModBlocks.moogleProjector.get()).build(null));
    public static final RegistryObject<BlockEntityType<SoAPlatformTileEntity>> TYPE_SOA_PLATFORM = TILE_ENTITIES.register("soa_platform", () -> BlockEntityType.Builder.of(SoAPlatformTileEntity::new, ModBlocks.station_of_awakening_core.get()).build(null));
    public static final RegistryObject<BlockEntityType<GummiEditorTileEntity>> TYPE_GUMMI_EDITOR = TILE_ENTITIES.register("gummi_editor", () -> BlockEntityType.Builder.of(GummiEditorTileEntity::new, ModBlocks.gummiEditor.get()).build(null));
    public static final RegistryObject<BlockEntityType<SoRCoreTileEntity>> TYPE_SOR_CORE_TE = TILE_ENTITIES.register("sor_core", () -> BlockEntityType.Builder.of(SoRCoreTileEntity::new, ModBlocks.sorCore.get()).build(null));
}
