package com.ssakura49.sakuratinker.library.interfaces.item;

public interface FadedTooltipExpends {
    int expends();

    int tooltips();

    default boolean crossFadedCheck() {
        return false;
    }
}
