package info.satscape.simukraft.client;

import java.io.File;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.satscape.simukraft.ModSimukraft;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.sound.SoundLoadEvent;


//// NOTE: MC 1.7+ audio works ALOT different to this.

public class EventSounds
{
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onSound(SoundLoadEvent event)
    {
        System.out.println("onSound EVENT HAS BEEN CALLED!!");
    }
}