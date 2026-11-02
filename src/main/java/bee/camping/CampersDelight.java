package bee.camping;

import bee.camping.networking.SetCookedLevelPacket;
import bee.camping.networking.SetRoastingPacket;
import bee.camping.registry.*;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CampersDelight implements ModInitializer {
	public static final String MOD_ID = "campers-delight";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Starting Initialization");
		CamperItems.init();
		CamperBlocks.init();
		CamperDataComponents.init();
		CamperAttachments.init();
		CamperDataPredicates.init();

		PayloadTypeRegistry.playC2S().register(SetCookedLevelPacket.TYPE, SetCookedLevelPacket.CODEC);
		PayloadTypeRegistry.playC2S().register(SetRoastingPacket.TYPE, SetRoastingPacket.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(SetCookedLevelPacket.TYPE, SetCookedLevelPacket::execute);
		ServerPlayNetworking.registerGlobalReceiver(SetRoastingPacket.TYPE, SetRoastingPacket::execute);
		LOGGER.info("Finished Initialization");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
