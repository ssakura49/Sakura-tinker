package com.ssakura49.sakuratinker.client;

public enum InLevelRenderType {
    TEXTURE_PARTICLE,
    CUSTOM;
    public static InLevelRenderType[] getCommon() {
        return new InLevelRenderType[]{TEXTURE_PARTICLE };
    }
}
