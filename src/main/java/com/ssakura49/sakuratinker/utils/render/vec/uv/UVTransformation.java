package com.ssakura49.sakuratinker.utils.render.vec.uv;

import com.ssakura49.sakuratinker.render.CCRenderState;
import com.ssakura49.sakuratinker.render.pipline.IVertexOperation;
import com.ssakura49.sakuratinker.utils.render.vec.ITransformation;

/**
 * Abstract supertype for any UV transformation
 */
public abstract class UVTransformation extends ITransformation<UV, UVTransformation> implements IVertexOperation {

    public static final int operationIndex = IVertexOperation.registerOperation();

    public UVTransformation at(UV point) {
        return new UVTransformationList(new UVTranslation(-point.u, -point.v), this, new UVTranslation(point.u, point.v));
    }

    public UVTransformationList with(UVTransformation t) {
        return new UVTransformationList(this, t);
    }

    @Override
    public boolean load(CCRenderState ccrs) {
        return !isRedundant();
    }

    @Override
    public void operate(CCRenderState ccrs) {
        apply(ccrs.vert.uv);
        ccrs.sprite = null;
    }

    @Override
    public int operationID() {
        return operationIndex;
    }
}
