package info.satscape.simukraft.common;

import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class LoginEventHandler {
	public void onLogin(PlayerLoggedInEvent e){
		PlayerData pd = new PlayerData(e.player);
		pd.getData();
		
		
	}
}
