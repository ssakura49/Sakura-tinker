package com.ssakura49.sakuratinker.render.lighting;

import com.ssakura49.sakuratinker.render.CCRenderState;
import com.ssakura49.sakuratinker.render.pipline.IVertexOperation;
import com.ssakura49.sakuratinker.utils.render.color.ColourRGBA;

/**
 * Faster precomputed version of LightModel that only works for axis planar sides
 */
public class PlanarLightModel implements IVertexOperation {

    public static PlanarLightModel standardLightModel = LightModel.standardLightModel.reducePlanar();

    public int[] colours;

    public PlanarLightModel(int[] colours) {
        this.colours = colours;
    }

    @Override
    public boolean load(CCRenderState ccrs) {
        if (!ccrs.computeLighting) {
            return false;
        }

        ccrs.pipeline.addDependency(ccrs.sideAttrib);
        ccrs.pipeline.addDependency(ccrs.colourAttrib);
        return true;
    }

    @Override
    public void operate(CCRenderState ccrs) {
        ccrs.colour = ColourRGBA.multiply(ccrs.colour, colours[ccrs.side]);
    }

    @Override
    public int operationID() {
        return LightModel.operationIndex;
    }
}

