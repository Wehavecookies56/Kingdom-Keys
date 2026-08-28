package online.kingdomkeys.kingdomkeys.client.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@OnlyIn(Dist.CLIENT)
public class KeybladeHitParticle extends SingleQuadParticle {
    private static final Map<ResourceLocation, ParticleRenderType> RENDER_TYPES = new ConcurrentHashMap<>();

    private final ParticleRenderType renderType;
    private final float peakSize;

    private KeybladeHitParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, KeybladeHitParticleOptions options) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.renderType = RENDER_TYPES.computeIfAbsent(options.texture(), BoundTexture::new);
        this.peakSize = options.scale();
        this.quadSize = options.scale();

        this.lifetime = 8 + this.random.nextInt(5);
        this.gravity = 0.0F;
        this.friction = 0.86F;

        // Given some drift of its own, so a burst spreads instead of every piece riding the same line
        this.xd = xSpeed + (this.random.nextFloat() - 0.5F) * 0.1F;
        this.yd = ySpeed + (this.random.nextFloat() - 0.5F) * 0.1F;
        this.zd = zSpeed + (this.random.nextFloat() - 0.5F) * 0.1F;

        this.roll = this.random.nextFloat() * Mth.TWO_PI;
        this.oRoll = this.roll;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        float progress = (this.age + scaleFactor) / this.lifetime;
        return this.peakSize * Mth.sin(Mth.clamp(progress, 0.0F, 1.0F) * Mth.PI);
    }

    @Override
    public void tick() {
        super.tick();
        this.oRoll = this.roll;
        this.roll += 0.06F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return renderType;
    }

    @Override
    protected float getU0() {
        return 0.0F;
    }

    @Override
    protected float getU1() {
        return 1.0F;
    }

    @Override
    protected float getV0() {
        return 0.0F;
    }

    @Override
    protected float getV1() {
        return 1.0F;
    }

    private record BoundTexture(ResourceLocation texture) implements ParticleRenderType {
        @Override
        public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
            RenderSystem.depthMask(true);
            RenderSystem.setShaderTexture(0, texture);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public String toString() {
            return "KEYBLADE_HIT[" + texture + "]";
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<KeybladeHitParticleOptions> {
        @Override
        public Particle createParticle(KeybladeHitParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new KeybladeHitParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }
}
