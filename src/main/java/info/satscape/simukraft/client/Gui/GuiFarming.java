package info.satscape.simukraft.client.Gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Mouse;

import info.satscape.simukraft.ModSimukraft;
import info.satscape.simukraft.ModSimukraft.GameMode;
import info.satscape.simukraft.common.FarmingBox;
import info.satscape.simukraft.common.FarmingBox.FarmType;
import info.satscape.simukraft.common.FolkData;
import info.satscape.simukraft.common.jobs.Job.Vocation;

public class GuiFarming extends GuiScreen
{
    FolkData theFolk = null;
    FarmingBox theFarmingBox = null;
    private int mouseCount = 0;

    public GuiFarming(FarmingBox farmingBox, FolkData folk)
    {
        this.theFarmingBox = farmingBox;
        theFolk = folk; /// is null before employing someone
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void initGui()
    {
        try
        {
            if (theFarmingBox.level == 0)
            {
                theFarmingBox.level = 1;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        buttonList.clear();
        buttonList.add(new GuiButton(0, width / 2 - 100, height - 30, "Done"));

        if (theFolk == null)
        {
            buttonList.add(new GuiButton(1, width / 2 - 100, 40, "Hire Farmer"));
        }
        else
        {
            buttonList.add(new GuiButton(1, width / 2 - 100, 40, "Fire " + theFolk.name));
        }

        try
        {
            buttonList.add(new GuiButton(2, width / 2 - 100, 100, theFarmingBox.farmType.toString() + " farm"));
            /*
            if (theFarmingBox.farmType==FarmType.WHEAT) {
            	buttonList.add(new GuiButton(2,width/2-100,100,"Wheat farm"));
            } else if (theFarmingBox.farmType==FarmType.PUMPKIN) {
            	buttonList.add(new GuiButton(2,width/2-100,100,"Pumpkin farm"));
            } else if (theFarmingBox.farmType==FarmType.MELON) {
            	buttonList.add(new GuiButton(2,width/2-100,100,"Melon farm"));
            } else if (theFarmingBox.farmType==FarmType.CARROT) {
            	buttonList.add(new GuiButton(2,width/2-100,100,"Carrot farm"));
            } else if (theFarmingBox.farmType==FarmType.POTATO) {
            	buttonList.add(new GuiButton(2,width/2-100,100,"Potato farm"));
            } else if (theFarmingBox.farmType==FarmType.CUSTOM) {
            	buttonList.add(new GuiButton(2,width/2-100,100,"Custom farm"));
            } else of (theFarmingBox.farmType==FarmType.SUGAR) {
            	button
            }
            */
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            GuiButton b;

            switch (theFarmingBox.level)
            {
                case 1:
                    if (theFarmingBox.farmType == FarmType.SUGAR || theFarmingBox.farmType == FarmType.CACTUS)
                    {
                        buttonList.add(b = new GuiButton(3, width / 2 - 100, 140, "Fully upgraded"));
                        b.enabled = false;
                    }
                    else
                    {
                        buttonList.add(new GuiButton(3, width / 2 - 100, 140, "Upgrade to Level " + (theFarmingBox.level + 1)));
                    }

                    break;

                case 2:
                    buttonList.add(new GuiButton(3, width / 2 - 100, 140, "Upgrade to Level " + (theFarmingBox.level + 1)));
                    break;

                case 3:
                    buttonList.add(b = new GuiButton(3, width / 2 - 100, 140, "Fully upgraded"));
                    b.enabled = false;
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        super.initGui();
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
        if (mouseCount < 10)
        {
            mouseCount++;
            Mouse.setGrabbed(false);
        }

        try
        {
            drawDefaultBackground();

            if (theFarmingBox == null)
            {
                drawCenteredString(fontRendererObj, "ERROR WITH FARMING BOX, Place 3 markers, then place Farming box", width / 2, 17, 0xffffff);
                return;
            }

            drawCenteredString(fontRendererObj, "Level " + theFarmingBox.level + " " + theFarmingBox.farmType.toString() +
                               " Farm", width / 2, 17, 0x80ff80);

            try
            {
                if (theFarmingBox.marker1XYZ == null)
                {
                    drawCenteredString(fontRendererObj, "Error: No markers placed - 3 markers are needed to farm an area.",
                                       width / 2, 27, 0xff0000);
                }
            }
            catch (Exception e)
            {
                drawCenteredString(fontRendererObj, "Error: Place markers BEFORE the farming box.",
                                   width / 2, 27, 0xff0000);
            }

            if (theFarmingBox.level == 1 && theFarmingBox.farmType == FarmType.SUGAR)
            {
                drawCenteredString(fontRendererObj, "Cannot upgrade sugar cane farms above level 1",
                                   width / 2, 130, 0xffffff);
            }
            else if (theFarmingBox.level == 1 && theFarmingBox.farmType == FarmType.CACTUS)
            {
                drawCenteredString(fontRendererObj, "Cannot upgrade cactus farms above level 1",
                                   width / 2, 130, 0xffffff);
            }
            else if (theFarmingBox.level < 3)
            {
                if (ModSimukraft.gameMode != GameMode.CREATIVE)
                {
                    drawCenteredString(fontRendererObj, "Upgrade will cost " + ModSimukraft.displayMoney(getUpgradeCost()) + " credits",
                                       width / 2, 130, 0xffffff);
                }
                else
                {
                    drawCenteredString(fontRendererObj, "Upgrade is Free",
                                       width / 2, 130, 0xffffff);
                }
            }

            super.drawScreen(i, j, f);
        }
        catch (Exception e)
        {
            e.printStackTrace();   //NPE on drawing background for several ticks
        }
    }

    @Override
    public void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled)
        {
            return;
        }

        if (guibutton.id == 0) // /cancel button
        {
            mc.currentScreen = null;
            mc.setIngameFocus();
            return;
        }
        else if (guibutton.displayString.contentEquals("Hire Farmer"))
        {
            GuiEmployFolk ui = new GuiEmployFolk(theFarmingBox, Vocation.CROPFARMER);
            mc.displayGuiScreen(ui);
        }
        else if (guibutton.displayString.startsWith("Fire "))
        {
            theFolk.selfFire();
            guibutton.enabled = false;
            mc.currentScreen = null;
            mc.setIngameFocus();
        }
        else if (guibutton.id == 2)   /// 5 way toggle for type of farm
        {
            if (theFarmingBox.farmType == FarmType.POTATO)
            {
                theFarmingBox.farmType = FarmType.PUMPKIN;
            }
            else if (theFarmingBox.farmType == FarmType.PUMPKIN)
            {
                theFarmingBox.farmType = FarmType.MELON;
            }
            else if (theFarmingBox.farmType == FarmType.MELON)
            {
                theFarmingBox.farmType = FarmType.WHEAT;
            }
            else if (theFarmingBox.farmType == FarmType.WHEAT)
            {
                theFarmingBox.farmType = FarmType.CARROT;
            }
            else if (theFarmingBox.farmType == FarmType.CARROT)
            {
                theFarmingBox.farmType = FarmType.CUSTOM;
            }
            else if (theFarmingBox.farmType == FarmType.CUSTOM)
            {
                theFarmingBox.farmType = FarmType.SUGAR;
            }
            else if (theFarmingBox.farmType == FarmType.SUGAR)
            {
                theFarmingBox.farmType = FarmType.CACTUS;
            }
            else if (theFarmingBox.farmType == FarmType.CACTUS)
            {
                theFarmingBox.farmType = FarmType.POTATO;
            }

            guibutton.displayString = theFarmingBox.farmType.toString() + " farm";
        }
        else if (guibutton.id == 3)     /// button to upgrade the farm to the next level
        {
            float cash = ModSimukraft.states.credits;

            if (ModSimukraft.gameMode == GameMode.CREATIVE)
            {
                cash = 1000f;
            }

            if (getUpgradeCost() > cash)
            {
                guibutton.displayString = "NOT ENOUGH";
                guibutton.enabled = false;
            }
            else if (theFarmingBox.getSizeLength() < 4 || theFarmingBox.getSizeWidth() < 4)    // min 6x6 (not sure why 4)
            {
                guibutton.displayString = "TOO SMALL";
                guibutton.enabled = false;
            }
            else
            {
                if (ModSimukraft.gameMode != GameMode.CREATIVE)
                {
                    ModSimukraft.states.credits -= getUpgradeCost();
                }

                ModSimukraft.farmToUpgradeCounter = 0;
                ModSimukraft.farmToUpgrade = theFarmingBox;
                mc.currentScreen = null;
                mc.setIngameFocus();
                return;
            }
        }
    }

    /** get the cost in credits of how much to upgrade to the next level */
    private Float getUpgradeCost()
    {
        Float ret = (float)(theFarmingBox.getSizeLength() * theFarmingBox.getSizeWidth());
        ret = ret / 15;
        return ret;
    }
}
