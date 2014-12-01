package info.satscape.simukraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class SoundHandler {

	public static boolean playSoundAtEntity(Entity entity, World world, String sound){
		try{
		world.playSoundAtEntity(entity, sound, 1, 1 );
		return true;
		}
		catch(Exception e){
		return false;
		}
		}
	
	public boolean broadcastSound(){
		return false;}

	public static boolean stopSoundAtEntity(ISound sound) {
		Minecraft.getMinecraft().getSoundHandler().stopSound(sound);
		return false;
		// TODO Auto-generated method stub
		
	}
}
