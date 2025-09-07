package com.ssakura49.sakuratinker.render.pipline;

import com.ssakura49.sakuratinker.render.pipline.attribute.AttributeKey;

/**
 * Management class for a vertex attribute such as colour, normal etc
 * This class should handle the loading of the attribute from an Attribute provided by
 * {@link IVertexSource#getAttribute(AttributeKey)} or the computation of this attribute from others
 *
 * @param <T> The type for this attribute eg. int[], Vector3[]
 */
public abstract class VertexAttribute<T> implements IVertexOperation {

    private final AttributeKey<T> key;
    /**
     * Set to true when the attribute is part of the pipeline. Should only be managed by CCRenderState when constructing the pipeline
     */
    public boolean active = false;

    public VertexAttribute(AttributeKey<T> key) {
        this.key = key;
    }

    @Override
    public int operationID() {
        return key.operationIndex;
    }
}
