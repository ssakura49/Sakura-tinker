package com.ssakura49.sakuratinker.client.baked.model;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.ssakura49.sakuratinker.utils.math.InterpHelper;
import com.ssakura49.sakuratinker.utils.math.MathUtils;
import com.ssakura49.sakuratinker.utils.render.VertexUtils;
import com.ssakura49.sakuratinker.utils.render.vec.Cuboid6;
import com.ssakura49.sakuratinker.utils.render.vec.Vector3;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

/**
 * A simple easy to manipulate quad format. Can be reset and then used on a different format.
 *
 * @author covers1624
 */
public class Quad implements IVertexProducer, IVertexConsumer {

    // Cache for normal computation.
    private final Vector3 v1 = new Vector3();
    private final Vector3 v2 = new Vector3();
    private final Vector3 t = new Vector3();
    private final Cuboid6 c = new Cuboid6();
    public CachedFormat format;
    public int tintIndex = -1;
    public Direction orientation;
    public boolean diffuseLighting = true;
    public TextureAtlasSprite sprite;
    public Quad.Vertex[] vertices = new Quad.Vertex[4];
    public boolean full;
    // Not copied.
    private int vertexIndex = 0;

    /**
     * Use this if you reset the quad each time you use it.
     */
    public Quad() {
    }

    /**
     * use this if you want to initialize the quad with a format.
     *
     * @param format The format.
     */
    public Quad(CachedFormat format) {
        this.format = format;
    }

    @Override
    public VertexFormat getVertexFormat() {
        return format.format;
    }

    @Override
    public void setQuadTint(int tint) {
        tintIndex = tint;
    }

    @Override
    public void setQuadOrientation(Direction orientation) {
        this.orientation = orientation;
    }

    @Override
    public void setApplyDiffuseLighting(boolean diffuse) {
        diffuseLighting = diffuse;
    }

    @Override
    public void setTexture(TextureAtlasSprite texture) {
        sprite = texture;
    }

    @Override
    public void put(int element, float... data) {
        if (full) {
            throw new RuntimeException("Unable to add data when full.");
        }
        Quad.Vertex v = vertices[vertexIndex];
        if (v == null) {
            v = new Quad.Vertex(format);
            vertices[vertexIndex] = v;
        }
        System.arraycopy(data, 0, v.raw[element], 0, data.length);
        if (element == (format.elementCount - 1)) {
            vertexIndex++;
            if (vertexIndex == 4) {
                vertexIndex = 0;
                full = true;
                if (orientation == null) {
                    calculateOrientation(false);
                }
            }
        }
    }

    @Override
    public void put(Quad quad) {
        copyFrom(quad);
    }

    @Override
    public void pipe(IVertexConsumer consumer) {
        if (consumer instanceof IVertexConsumer) {
            consumer.put(this);
        } else {
            consumer.setQuadTint(tintIndex);
            consumer.setQuadOrientation(orientation);
            consumer.setApplyDiffuseLighting(diffuseLighting);
            consumer.setTexture(sprite);
            for (Quad.Vertex v : vertices) {
                for (int e = 0; e < format.elementCount; e++) {
                    consumer.put(e, v.raw[e]);
                }
            }
        }
    }

    /**
     * Used to reset the interpolation values inside the provided helper.
     *
     * @param helper The helper.
     * @param s      The axis. side >> 1;
     * @return The same helper.
     */
    public InterpHelper resetInterp(InterpHelper helper, int s) {
        helper.reset(
                vertices[0].dx(s), vertices[0].dy(s),
                vertices[1].dx(s), vertices[1].dy(s),
                vertices[2].dx(s), vertices[2].dy(s),
                vertices[3].dx(s), vertices[3].dy(s)
        );
        return helper;
    }

    /**
     * Clamps the Quad inside the box.
     *
     * @param bb The box.
     */
    public void clamp(AABB bb) {
        clamp(c.set(bb));
    }

    /**
     * Clamps the Quad inside the box.
     *
     * @param cuboid The box.
     */
    public void clamp(Cuboid6 cuboid) {
        for (Quad.Vertex vertex : vertices) {
            float[] vec = vertex.vec;
            vec[0] = (float) MathUtils.clip(vec[0], cuboid.min.x, cuboid.max.x);
            vec[1] = (float) MathUtils.clip(vec[1], cuboid.min.y, cuboid.max.y);
            vec[2] = (float) MathUtils.clip(vec[2], cuboid.min.z, cuboid.max.z);
        }
        calculateOrientation(true);
    }

