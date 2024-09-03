package online.kingdomkeys.kingdomkeys.client;

import com.google.gson.JsonParseException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.*;
import online.kingdomkeys.kingdomkeys.client.gui.ConfirmChoiceMenuPopup;
import online.kingdomkeys.kingdomkeys.client.gui.OrgPortalGui;
import online.kingdomkeys.kingdomkeys.client.gui.SavePointScreen;
import online.kingdomkeys.kingdomkeys.client.gui.castle_oblivion.CardSelectionScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.customize.MenuCustomizeMagicScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.customize.MenuCustomizeShortcutsScreen;
import online.kingdomkeys.kingdomkeys.client.gui.organization.AlignmentSelectionScreen;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.COMinimap;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.SoAMessages;
import online.kingdomkeys.kingdomkeys.client.gui.synthesis.SynthesisScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormData;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.OrgPortalEntity;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationData;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.limit.LimitData;
import online.kingdomkeys.kingdomkeys.limit.ModLimits;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.MagicData;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.stc.*;
import online.kingdomkeys.kingdomkeys.sound.AeroSoundInstance;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeData;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopListRegistry;
import online.kingdomkeys.kingdomkeys.synthesis.shop.names.NamesListRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;

public class ClientPacketHandler {

    public static void openMagicCustomize(LinkedHashMap<String, int[]> knownMagic) {
        Minecraft.getInstance().setScreen(new MenuCustomizeMagicScreen(knownMagic));
    }

    public static void openShortcutsCustomize(LinkedHashMap<String, int[]> knownMagic) {
        Minecraft.getInstance().setScreen(new MenuCustomizeShortcutsScreen(knownMagic));
    }

    public static void openAlignment() {
        Minecraft.getInstance().setScreen(new AlignmentSelectionScreen());
    }

    public static void openChoice(SCOpenChoiceScreen message) {
        Minecraft.getInstance().setScreen(new ConfirmChoiceMenuPopup(message.state(), message.choice(), message.pos()));
        SoAMessages.INSTANCE.clearMessage();
    }

    public static void syncOrgPortal(SCSyncOrgPortalPacket msg) {
        Player player = Minecraft.getInstance().player;
        OrgPortalEntity portal = new OrgPortalEntity(player.level(), msg.pos(), msg.destPos(), msg.dimension(), msg.pos() != msg.destPos());
        player.level().addFreshEntity(portal);
    }

    public static void showOrgPortalGUI(SCShowOrgPortalGUI message) {
        Minecraft.getInstance().setScreen(new OrgPortalGui(message.pos()));
    }

    public static void openSynthesisGui(String inv, String name, int moogle) {
        if(inv != null && !inv.equals(""))
            Minecraft.getInstance().setScreen(new SynthesisScreen(inv, name, moogle));
        else
            Minecraft.getInstance().setScreen(new SynthesisScreen(name));
        Minecraft.getInstance().level.playSound(Minecraft.getInstance().player, Minecraft.getInstance().player.blockPosition(), ModSounds.kupo.get(), SoundSource.MASTER, 1, 1);
    }

    public static void recalcEyeHeight() {
        Player player = Minecraft.getInstance().player;
        player.refreshDimensions();
    }

    public static void aeroSoundInstance(int entID) {
        Player player = Minecraft.getInstance().player;
        Entity ent = player.level().getEntity(entID);
        if(ent != null && ent instanceof LivingEntity entity)
            Minecraft.getInstance().getSoundManager().queueTickingSound(new AeroSoundInstance(entity));
    }

    public static void syncCapability(SCSyncPlayerData message) {
        PlayerData playerData = PlayerData.get((Player) Minecraft.getInstance().level.getEntity(message.player()));
        playerData.deserializeNBT(Minecraft.getInstance().level.registryAccess(), message.data());
        Minecraft.getInstance().player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(playerData.getMaxHP());
    }

    public static void syncDriveFormData(SCSyncDriveFormData message) {
        Player player = Minecraft.getInstance().player;
        for (int i = 0; i < message.names().size(); i++) {
            DriveForm driveform = ModDriveForms.registry.get(ResourceLocation.parse(message.names().get(i)));
            String d = message.data().get(i);
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(d.getBytes())));

