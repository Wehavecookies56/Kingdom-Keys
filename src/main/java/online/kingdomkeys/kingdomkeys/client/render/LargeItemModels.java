package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.ClientHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.mixin.client.LightTextureAccessor;
import online.kingdomkeys.kingdomkeys.mixin.client.RenderSystemAccessor;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class LargeItemModels {
    /** Below this many quads the ordinary path is cheap enough that caching would gain nothing. */
    private static final int MIN_QUADS = 2000;

    /** A mesh nobody has drawn for this long is let go, which is also how a resource reload is cleaned up. */
    private static final long UNUSED_FOR_MS = 60_000L;

    /** How often the unused ones are looked for, in client ticks. */
    private static final int SWEEP_EVERY = 200;

    /**
     * How far a model may reach from its origin, in its own units, for the fog check. The biggest
     * keyblades run to about two and a half; this leaves room for the ones that do not fit the cube.
     */
    private static final float MODEL_REACH = 3.0F;

    private static final Direction[] SIDES_AND_NONE = {Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, null};

    private record Part(RenderType type, VertexBuffer buffer) {
    }

    private static final class Mesh {
        final List<Part> parts;
        long lastUsed;

        Mesh(List<Part> parts) {
            this.parts = parts;
            this.lastUsed = Util.getMillis();
        }

        void close() {
            parts.forEach(part -> part.buffer().close());
        }
    }

    /** Remembered answer for a model that is not worth baking, so it is only ever looked at once. */
    private static final Mesh SKIP = new Mesh(List.of());

    /**
     * One map per value of vanilla's "cull" flag, which decides the render type a model is drawn
     * with, and a third for models drawn raw by another renderer with a render type of its choosing.
     */
    @SuppressWarnings("unchecked")
    private static final Map<BakedModel, Mesh>[] MESHES = new Map[] {new IdentityHashMap<>(), new IdentityHashMap<>(), new IdentityHashMap<>()};

    private static final int RAW = 2;

    private static boolean broken;
    private static int ticks;

    private LargeItemModels() {
    }

    /**
     * Draws the item if it is one worth drawing this way, and answers whether it did. A false
     * answer leaves the pose exactly as it found it, so vanilla can carry on as if nothing happened.
     */
    public static boolean render(ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack pose, int light, int overlay, BakedModel model) {
        // Vanilla swaps these two for other models in some contexts
        if (!eligible(stack, context, overlay) || stack.is(Items.TRIDENT) || stack.is(Items.SPYGLASS) || model.isCustomRenderer()) {
            return false;
        }

        pose.pushPose();

        try {
            BakedModel shown = ClientHooks.handleCameraTransforms(pose, model, context, leftHand);

            if (shown.isCustomRenderer()) {
                return false;
            }

            pose.translate(-0.5F, -0.5F, -0.5F);

            // Before baking, so a model only ever seen from out in the fog never takes up a buffer
            if (!clearOfFog(pose)) {
                return false;
            }

            Mesh mesh = meshFor(shown, stack, cull(stack, context));

            if (mesh == SKIP) {
                return false;
            }

            draw(mesh, pose, light);
            mesh.lastUsed = Util.getMillis();
            return true;
        } catch (Throwable problem) {
            broken = true;
            KingdomKeys.LOGGER.error("Baked item models turned off for this session after an error; items fall back to vanilla rendering", problem);
            return false;
        } finally {
            pose.popPose();
        }
    }

    /**
     * Draws one model as it stands, in a pose that already has its display transform, with the
     * render type the caller would have used. For renderers that take a model apart and draw the
     * pieces themselves, like the keychain one: they get the same saving on each piece.
     */
    public static boolean drawRaw(BakedModel model, ItemStack stack, RenderType type, ItemDisplayContext context, PoseStack pose, int light, int overlay) {
        if (!eligible(stack, context, overlay) || !clearOfFog(pose)) {
            return false;
        }

        try {
            Map<BakedModel, Mesh> meshes = MESHES[RAW];
            Mesh mesh = meshes.get(model);

            // A mesh is baked for one render type, and a different one asked for is a different mesh
            if (mesh == null || (mesh != SKIP && mesh.parts.getFirst().type() != type)) {
                if (mesh != null && mesh != SKIP) {
                    mesh.close();
                }

                mesh = bake(stack, List.of(model), pass -> List.of(type));
                meshes.put(model, mesh);
            }

            if (mesh == SKIP) {
                return false;
            }

            draw(mesh, pose, light);
            mesh.lastUsed = Util.getMillis();
            return true;
        } catch (Throwable problem) {
            broken = true;
            KingdomKeys.LOGGER.error("Baked item models turned off for this session after an error; items fall back to vanilla rendering", problem);
            return false;
        }
    }

    /**
     * What every draw has to satisfy before anything is looked up: switched on for this context,
     * no hurt overlay, no glint, which is a second pass over the whole mesh, and no shader pack.
     */
    private static boolean eligible(ItemStack stack, ItemDisplayContext context, int overlay) {
        return !broken && !stack.isEmpty() && enabledFor(context) && overlay == OverlayTexture.NO_OVERLAY && !stack.hasFoil() && !shaderPackInUse();
    }

    /**
     * Whether the whole model sits nearer than the fog begins.
     *
     * <p>The item shaders measure fog from each vertex's position as uploaded. Vanilla uploads
     * positions relative to the camera; a baked buffer holds them relative to the model, so fog
     * would read them as right on top of the viewer and never apply. It does not need to: fog
     * leaves anything nearer than its start untouched, so as long as every vertex is inside that
     * distance, a baked draw is the same picture. Anything further out — or everything, underwater
     * or blinded, when the fog closes right in — goes the ordinary way.</p>
     */
    private static boolean clearOfFog(PoseStack pose) {
        Matrix4f at = pose.last().pose();
        float distance = (float) Math.sqrt(at.m30() * at.m30() + at.m31() * at.m31() + at.m32() * at.m32());
        Vector3f scale = at.getScale(new Vector3f());
        float reach = MODEL_REACH * Math.max(scale.x(), Math.max(scale.y(), scale.z()));

        return distance + reach < RenderSystem.getShaderFogStart();
    }

    /** Lets go of the meshes nobody has drawn for a while. Called once per client tick. */
    public static void tick() {
        if (++ticks < SWEEP_EVERY) {
            return;
        }

        ticks = 0;
        long cutoff = Util.getMillis() - UNUSED_FOR_MS;

        for (Map<BakedModel, Mesh> meshes : MESHES) {
            Iterator<Mesh> it = meshes.values().iterator();

            while (it.hasNext()) {
                Mesh mesh = it.next();

                // The skipped ones cost nothing to keep and would only be worked out again
                if (mesh != SKIP && mesh.lastUsed < cutoff) {
                    mesh.close();
                    it.remove();
                }
            }
        }
    }

    /** Frees everything, for when the models themselves are about to change under it. */
    public static void clear() {
        for (Map<BakedModel, Mesh> meshes : MESHES) {
            meshes.values().forEach(mesh -> {
                if (mesh != SKIP) {
                    mesh.close();
                }
            });
            meshes.clear();
        }
    }

    private static boolean enabledFor(ItemDisplayContext context) {
        if (context == ItemDisplayContext.FIXED) {
            return ModConfigs.bakeDisplayedModels;
        }

        // The inventory draws keyblades as a flat sprite anyway, and has lighting and batching of its own
        if (context == ItemDisplayContext.GUI || context == ItemDisplayContext.NONE) {
            return false;
        }

        return ModConfigs.bakeHeldModels;
    }

    /** Vanilla's own test, repeated so the render type baked is the one vanilla would have used. */
    private static boolean cull(ItemStack stack, ItemDisplayContext context) {
        if (context != ItemDisplayContext.GUI && !context.firstPerson() && stack.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            return !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
        }

        return true;
    }

    private static Mesh meshFor(BakedModel shown, ItemStack stack, boolean cull) {
        Map<BakedModel, Mesh> meshes = MESHES[cull ? 1 : 0];
        Mesh mesh = meshes.get(shown);

        if (mesh == null) {
            mesh = bake(stack, shown.getRenderPasses(stack, cull), pass -> pass.getRenderTypes(stack, cull));
            meshes.put(shown, mesh);
        }

        return mesh;
    }

    /**
     * Bakes the model the way {@link ItemRenderer} would draw it, only once and into GPU buffers:
     * the same passes, the same render types, and vanilla's own quad writer, so there is no second
     * copy of the rules to drift out of step.
     */
    private static Mesh bake(ItemStack stack, List<BakedModel> passes, Function<BakedModel, List<RenderType>> typesOf) {
        // Worth it at all? Counted before anything is allocated, and tinted models are left alone
        // since their colour belongs to the stack rather than to the model
        int quads = 0;
        RandomSource random = RandomSource.create();

        for (BakedModel pass : passes) {
            for (RenderType type : typesOf.apply(pass)) {
                if (type.format() != DefaultVertexFormat.NEW_ENTITY) {
                    return SKIP;
                }
            }

            for (Direction side : SIDES_AND_NONE) {
                random.setSeed(42L);

                for (BakedQuad quad : pass.getQuads(null, side, random)) {
                    if (quad.isTinted()) {
                        return SKIP;
                    }
                    quads++;
                }
            }
        }

        if (quads < MIN_QUADS) {
            return SKIP;
        }

        ItemRenderer items = Minecraft.getInstance().getItemRenderer();
        Map<RenderType, BufferBuilder> builders = new LinkedHashMap<>();
        List<ByteBufferBuilder> backing = new ArrayList<>();
        PoseStack identity = new PoseStack();

        try {
            for (BakedModel pass : passes) {
                for (RenderType type : typesOf.apply(pass)) {
                    BufferBuilder builder = builders.computeIfAbsent(type, t -> {
                        ByteBufferBuilder bytes = new ByteBufferBuilder(t.bufferSize());
                        backing.add(bytes);
                        return new BufferBuilder(bytes, t.mode(), t.format());
                    });

                    items.renderModelLists(pass, stack, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, identity, builder);
                }
            }

            List<Part> parts = new ArrayList<>();

            for (Map.Entry<RenderType, BufferBuilder> entry : builders.entrySet()) {
                MeshData data = entry.getValue().build();

                if (data == null) {
                    continue;
                }

                VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
                buffer.bind();
                buffer.upload(data);
                VertexBuffer.unbind();
                parts.add(new Part(entry.getKey(), buffer));
            }

            return parts.isEmpty() ? SKIP : new Mesh(parts);
        } finally {
            backing.forEach(ByteBufferBuilder::close);
        }
    }

    private static void draw(Mesh mesh, PoseStack pose, int light) {
        PoseStack.Pose last = pose.last();
        Matrix4f modelView = new Matrix4f(RenderSystem.getModelViewMatrix()).mul(last.pose());
        Matrix4f projection = RenderSystem.getProjectionMatrix();

        float[] colour = RenderSystem.getShaderColor();
        float red = colour[0], green = colour[1], blue = colour[2], alpha = colour[3];
        float[] dim = lightRatio(light);

        Vector3f[] lights = RenderSystemAccessor.kingdomKeys$getShaderLightDirections();
        Vector3f light0 = lights[0], light1 = lights[1];
        boolean turnLights = light0 != null && light1 != null;

        RenderSystem.setShaderColor(red * dim[0], green * dim[1], blue * dim[2], alpha);

        if (turnLights) {
            // The vertex path turns each normal by this matrix; turning the lights by its transpose
            // instead gives the same dot product without touching the baked normals
            Matrix3f intoModel = new Matrix3f(last.normal()).transpose();
            RenderSystem.setShaderLights(intoModel.transform(new Vector3f(light0)).normalize(), intoModel.transform(new Vector3f(light1)).normalize());
        }

        try {
            for (Part part : mesh.parts) {
                part.type().setupRenderState();

                try {
                    ShaderInstance shader = RenderSystem.getShader();

                    if (shader != null) {
                        part.buffer().bind();
                        part.buffer().drawWithShader(modelView, projection, shader);
                        VertexBuffer.unbind();
                    }
                } finally {
                    part.type().clearRenderState();
                }
            }
        } finally {
            RenderSystem.setShaderColor(red, green, blue, alpha);

            if (turnLights) {
                RenderSystem.setShaderLights(light0, light1);
            }
        }
    }

    /**
     * How much darker this light is than full brightness, per channel, read off the lightmap the
     * shader is about to sample. The buffer was baked at full brightness, so this is the whole of
     * the difference.
     */
    private static float[] lightRatio(int light) {
        NativeImage pixels = ((LightTextureAccessor) Minecraft.getInstance().gameRenderer.lightTexture()).kingdomKeys$getLightPixels();

        if (pixels == null) {
            return new float[] {1F, 1F, 1F};
        }

        int block = Mth.clamp(LightTexture.block(light), 0, 15);
        int sky = Mth.clamp(LightTexture.sky(light), 0, 15);

        // Stored as ABGR, red in the low byte, block light across and sky light down
        int here = pixels.getPixelRGBA(block, sky);
        int full = pixels.getPixelRGBA(15, 15);

        return new float[] {channel(here, full, 0), channel(here, full, 8), channel(here, full, 16)};
    }

    private static float channel(int here, int full, int shift) {
        int top = (full >> shift) & 0xFF;
        return top == 0 ? 0F : ((here >> shift) & 0xFF) / (float) top;
    }

    //region Shader packs

    private static final boolean IRIS = ModList.get() != null && (ModList.get().isLoaded("iris") || ModList.get().isLoaded("oculus"));
    private static Object irisApi;
    private static Method shaderPackInUse;
    private static boolean irisLooked;

    /**
     * A shader pack swaps the entity shaders for its own, which expect a vertex format this buffer
     * was not baked in. With one active, everything goes the ordinary way.
     */
    private static boolean shaderPackInUse() {
        if (!IRIS) {
            return false;
        }

        if (!irisLooked) {
            irisLooked = true;

            try {
                Class<?> api = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
                irisApi = api.getMethod("getInstance").invoke(null);
                shaderPackInUse = api.getMethod("isShaderPackInUse");
            } catch (Throwable ignored) {
                irisApi = null;
            }
        }

        if (irisApi == null) {
            // Present but not answering: assume the worst and stay out of its way
            return true;
        }

        try {
            return (boolean) shaderPackInUse.invoke(irisApi);
        } catch (Throwable ignored) {
            return true;
        }
    }

    //endregion
}