    /**
     * Re-calculates the Orientation of this quad,
     * optionally the normal vector.
     *
     * @param setNormal If the normal vector should be updated.
     */
    public void calculateOrientation(boolean setNormal) {
        v1.set(vertices[3].vec).subtract(t.set(vertices[1].vec));
        v2.set(vertices[2].vec).subtract(t.set(vertices[0].vec));

        Vector3 normal = v2.crossProduct(v1).normalize();

        if (format.hasNormal && setNormal) {
            for (Quad.Vertex vertex : vertices) {
                vertex.normal[0] = (float) normal.x;
                vertex.normal[1] = (float) normal.y;
                vertex.normal[2] = (float) normal.z;
                vertex.normal[3] = 0;
            }
        }
        orientation = Direction.getNearest(normal.x, normal.y, normal.z);
    }

    /**
     * Used to create a new quad complete copy of this one.
     *
     * @return The new quad.
     */
    public Quad copy() {
        if (!full) {
            throw new RuntimeException("Only copying full quads is supported.");
        }
        Quad quad = new Quad(format);
        quad.tintIndex = tintIndex;
        quad.orientation = orientation;
        quad.diffuseLighting = diffuseLighting;
        quad.sprite = sprite;
        quad.full = true;
        for (int i = 0; i < 4; i++) {
            quad.vertices[i] = vertices[i].copy();
        }
        return quad;
    }

    /**
     * Copies the data inside the given quad to this one. This ignores VertexFormat, please make sure your quads are in
     * the same format.
     *
     * @param quad The Quad to copy from.
     * @return This quad.
     */
    public Quad copyFrom(Quad quad) {
        tintIndex = quad.tintIndex;
        orientation = quad.orientation;
        diffuseLighting = quad.diffuseLighting;
        sprite = quad.sprite;
        full = quad.full;
        for (int v = 0; v < 4; v++) {
            for (int e = 0; e < format.elementCount; e++) {
                System.arraycopy(quad.vertices[v].raw[e], 0, vertices[v].raw[e], 0, 4);
            }
        }
        return this;
    }

    /**
     * Reset the quad to the new format.
     *
     * @param format The new format.
     */
    public void reset(CachedFormat format) {
        this.format = format;
        tintIndex = -1;
        orientation = null;
        diffuseLighting = true;
        sprite = null;
        for (int i = 0; i < vertices.length; i++) {
            Quad.Vertex v = vertices[i];
            if (v == null) {
                vertices[i] = v = new Quad.Vertex(format);
            }
            v.reset(format);
        }
        vertexIndex = 0;
        full = false;
    }

    /**
     * Rewind this Quad without completely resetting it.
     */
    public void rewind() {
        vertexIndex = 0;
        full = false;
    }

    /**
     * Bakes this Quad to a BakedQuad.
     *
     * @return The BakedQuad.
     */
    public BakedQuad bake() {
        int[] packedData = new int[format.format.getVertexSize()];
        for (int v = 0; v < 4; v++) {
            for (int e = 0; e < format.elementCount; e++) {
                VertexUtils.pack(vertices[v].raw[e], packedData, format.format, v, e);
            }
        }

        return makeQuad(packedData);
    }

    // Broken out as a stub for mixins to target easier.
    private BakedQuad makeQuad(int[] packedData) {
        if (format.format != DefaultVertexFormat.BLOCK) {
            throw new IllegalStateException("Unable to bake this quad to the specified format. " + format.format);
        }
        return new BakedQuad(packedData, tintIndex, orientation, sprite, diffuseLighting);
    }

    /**
     * A simple vertex format.
     */
    public static class Vertex {

        public CachedFormat format;

        /**
         * The raw data.
         */
        public float[][] raw;

        // References to the arrays inside raw.
        public float[] vec;
        public float[] normal;
        public float[] color;
        public float[] uv;
        public float[] overlay;
        public float[] lightmap;

        /**
         * Create a new Vertex.
         *
         * @param format The format for the vertex.
         */
        public Vertex(CachedFormat format) {
            this.format = format;
            raw = new float[format.elementCount][4];
            preProcess();
        }

