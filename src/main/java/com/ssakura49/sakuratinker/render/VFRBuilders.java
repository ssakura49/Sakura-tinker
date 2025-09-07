package com.ssakura49.sakuratinker.render;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.ssakura49.sakuratinker.render.shader.STRenderType;
import com.ssakura49.sakuratinker.render.trail.TrailPoint;
import com.ssakura49.sakuratinker.render.trail.TrailRenderPoint;
import com.ssakura49.sakuratinker.utils.STUtils;
import com.ssakura49.sakuratinker.utils.SafeClassUtil;
import com.ssakura49.sakuratinker.utils.other.VecHelper;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <pre>Vertex Format Renderer Builder<pre>
 * @author MegaDarkness
 */
public class VFRBuilders {
    public VFRBuilders() {
    }

    public static WorldVFRBuilder createWorld() {
        return new WorldVFRBuilder();
    }

    public static class WorldVFRBuilder {
        public static final Object2ObjectOpenHashMap<VertexFormatElement, WorldVertexConsumerActor> CONSUMER_INFO_MAP = new Object2ObjectOpenHashMap<>();

        static {
            CONSUMER_INFO_MAP.put(DefaultVertexFormat.ELEMENT_POSITION, (consumer, last, builder, x, y, z, u, v) -> {
                if (last == null) {
                    consumer.vertex(x, y, z);
                } else {
                    consumer.vertex(last, x, y, z);
                }

            });
            CONSUMER_INFO_MAP.put(DefaultVertexFormat.ELEMENT_COLOR, (consumer, last, builder, x, y, z, u, v) -> consumer.color(builder.r, builder.g, builder.b, builder.a));
            CONSUMER_INFO_MAP.put(DefaultVertexFormat.ELEMENT_UV0, (consumer, last, builder, x, y, z, u, v) -> consumer.uv(u, v));
            CONSUMER_INFO_MAP.put(DefaultVertexFormat.ELEMENT_UV2, (consumer, last, builder, x, y, z, u, v) -> consumer.uv2(builder.light));
        }

        public float r = 1.0F;
        public float g = 1.0F;
        public float b = 1.0F;
        public float a = 1.0F;
        protected int light = 15728880;
        protected float u0 = 0.0F;
        protected float v0 = 0.0F;
        protected float u1 = 1.0F;
        protected float v1 = 1.0F;
        protected MultiBufferSource bufferSource;
        protected RenderType renderType;
        protected VertexFormat format;
        protected WorldVFRBuilder.WorldVertexConsumerActor supplier;
        protected VertexConsumer vertexConsumer;
        protected Object2ObjectOpenHashMap<Object, Consumer<WorldVFRBuilder>> modularActors;
        protected int modularActorAddIndex;
        protected int modularActorGetIndex;

        public WorldVFRBuilder() {
            this.bufferSource = Minecraft.getInstance().renderBuffers.bufferSource();
            this.modularActors = new Object2ObjectOpenHashMap<>();
        }

        public WorldVFRBuilder replaceBufferSource(MultiBufferSource bufferSource) {
            this.bufferSource = bufferSource;
            return this;
        }

        public WorldVFRBuilder setRenderTypeRaw(RenderType renderType) {
            this.renderType = renderType;
            return this;
        }

        public WorldVFRBuilder setFormatRaw(VertexFormat format) {
            this.format = format;
            return this;
        }

        public WorldVFRBuilder setVertexSupplier(WorldVFRBuilder.WorldVertexConsumerActor supplier) {
            this.supplier = supplier;
            return this;
        }

        public VertexConsumer getVertexConsumer() {
            if (this.vertexConsumer == null) {
                this.setVertexConsumer(this.bufferSource.getBuffer(this.renderType));
            }

            return this.vertexConsumer;
        }

        public WorldVFRBuilder setVertexConsumer(VertexConsumer vertexConsumer) {
            this.vertexConsumer = vertexConsumer;
            return this;
        }

        public WorldVFRBuilder addModularActor(Consumer<WorldVFRBuilder> actor) {
            return this.addModularActor(this.modularActorAddIndex, actor);
        }

