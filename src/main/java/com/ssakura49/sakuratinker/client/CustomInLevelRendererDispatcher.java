package com.ssakura49.sakuratinker.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.auto.CILAutoRegisterHandler;
import com.ssakura49.sakuratinker.render.IBufferBuilder;
import com.ssakura49.sakuratinker.render.VFRBuilders;
import com.ssakura49.sakuratinker.render.shader.STRenderType;
import com.ssakura49.sakuratinker.utils.math.MathUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.*;

import java.lang.Math;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CustomInLevelRendererDispatcher {
    public static VFRBuilders.WorldVFRTrailBuilder normalStarTrailsBuilder;
    public static final ResourceLocation LOCATION_PARTICLES = SakuraTinker.location("textures/atlas/cil_particles.png");
    public static final ResourceLocation CIL_PARTICLE_INFO = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "cil_particle");
    public static final Object2ObjectOpenHashMap<InLevelRenderType, List<ICustomInLevelRenderTask>> renderersSet = new Object2ObjectOpenHashMap<>();
    public static final List<CustomTextureParticleRenderer> textureParticles = new ArrayList<>();
    public static final Object2ObjectOpenHashMap<Class<? extends CustomTextureParticleRenderer>, MutableSpriteSet> spriteSets = new Object2ObjectOpenHashMap<>();
    public static final List<ICustomTickrateRenderer> tickRate = new ArrayList<>();
    public static final List<Entity> allTickingEntities = Collections.synchronizedList(new ArrayList<>());
    public static TextureAtlas particleAtlas;
    public static Quaternionf rotation = new Quaternionf(0, 0, 0, 1);
    public static PoseStack worldMatrix;
    public static IBufferBuilder cilFastBufferBuilder = new IBufferBuilder(1024 * 100);
    public static boolean isShadering;
    static Minecraft mc = Minecraft.getInstance();
    static {
        for (InLevelRenderType inLevelRenderType : InLevelRenderType.values()) {
            renderersSet.put(inLevelRenderType, new ArrayList<>());
        }
    }
    public static void initTextureParticle(Class<? extends CustomTextureParticleRenderer> clazz) {
        spriteSets.put(clazz, new MutableSpriteSet());
    }

    public static synchronized void addTask(InLevelRenderType levelRenderType, ICustomInLevelRenderTask task) {
        renderersSet.get(levelRenderType).add(task);
    }

    public static boolean in(ICustomInLevelRenderTask instance, Camera camera, float x, float y, float z) {
        assert mc.player != null;
        Vector3f cameraPos = camera.getPosition().toVector3f();
        Vec3 playerPos = mc.player.position;
        double dis2 = cameraPos.distanceSquared(x, y, z);
        float r2 = instance.radius() * instance.radius();
        if (dis2 > (instance.maxRenderDistance() < 0F ? mc.gameRenderer.getDepthFar() * mc.gameRenderer.getDepthFar() + r2 : instance.maxRenderDistance()))
            return false;
        if (!mc.options.getCameraType().isFirstPerson() && mc.player != null && dis2 < 0.5F + cameraPos.distanceSquared((float) playerPos.x, (float) playerPos.y, (float) playerPos.z))
            return true;
        double angel = 180D * 0.01745F - calculateRadians(new Vector3f(0), camera.getLookVector(),
                new Vector3f(x, y, z), cameraPos) - MathUtils.asin(r2 / dis2);
        if (angel <= 90F * 0.01745F)
            return true;

        return instance.alwaysRender();
    }

    public static void runAllTasks(Camera camera, PoseStack matrix, float partialTicks, double d3, double d4, double d5, LightTexture lightTexture, Frustum frustum) {
        worldMatrix = matrix;
        ProfilerFiller profilerFiller = mc.getProfiler();
        if (STConfig.Client.no_mod_render) {
            profilerFiller.push("CIL_render");
            profilerFiller.push("CIL_render_end");
            profilerFiller.pop();
            profilerFiller.popPush("CIL_other_render_end");
            profilerFiller.pop();
            profilerFiller.pop();
            return;
        }
        if (mc.level != null) {
            profilerFiller.push("CIL_main");
            if (normalStarTrailsBuilder == null)
                normalStarTrailsBuilder = VFRBuilders.WorldVFRTrailBuilder.create();
            MultiBufferSource.BufferSource buffer = mc.renderBuffers.bufferSource();
            lightTexture.turnOnLightLayer();
            lightTexture.updateLightTexture(mc.getPartialTick());
            synchronized (textureParticles) {
                if (!textureParticles.isEmpty() && (mc.player != null && mc.player.isAlive())) {
                    RenderSystem.enableDepthTest();
                    RenderSystem.activeTexture(org.lwjgl.opengl.GL13.GL_TEXTURE2);
                    RenderSystem.activeTexture(org.lwjgl.opengl.GL13.GL_TEXTURE0);
                    Tesselator tesselator = Tesselator.getInstance();
                    BufferBuilder bufferbuilder = tesselator.getBuilder();
                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.NEW_ENTITY);
                    CustomTextureParticleRenderer current;
                    final List<CustomTextureParticleRenderer> temp = new ArrayList<>();
                    matrix.pushPose();
                    matrix.translate(-d3, -d4, -d5);
                    for (int i = textureParticles.size() - 1; i >= 0; i--) {
                        current = textureParticles.get(i);
                        if (current != null) {
                            if (current.isEnable()) {
                                profilerFiller.push("CILTP_render");
                                //textureParticleRenderer.setup();
                                double d0 = current.getX(partialTicks);
                                double d1 = current.getY(partialTicks);
                                double d2 = current.getZ(partialTicks);
                                int packedLight = current.getLightColor(d0, d1, d2, mc.level);
                                if (Modifier.isFinal(current.flag)) {
                                    profilerFiller.push("CILTP_light");
                                    profilerFiller.pop();
                                    matrix.translate(d0, d1, d2);
                                    profilerFiller.push("CILTP_particle_render");
                                    current.doParticleRenderTask(bufferbuilder, mc.level, camera, matrix, packedLight, partialTicks);
                                    profilerFiller.pop();
                                    matrix.translate(-d0, -d1, -d2);
                                    if (i > 1) {
                                        CustomTextureParticleRenderer next = textureParticles.get(i - 1);
                                        if (current.disSqr(next) < 1.5f)
                                            next.flag = next.flag | Modifier.FINAL;
                                    }
                                    current.flag = 0x00000000;
                                } else {
                                    profilerFiller.push("CILTP_checkIn");
                                    boolean check = in(current, camera, (float) d0, (float) d1, (float) d2);
                                    profilerFiller.pop();
                                    if (check) {
                                        profilerFiller.push("CILTP_light");
                                        profilerFiller.pop();
                                        matrix.translate(d0, d1, d2);
                                        profilerFiller.push("CILTP_particle_render");
                                        current.doParticleRenderTask(bufferbuilder, mc.level, camera, matrix, packedLight, partialTicks);
                                        profilerFiller.pop();
                                        matrix.translate(-d0, -d1, -d2);
                                        if (i > 1) {
                                            CustomTextureParticleRenderer next = textureParticles.get(i - 1);
                                            if (current.disSqr(next) < 1.5f)
                                                next.flag = next.flag | Modifier.FINAL;
                                        }
                                        current.flag = 0x00000000;
                                    }
                                }
                                profilerFiller.pop();
                            } else {
                                temp.add(current);
                            }
                        }
                    }
                    matrix.popPose();
                    for (CustomTextureParticleRenderer c : temp) {
                        textureParticles.remove(c);
                        c.disable();
                    }
                    profilerFiller.push("CILTP_render_end");
                    STRenderType.PRT_WHITE.setupRenderState();
                    RenderSystem.setShaderTexture(0, CustomInLevelRendererDispatcher.LOCATION_PARTICLES);
                    RenderSystem.setShader(GameRenderer::getRendertypeBeaconBeamShader);
                    tesselator.end();
                    STRenderType.PRT_WHITE.clearRenderState();
                    RenderSystem.depthMask(true);
                    profilerFiller.pop();
                }
            }
            profilerFiller.push("CIL_trail");
            if (!normalStarTrailsBuilder.toRender.isEmpty()) {
                matrix.pushPose();
                matrix.translate(-d3, -d4, -d5);
                normalStarTrailsBuilder.renderTrails();
                matrix.popPose();
            }
            profilerFiller.pop();
            if (totalSize(renderersSet) > 0 && (mc.player != null && mc.player.isAlive())) {
                for (InLevelRenderType levelRenderType : InLevelRenderType.values()) {
                    List<ICustomInLevelRenderTask> currentRenderSet = renderersSet.get(levelRenderType);
                    ICustomInLevelRenderTask iCustomInLevelRenderTask;
                    for (int i = currentRenderSet.size() - 1; i >= 0; i--) {
                        iCustomInLevelRenderTask = currentRenderSet.get(i);
                        if (iCustomInLevelRenderTask != null) {
                            if (iCustomInLevelRenderTask.isEnable()) {
                                profilerFiller.push("CIL_render");
                                double d0 = iCustomInLevelRenderTask.getX();
                                double d1 = iCustomInLevelRenderTask.getY();
                                double d2 = iCustomInLevelRenderTask.getZ();
                                if (in(iCustomInLevelRenderTask, camera, (float) d0, (float) d1, (float) d2)) {
                                    int packedLight = iCustomInLevelRenderTask.getLightColor(d0, d1, d2, mc.level);
                                    matrix.pushPose();
                                    matrix.translate(d0 - d3, d1 - d4, d2 - d5);
                                    iCustomInLevelRenderTask.doLevelRenderTask(buffer, mc.level, camera, matrix, packedLight, partialTicks);
                                    matrix.popPose();
                                }
                                profilerFiller.pop();
                            } else {
                                profilerFiller.push("CIL_clean_should_removed");
                                currentRenderSet.remove(iCustomInLevelRenderTask);
                                iCustomInLevelRenderTask.disable();
                                profilerFiller.pop();
                            }
                        }
                    }
                }
            }
            lightTexture.turnOffLightLayer();
        }

    }

    @SubscribeEvent
    public static void clientLeftGame(ClientPlayerNetworkEvent.LoggingOut event) {
        synchronized (renderersSet) {
            List<ICustomInLevelRenderTask> allRenderers = allRenderers();
            int totalSize = allRenderers.size();
            if (mc.level == null) {
                if (totalSize > 0) {
                    for (InLevelRenderType renderType : InLevelRenderType.values()) {
                        List<ICustomInLevelRenderTask> set = renderersSet.get(renderType);
                        set.forEach(ICustomInLevelRenderTask::disable);
                        set.clear();
                    }
                }
                return;
            }
        }
        synchronized (textureParticles) {
            int totalSize = textureParticles.size();
            if (mc.level == null && totalSize > 0) {
                textureParticles.forEach(ICustomInLevelRenderTask::disable);
                textureParticles.clear();
            }
        }
    }

    @SubscribeEvent
    public static void onTickrateRenderer(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !mc.pause) {
            ProfilerFiller profilerFiller = mc.getProfiler();
            profilerFiller.push("CIL_render_tick");
            synchronized (tickRate) {
                if (!tickRate.isEmpty()) {
                    for (int i = tickRate.size() - 1; i >= 0; i--) {
                        ICustomTickrateRenderer tickrateRenderer = tickRate.get(i);
                        if (tickrateRenderer.isEnable())
                            tickrateRenderer.tick(mc.level, mc.gameRenderer.getMainCamera(), mc.getPartialTick());
                        else tickRate.remove(tickrateRenderer);
                    }
                }
            }
            profilerFiller.popPush(SakuraTinker.MODID + "_getEntities");
            if (mc.level != null) {
                allTickingEntities.clear();
                mc.level.tickingEntities.forEach(allTickingEntities::add);
                allTickingEntities.addAll(mc.level.getPartEntities());
            }
            profilerFiller.pop();
        }
    }

    @SubscribeEvent
    public static void levelReloadRenderTypes(LevelEvent.Load event) {
        if (event.getLevel().isClientSide())
            STRenderType.reloadParticleRenderTypes();
    }

    public static double calculateRadians(
            Vector3f line1Start, Vector3f line1End,
            Vector3f line2Start, Vector3f line2End) {
        Vector3d dir1 = new Vector3d(line1End);
        dir1.sub(line1Start);
        Vector3d dir2 = new Vector3d(line2End);
        dir2.sub(line2Start);
        Vector3d crossProduct = new Vector3d();
        dir1.cross(dir2, crossProduct);

        if (crossProduct.equals(new Vector3d(0, 0, 0))) {
            return 0;
        }
        Matrix3d matrix = new Matrix3d();
        matrix.setColumn(0, dir1);
        matrix.setColumn(1, dir2);
        matrix.setColumn(2, crossProduct);

        double angle = Math.atan2(matrix.determinant(), dir1.dot(dir2));
        if (angle < 0) {
            angle += 2 * Math.PI;
        }

        return angle;
    }
    public static double calculateRadians(Vector2f v1, Vector2f v2) {
        return calculateRadians(v1.x, v1.y, v2.x, v2.y);
    }
    public static double calculateRadians(double x1, double y1, double x2, double y2) {
        double dotProduct = x1 * x2 + y1 * y2;
        double magnitude1 = Math.sqrt(x1 * x1 + y1 * y1);
        double magnitude2 = Math.sqrt(x2 * x2 + y2 * y2);
        double cosTheta = dotProduct / (magnitude1 * magnitude2);
        return MathUtils.acos(cosTheta);
    }
    public static int totalSize(Map<?, ? extends Collection<?>> map) {
        int size = 0;
        for (Collection<?> collection : map.values()) size += collection.size();
        return size;
    }

    public static List<ICustomInLevelRenderTask> allRenderers() {
        return renderersSet.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static void endRenderType(BufferBuilder p_277996_, VertexSorting p_277677_, RenderType renderType) {
        if (p_277996_.building()) {
            if (renderType.sortOnUpload) {
                p_277996_.setQuadSorting(p_277677_);
            }

            BufferBuilder.RenderedBuffer bufferbuilder$renderedbuffer = p_277996_.end();
            renderType.setupRenderState();
            BufferUploader.drawWithShader(bufferbuilder$renderedbuffer);
            renderType.clearRenderState();
        }
    }

    public static void init() {
        particleAtlas = new TextureAtlas(LOCATION_PARTICLES);
        TextureManager textureManager = mc.textureManager;
        textureManager.register(particleAtlas.location(), particleAtlas);
    }

    public static void close() {
        if (particleAtlas != null)
            particleAtlas.clearTextureData();
    }

    public static List<ResourceLocation> texturesToDefinition(CustomTextureParticleRenderer renderer) {
        return renderer.allTextures().stream()
                .map(rl -> rl.withPath(rl.getPath().substring(rl.getPath().lastIndexOf('/') + 1, rl.getPath().lastIndexOf(".png"))))
                .toList();
    }

    public static CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparationBarrier, ResourceManager resourceManager, Executor exe0, Executor exe1) {
        if (particleAtlas == null) init();
        synchronized (CustomInLevelRendererDispatcher.class) {
            ProfilerFiller profiler = mc.getProfiler();
            profiler.push("CIL_texture_atlas");

            CompletableFuture<List<ParticleDefinition>> completablefuture = CompletableFuture.supplyAsync(() -> CILAutoRegisterHandler.textureParticles, exe0)
                    .thenCompose((textureParClassSet) -> {
                        List<CompletableFuture<ParticleDefinition>> list = new ArrayList<>(textureParClassSet.size());
                        textureParClassSet.forEach(
                                (clazz, emptyInstance) -> list.add(CompletableFuture.supplyAsync(
                                        () -> new ParticleDefinition(clazz, Optional.of(texturesToDefinition(emptyInstance))), exe0)));
                        return Util.sequence(list);
                    });
            CompletableFuture<SpriteLoader.Preparations> preparations = SpriteLoader.create(particleAtlas).loadAndStitch(resourceManager, CIL_PARTICLE_INFO, 0, exe0).thenCompose(SpriteLoader.Preparations::waitForUpload);
            CompletableFuture<Void> result = CompletableFuture.allOf(preparations, completablefuture).thenCompose(preparationBarrier::wait).thenAcceptAsync((voiD) -> {
                profiler.startTick();
                profiler.push("upload");
                SpriteLoader.Preparations spriteloader$preparations = preparations.join();
                particleAtlas.upload(spriteloader$preparations);
                profiler.popPush("bindSpriteSets");
                Set<ResourceLocation> set = new HashSet<>();
                TextureAtlasSprite textureatlassprite = spriteloader$preparations.missing();
                completablefuture.join().forEach((particleDefinition) -> {
                    Optional<List<ResourceLocation>> optional = particleDefinition.sprites();
                    if (optional.isPresent()) {
                        List<TextureAtlasSprite> list = new ArrayList<>();
                        spriteloader$preparations.regions().forEach(SakuraTinker::out);
                        for (ResourceLocation resourcelocation : optional.get()) {
                            TextureAtlasSprite textureatlassprite1 = spriteloader$preparations.regions().get(resourcelocation);
                            if (textureatlassprite1 == null) {
                                set.add(resourcelocation);
                                list.add(textureatlassprite);
                            } else {
                                list.add(textureatlassprite1);
                            }
                        }

                        if (list.isEmpty()) {
                            list.add(textureatlassprite);
                        }

                        spriteSets.get(particleDefinition.clazz).rebind(list);
                    }
                });
                if (!set.isEmpty()) {
                    SakuraTinker.out("Missing cil_particle sprites: {" + set.stream().sorted().map(ResourceLocation::toString).collect(Collectors.joining(",")) + "}");
                }
                profiler.pop();
                profiler.endTick();
            }, exe1);
            profiler.pop();
            return result;
        }
    }

    @OnlyIn(Dist.CLIENT)
    record ParticleDefinition(Class<? extends CustomTextureParticleRenderer> clazz,
                              Optional<List<ResourceLocation>> sprites) {
    }

    @OnlyIn(Dist.CLIENT)
    public static class MutableSpriteSet implements SpriteSet {
        private List<TextureAtlasSprite> sprites;

        public TextureAtlasSprite get(int age, int maxLife) {
            return this.sprites.get(age * (this.sprites.size() - 1) / maxLife);
        }

        public TextureAtlasSprite get(RandomSource random) {
            return this.sprites.get(random.nextInt(this.sprites.size()));
        }

        public TextureAtlasSprite get(int index) {
            return this.sprites.get(index);
        }

        public List<TextureAtlasSprite> getSprites() {
            return sprites;
        }

        public void rebind(List<TextureAtlasSprite> provides) {
            this.sprites = ImmutableList.copyOf(provides);
        }
    }
}