        /**
         * Creates a new Vertex using the data inside the other. A copy!
         *
         * @param other The other.
         */
        public Vertex(Quad.Vertex other) {
            format = other.format;
            raw = other.raw.clone();
            for (int v = 0; v < format.elementCount; v++) {
                raw[v] = other.raw[v].clone();
            }
            preProcess();
        }

        /**
         * Pulls references to the individual element's arrays inside raw. Modifying the individual element arrays will
         * update raw.
         */
        public void preProcess() {
            if (format.hasPosition) {
                vec = raw[format.positionIndex];
            }
            if (format.hasNormal) {
                normal = raw[format.normalIndex];
            }
            if (format.hasColor) {
                color = raw[format.colorIndex];
            }
            if (format.hasUV) {
                uv = raw[format.uvIndex];
            }
            if (format.hasOverlay) {
                overlay = raw[format.overlayIndex];
            }
            if (format.hasLightMap) {
                lightmap = raw[format.lightMapIndex];
            }
        }

        /**
         * Gets the 2d X coord for the given axis.
         *
         * @param s The axis. side >> 1
         * @return The x coord.
         */
        public float dx(int s) {
            if (s <= 1) {
                return vec[0];
            } else {
                return vec[2];
            }
        }

        /**
         * Gets the 2d Y coord for the given axis.
         *
         * @param s The axis. side >> 1
         * @return The y coord.
         */
        public float dy(int s) {
            if (s > 0) {
                return vec[1];
            } else {
                return vec[2];
            }
        }

        /**
         * Interpolates the new color values for this Vertex using the others as a reference.
         *
         * @param interpHelper The InterpHelper to use.
         * @param others       The other Vertices to use as a template.
         * @return The same Vertex.
         */
        public Quad.Vertex interpColorFrom(InterpHelper interpHelper, Quad.Vertex[] others) {
            for (int e = 0; e < 4; e++) {
                float p1 = others[0].color[e];
                float p2 = others[1].color[e];
                float p3 = others[2].color[e];
                float p4 = others[3].color[e];
                // Only interpolate if colors are different.
                if (p1 != p2 || p2 != p3 || p3 != p4) {
                    color[e] = interpHelper.interpolate(p1, p2, p3, p4);
                }
            }
            return this;
        }

        /**
         * Interpolates the new UV values for this Vertex using the others as a reference.
         *
         * @param interpHelper The InterpHelper to use.
         * @param others       The other Vertices to use as a template.
         * @return The same Vertex.
         */
        public Quad.Vertex interpUVFrom(InterpHelper interpHelper, Quad.Vertex[] others) {
            for (int e = 0; e < 2; e++) {
                float p1 = others[0].uv[e];
                float p2 = others[1].uv[e];
                float p3 = others[2].uv[e];
                float p4 = others[3].uv[e];
                if (p1 != p2 || p2 != p3 || p3 != p4) {
                    uv[e] = interpHelper.interpolate(p1, p2, p3, p4);
                }
            }
            return this;
        }

        /**
         * Interpolates the new LightMap values for this Vertex using the others as a reference.
         *
         * @param interpHelper The InterpHelper to use.
         * @param others       The other Vertices to use as a template.
         * @return The same Vertex.
         */
        public Quad.Vertex interpLightMapFrom(InterpHelper interpHelper, Quad.Vertex[] others) {
            for (int e = 0; e < 2; e++) {
                float p1 = others[0].lightmap[e];
                float p2 = others[1].lightmap[e];
                float p3 = others[2].lightmap[e];
                float p4 = others[3].lightmap[e];
                if (p1 != p2 || p2 != p3 || p3 != p4) {
                    lightmap[e] = interpHelper.interpolate(p1, p2, p3, p4);
                }
            }
            return this;
        }

        /**
         * Copies this Vertex to a new one.
         *
         * @return The new Vertex.
         */
        public Quad.Vertex copy() {
            return new Quad.Vertex(this);
        }

        /**
         * Resets the Vertex to a new format. Expands the raw array if needed.
         *
         * @param format The format to reset to.
         */
        public void reset(CachedFormat format) {
            // If the format is different and our raw array is smaller, then expand it.
            if (!this.format.equals(format) && format.elementCount > raw.length) {
                raw = new float[format.elementCount][4];
            }
            this.format = format;

            vec = null;
            normal = null;
            color = null;
            uv = null;
            lightmap = null;

            preProcess();
        }
    }
}
