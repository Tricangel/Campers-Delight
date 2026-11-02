package bee.camping.registry;

import bee.camping.CampersDelight;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public class CamperAttachments {

    public static final AttachmentType<Boolean> COOKING_CAMPFIRE_ROASTABLE = AttachmentRegistry.create(
            CampersDelight.id("cooking_campfire_roastable")
    );

    public static final AttachmentType<Integer> COOKING_CAMPFIRE_ROASTABLE_TIME = AttachmentRegistry.create(
            CampersDelight.id("cooking_campfire_roastable_time")
    );

    public static void init() {}

}
