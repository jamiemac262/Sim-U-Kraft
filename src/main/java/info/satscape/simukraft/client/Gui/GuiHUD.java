package info.satscape.simukraft.client.Gui;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import info.satscape.simukraft.ModSimukraft;
import info.satscape.simukraft.ModSimukraft.GameMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class GuiHUD extends Gui{
	private Minecraft mc;
	GuiScreen hud;
	public GuiHUD(Minecraft mc)
	  {
	    super();
	   
	    // We need this to invoke the render engine.
	    this.mc = mc;
	    hud = new GuiScreen();
	    
	  }
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onGui(RenderGameOverlayEvent e)
    {
		if(e.isCancelable() || e.type != ElementType.EXPERIENCE)
	    {      
	      return;
	    }

    	//System.out.println("onGui() HAS BEEN CALLED!");
        //Draw the HUD
        if (mc.currentScreen == null)
        {
            String worldname = "unknown";

            try
            {
                if (ModSimukraft.states.gameModeNumber == 10)
                {
                    return;
                }

                worldname = mc.getIntegratedServer().getFolderName();
                worldname = MinecraftServer.getServer().getFolderName();
            }
            catch (Exception ex)
            {
                //e.printStackTrace();
                hud.drawString(mc.fontRenderer, "Sim-U-Kraft is not SMP", hud.width / 2, 2, 0xffffff);
                return;
            }

            try
            {
                if (ModSimukraft.proxy.ranStartup)
                {
                    int HUDoffset = 0;

                    if (mc.thePlayer.dimension == 1)
                    {
                        HUDoffset = 20;
                    }

                    HUDoffset += ModSimukraft.configHUDoffset;

                    if (ModSimukraft.gameMode == GameMode.CREATIVE)
                    {
                        hud.drawString(mc.fontRenderer, worldname + " (" + ModSimukraft.getDayOfWeek() +
                                       ") - Population: " + ModSimukraft.theFolks.size() , hud.width / 2, 2 + HUDoffset, 0xffffff);
                    }
                    else
                    {
                        hud.drawString(mc.fontRenderer, worldname + " (" + ModSimukraft.getDayOfWeek() +
                                       ") - Population: " + ModSimukraft.theFolks.size() +
                                       "   Sim-U-credits: " + ModSimukraft.displayMoney(ModSimukraft.states.credits), hud.width / 2, 2 + HUDoffset, 0xffffff);
                    }
                }
                else
                {
                    hud.drawString(mc.fontRenderer, "Loading Sim-U-Kraft...", hud.width / 2, 2, 0xffffff);
                }
                hud.initGui();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
