package com.ssakura49.sakuratinker.render.shader.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class STShaderInstance extends ShaderInstance {
    private final List<Runnable> applyCallbacks = new LinkedList<>();
//    private final Map<String, STUniform> stUniforms = new HashMap<>();

    protected STShaderInstance(ResourceProvider resourceProvider, ResourceLocation shaderLocation, VertexFormat format) throws IOException {
        super(resourceProvider, shaderLocation, format);

    }

    public static STShaderInstance create(ResourceProvider resourceProvider, ResourceLocation loc, VertexFormat format) {
        try {
            return new STShaderInstance(resourceProvider, loc, format);
        } catch (IOException var4) {
            throw new RuntimeException("Failed to initialize shader.", var4);
        }
    }

    private static float[] parseFloats(int count, JsonArray jsonValues) throws ChainedJsonException {
        int i = 0;
        float[] values = new float[Math.max(count, 16)];
        Iterator var4 = jsonValues.iterator();

        while (var4.hasNext()) {
            JsonElement jsonValue = (JsonElement) var4.next();

            try {
                values[i++] = GsonHelper.convertToFloat(jsonValue, "value");
            } catch (Exception var8) {
                ChainedJsonException chainedjsonexception = ChainedJsonException.forException(var8);
                chainedjsonexception.prependJsonKey("values[" + i + "]");
                throw chainedjsonexception;
            }
        }

        if (count > 1 && jsonValues.size() == 1) {
            Arrays.fill(values, 1, values.length, values[0]);
        }

        return Arrays.copyOfRange(values, 0, count);
    }

    private static int[] parseInts(int count, JsonArray jsonValues) throws ChainedJsonException {
        int i = 0;
        int[] values = new int[Math.max(count, 16)];
        Iterator var4 = jsonValues.iterator();

        while (var4.hasNext()) {
            JsonElement jsonValue = (JsonElement) var4.next();

            try {
                values[i++] = GsonHelper.convertToInt(jsonValue, "value");
            } catch (Exception var8) {
                ChainedJsonException chainedjsonexception = ChainedJsonException.forException(var8);
                chainedjsonexception.prependJsonKey("values[" + i + "]");
                throw chainedjsonexception;
            }
        }

        if (count > 1 && jsonValues.size() == 1) {
            Arrays.fill(values, 1, values.length, values[0]);
        }

        return Arrays.copyOfRange(values, 0, count);
    }

    private static double[] parseDoubles(int count, JsonArray jsonValues) throws ChainedJsonException {
        int i = 0;
        double[] values = new double[Math.max(count, 16)];
        Iterator var4 = jsonValues.iterator();

        while (var4.hasNext()) {
            JsonElement jsonValue = (JsonElement) var4.next();

            try {
                values[i++] = GsonHelper.convertToDouble(jsonValue, "value");
            } catch (Exception var8) {
                ChainedJsonException chainedjsonexception = ChainedJsonException.forException(var8);
                chainedjsonexception.prependJsonKey("values[" + i + "]");
                throw chainedjsonexception;
            }
        }

        if (count > 1 && jsonValues.size() == 1) {
            Arrays.fill(values, 1, values.length, values[0]);
        }

        return Arrays.copyOfRange(values, 0, count);
    }

    public void onApply(Runnable callback) {
        this.applyCallbacks.add(callback);
    }

    @Override
    public void apply() {
        for (Runnable callback : applyCallbacks) callback.run();
        super.apply();
    }

    public @Nullable STUniform getUniform(@NotNull String name) {
        return (STUniform) super.getUniform(name);
    }

    public void parseUniformNode(@NotNull JsonElement json) throws ChainedJsonException {
        JsonObject obj = GsonHelper.convertToJsonObject(json, "uniform");
        String name = GsonHelper.getAsString(obj, "name");
        String typeStr = GsonHelper.getAsString(obj, "type");
        UniformType type = UniformType.parse(typeStr);
        if (type == null) {
            throw new ChainedJsonException("Invalid type '%s'. See UniformType enum. All vanilla types supported.".formatted(typeStr));
        } else {
            int count;
            count = GsonHelper.getAsInt(obj, "count");
            label45:
            switch (type) {
                case FLOAT:
                    switch (count) {
                        case 2:
                            type = UniformType.VEC2;
                            break label45;
                        case 3:
                            type = UniformType.VEC3;
                            break label45;
                        case 4:
                            type = UniformType.VEC4;
                        default:
                            break label45;
                    }
                case INT:
                    switch (count) {
                        case 2:
                            type = UniformType.I_VEC2;
                            break label45;
                        case 3:
                            type = UniformType.I_VEC3;
                            break label45;
                        case 4:
                            type = UniformType.I_VEC4;
                        default:
                            break label45;
                    }
                case U_INT:
                    switch (count) {
                        case 2:
                            type = UniformType.U_VEC2;
                            break;
                        case 3:
                            type = UniformType.U_VEC3;
                            break;
                        case 4:
                            type = UniformType.U_VEC4;
                    }
            }

            STUniform uniform = STUniform.makeUniform(name, type, count, this);
            JsonArray jsonValues = GsonHelper.getAsJsonArray(obj, "values");
            if (jsonValues.size() != count && jsonValues.size() > 1) {
                throw new ChainedJsonException("Invalid amount of values specified (expected " + count + ", found " + jsonValues.size() + ")");
            } else {
                switch (type.getCarrier()) {
                    case INT:
                    case U_INT:
                        uniform.glUniformI(parseInts(count, jsonValues));
                        break;
                    case FLOAT:
                    case MATRIX:
                        uniform.glUniformF(false, parseFloats(count, jsonValues));
                        break;
                    case DOUBLE:
                    case D_MATRIX:
                        uniform.glUniformD(false, parseDoubles(count, jsonValues));
                }

                this.uniforms.add(uniform);
            }
        }
    }
}
