package com.ssakura49.sakuratinker.library.hooks.builder;

import com.ssakura49.sakuratinker.library.logic.context.ReplaceContext;

import java.util.Collection;

public interface ReplaceMaterialModifierHook {
    boolean replaceItem(ReplaceContext context, int inputIndex, boolean secondary);

    record AllMerger(Collection<ReplaceMaterialModifierHook> modules) implements ReplaceMaterialModifierHook{
        @Override
        public boolean replaceItem(ReplaceContext context, int inputIndex, boolean secondary) {
            for(ReplaceMaterialModifierHook module : modules){
                boolean result = module.replaceItem(context, inputIndex, secondary);
                if(result){
                    return true;
                }
            }
            return false;
        }
    }
}
