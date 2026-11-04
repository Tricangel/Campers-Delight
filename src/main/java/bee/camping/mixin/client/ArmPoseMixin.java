package bee.camping.mixin.client;

import net.minecraft.client.model.HumanoidModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HumanoidModel.ArmPose.class)
public enum ArmPoseMixin {
    CAMPERS_DELIGHT_CAMPFIRE_ROASTABLE(false, false);

    @Shadow
    ArmPoseMixin(boolean bl, boolean bl2) {
    }
}
