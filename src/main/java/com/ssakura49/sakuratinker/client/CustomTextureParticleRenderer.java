package com.ssakura49.sakuratinker.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * 实现此类后，请使用此接口提供的渲染方法 {@link CustomTextureParticleRenderer#doParticleRenderTask(VertexConsumer, ClientLevel, Camera, PoseStack, int, float)}
 */
public abstract class CustomTextureParticleRenderer implements ICustomTickrateRenderer {
    private static final AABB NORMAL = new AABB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    private final CustomInLevelRendererDispatcher.MutableSpriteSet currentSpriteSet;
    public int flag = 0x00000000;
    public double x;
    public double y;
    public double z;
    public double xOld;
    public double yOld;
    public double zOld;
    protected float bbWidth = 0.05F;
    protected float bbHeight = 0.05F;
    protected TextureAtlasSprite sprite;
    private AABB bb = NORMAL;

    /**
     * 必须在构造方法里调用<pre>
     * WARNING:渲染未正确渲染时, 在构造方法处检查输出
     */
    public CustomTextureParticleRenderer() {
        this.currentSpriteSet = CustomInLevelRendererDispatcher.spriteSets.get(this.getClass());
        if (currentSpriteSet != null) {
            this.setSprite(currentSpriteSet.get(0));
        } else {
            if (SakuraTinker.modInitialized) {
                throw new NullPointerException("Cannot init CIL_CustomTextureParticle");
            }
        }
    }

    /**
     * Invoked by {@link CustomInLevelRendererDispatcher#runAllTasks(Camera, PoseStack, float, double, double, double, LightTexture, Frustum)} to render this renderer instance
     *
     * @param consumer     Provided VC (VertexFormat:Particle ,Mode:QUAD)
     * @param level        ClientLevel instance
     * @param camera       Main camera (from {@link GameRenderer#getMainCamera()})
     * @param matrix       the pose stack that has transformed for rendering in level
     * @param packedLight  the light 0~0xF000F0
     * @param partialTicks first parameter of {@link net.minecraft.util.Mth#lerp(float, float, float)}
     */
    public abstract void doParticleRenderTask(VertexConsumer consumer, ClientLevel level, Camera camera, PoseStack matrix, int packedLight, float partialTicks);

    /**
     * Invoked in method {@link ICustomInLevelRenderTask#enable()}
     */
    @Override
    public void addTask() {
        CustomInLevelRendererDispatcher.textureParticles.add(this);
        CustomInLevelRendererDispatcher.tickRate.add(this);
    }

    /**
     * GL指令操作,建议使用当前粒子要使用的渲染类型的{@link net.minecraft.client.renderer.RenderType#setupState}
     *
     * @return a runnable
     */
    @Deprecated
    public abstract Runnable setupRenderState();

    /**
     * 渲染时进行指令setup
     */
    @Deprecated
    public void setup() {
        setupRenderState().run();
    }

    /**
     * GL指令操作,建议使用当前粒子要使用的渲染类型的{@link net.minecraft.client.renderer.RenderType#clearState}
     *
     * @return a runnable
     */
    @Deprecated
    public abstract Runnable clearRenderState();

    /**
     * 渲染完成进行指令clear
     */
    @Deprecated
    public void clear() {
        clearRenderState().run();
    }

    /**
     * @return 获取当前 {@link TextureAtlasSprite#getU0()}
     */
    public float getU0() {
        TextureAtlasSprite atlasSprite = this.getSprite();
        if (atlasSprite != null)
            return this.getSprite().getU0();
        return 0.0F;
    }

    /**
     * @return 获取当前 {@link TextureAtlasSprite#getU1()}
     */
    public float getU1() {
        TextureAtlasSprite atlasSprite = this.getSprite();
        if (atlasSprite != null)
            return this.getSprite().getU1();
        return 1.0F;
    }

    /**
     * @return 获取当前 {@link TextureAtlasSprite#getV0()}
     */
    public float getV0() {
        TextureAtlasSprite atlasSprite = this.getSprite();
        if (atlasSprite != null)
            return this.getSprite().getV0();
        return 0.0F;
    }

    /**
     * @return 获取当前 {@link TextureAtlasSprite#getV1()} ()}
     */
    public float getV1() {
        TextureAtlasSprite atlasSprite = this.getSprite();
        if (atlasSprite != null)
            return this.getSprite().getV1();
        return 1.0F;
    }

    /**
     * WARNING:仅仅在资源重载时再次调用
     * {@link CustomInLevelRendererDispatcher#reload(PreparableReloadListener.PreparationBarrier, ResourceManager, Executor, Executor)}{@link CustomInLevelRendererDispatcher#texturesToDefinition(CustomTextureParticleRenderer)}
     *
     * @return 该粒子要应用的所有情况下的纹理
     */
    public abstract List<ResourceLocation> allTextures();

    /**
     * @return 返回当前粒子的纹理图集精灵
     * <pre>
     * Example:
     * <pre> {@code
     *  class TextureExample extends CustomTextureParticleRenderer {
     *         protected TextureAtlasSprite sprite;
     *         TextureExample() {
     *             super();
     *             this.setSprite(this.getCurrentSpriteSet().get(...));
     *         }
     *         @Override
     *         public void setSprite(TextureAtlasSprite sprite) {
     *             this.sprite = sprite;
     *         }
     *         @Override
     *         public TextureAtlasSprite getSprite() {
     *             return sprite;
     *         }
     *     }
     * }}</pre>
     * </pre>
     * WARNING:{@link CustomTextureParticleRenderer#getSprite()}的返回值(TextureAtlasSprite)的纹理png必须在 assets->[mod_id]->textures->cil_particles包下
     */
    public TextureAtlasSprite getSprite() {
        return this.sprite;
    }

    /**
     * 设置当前粒子的纹理图集精灵
     *
     * @param sprite TextureAtlasSprite instance
     */
    public void setSprite(TextureAtlasSprite sprite) {
        if (this.sprite != sprite)
            this.sprite = sprite;
    }

    /**
     * @return 获取根据 {@link CustomTextureParticleRenderer#allTextures()} 生成的复合SpriteSet对象
     */
    public CustomInLevelRendererDispatcher.MutableSpriteSet getCurrentSpriteSet() {
        return this.currentSpriteSet;
    }

    /**
     * 在 {@link CustomTextureParticleRenderer#getCurrentSpriteSet()}中随机选择一个 纹理图集精灵 作为当前粒子的 纹理图集精灵
     *
     * @param random 随机数
     */
    public void pickSprite(RandomSource random) {
        this.setSprite(getCurrentSpriteSet().get(random));
    }

    /**
     * 在 {@link CustomTextureParticleRenderer#getCurrentSpriteSet()}中根据 粒子存在时间对于最大生命值的占比 选择一个 纹理图集精灵 作为当前粒子的 纹理图集精灵
     *
     * @param maxLife 最大存在时间
     */
    public void setSpriteFromAge(int maxLife) {
        if (!this.isEnable()) {
            this.setSprite(getCurrentSpriteSet().get(this.tickCount(), maxLife));
        }
    }

    /**
     * 弃用
     */
    @Override
    public final double getX() {
        return 0D;
    }

    /**
     * 弃用
     */
    @Override
    public final double getY() {
        return 0D;
    }

    /**
     * 弃用
     */
    @Override
    public final double getZ() {
        return 0D;
    }

    public double getX(float partialTicks) {
        return Mth.lerp(partialTicks, xOld, x);
    }

    public double getY(float partialTicks) {
        return Mth.lerp(partialTicks, yOld, y);
    }

    public double getZ(float partialTicks) {
        return Mth.lerp(partialTicks, zOld, z);
    }

    protected void updatePos(double x, double y, double z) {
        this.xOld = this.x;
        this.yOld = this.y;
        this.zOld = this.z;
        this.x = x;
        this.y = y;
        this.z = z;
        this.setBoundingBoxPos(x, y, z);
    }

    public void setBoundingBoxPos(double x, double y, double z) {
        float f = this.bbWidth / 2.0F;
        float f1 = this.bbHeight;
        this.setBoundingBox(new AABB(x - (double) f, y, z - (double) f, x + (double) f, y + (double) f1, z + (double) f));
    }

    public double disSqr(ICustomInLevelRenderTask task) {
        double[] pos = new double[]{getX(), getY(), getZ()};
        return (pos[0] - task.getX()) * (pos[0] - task.getX()) + (pos[1] - task.getY()) * (pos[1] - task.getY()) + (pos[2] - task.getZ()) * (pos[2] - task.getZ());
    }

    public AABB getBoundingBox() {
        return bb;
    }

    public void setBoundingBox(AABB bb) {
        this.bb = bb;
    }

    protected void setSize(float p_107251_, float p_107252_) {
        if (p_107251_ != this.bbWidth || p_107252_ != this.bbHeight) {
            this.bbWidth = p_107251_;
            this.bbHeight = p_107252_;
            AABB aabb = this.getBoundingBox();
            double d0 = (aabb.minX + aabb.maxX - (double) p_107251_) / 2.0D;
            double d1 = (aabb.minZ + aabb.maxZ - (double) p_107251_) / 2.0D;
            this.setBoundingBox(new AABB(d0, aabb.minY, d1, d0 + (double) this.bbWidth, aabb.minY + (double) this.bbHeight, d1 + (double) this.bbWidth));
        }

    }
}

