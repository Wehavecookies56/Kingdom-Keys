package online.kingdomkeys.kingdomkeys.client;

import com.google.gson.JsonParseException;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.CastleOblivionCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
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
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationData;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.limit.LimitData;
import online.kingdomkeys.kingdomkeys.limit.ModLimits;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.MagicData;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.stc.*;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.sound.AeroSoundInstance;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeData;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopListRegistry;
import online.kingdomkeys.kingdomkeys.synthesis.shop.names.NamesListRegistry;
import online.kingdomkeys.kingdomkeys.util.IDisabledAnimations;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.Title;
import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;

public class ClientUtils {

    public static boolean getResourceExists(String path){
        try {
            Minecraft.getInstance().getResourceManager().getResourceOrThrow(new ResourceLocation(KingdomKeys.MODID, path));
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public static ResourceLocation getResourceExistsOrDefault(String path, String name, String defaultName){
        return new ResourceLocation(KingdomKeys.MODID, String.format(path, getResourceExists(String.format(path, name)) ? name : defaultName));
    }

	public static DistExecutor.SafeRunnable openMagicCustomize(LinkedHashMap<String, int[]> knownMagic) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Minecraft.getInstance().setScreen(new MenuCustomizeMagicScreen(knownMagic));
            }
        };
    }
	
	public static DistExecutor.SafeRunnable openShortcutsCustomize(LinkedHashMap<String, int[]> knownMagic) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Minecraft.getInstance().setScreen(new MenuCustomizeShortcutsScreen(knownMagic));
            }
        };
    }

    public static DistExecutor.SafeRunnable openAlignment() {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Minecraft.getInstance().setScreen(new AlignmentSelectionScreen());
            }
        };
    }

    public static DistExecutor.SafeRunnable openChoice(SCOpenChoiceScreen message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Minecraft.getInstance().setScreen(new ConfirmChoiceMenuPopup(message.state, message.choice, message.pos));
                SoAMessages.INSTANCE.clearMessage();
            }
        };
    }

    public static DistExecutor.SafeRunnable syncOrgPortal(SCSyncOrgPortalPacket msg) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Player player = Minecraft.getInstance().player;
                OrgPortalEntity portal;
                if (msg.pos != msg.destPos)
                    portal = new OrgPortalEntity(player.level(), msg.pos, msg.destPos, msg.dimension, true);
                else
                    portal = new OrgPortalEntity(player.level(), msg.pos, msg.destPos, msg.dimension, false);

                player.level().addFreshEntity(portal);
            }
        };
    }

    public static DistExecutor.SafeRunnable showOrgPortalGUI(SCShowOrgPortalGUI message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Minecraft.getInstance().setScreen(new OrgPortalGui(message.pos));
            }
        };
    }

    public static DistExecutor.SafeRunnable openSynthesisGui(String inv, String name, int moogle) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
            	if(inv != null && !inv.equals(""))
            		Minecraft.getInstance().setScreen(new SynthesisScreen(inv, name, moogle));
            	else
            		Minecraft.getInstance().setScreen(new SynthesisScreen(name));
                Minecraft.getInstance().level.playSound(Minecraft.getInstance().player, Minecraft.getInstance().player.blockPosition(), ModSounds.kupo.get(), SoundSource.MASTER, 1, 1);
            }
        };
    }

    public static DistExecutor.SafeRunnable recalcEyeHeight() {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Player player = Minecraft.getInstance().player;
                player.refreshDimensions();
            }
        };
    }
    
    public static DistExecutor.SafeRunnable aeroSoundInstance(int entID) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Player player = Minecraft.getInstance().player;
                Entity ent = player.level().getEntity(entID);
                if(ent != null && ent instanceof LivingEntity entity)
                	Minecraft.getInstance().getSoundManager().queueTickingSound(new AeroSoundInstance(entity));

            }
        };
    }

    public static DistExecutor.SafeRunnable syncCapability(SCSyncCapabilityPacket message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                IPlayerCapabilities playerData = ModCapabilities.getPlayer(Minecraft.getInstance().player);

                playerData.setLevel(message.level);
                playerData.setExperience(message.exp);
                playerData.setExperienceGiven(message.expGiven);
                playerData.setStrengthStat(message.strength);
                playerData.setMagicStat(message.magic);
                playerData.setDefenseStat(message.defense);
                playerData.setMaxAPStat(message.maxAP);
                playerData.setMP(message.MP);
                playerData.setMaxMP(message.maxMP);
                playerData.setRecharge(message.recharge);
                playerData.setMaxHP(message.maxHp);
                playerData.setDP(message.dp);
                playerData.setFP(message.fp);
                playerData.setMaxDP(message.maxDP);
                playerData.setMunny(message.munny);
                playerData.setFocus(message.focus);
                playerData.setMaxFocus(message.maxFocus);

                playerData.setMessages(message.messages);
                playerData.setBFMessages(message.bfMessages);
                playerData.setDFMessages(message.dfMessages);

                playerData.setKnownRecipeList(message.recipeList);
                playerData.setMagicsMap(message.magicsMap);
                playerData.setShotlockList(message.shotlockList);
                playerData.setEquippedShotlock(message.equippedShotlock);
                playerData.setDriveFormMap(message.driveFormMap);
                playerData.setVisibleDriveForms(message.visibleDriveForms);
                playerData.setAbilityMap(message.abilityMap);
                playerData.setAntiPoints(message.antipoints);
                playerData.setPartiesInvited(message.partyList);
                playerData.setMaterialMap(message.materialMap);
                playerData.equipAllKeychains(message.keychains, false);
                playerData.equipAllItems(message.items, false);
                playerData.equipAllAccessories(message.accessories, false);
                playerData.equipAllKBArmor(message.kbArmors, false);
                playerData.equipAllArmors(message.armors, false);
                playerData.setMaxAccessories(message.maxAccessories);
                playerData.setMaxArmors(message.maxArmors);
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
                playerData.setMagicCasttimeTicks(message.magicCasttimeTicks);
                playerData.setMagicCooldownTicks(message.magicCooldownTicks);

                playerData.setReactionCommands(message.reactionList);
                playerData.setShortcutsMap(message.shortcutsMap);
                
                playerData.setSynthLevel(message.synthLevel);
                playerData.setSynthExperience(message.synthExp);
                
                playerData.setRespawnROD(message.respawnROD);

                playerData.setSingleStyle(message.singleStyle);
                playerData.setDualStyle(message.dualStyle);
                
                Minecraft.getInstance().player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(message.maxHp);
            }
        };
    }

    public static DistExecutor.SafeRunnable syncDriveFormData(SCSyncDriveFormData message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
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
            }
        };
    }

    public static DistExecutor.SafeRunnable syncWorldCapability(SCSyncWorldCapability message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Level world = Minecraft.getInstance().level;
                IWorldCapabilities worldData = ModCapabilities.getWorld(world);
                worldData.read(message.data);
            }
        };
    }

    public static DistExecutor.SafeRunnable syncSynthesisData(SCSyncSynthesisData message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Player player = Minecraft.getInstance().player;

                RecipeRegistry.getInstance().clearRegistry();

                message.recipes.forEach(recipe -> {
                    RecipeRegistry.getInstance().register(recipe);
                });
            }
        };
    }

    public static DistExecutor.SafeRunnable syncShopData(SCSyncShopData message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Player player = Minecraft.getInstance().player;

                ShopListRegistry.getInstance().clearRegistry();

                message.list.forEach(shopItem -> {
                    ShopListRegistry.getInstance().register(shopItem);
                });
            }
        };
    }
    
    public static DistExecutor.SafeRunnable syncKeybladeData(SCSyncKeybladeData message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Player player = Minecraft.getInstance().player;

                for (int i = 0; i < message.names.size(); i++) {
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
                    if (result.keychain != null)
                        result.keychain.setKeyblade(keyblade);
                    IOUtils.closeQuietly(br);
                }
            }
        };
    }

    public static DistExecutor.SafeRunnable syncMagicData(SCSyncMagicData message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Player player = Minecraft.getInstance().player;

                for (int i = 0; i < message.names.size(); i++) {
                    Magic magic = ModMagic.registry.get().getValue(new ResourceLocation(message.names.get(i)));
                    String d = message.data.get(i);
                    BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(d.getBytes())));

                    MagicData result;
                    try {
                        result = SCSyncMagicData.GSON_BUILDER.fromJson(br, MagicData.class);

                    } catch (JsonParseException e) {
                        KingdomKeys.LOGGER.error("Error parsing magic json file {}: {}", message.names.get(i), e);
                        continue;
                    }
                    magic.setMagicData(result);
                    IOUtils.closeQuietly(br);
                }
            }
        };
    }
    
    public static DistExecutor.SafeRunnable syncLimitData(SCSyncLimitData message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Player player = Minecraft.getInstance().player;

                for (int i = 0; i < message.names.size(); i++) {
                    Limit limit = ModLimits.registry.get().getValue(new ResourceLocation(message.names.get(i)));
                    String d = message.data.get(i);
                    BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(d.getBytes())));

                    LimitData result;
                    try {
                        result = SCSyncLimitData.GSON_BUILDER.fromJson(br, LimitData.class);

                    } catch (JsonParseException e) {
                        KingdomKeys.LOGGER.error("Error parsing limit json file {}: {}", message.names.get(i), e);
                        continue;
                    }
                    limit.setLimitData(result);
                    IOUtils.closeQuietly(br);
                }
            }
        };
    }

    public static DistExecutor.SafeRunnable syncOrgData(SCSyncOrganizationData message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Player player = Minecraft.getInstance().player;

                for (int i = 0; i < message.names.size(); i++) {
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
            }
        };
    }

    public static DistExecutor.SafeRunnable openCODoorGui(SCOpenCODoorGui message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Minecraft.getInstance().setScreen(new CardSelectionScreen((CardDoorTileEntity)Minecraft.getInstance().level.getBlockEntity(message.pos)));
            }
        };
    }

    public static DistExecutor.SafeRunnable syncCastleOblivionInterior(SCSyncCastleOblivionInteriorCapability message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Level world = Minecraft.getInstance().level;
                CastleOblivionCapabilities.ICastleOblivionInteriorCapability worldData = ModCapabilities.getCastleOblivionInterior(world);
                worldData.deserializeNBT(message.data);
            }
        };
    }

    public static DistExecutor.SafeRunnable syncMoogleNames(SCSyncMoogleNames message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                NamesListRegistry.getInstance().clearRegistry();
                NamesListRegistry.getInstance().setRegistry(message.names);
            }
        };
    }

    public enum Angle{
    	X,Y,Z
    }
    
    public static class ModelAnimation {
        public ModelPart model;
        public ModelPart modelCounterpart;
        public float defVal;
        public float minVal;
        public float maxVal;
        public float actVal;
        public Angle angle;
        public boolean increasing;

        public ModelAnimation(ModelPart model, float defVal, float minVal, float maxVal, float actVal, boolean increasing, Angle angle, @Nullable ModelPart counterpart) {
            this.model = model;
            this.defVal = defVal;
            this.minVal = minVal;
            this.maxVal = maxVal;
            this.actVal = actVal;
            this.increasing = increasing;
            this.angle = angle;
            this.modelCounterpart = counterpart;
        }

        @Override
        public String toString() {
            return defVal + ": " + actVal + " " + increasing;
        }

		public void animate() {
            if(model != null) {
                if(increasing) { //animnation increase
                    actVal += 2;
                    if(actVal >= maxVal) {
                        increasing = false;
                    }
                } else { //Animation decrease
                    actVal -= 2;
                    if(actVal <= minVal) {
                        increasing = true;
                    }
                }
                switch(angle) {
                case X:
                    model.xRot = (float) Math.toRadians(actVal);
                    if(modelCounterpart != null) {
                        modelCounterpart.xRot = (float) Math.toRadians(defVal*2-actVal);
                    }
                	break;
                case Y:
                    model.yRot = (float) Math.toRadians(actVal);
                    if(modelCounterpart != null) {
                        modelCounterpart.yRot = (float) Math.toRadians(defVal*2-actVal);
                    }
                	break;
                case Z:
                    model.zRot = (float) Math.toRadians(actVal);
                    if(modelCounterpart != null) {
                        modelCounterpart.zRot = (float) Math.toRadians(defVal*2-actVal);
                    }
                	break;
                }
			}
            
		}
		
		public void setDefault() {
            if(model != null) {
                switch(angle) {
                case X:
                    model.xRot = (float) Math.toRadians(defVal);
                    if(modelCounterpart != null) {
                        modelCounterpart.xRot = (float) Math.toRadians(defVal);
                    }
                	break;
                case Y:
                    model.yRot = (float) Math.toRadians(defVal);
                    if(modelCounterpart != null) {
                        modelCounterpart.yRot = (float) Math.toRadians(defVal);
                    }
                	break;
                case Z:
                    model.zRot = (float) Math.toRadians(defVal);
                    if(modelCounterpart != null) {
                        modelCounterpart.zRot = (float) Math.toRadians(defVal);
                    }
                	break;
                }
			}
		}
    }
    

    @OnlyIn(Dist.CLIENT)
    public static void blitScaled(ResourceLocation texture, GuiGraphics gui, float x, float y, int u, int v, int width, int height, float scaleX, float scaleY) {
        gui.pose().pushPose();
        gui.pose().translate(x, y, 0);
        gui.pose().scale(scaleX, scaleY, 1);
        gui.blit(texture, 0, 0, u, v, width, height);
        gui.pose().popPose();
    }

    @OnlyIn(Dist.CLIENT)
    public static void blitScaled(ResourceLocation texture, GuiGraphics gui, float x, float y, int u, int v, int width, int height, float scaleXY) {
        blitScaled(texture, gui, x, y, u, v, width, height, scaleXY, scaleXY);
    }

    @OnlyIn(Dist.CLIENT)
    public static void drawStringScaled(GuiGraphics gui, float x, float y, String text, int colour, float scaleX, float scaleY) {
        gui.pose().pushPose();
        gui.pose().translate(x, y, 0);
        gui.pose().scale(scaleX, scaleY, 1);
        gui.drawString(Minecraft.getInstance().font, text, 0, 0, colour);
        gui.pose().popPose();
    }

    @OnlyIn(Dist.CLIENT)
    public static void drawStringScaled(GuiGraphics gui, float x, float y, String text, int colour, float scaleXY) {
        drawStringScaled(gui, x, y, text, colour, scaleXY, scaleXY);
    }

    public static void drawSplitString(GuiGraphics gui, String text, int x, int y, int len, int color) {
        gui.drawWordWrap(Minecraft.getInstance().font, FormattedText.of(text), x, y, len, color);
    }

    public static void drawItemAsIcon(ItemStack itemStack, PoseStack poseStack, int positionX, int positionY, int size) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        //Code stolen from ItemRenderer.renderGuiItem and changed to suit scaled items instead of fixing size to 16
        BakedModel itemBakedModel = itemRenderer.getModel(itemStack, null, null, 0);

        Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.pushPose();
        poseStack.translate(positionX, positionY, 100.0F);
        poseStack.translate(8.0D, 8.0D, 0.0D);
        poseStack.scale(1.0F, -1.0F, 1.0F);
        poseStack.scale(size, size, size);
        RenderSystem.applyModelViewMatrix();
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean flag = !itemBakedModel.usesBlockLight();
        if (flag) {
            Lighting.setupForFlatItems();
        }

        itemRenderer.render(itemStack, ItemDisplayContext.GUI, false, poseStack, multibuffersource$buffersource, 15728880, OverlayTexture.NO_OVERLAY, itemBakedModel);
        multibuffersource$buffersource.endBatch();
        if (flag) {
            Lighting.setupFor3DItems();
        }

        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
    }

  //Copy of InventoryScreen.renderEntityInInventory to disable animations, so if it breaks in an update, use that to fix it
  	public static void renderPlayerNoAnims(PoseStack posestack, int pPosX, int pPosY, int pScale, float pMouseX, float pMouseY, LivingEntity pLivingEntity) {
  		float f = (float)Math.atan((double)(pMouseX / 40.0F));
  		float f1 = (float)Math.atan((double)(pMouseY / 40.0F));
  		renderPlayerNoAnimsRaw(posestack, pPosX, pPosY, pScale, f, f1, (Player) pLivingEntity);
  	}

    public static boolean disableEFMAnims = false;
  	
  	//Slightly modified copy of InventoryScreen.renderEntityInInventoryRaw to disable animations, so if it breaks in an update, use that to fix it
  	@SuppressWarnings({ "deprecation", "unchecked" })
	public static void renderPlayerNoAnimsRaw(PoseStack p_275396_, int p_275688_, int p_275245_, int p_275535_, float angleXComponent, float angleYComponent, LivingEntity p_275689_) {
        float f = angleXComponent;
        float f1 = angleYComponent;
        Quaternionf quaternionf = (new Quaternionf()).rotateZ((float) Math.PI);
        Quaternionf quaternionf1 = (new Quaternionf()).rotateX(f1 * 20.0F * ((float) Math.PI / 180F));
        quaternionf.mul(quaternionf1);
        float f2 = p_275689_.yBodyRot;
        float f3 = p_275689_.getYRot();
        float f4 = p_275689_.getXRot();
        float f5 = p_275689_.yHeadRotO;
        float f6 = p_275689_.yHeadRot;
        p_275689_.yBodyRot = 180.0F + f * 20.0F;
        p_275689_.setYRot(180.0F + f * 40.0F);
        p_275689_.setXRot(-f1 * 20.0F);
        p_275689_.yHeadRot = p_275689_.getYRot();
        p_275689_.yHeadRotO = p_275689_.getYRot();

        double d0 = 1000.0D;
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate(0.0D, 0.0D, 1000.0D);
        RenderSystem.applyModelViewMatrix();
        p_275396_.pushPose();
        p_275396_.translate((double) p_275688_, (double) p_275245_, -950.0D);
        p_275396_.mulPoseMatrix((new Matrix4f()).scaling((float) p_275535_, (float) p_275535_, (float) (-p_275535_)));
        p_275396_.mulPose(quaternionf);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (quaternionf1 != null) {
            quaternionf1.conjugate();
            entityrenderdispatcher.overrideCameraOrientation(quaternionf1);
        }

        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer = (LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer((AbstractClientPlayer) p_275689_);
            ((IDisabledAnimations) renderer).setDisabled(true);
            disableEFMAnims = true;
            renderer.render((AbstractClientPlayer) p_275689_, 0, 1, p_275396_, multibuffersource$buffersource, 15728880);
            renderer = (LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer((AbstractClientPlayer) p_275689_);
            ((IDisabledAnimations) renderer).setDisabled(false);
            disableEFMAnims = false;
        });

        multibuffersource$buffersource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);
        p_275396_.popPose();
        Lighting.setupFor3DItems();
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();

        p_275689_.yBodyRot = f2;
        p_275689_.setYRot(f3);
        p_275689_.setXRot(f4);
        p_275689_.yHeadRotO = f5;
        p_275689_.yHeadRot = f6;
    }
  	
  	public static List<Component> getTooltip(List<Component> tooltip, ItemStack stack) {
		float baseStr = 0, baseMag = 0;
		float totalStr = 0, totalMag = 0;
		String desc = "";
		MutableComponent ln1 = null;
		
		KeybladeItem kbItem = null;
		
		if(stack == null)
			return tooltip;
		
		if(stack.getItem() instanceof KeybladeItem keyblade) {
			kbItem = keyblade;
		} else if (stack.getItem() instanceof KeychainItem keychain) {
			kbItem = keychain.getKeyblade();
		}
		
		if(kbItem != null) {
			if(kbItem.getKeybladeLevel(stack) > 0)
				ln1 = (Component.translatable(ChatFormatting.YELLOW+"Level %s", kbItem.getKeybladeLevel(stack)));
			
			baseStr = kbItem.getStrength(kbItem.getKeybladeLevel(stack))+DamageCalculation.getSharpnessDamage(stack);
			totalStr = DamageCalculation.getKBStrengthDamage(Minecraft.getInstance().player,stack)+DamageCalculation.getSharpnessDamage(stack);

			baseMag = kbItem.getMagic(kbItem.getKeybladeLevel(stack));
			totalMag = DamageCalculation.getMagicDamage(Minecraft.getInstance().player,stack);
			
			desc = kbItem.getDesc();
			
		} else if(stack.getItem() instanceof IOrgWeapon orgItem) {
			ln1 = Component.translatable(ChatFormatting.YELLOW + "" + orgItem.getMember());
			
			baseStr = orgItem.getStrength() + DamageCalculation.getSharpnessDamage(stack);
			totalStr = DamageCalculation.getOrgStrengthDamage(Minecraft.getInstance().player, stack)+DamageCalculation.getSharpnessDamage(stack);
			
			baseMag = orgItem.getMagic(); 
			totalMag = DamageCalculation.getOrgMagicDamage(Minecraft.getInstance().player, orgItem);
			
			desc = orgItem.getDesc();
		}
		
		if(ln1 != null)
			tooltip.add(ln1);
		
		tooltip.add(Component.translatable(ChatFormatting.RED + Utils.translateToLocal(Strings.Gui_Menu_Status_Strength)+" %s", baseStr + " [" + totalStr + "]"));
		tooltip.add(Component.translatable(ChatFormatting.BLUE + Utils.translateToLocal(Strings.Gui_Menu_Status_Magic)+" %s", baseMag + " [" + totalMag + "]"));
		tooltip.add(Component.translatable(ChatFormatting.WHITE + "" + ChatFormatting.ITALIC + desc));
		
		return tooltip;
	}

	public static DistExecutor.SafeRunnable showTitles(SCShowMessagesPacket message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
        		SoAMessages.INSTANCE.clearMessage();
        		for(Title t : message.titles) {
        			SoAMessages.INSTANCE.queueMessage(t);
        		}
        		//SoAMessages.INSTANCE.queueMessages((Title[]) message.titles.toArray());
            }
        };
    }

    public static Matrix4f getMVMatrix(PoseStack poseStack, float posX, float posY, float posZ, float x, float y, float z, boolean lockRotation, float partialTicks) {
        poseStack.pushPose();
        poseStack.translate(-posX, -posY, -posZ);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        float screenX = posX + x;
        float screenY = posY + y;
        float screenZ = posZ + z;

        Matrix4f viewMatrix = poseStack.last().pose();
        Matrix4f finalMatrix = new Matrix4f();
        finalMatrix.translate(-screenX, screenY, -screenZ);
        poseStack.popPose();

        if (lockRotation) {
            finalMatrix.m00(viewMatrix.m00());
            finalMatrix.m01(viewMatrix.m10());
            finalMatrix.m02(viewMatrix.m20());
            finalMatrix.m10(viewMatrix.m01());
            finalMatrix.m11(viewMatrix.m11());
            finalMatrix.m12(viewMatrix.m21());
            finalMatrix.m20(viewMatrix.m02());
            finalMatrix.m21(viewMatrix.m12());
            finalMatrix.m22(viewMatrix.m22());
        }

        finalMatrix = viewMatrix.mul(finalMatrix);

        return finalMatrix;
    }

    public static Matrix4f getMVMatrix(PoseStack poseStack, LivingEntity entity, float x, float y, float z, boolean lockRotation, float partialTicks) {
        float posX = (float) Mth.lerp(partialTicks, entity.xOld, entity.getX());
        float posY = (float)Mth.lerp(partialTicks, entity.yOld, entity.getY());
        float posZ = (float)Mth.lerp(partialTicks, entity.zOld, entity.getZ());
        return getMVMatrix(poseStack,posX,posY,posZ,x,y,z,lockRotation,partialTicks);
    }

    public static final RenderType SHOTLOCK_INDICATOR = RenderType.create(KingdomKeys.MODID+":shotlock_indicator", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(KingdomKeys.MODID,"textures/gui/shotlock_indicator.png"),
                    false, false)).setTransparencyState(RenderStateShard.NO_TRANSPARENCY).setLightmapState(RenderStateShard.NO_LIGHTMAP).setOverlayState(RenderStateShard.NO_OVERLAY).createCompositeState(true));

      public static final RenderType ULTIMATE_SHOTLOCK_INDICATOR = RenderType.create(KingdomKeys.MODID+":shotlock_indicator", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(KingdomKeys.MODID,"textures/gui/ultimate_shotlock_indicator.png"),
                    false, false)).setTransparencyState(RenderStateShard.NO_TRANSPARENCY).setLightmapState(RenderStateShard.NO_LIGHTMAP).setOverlayState(RenderStateShard.NO_OVERLAY).createCompositeState(true));

    public static void drawSingleShotlockIndicator(int entityID, PoseStack matStackIn, MultiBufferSource bufferIn, float partialTicks) {
        Player localPlayer = Minecraft.getInstance().player;
        IPlayerCapabilities localPlayerData = ModCapabilities.getPlayer(localPlayer);
        Shotlock shotlock = Utils.getPlayerShotlock(localPlayer);

        if(localPlayer.level().getEntity(entityID) instanceof LivingEntity entityIn) {
            float x = (float) (localPlayer.getX() - entityIn.getX()) * 0.3F;
            float y = (float) (localPlayer.getY() - entityIn.getY()) * 0.3F;
            float z = (float) (localPlayer.getZ() - entityIn.getZ()) * 0.3F;
            Matrix4f mvMatrix = getMVMatrix(matStackIn, entityIn, x, y + entityIn.getBbHeight() / 2, z, true, partialTicks);
            float renderSize = 1.5F + shotlock.getCooldown() * 0.2F - ClientEvents.focusingAnEntityTicks * 0.2F;
            ClientUtils.drawTexturedModalRect2DPlane(mvMatrix, bufferIn.getBuffer(ULTIMATE_SHOTLOCK_INDICATOR), -renderSize, -renderSize, renderSize, renderSize, 0, 0, 256, 256);
        }
    }
    public static void drawShotlockIndicator(LivingEntity entityIn, PoseStack matStackIn, MultiBufferSource bufferIn, float partialTicks) {
        Player localPlayer = Minecraft.getInstance().player;
        IPlayerCapabilities localPlayerData = ModCapabilities.getPlayer(localPlayer);
        Shotlock shotlock = Utils.getPlayerShotlock(localPlayer);

        for (Utils.ShotlockPosition shotlockEnemy : localPlayerData.getShotlockEnemies()) {
            float ex = (float) entityIn.getX(); //Random offsets
            float ey = (float) entityIn.getY();
            float ez = (float) entityIn.getZ();
            float renderSize = 1.5F;
            if(shotlock.getMaxLocks() > 1) {
                ex += shotlockEnemy.x(); //Random offsets
                ey += shotlockEnemy.y();
                ez += shotlockEnemy.z();
                renderSize = 0.1F;
            }
            float x = (float) (localPlayer.getX() - ex)*0.3F;
            float y = (float) (localPlayer.getY() - ey)*0.3F;
            float z = (float) (localPlayer.getZ() - ez)*0.3F;
            Matrix4f mvMatrix = getMVMatrix(matStackIn, entityIn, x,y+entityIn.getBbHeight()/2,z, true, partialTicks);

            //Random Circles
            if(shotlockEnemy.id() == entityIn.getId()) {
                ClientUtils.drawTexturedModalRect2DPlane(mvMatrix, bufferIn.getBuffer(shotlock.getMaxLocks() == 1 ? ULTIMATE_SHOTLOCK_INDICATOR : SHOTLOCK_INDICATOR), -renderSize, -renderSize, renderSize, renderSize, 0, 0, 256, 256);
            }
        }
    }

    public static void drawShotlockIndicator(BlockPos pos, PoseStack matStackIn, MultiBufferSource bufferIn, float partialTicks) {
        Player localPlayer = Minecraft.getInstance().player;
        float x = (float) (localPlayer.getX() - pos.getX())*0.8F;
        float y = (float) (localPlayer.getY() - pos.getY())*0.8F;
        float z = (float) (localPlayer.getZ() - pos.getZ())*0.8F;
        Matrix4f mvMatrix = getMVMatrix(matStackIn, x,y,z, 0.5F, 0.5F, 0.5F, true, partialTicks);
        ClientUtils.drawTexturedModalRect2DPlane(mvMatrix, bufferIn.getBuffer(SHOTLOCK_INDICATOR), -0.6f,-0.6f,0.6f,0.6f, 0, 0, 256, 256);
    }

    public static void drawTexturedModalRect2DPlane(Matrix4f matrix, VertexConsumer vertexBuilder, float minX, float minY, float maxX, float maxY, float minTexU, float minTexV, float maxTexU, float maxTexV) {
        drawTexturedModalRect3DPlane(matrix, vertexBuilder, minX, minY, 0, maxX, maxY, 0, minTexU, minTexV, maxTexU, maxTexV);
    }

    public static void drawTexturedModalRect3DPlane(Matrix4f matrix, VertexConsumer vertexBuilder, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float minTexU, float minTexV, float maxTexU, float maxTexV) {
        float cor = 0.00390625F;
        vertexBuilder.vertex(matrix, minX, minY, maxZ).uv((minTexU * cor), (maxTexV) * cor).endVertex();
        vertexBuilder.vertex(matrix, maxX, minY, maxZ).uv((maxTexU * cor), (maxTexV) * cor).endVertex();
        vertexBuilder.vertex(matrix, maxX, maxY, minZ).uv((maxTexU * cor), (minTexV) * cor).endVertex();
        vertexBuilder.vertex(matrix, minX, maxY, minZ).uv((minTexU * cor), (minTexV) * cor).endVertex();
    }

    public static DistExecutor.SafeRunnable openSavePointScreen(SCOpenSavePointScreen message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Minecraft.getInstance().setScreen(new SavePointScreen((SavepointTileEntity) Minecraft.getInstance().level.getBlockEntity(message.tileEntity()), message.savePoints(), message.create()));
            }
        };
    }

    public static DistExecutor.SafeRunnable updateSavePoints(SCUpdateSavePoints message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                if (Minecraft.getInstance().screen instanceof SavePointScreen savePointScreen) {
                    savePointScreen.updateSavePointsFromServer(message.savePoints());
                }
            }
        };
    }

    public static DistExecutor.SafeRunnable deleteScreenshot(SCDeleteSavePointScreenshot message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
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
        };
    }

    public static DistExecutor.SafeRunnable updateCORooms(SCUpdateCORooms message) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                COMinimap.rooms = message.rooms();
            }
        };
    }
}





























