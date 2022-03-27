package online.kingdomkeys.kingdomkeys.client;

import com.google.gson.JsonParseException;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.ConfirmChoiceMenuPopup;
import online.kingdomkeys.kingdomkeys.client.gui.OrgPortalGui;
import online.kingdomkeys.kingdomkeys.client.gui.SoAMessages;
import online.kingdomkeys.kingdomkeys.client.gui.organization.AlignmentSelectionScreen;
import online.kingdomkeys.kingdomkeys.client.gui.synthesis.SynthesisScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormData;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.OrgPortalEntity;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationData;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.MagicData;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.stc.*;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeData;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

public class ClientUtils {

    public static DistExecutor.SafeRunnable openAlignment() {
        return () -> Minecraft.getInstance().setScreen(new AlignmentSelectionScreen());
    }

    public static DistExecutor.SafeRunnable openChoice(SCOpenChoiceScreen message) {
        return () -> {
            Minecraft.getInstance().setScreen(new ConfirmChoiceMenuPopup(message.state, message.choice, message.pos));
            SoAMessages.INSTANCE.clearMessage();
        };
    }

    public static DistExecutor.SafeRunnable syncOrgPortal(SCSyncOrgPortalPacket msg) {
        return () -> {
            Player player = Minecraft.getInstance().player;
            OrgPortalEntity portal;
            if(msg.pos != msg.destPos)
                portal = new OrgPortalEntity(player.level, player, msg.pos, msg.destPos, msg.dimension, true);
            else
                portal = new OrgPortalEntity(player.level, player, msg.pos, msg.destPos, msg.dimension, false);

            player.level.addFreshEntity(portal);
        };
    }

    public static DistExecutor.SafeRunnable showOrgPortalGUI(SCShowOrgPortalGUI message) {
        return () -> Minecraft.getInstance().setScreen(new OrgPortalGui(message.pos));
    }

    public static DistExecutor.SafeRunnable openSynthesisGui() {
        return () -> {
            Minecraft.getInstance().setScreen(new SynthesisScreen());
            Minecraft.getInstance().level.playSound(Minecraft.getInstance().player, Minecraft.getInstance().player.blockPosition(), ModSounds.kupo.get(), SoundSource.MASTER, 1, 1);
        };
    }

    public static DistExecutor.SafeRunnable recalcEyeHeight() {
        return () -> {
            Player player = Minecraft.getInstance().player;
            player.refreshDimensions();
        };
    }

    public static DistExecutor.SafeRunnable syncCapability(SCSyncCapabilityPacket message) {
        return () -> {
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(Minecraft.getInstance().player);

            playerData.setLevel(message.level);
            playerData.setExperience(message.exp);
            playerData.setExperienceGiven(message.expGiven);
            playerData.setStrength(message.strength);
            playerData.setMagic(message.magic);
            playerData.setDefense(message.defense);
            playerData.setBoostStrength(message.boostStr);
            playerData.setBoostMagic(message.boostMag);
            playerData.setBoostDefense(message.boostDef);
            playerData.setMP(message.MP);
            playerData.setMaxMP(message.maxMP);
            playerData.setRecharge(message.recharge);
            playerData.setMaxHP(message.maxHp);
            playerData.setMaxAP(message.maxAP);
            playerData.setBoostMaxAP(message.boostMaxAP);
            playerData.setDP(message.dp);
            playerData.setFP(message.fp);
            playerData.setMaxDP(message.maxDP);
            playerData.setMunny(message.munny);
            playerData.setFocus(message.focus);
            playerData.setMaxFocus(message.maxFocus);

            playerData.setMessages(message.messages);
            playerData.setDFMessages(message.dfMessages);

            playerData.setKnownRecipeList(message.recipeList);
            playerData.setMagicsMap(message.magicsMap);
            playerData.setShotlockList(message.shotlockList);
            playerData.setEquippedShotlock(message.equippedShotlock);
            playerData.setDriveFormMap(message.driveFormMap);
            playerData.setAbilityMap(message.abilityMap);
            playerData.setAntiPoints(message.antipoints);
            playerData.setPartiesInvited(message.partyList);
            playerData.setMaterialMap(message.materialMap);
            playerData.equipAllKeychains(message.keychains, false);
            playerData.equipAllItems(message.items, false);
            playerData.equipAllAccessories(message.accessories, false);
            playerData.setActiveDriveForm(message.driveForm);

            playerData.setReturnDimension(message.returnDim);
            playerData.setReturnLocation(message.returnPos);
            playerData.setSoAState(message.soAstate);
            playerData.setChoice(message.choice);
            playerData.setSacrifice(message.sacrifice);
            playerData.setChoicePedestal(message.choicePedestal);
            playerData.setSacrificePedestal(message.sacrificePedestal);

            playerData.setHearts(message.hearts);
            playerData.setAlignment(message.alignment);
            playerData.equipWeapon(message.equippedWeapon);
            playerData.setWeaponsUnlocked(message.unlocks);
            playerData.setLimitCooldownTicks(message.limitCooldownTicks);
            playerData.setMagicCooldownTicks(message.magicCooldownTicks);

            playerData.setReactionCommands(message.reactionList);
            playerData.setShortcutsMap(message.shortcutsMap);

            Minecraft.getInstance().player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(message.maxHp);
        };
    }

