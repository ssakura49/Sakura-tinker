package com.ssakura49.sakuratinker.library.mixinplugin;

import com.ssakura49.sakuratinker.utils.SafeClassUtil;

public class EeeabsmobsMixinPlugin extends MixinPlugin{
    @Override
    public String[] getRequiredModIds() {
        return new String[]{SafeClassUtil.Modid.Eeeabsmobs};
    }
}
