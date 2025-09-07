package com.ssakura49.sakuratinker.client.baked.model;


public interface IVertexProducer {

    /**
     * @param consumer consumer to receive the vertex data this producer can provide
     */
    void pipe(IVertexConsumer consumer);
}