        public WorldVFRBuilder addModularActor(Object key, Consumer<WorldVFRBuilder> actor) {
            if (this.modularActors == null) {
                this.modularActors = new Object2ObjectOpenHashMap<>();
            }

            this.modularActors.put(key, actor);
            return this;
        }

        public Optional<Object2ObjectOpenHashMap<Object, Consumer<WorldVFRBuilder>>> getModularActors() {
            return Optional.ofNullable(this.modularActors);
        }

        public Optional<Consumer<WorldVFRBuilder>> getNextModularActor() {
            return Optional.ofNullable(this.modularActors).map((m) -> m.get(this.modularActorGetIndex++));
        }

        public MultiBufferSource getBufferSource() {
            return this.bufferSource;
        }

        public RenderType getRenderType() {
            return this.renderType;
        }

        public WorldVFRBuilder setRenderType(RenderType renderType) {
            return this.setRenderTypeRaw(renderType).setFormat(renderType.format()).setVertexConsumer(this.bufferSource.getBuffer(renderType));
        }

        public VertexFormat getFormat() {
            return this.format;
        }

        public WorldVFRBuilder setFormat(VertexFormat format) {
            ImmutableList<VertexFormatElement> elements = format.getElements();
            return this.setFormatRaw(format).setVertexSupplier((consumer, last, builder, x, y, z, u, v) -> {

                for (VertexFormatElement element : elements) {
                    CONSUMER_INFO_MAP.get(element).placeVertex(consumer, last, this, x, y, z, u, v);
                }

                consumer.endVertex();
            });
        }

        public WorldVFRBuilder.WorldVertexConsumerActor getSupplier() {
            return this.supplier;
        }

        public WorldVFRBuilder setColor(Color color) {
            return this.setColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue());
        }

        public WorldVFRBuilder setColor(Color color, float a) {
            return this.setColor(color).setAlpha(a);
        }

        public WorldVFRBuilder setColor(float r, float g, float b, float a) {
            return this.setColor(r, g, b).setAlpha(a);
        }

        public WorldVFRBuilder setColor(Vector4f color) {
            return this.setColor(color.x, color.y, color.z).setAlpha(color.w);
        }

        public WorldVFRBuilder setColor(Vector4f color, float alpha) {
            return this.setColor(color.x, color.y, color.z).setAlpha(alpha);
        }

        public WorldVFRBuilder setColor(float r, float g, float b) {
            this.r = r / 255.0F;
            this.g = g / 255.0F;
            this.b = b / 255.0F;
            return this;
        }

        public WorldVFRBuilder setColorRaw(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
            return this;
        }

        public WorldVFRBuilder setAlpha(float a) {
            this.a = a;
            return this;
        }

        public WorldVFRBuilder setLight(int light) {
            this.light = light;
            return this;
        }

        public WorldVFRBuilder setUV(float u0, float v0, float u1, float v1) {
            this.u0 = u0;
            this.v0 = v0;
            this.u1 = u1;
            this.v1 = v1;
            return this;
        }

        public WorldVFRBuilder renderBeam(Matrix4f last, BlockPos start, BlockPos end, float width) {
            return this.renderBeam(last, VecHelper.getCenterOf(start), VecHelper.getCenterOf(end), width);
        }

        public WorldVFRBuilder renderBeam(@Nullable Matrix4f last, Vec3 start, Vec3 end, float width) {
            Minecraft minecraft = Minecraft.getInstance();
            Vec3 cameraPosition = minecraft.getBlockEntityRenderDispatcher().camera.getPosition();
            return this.renderBeam(last, start, end, width, cameraPosition);
        }

        public WorldVFRBuilder renderBeam(@Nullable Matrix4f last, Vec3 start, Vec3 end, float width, Consumer<WorldVFRBuilder> consumer) {
            Minecraft minecraft = Minecraft.getInstance();
            Vec3 cameraPosition = minecraft.getBlockEntityRenderDispatcher().camera.getPosition();
            return this.renderBeam(last, start, end, width, cameraPosition, consumer);
        }

