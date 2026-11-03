package bee.camping.mixin;

import bee.camping.client.data.CampfireRoastableRenderState;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends HumanoidRenderState> extends EntityModel<T> implements ArmedModel<T>, HeadedModel {

    @Shadow
    @Final
    public ModelPart rightArm;

    protected HumanoidModelMixin(ModelPart modelPart) {
        super(modelPart);
    }

    @Inject(method = "poseRightArm", at = @At("HEAD"), cancellable = true)
    private void poseRightArm(T humanoidRenderState, CallbackInfo ci) {

        if (humanoidRenderState.getDataOrDefault(CampfireRoastableRenderState.KEY, new CampfireRoastableRenderState()).isRoasting) {
            rightArm.xRot = -1;
            ci.cancel();
        }

    }

}