            DriveFormData result;
            try {
                result = SCSyncDriveFormData.GSON_BUILDER.fromJson(br, DriveFormData.class);

            } catch (JsonParseException e) {
                KingdomKeys.LOGGER.error("Error parsing json file {}: {}", message.names().get(i), e);
                continue;
            }
            driveform.setDriveFormData(result);
            IOUtils.closeQuietly(br);
        }
    }

    public static void syncSynthesisData(SCSyncSynthesisData message) {
        Player player = Minecraft.getInstance().player;
        RecipeRegistry.getInstance().clearRegistry();
        message.recipes().forEach(recipe -> {
            RecipeRegistry.getInstance().register(recipe);
        });
    }

    public static void syncShopData(SCSyncShopData message) {
        Player player = Minecraft.getInstance().player;
        ShopListRegistry.getInstance().clearRegistry();
        message.list().forEach(shopItem -> {
            ShopListRegistry.getInstance().register(shopItem);
        });
    }

    public static void syncKeybladeData(SCSyncKeybladeData message) {
        Player player = Minecraft.getInstance().player;
        for (int i = 0; i < message.names().size(); i++) {
            KeybladeItem keyblade = (KeybladeItem) BuiltInRegistries.ITEM.get(ResourceLocation.parse(message.names().get(i)));
            String d = message.data().get(i);
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(d.getBytes())));

            KeybladeData result;
            try {
                result = SCSyncKeybladeData.GSON_BUILDER.fromJson(br, KeybladeData.class);

            } catch (JsonParseException e) {
                KingdomKeys.LOGGER.error("Error parsing json file {}: {}", message.names().get(i), e);
                continue;
            }
            keyblade.setKeybladeData(result);
            if (result.keychain != null)
                result.keychain.setKeyblade(keyblade);
            IOUtils.closeQuietly(br);
        }
    }

    public static void syncMagicData(SCSyncMagicData message) {
        Player player = Minecraft.getInstance().player;
        for (int i = 0; i < message.names().size(); i++) {
            Magic magic = ModMagic.registry.get(ResourceLocation.parse(message.names().get(i)));
            String d = message.data().get(i);
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(d.getBytes())));

            MagicData result;
            try {
                result = SCSyncMagicData.GSON_BUILDER.fromJson(br, MagicData.class);

            } catch (JsonParseException e) {
                KingdomKeys.LOGGER.error("Error parsing magic json file {}: {}", message.names().get(i), e);
                continue;
            }
            magic.setMagicData(result);
            IOUtils.closeQuietly(br);
        }
    }

    public static void syncLimitData(SCSyncLimitData message) {
        Player player = Minecraft.getInstance().player;
        for (int i = 0; i < message.names().size(); i++) {
            Limit limit = ModLimits.registry.get(ResourceLocation.parse(message.names().get(i)));
            String d = message.data().get(i);
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(d.getBytes())));

            LimitData result;
            try {
                result = SCSyncLimitData.GSON_BUILDER.fromJson(br, LimitData.class);

            } catch (JsonParseException e) {
                KingdomKeys.LOGGER.error("Error parsing limit json file {}: {}", message.names().get(i), e);
                continue;
            }
            limit.setLimitData(result);
            IOUtils.closeQuietly(br);
        }
    }

    public static void syncOrgData(SCSyncOrganizationData message) {
        Player player = Minecraft.getInstance().player;
        for (int i = 0; i < message.names().size(); i++) {
            IOrgWeapon weapon = (IOrgWeapon) BuiltInRegistries.ITEM.get(ResourceLocation.parse(message.names().get(i)));

            String d = message.data().get(i);
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(d.getBytes())));

            OrganizationData result;
            try {
                result = SCSyncOrganizationData.GSON_BUILDER.fromJson(br, OrganizationData.class);

            } catch (JsonParseException e) {
                KingdomKeys.LOGGER.error("Error parsing json file {}: {}", message.names().get(i), e);
                continue;
            }
            weapon.setOrganizationData(result);
            IOUtils.closeQuietly(br);
        }
    }

    public static void openCODoorGui(SCOpenCODoorGui message) {
        Minecraft.getInstance().setScreen(new CardSelectionScreen((CardDoorTileEntity)Minecraft.getInstance().level.getBlockEntity(message.pos())));
    }

    public static void syncCastleOblivionInterior(SCSyncCastleOblivionInteriorData message) {
        ClientLevel world = Minecraft.getInstance().level;
        CastleOblivionData.InteriorData.setClientCache(world, CastleOblivionData.InteriorData.load(message.data(), world.registryAccess()));
    }

    public static void syncWorldData(SCSyncWorldData message) {
        ClientLevel world = Minecraft.getInstance().level;
        WorldData.setClientCache(WorldData.load(message.data(), world.registryAccess()));
    }

    public static void syncMoogleNames(SCSyncMoogleNames message) {
        NamesListRegistry.getInstance().clearRegistry();
        NamesListRegistry.getInstance().setRegistry(message.names());
    }

    public static void openSavePointScreen(SCOpenSavePointScreen message) {
        Minecraft.getInstance().setScreen(new SavePointScreen((SavepointTileEntity) Minecraft.getInstance().level.getBlockEntity(message.tileEntity()), message.savePoints(), message.create()));
            }

    public static void updateSavePoints(SCUpdateSavePoints message) {
        if (Minecraft.getInstance().screen instanceof SavePointScreen savePointScreen) {
            savePointScreen.updateSavePointsFromServer(message.savePoints());
        }
    }

    public static void deleteScreenshot(SCDeleteSavePointScreenshot message) {
        File screenshotFile = ScreenshotManager.getScreenshotFile(message.name(), message.uuid());
        if (screenshotFile != null) {
            String path = screenshotFile.getPath();
            if (!screenshotFile.delete()) {
                KingdomKeys.LOGGER.warn("Failed to delete screenshot file {}", path);
            } else {
                KingdomKeys.LOGGER.info("Deleted save point screenshot: {}", screenshotFile.getName());
            }
        }
    }

    public static void updateCORooms(SCUpdateCORooms message) {
        COMinimap.rooms = message.rooms();
    }


    public static void showTitles(SCShowMessagesPacket message) {
        SoAMessages.INSTANCE.clearMessage();
        for(Utils.Title t : message.titles()) {
            SoAMessages.INSTANCE.queueMessage(t);
        }
        //SoAMessages.INSTANCE.queueMessages((Title[]) message.titles.toArray());
    }
    
}
