package com.ssakura49.sakuratinker.content.entity.terraprisma;

public record PrismStats(
        float maxHealth,
        float movementSpeed,
        float flyingSpeed,
        float attackDamage,
        float followRange,
        int attackCooldown
) {}