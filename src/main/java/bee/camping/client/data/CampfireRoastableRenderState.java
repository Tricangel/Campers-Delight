package bee.camping.client.data;

import bee.camping.CampersDelight;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

public class CampfireRoastableRenderState {
    public static final RenderStateDataKey<CampfireRoastableRenderState> KEY = RenderStateDataKey.create(() -> "is_roasting_on_campfire");

    public boolean isRoasting = false;
}
