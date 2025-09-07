package com.ssakura49.sakuratinker.client.baked.loader;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.ssakura49.sakuratinker.client.baked.tinker.CosmicToolModelGeometry;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import slimeknights.tconstruct.library.client.model.tools.ToolModel;

public class CosmicToolModelLoader implements IGeometryLoader<CosmicToolModelGeometry> {
    public static final CosmicModelLoader INSTANCE = new CosmicModelLoader();
    @Override
    public CosmicToolModelGeometry read(JsonObject json, JsonDeserializationContext context) {
        ToolModel model = ToolModel.LOADER.read(json.getAsJsonObject("base"), context);
        return new CosmicToolModelGeometry(model);
    }
}
