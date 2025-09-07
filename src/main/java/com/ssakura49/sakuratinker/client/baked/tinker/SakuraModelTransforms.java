package com.ssakura49.sakuratinker.client.baked.tinker;

import com.mojang.math.Axis;
import com.mojang.math.Transformation;
import com.ssakura49.sakuratinker.client.baked.model.PerspectiveModelState;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;

public class SakuraModelTransforms {
    public static final PerspectiveModelState TOOL = new PerspectiveModelState(Map.of(
            ItemDisplayContext.GUI, new Transformation(
                    new Vector3f(0.0f, 0.0f, 0.0f),
                    Axis.YP.rotationDegrees(30.0f).mul(Axis.XP.rotationDegrees(-30.0f)),
                    new Vector3f(0.85f, 0.85f, 0.85f),
                    new Quaternionf()
            ),
            ItemDisplayContext.FIXED, new Transformation(
                    new Vector3f(0.0f, 0.0f, 0.0f),
                    new Quaternionf(),
                    new Vector3f(0.5f, 0.5f, 0.5f),
                    new Quaternionf()
            )
    ));
}