    public static DistExecutor.SafeRunnable syncDriveFormData(SCSyncDriveFormData message) {
        return () -> {
            Player player = Minecraft.getInstance().player;

            for (int i = 0; i < message.names.size(); i++) {
                DriveForm driveform = ModDriveForms.registry.get().getValue(new ResourceLocation(message.names.get(i)));
                String d = message.data.get(i);
                BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(d.getBytes())));

                DriveFormData result;
                try {
                    result = SCSyncDriveFormData.GSON_BUILDER.fromJson(br, DriveFormData.class);

                } catch (JsonParseException e) {
                    KingdomKeys.LOGGER.error("Error parsing json file {}: {}", message.names.get(i), e);
                    continue;
                }
                driveform.setDriveFormData(result);
                IOUtils.closeQuietly(br);
            }
        };
    }

    public static DistExecutor.SafeRunnable syncWorldCapability(SCSyncWorldCapability message) {
        return () -> {
            Level world = Minecraft.getInstance().level;
            IWorldCapabilities worldData = ModCapabilities.getWorld(world);
            worldData.read(message.data);
        };
    }

    public static DistExecutor.SafeRunnable syncSynthesisData(SCSyncSynthesisData message) {
        return () -> {
            Player player = Minecraft.getInstance().player;

            RecipeRegistry.getInstance().clearRegistry();

            message.recipes.forEach(recipe -> {
                RecipeRegistry.getInstance().register(recipe);
            });
        };
    }

    public static DistExecutor.SafeRunnable syncKeybladeData(SCSyncKeybladeData message) {
        return () -> {
            Player player = Minecraft.getInstance().player;

            for(int i = 0;i<message.names.size();i++) {
                KeybladeItem keyblade = (KeybladeItem) ForgeRegistries.ITEMS.getValue(new ResourceLocation(message.names.get(i)));
                String d = message.data.get(i);
                BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(d.getBytes())));

                KeybladeData result;
                try {
                    result = SCSyncKeybladeData.GSON_BUILDER.fromJson(br, KeybladeData.class);

                } catch (JsonParseException e) {
                    KingdomKeys.LOGGER.error("Error parsing json file {}: {}", message.names.get(i), e);
                    continue;
                }
                keyblade.setKeybladeData(result);
                if(result.keychain != null)
                    result.keychain.setKeyblade(keyblade);
                IOUtils.closeQuietly(br);
            }
        };
    }

    public static DistExecutor.SafeRunnable syncMagicData(SCSyncMagicData message) {
        return () -> {
            Player player = Minecraft.getInstance().player;

            for (int i = 0; i < message.names.size(); i++) {
                Magic magic = ModMagic.registry.get().getValue(new ResourceLocation(message.names.get(i)));
                String d = message.data.get(i);
                BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(d.getBytes())));

                MagicData result;
                try {
                    result = SCSyncMagicData.GSON_BUILDER.fromJson(br, MagicData.class);

                } catch (JsonParseException e) {
                    KingdomKeys.LOGGER.error("Error parsing json file {}: {}", message.names.get(i), e);
                    continue;
                }
                magic.setMagicData(result);
                IOUtils.closeQuietly(br);
            }
        };
    }

    public static DistExecutor.SafeRunnable syncOrgData(SCSyncOrganizationData message) {
        return () -> {
            Player player = Minecraft.getInstance().player;

            for(int i = 0;i<message.names.size();i++) {
                IOrgWeapon weapon = (IOrgWeapon) ForgeRegistries.ITEMS.getValue(new ResourceLocation(message.names.get(i)));

                String d = message.data.get(i);
                BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(d.getBytes())));

                OrganizationData result;
                try {
                    result = SCSyncOrganizationData.GSON_BUILDER.fromJson(br, OrganizationData.class);

                } catch (JsonParseException e) {
                    KingdomKeys.LOGGER.error("Error parsing json file {}: {}", message.names.get(i), e);
                    continue;
                }
                weapon.setOrganizationData(result);
                IOUtils.closeQuietly(br);
            }
        };
    }

}
