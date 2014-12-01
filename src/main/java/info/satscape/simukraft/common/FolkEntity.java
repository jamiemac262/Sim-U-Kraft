package info.satscape.simukraft.common;

import info.satscape.simukraft.ModSimukraft;
import net.minecraft.entity.EntityList;

import cpw.mods.fml.common.registry.EntityRegistry;

public class FolkEntity {
	public static void mainRegistry() {
registerEntity();
	}

	public static void registerEntity() {
		createEntity(EntityFolk.class, "Folk", 0xFF0000, 0x00FF08);

	}

	public static void createEntity(Class entityClass, String entityName,
			int solidColour, int spotColour) {
		int randomId = EntityRegistry.findGlobalUniqueEntityId();

		EntityRegistry
				.registerGlobalEntityID(entityClass, entityName, randomId);
		EntityRegistry.registerModEntity(entityClass, entityName, randomId,
				ModSimukraft.modInstance, 64, 1, true);
		createEgg(randomId, solidColour, spotColour);
	}

	private static void createEgg(int randomId, int solidColour, int spotColour) {
		EntityList.entityEggs.put(Integer.valueOf(randomId), new EntityList.EntityEggInfo(randomId, solidColour, spotColour));
	}
}
