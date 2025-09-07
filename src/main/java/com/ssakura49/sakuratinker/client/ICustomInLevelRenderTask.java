package com.ssakura49.sakuratinker.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;

/**
 * 实现此接口的类皆可被{@link CustomInLevelRendererDispatcher}读取，用于渲染于世界之中。
 * 注意方法{@link ICustomInLevelRenderTask#enable()}{@link ICustomInLevelRenderTask#disable()}的实现，这至关重要。
 * 通过编写一个ServerToClient的数据包以做到多玩家同步渲染。
 */
public interface ICustomInLevelRenderTask {
    /**
     * @return x pos in the world
     */
    double getX();

    /**
     * @return y pos in the world
     */
    double getY();

    /**
     * @return z pos in the world
     */
    double getZ();

    /**
     * Invoked by {@link CustomInLevelRendererDispatcher#runAllTasks(Camera, PoseStack, float, double, double, double, LightTexture)} to render this renderer instance
     *
     * @param level        ClientLevel instance
     * @param camera       Main camera (from {@link GameRenderer#getMainCamera()})
     * @param matrix       the pose stack that has transformed for rendering in level
     * @param packedLight  the light 0~0xF000F0
     * @param partialTicks first parameter of {@link net.minecraft.util.Mth#lerp(float, float, float)}
     */
    void doLevelRenderTask(MultiBufferSource.BufferSource bufferSource, ClientLevel level, Camera camera, PoseStack matrix, int packedLight, float partialTicks);

    /**
     * 调用{@link CustomInLevelRendererDispatcher#addTask(InLevelRenderType, ICustomInLevelRenderTask)}或者{@link ICustomInLevelRenderTask#addTask()}
     * 如果你的渲染器引用了TimeHelper或者TimeStopTimeHelper,请在这里初始化
     */
    void enable();

    /**
     * 如果你的渲染器引用了TimeHelper或者TimeStopTimeHelper,请在这里清除数据并移除对应列表
     */
    void disable();

    /**
     * 当返回false时，不启用渲染并移除渲染队列
     *
     * @return should render and not remove from renderQueue
     */
    boolean isEnable();

    /**
     * @return radius for cull
     */
    default float radius() {
        return 0F;
    }

    /**
     * @return max rendering distance (if < 0, use default distance)
     */
    default float maxRenderDistance() {
        return -1.0F;
    }

    /**
     * @return and did not cull
     */
    default boolean alwaysRender() {
        return false;
    }

    default int getLightColor(double x, double y, double z, ClientLevel level) {
        BlockPos blockpos = BlockPos.containing(x, y, z);
        return level.hasChunkAt(blockpos) ? LevelRenderer.getLightColor(level, blockpos) : 0;
    }

    /**
     * @return InLevelRenderType
     */
    InLevelRenderType inLevelType();

    default void addTask() {
        CustomInLevelRendererDispatcher.addTask(this.inLevelType(), this);
    }
}