package com.ssakura49.sakuratinker;

import com.ssakura49.sakuratinker.register.STItems;
import com.ssakura49.sakuratinker.utils.ModListUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import slimeknights.tconstruct.shared.TinkerMaterials;

import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class STConfig {
    public static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static final ForgeConfigSpec clientSpec;
    public static final Client CLIENT;
    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static final Server SERVER;
    public static final ForgeConfigSpec serverSpec;
    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        serverSpec = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, STConfig.commonSpec);
    }
    public static class Common {
        public final ForgeConfigSpec.DoubleValue REDUCTION_PER_HIT;
        public final ForgeConfigSpec.DoubleValue MAX_REDUCTION;
        public final ForgeConfigSpec.IntValue RESET_TICK;
        public final ForgeConfigSpec.DoubleValue BLADE_CONVERGENCE_DROP_CHANCE;
        public final ForgeConfigSpec.ConfigValue<String> BLADE_CONVERGENCE_DROP_MOBS;
        public final ForgeConfigSpec.DoubleValue TORTURE_BASE_DAMAGE_THRESHOLD;
        public final ForgeConfigSpec.DoubleValue TORTURE_DAMAGE_MULTIPLIER;
        public final ForgeConfigSpec.DoubleValue Reapers_Blessing_MAX_BONUS;
        public final ForgeConfigSpec.DoubleValue Reapers_Blessing_BONUS_PER_HEALTH;
        public final ForgeConfigSpec.DoubleValue VOID_PEARL_DROP_CHANCE;
        public final ForgeConfigSpec.ConfigValue<String> VOID_PEARL_DROP_MOBS;
        public final ForgeConfigSpec.ConfigValue<String> slimeCrystalEarth;
        public final ForgeConfigSpec.ConfigValue<String> slimeCrystalSky;
        public final ForgeConfigSpec.ConfigValue<String> slimeCrystalNether;
        public final ForgeConfigSpec.ConfigValue<String> goldBlockItem;
        public final ForgeConfigSpec.BooleanValue CHARMS_ALLOW_MULTIPLE;
        public final ForgeConfigSpec.DoubleValue RUINATION_DAMAGE_FACTOR;
        public final ForgeConfigSpec.DoubleValue LIFE_RATIO_PERCENT;
        public final ForgeConfigSpec.IntValue tickInterval;
        public final ForgeConfigSpec.DoubleValue absorptionPerTick;
        public final ForgeConfigSpec.DoubleValue ShitakusoBonus;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.comment("魂樱刻印配方修改").push("recipe");
            slimeCrystalEarth = builder
                    .comment("大地史莱姆水晶 (default: sakuratinker:slime_crystal_earth)")
                    .define("slimeCrystalEarth", "sakuratinker:slime_crystal_earth");
            slimeCrystalSky = builder
                    .comment("天空史莱姆水晶 (default: sakuratinker:slime_crystal_sky)")
                    .define("slimeCrystalSky", "sakuratinker:slime_crystal_sky");
            slimeCrystalNether = builder
                    .comment("下界史莱姆水晶 (default: sakuratinker:slime_crystal_nether")
                    .define("slimeCrystalNether", "sakuratinker:slime_crystal_nether");
            goldBlockItem = builder
                    .comment("金块 (default: minecraft:gold_block)")
                    .define("goldBlock", "minecraft:gold_block");
            builder.pop();

            builder.comment("折磨效果").push("torture");
            this.TORTURE_BASE_DAMAGE_THRESHOLD = builder
                    .comment("基础移动多少米触发伤害(1.0 = 1格)")
                    .defineInRange("baseDamageThreshold", 1.0, 0.1 , Integer.MAX_VALUE);
            this.TORTURE_DAMAGE_MULTIPLIER = builder
                    .comment("每等级对阈值的影响系数(0.2 = 每等级减少20%阈值)")
                    .defineInRange("damageMultiplier", 0.2, 0.0 , 1.0);
            builder.pop();

            builder.comment("高频结界配置").push("high_frequency_barrier");
            this.REDUCTION_PER_HIT = builder
                    .comment("每次受到伤害时增加的伤害减免比例 (0.0 - 1.0)")
                    .defineInRange("reductionPerHit", 0.05, 0.0, 1.0);
            this.MAX_REDUCTION = builder
                    .comment("最大伤害减免比例 (0.0 - 1.0)")
                    .defineInRange("maxReduction", 0.50, 0.0, 1.0);
            this.RESET_TICK = builder
                    .comment("多少tick未受攻击后重置层数 (20 ticks = 1秒，0表示不重置)")
                    .defineInRange("resetTicks", 100, 0, Integer.MAX_VALUE);
            builder.pop();

            builder.comment("万剑归一掉落配置").push("item_drops");
            this.BLADE_CONVERGENCE_DROP_CHANCE = builder
                    .comment("万剑归一的掉落概率 (0.0 - 1.0)")
                    .defineInRange("bladeConvergenceDropChance", 0.1, 0.0, 1.0);
            this.BLADE_CONVERGENCE_DROP_MOBS = builder
                    .comment("可以掉落万剑归一的生物 (用逗号分隔)，例如: 'minecraft:wither,minecraft:ender_dragon'")
                    .define("bladeConvergenceDropMobs", "minecraft:ender_dragon");
            builder.pop();

            builder.comment("死神祝福配置").push("reapers_blessing");
            this.Reapers_Blessing_MAX_BONUS = builder
                    .comment("最大加成 (百分比, 例如 0.4 是 40%)")
                    .defineInRange("maxBonus", 0.4, 0.0, 1.0);
            this.Reapers_Blessing_BONUS_PER_HEALTH = builder
                    .comment("每百分比的生命加成")
                    .defineInRange("bonusPerHealth", 0.01, 0.0, 0.1);
            builder.pop();

            builder.comment("虚空珍珠掉落配置").push("void_pearl_drop");
            this.VOID_PEARL_DROP_CHANCE = builder
                    .comment("掉落概率 (0.0 - 1.0)")
                    .defineInRange("drop_chance", 0.01, 0.0, 1.0);
            this.VOID_PEARL_DROP_MOBS = builder
                    .comment("可以掉落虚空珍珠的生物 (用逗号分隔)，例如: 'minecraft:wither,minecraft:ender_dragon'")
                    .define("drop_mobs", "minecraft:ender_dragon");
            builder.pop();

            builder.comment("护符配置").push("charms");
            this.CHARMS_ALLOW_MULTIPLE = builder
                    .comment("设为true允许装备多件护符")
                    .define("allowMultipleCharms", false);
            builder.pop();

            builder.comment("罪孽根源配置").push("Ruination Modifier Config");
            this.RUINATION_DAMAGE_FACTOR = builder
                    .comment("罪孽根源的伤害系数")
                    .defineInRange("ruinationDamage", 0.03, 0.0, 1.0);
            builder.pop();

            builder.comment("生命之尺").push("Life Ratio Modifier Config");
            this.LIFE_RATIO_PERCENT = builder
                    .comment("生命之尺的增幅系数")
                    .defineInRange("lifeRatio", 0.1, 0.0, 1.0);
            builder.pop();

            builder.push("伤害吸收配置");
            tickInterval = builder
                    .comment("每tick恢复的额外生命 (default: 5)")
                    .defineInRange("tickInterval", 5, 1, 1000);
            absorptionPerTick = builder
                    .comment("恢复生命的间隔 (default: 0.2)")
                    .defineInRange("absorptionPerTick", 0.2, 0.0, 100.0);
            builder.pop();

            builder.push("下克上");
            ShitakusoBonus = builder
                    .comment("Multiplier for damage scaling based on health ratio (default: 0.1)")
                    .defineInRange("k", 0.1, 0.0, 10.0);

            builder.pop();
        }
    }

    public static class Client{
        public Client(ForgeConfigSpec.Builder builder){

        }
    }

    public static class Server{
        public Server(ForgeConfigSpec.Builder builder) {

        }
    }

    private static Supplier<Item> getConfiguredItem(ForgeConfigSpec.ConfigValue<String> config, Supplier<Item> defaultItem) {
        return () -> {
            String value = config.get();
            try {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(value));
                return item != null ? item : defaultItem.get();
            } catch (Exception e) {
                SakuraTinker.LOGGER.error("Invalid item ID in config: {}", value, e);
                return defaultItem.get();
            }
        };
    }

    public static Supplier<Item> slime_crystal_earth() {
        return getConfiguredItem(COMMON.slimeCrystalEarth,
                () -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(SakuraTinker.MODID, "slime_crystal_earth")));
    }

    public static Supplier<Item> slime_crystal_sky() {
        return getConfiguredItem(COMMON.slimeCrystalSky,
                () -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(SakuraTinker.MODID, "slime_crystal_sky")));
    }

    public static Supplier<Item> slime_crystal_nether() {
        return getConfiguredItem(COMMON.slimeCrystalNether,
                () -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(SakuraTinker.MODID, "slime_crystal_nether")));
    }

    public static Supplier<Item> getGoldBlock() {
        return getConfiguredItem(COMMON.goldBlockItem, () -> Items.GOLD_BLOCK);
    }

    public static boolean allowMultipleCharms() {
        return COMMON.CHARMS_ALLOW_MULTIPLE.get();
    }
}