        public WorldVFRBuilder renderBeam(@Nullable Matrix4f last, Vec3 start, Vec3 end, float width, Vec3 cameraPosition) {
            return this.renderBeam(last, start, end, width, cameraPosition, (builder) -> {
            });
        }

        public WorldVFRBuilder renderBeam(@Nullable Matrix4f last, Vec3 start, Vec3 end, float width, Vec3 cameraPosition, Consumer<WorldVFRBuilder> consumer) {
            Vec3 delta = end.subtract(start);
            Vec3 normal = start.subtract(cameraPosition).cross(delta).normalize().multiply(width / 2.0F, width / 2.0F, width / 2.0F);
            Vec3[] positions = new Vec3[]{start.subtract(normal), start.add(normal), end.add(normal), end.subtract(normal)};
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, (float) positions[0].x, (float) positions[0].y, (float) positions[0].z, this.u0, this.v1);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, (float) positions[1].x, (float) positions[1].y, (float) positions[1].z, this.u1, this.v1);
            consumer.accept(this);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, (float) positions[2].x, (float) positions[2].y, (float) positions[2].z, this.u1, this.v0);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, (float) positions[3].x, (float) positions[3].y, (float) positions[3].z, this.u0, this.v0);
            return this;
        }

        public WorldVFRBuilder renderTrail(PoseStack stack, java.util.List<TrailPoint> trailSegments, float width) {
            return this.renderTrail(stack, trailSegments, (f) -> width, (f) -> {
            });
        }

        /**
         *  xxx.renderTrail
         *  vc.endVertex();
         *  renderTrail
         *  vc.endVertex
         *  ...
         *  ...
         *  renderTrail
         *  NO endVertex
         */
        public WorldVFRBuilder renderTrail(PoseStack stack, java.util.List<TrailPoint> trailSegments, Function<Float, Float> widthFunc) {
            return this.renderTrail(stack, trailSegments, widthFunc, (f) -> {
            });
        }

        public WorldVFRBuilder renderTrail(PoseStack stack, java.util.List<TrailPoint> trailSegments, Function<Float, Float> widthFunc, Consumer<Float> vfxOperator) {
            return this.renderTrail(stack.last().pose(), trailSegments, widthFunc, vfxOperator);
        }

        public WorldVFRBuilder renderTrail(Matrix4f pose, java.util.List<TrailPoint> trailSegments, Function<Float, Float> widthFunc, Consumer<Float> vfxOperator) {
            if (trailSegments.size() < 2) {
                return this;
            } else {
                java.util.List<Vector4f> positions = trailSegments.stream().map(TrailPoint::getMatrixPosition).peek((p) -> p.mul(pose)).toList();
                int count = trailSegments.size() - 1;
                float increment = 1.0F / (float) count;
                TrailRenderPoint[] points = new TrailRenderPoint[trailSegments.size()];

                for (int i = 1; i < count; ++i) {
                    float width = widthFunc.apply(increment * (float) i);
                    Vector4f previous = positions.get(i - 1);
                    Vector4f current = positions.get(i);
                    Vector4f next = positions.get(i + 1);
                    points[i] = new TrailRenderPoint(current, RendererUtils.perpendicularTrailPoints(previous, next, width));
                }

                points[0] = new TrailRenderPoint(positions.get(0), RendererUtils.perpendicularTrailPoints(positions.get(0), positions.get(1), widthFunc.apply(0.0F)));
                points[count] = new TrailRenderPoint(positions.get(count), RendererUtils.perpendicularTrailPoints(positions.get(count - 1), positions.get(count), widthFunc.apply(1.0F)));
                return this.renderPoints(points, this.u0, this.v0, this.u1, this.v1, vfxOperator);
            }
        }

        public WorldVFRBuilder renderPoints(TrailRenderPoint[] points, float u0, float v0, float u1, float v1, Consumer<Float> vfxOperator) {
            int count = points.length - 1;
            float increment = 1.0F / (float) count;
            vfxOperator.accept(0.0F);
            points[0].renderStart(this.getVertexConsumer(), this, u0, v0, u1, Mth.lerp(increment, v0, v1));

            for (int i = 1; i < count; ++i) {
                float current = Mth.lerp((float) i * increment, v0, v1);
                vfxOperator.accept(current);
                points[i].renderMid(this.getVertexConsumer(), this, u0, current, u1, current);
            }

            vfxOperator.accept(1.0F);
            points[count].renderEnd(this.getVertexConsumer(), this, u0, Mth.lerp((float) count * increment, v0, v1), u1, v1);

            return this;
        }

        public WorldVFRBuilder renderQuad(PoseStack stack, float size) {
            return this.renderQuad(stack, size, size);
        }

        public WorldVFRBuilder renderQuad(PoseStack stack, float width, float height) {
            Vector3f[] positions = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F)};
            return this.renderQuad(stack, positions, width, height);
        }

        public WorldVFRBuilder renderQuad(PoseStack stack, Vector3f[] positions, float size) {
            return this.renderQuad(stack, positions, size, size);
        }

        public WorldVFRBuilder renderQuad(PoseStack stack, Vector3f[] positions, float width, float height) {

            for (Vector3f position : positions) {
                position.mul(width, height, width);
            }

            return this.renderQuad(stack.last().pose(), positions);
        }

        public WorldVFRBuilder renderQuad(Matrix4f last, Vector3f[] positions) {
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, positions[0].x(), positions[0].y(), positions[0].z(), this.u0, this.v1);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, positions[1].x(), positions[1].y(), positions[1].z(), this.u1, this.v1);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, positions[2].x(), positions[2].y(), positions[2].z(), this.u1, this.v0);
            this.supplier.placeVertex(this.getVertexConsumer(), last, this, positions[3].x(), positions[3].y(), positions[3].z(), this.u0, this.v0);
            return this;
        }

        public void flush() {
            RenderSystem.disableDepthTest();
            if (this.bufferSource instanceof MultiBufferSource.BufferSource bs)
                bs.endBatch();
            RenderSystem.enableDepthTest();
        }

        public interface WorldVertexConsumerActor {
            void placeVertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, WorldVFRBuilder builder, float var4, float var5, float var6, float u, float v);
        }
    }
    public static class WorldVFRTrailBuilder extends WorldVFRBuilder {
        public final List<TrailRenderTask> toRender = Collections.synchronizedList(new ArrayList<>());
        WorldVFRTrailBuilder() {
            this.modularActors = new Object2ObjectOpenHashMap<>();
            this.bufferSource = Minecraft.getInstance().renderBuffers.bufferSource();
            this.setRenderType(null);
        }

        public static WorldVFRTrailBuilder create() {
            return new WorldVFRTrailBuilder();
        }

        @Override
        public RenderType getRenderType() {
            return SafeClassUtil.usingShaderPack() ? STRenderType.PRT_WHITE : STRenderType.PRT_LIGHT_WHITE;
        }

        @Override
        public WorldVFRBuilder setRenderType(RenderType renderType) {
            return super.setRenderType(this.getRenderType());
        }

        @Override
        public MultiBufferSource getBufferSource() {
            return Minecraft.getInstance().renderBuffers.bufferSource();
        }

        public VertexConsumer getVertexConsumer() {
            if (this.vertexConsumer == null) {
                this.setVertexConsumer(this.bufferSource.getBuffer(this.getRenderType()));
            }

            return this.vertexConsumer;
        }
        public void addTrailListRenderTask(TrailRenderTask task) {
            toRender.add(task);
        }
        public void renderTrails() {
            setRenderType(!SafeClassUtil.usingShaderPack() ? STRenderType.PRT_LIGHT_WHITE : STRenderType.PRT_WHITE);
            synchronized (toRender) {
                STUtils.safelyForEach(toRender, (task, i) -> {
                    task.task(this);
                });
            }
            flush();
            clear();
        }
        public void clear() {
            toRender.clear();
        }
        public interface TrailRenderTask {
            void task(WorldVFRTrailBuilder vfrTrailBuilder);
        }
    }
}
