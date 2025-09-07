package com.ssakura49.sakuratinker.data.generator.providiers.tinker;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.data.generator.STMaterialId;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.data.tinkering.AbstractMaterialTagProvider;

public class STMaterialTagProvider extends AbstractMaterialTagProvider {
    public STMaterialTagProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, SakuraTinker.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(TinkerTags.Materials.EXCLUDE_FROM_LOOT).addOptional(
                STMaterialId.aurumos,
                STMaterialId.bear_interest,
                STMaterialId.eezo,
                STMaterialId.nihilite,
                STMaterialId.blood_bound_steel,
                STMaterialId.chimera_gamma,
                STMaterialId.echo_slimesteel,
                STMaterialId.frost_slimesteel,
                STMaterialId.goozma,
                STMaterialId.mycelium_slimesteel,
                STMaterialId.orichalcum,
                STMaterialId.prometheum,
                STMaterialId.pyrothium,
                STMaterialId.soul_sakura,
                STMaterialId.south_star,
                STMaterialId.steady_alloy,
                STMaterialId.terracryst,
                STMaterialId.dragon_sinew,
                STMaterialId.gluttonous,
                STMaterialId.delusion,
                STMaterialId.cold_iron_alloy,
                STMaterialId.BuddyCard.perfect_buddysteel,
                STMaterialId.IceAndFire.amphithere_feather,
                STMaterialId.IceAndFire.dragon_ice_steel,
                STMaterialId.IceAndFire.dragon_fire_steel,
                STMaterialId.IceAndFire.dragon_lightning_steel,
                STMaterialId.Botania.elementium,
                STMaterialId.Botania.elementium,
                STMaterialId.Botania.terra_steel,
                STMaterialId.Botania.gaia,
                STMaterialId.ExtraBotany.orichalcos,
                STMaterialId.YoukaiHomeComing.youkai,
                STMaterialId.YoukaiHomeComing.fairy_ice_crystal,
                STMaterialId.TwilightForest.raven_feather,
                STMaterialId.TwilightForest.fiery_crystal,
                STMaterialId.EnigmaticLegacy.etherium,
                STMaterialId.ReAvaritia.colorful,
                STMaterialId.ReAvaritia.infinity,
                STMaterialId.ReAvaritia.neutron,
                STMaterialId.ReAvaritia.crystal_matrix,
                STMaterialId.IronSpellBook.arcane_alloy,
                STMaterialId.ClouderTinker.pyrothium,
                STMaterialId.DreadSteel.dread_steel,
                STMaterialId.Goety.cursed_metal,
                STMaterialId.Goety.unholy_alloy,
                STMaterialId.Goety.dark_metal,
                STMaterialId.BuddyCard.buddysteel,
                STMaterialId.BuddyCard.charged_buddysteel,
                STMaterialId.BuddyCard.crimson_buddysteel,
                STMaterialId.BuddyCard.void_buddysteel,
                STMaterialId.BuddyCard.perfect_buddysteel,
                STMaterialId.BuddyCard.true_perfect_buddysteel,
                STMaterialId.EnigmaticLegacy.nefarious,
                STMaterialId.GoetyRevelation.apocalyptium
        ).replace(false);
        tag(TinkerTags.Materials.NETHER).replace(false).addOptional(

        );
    }

    @Override
    public String getName() {
        return "Sakura Tinker Material Tag Provider";
    }
}
