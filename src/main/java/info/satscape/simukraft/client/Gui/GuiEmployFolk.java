package info.satscape.simukraft.client.Gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import info.satscape.simukraft.ModSimukraft;
import info.satscape.simukraft.ModSimukraft.GameMode;
import info.satscape.simukraft.common.CommonProxy.V3;
import info.satscape.simukraft.common.FarmingBox;
import info.satscape.simukraft.common.FolkData;
import info.satscape.simukraft.common.MiningBox;
import info.satscape.simukraft.common.jobs.Job;
import info.satscape.simukraft.common.jobs.JobBaker;
import info.satscape.simukraft.common.jobs.JobBuilder;
/*import info.satscape.simukraft.common.jobs.JobBuildersMerchant;
import info.satscape.simukraft.common.jobs.JobButcher;
import info.satscape.simukraft.common.jobs.JobCourier;
import info.satscape.simukraft.common.jobs.JobCropFarmer;
import info.satscape.simukraft.common.jobs.JobGlassMaker;
import info.satscape.simukraft.common.jobs.JobGrocer;
import info.satscape.simukraft.common.jobs.JobLivestockFarmer;
import info.satscape.simukraft.common.jobs.JobLumberjack;
import info.satscape.simukraft.common.jobs.JobMiner;
import info.satscape.simukraft.common.jobs.JobShepherd;
import info.satscape.simukraft.common.jobs.JobSoldier;
import info.satscape.simukraft.common.jobs.JobTerraformer;*/
import info.satscape.simukraft.common.jobs.Job.Vocation;
import info.satscape.simukraft.common.PathBox;

public class GuiEmployFolk extends GuiScreen
{
    FolkData theFolk;
    V3 controlBoxLocation;
    String buildDirection = "";
    MiningBox miningBox;
    FarmingBox farmingBox;
    PathBox pathBox;
    Vocation vocation;
    private int mouseCount = 0;

    /** folks selected to be employed */
    private ArrayList<GuiButton> selectedFolks = new ArrayList<GuiButton>();
    private int maxEmployees = 1;

    public GuiEmployFolk(V3 controlBoxLocation, String dir, Vocation vocation)
    {
        this.controlBoxLocation = controlBoxLocation;
        buildDirection = dir;
        this.vocation = vocation;

        if (this.vocation == Vocation.BUILDER)
        {
            maxEmployees = 1;
        }
    }

    public GuiEmployFolk(MiningBox b, Vocation v)
    {
        try
        {
            controlBoxLocation = b.location;
            vocation = v;
            miningBox = b;
            maxEmployees = 1;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public GuiEmployFolk(FarmingBox b, Vocation v)
    {
        try
        {
            controlBoxLocation = b.location;
            vocation = v;
            farmingBox = b;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public GuiEmployFolk(PathBox thePathBox, Vocation v)
    {
        try
        {
            controlBoxLocation = thePathBox.location;
            vocation = v;
            pathBox = thePathBox;
            maxEmployees = 1;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void initGui()
    {
        buttonList.clear();
        buttonList.add(new GuiButton(0, width / 2 - 200, height - 30, "Cancel"));
        buttonList.add(new GuiButton(1000, width / 2 , height - 30, "OK"));
        ArrayList folks = FolkData.getFolkUnemployed(false);

        try
        {
            int x = 10, y = 40, idx = 1;

            for (int f = 0; f < folks.size(); f++)
            {
                FolkData folk = (FolkData) folks.get(f);
                String xp = "";
                int ixp = 0;

                if (this.vocation == Vocation.BUILDER)
                {
                    ixp = (int)Math.floor(folk.levelBuilder);
                    xp = " (" + ixp + ")";
                }
                else if (this.vocation == Vocation.MINER)
                {
                    ixp = (int)Math.floor(folk.levelMiner);
                    xp = " (" + ixp + ")";
                }
                else if (this.vocation == Vocation.SOLDIER)
                {
                    ixp = (int)Math.floor(folk.levelSoldier);
                    xp = " (" + ixp + ")";
                }

                buttonList.add(new GuiButton(idx, x, y, 110, 20, folk.name + xp));
                idx++;
                x += 110;

                if ((x + 110) > width)
                {
                    x = 10;
                    y += 20;
                }

                if ((y + 20) > (height - 50))
                {
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();

        try
        {
            if (mouseCount < 10)
            {
                mouseCount++;
                Mouse.setGrabbed(false);
            }

            drawCenteredString(fontRendererObj, "Choose who you'd like to Employ as a " + this.vocation.toString(), width / 2, 17, 0xffffff);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        super.drawScreen(i, j, f);
    }

    @Override
    public void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled)
        {
            return;
        }

        /** cancel button */
        if (guibutton.id == 0)
        {
            mc.currentScreen = null;
            mc.setIngameFocus();
            return;
        }

        /** clicked on a folk */
        if (guibutton.id > 0 && guibutton.id < 1000)
        {
            if (selectedFolks.size() < maxEmployees)
            {
                selectedFolks.add(guibutton);
                guibutton.enabled = false;
            }
        }

        /** ok, hire the selected folk */
        if (guibutton.id == 1000)
        {
            if (ModSimukraft.states.credits <= 0 && ModSimukraft.gameMode != GameMode.CREATIVE)
            {
                ModSimukraft.sendChat("You need some Sim-u-Credits to employ folks.");
                mc.currentScreen = null;
                mc.setIngameFocus();
                return;
            }

            if (ModSimukraft.gameMode == GameMode.CREATIVE && this.vocation == Vocation.MERCHANT)
            {
                ModSimukraft.sendChat("Builder's merchant cannot be hired in creative mode");
                mc.currentScreen = null;
                mc.setIngameFocus();
                return;
            }

            ArrayList<FolkData> efolks = new ArrayList<FolkData>();

            for (int w = 0; w < selectedFolks.size(); w++)
            {
                GuiButton button = selectedFolks.get(w);
                String folkname = button.displayString;

                if (folkname.contains("("))
                {
                    folkname = button.displayString.substring(0, button.displayString.indexOf(" (")).trim();
                }

                FolkData f = (FolkData) FolkData.getFolkByName(folkname);
                f.statusText = "Going to my new job...";
                efolks.add(f);
                this.hireFolks(efolks);
            }
        }
    }

    public void hireFolks(ArrayList<FolkData> efolks)
    {

        for (int i = 0; i < efolks.size(); i++)
        {
            FolkData efolk = efolks.get(i);
            efolk.employedAt = this.controlBoxLocation;
            efolk.setTheirJob(this.vocation);

            if (ModSimukraft.isDayTime())
            {
                efolk.gotoXYZ(efolk.employedAt, null);
            }

        }

        mc.currentScreen = null;

        if (this.vocation == Vocation.BUILDER)
        {
            GuiBuildingConstructor ui = new GuiBuildingConstructor(this.controlBoxLocation, this.buildDirection, efolks);
            mc.displayGuiScreen(ui);
        }
        else if (this.vocation == Vocation.TERRAFORMER)
        {
            GuiBuildingConstructor ui = new GuiBuildingConstructor(this.controlBoxLocation, this.buildDirection, efolks);
            mc.displayGuiScreen(ui);
        }
        else if (this.vocation == Vocation.MINER)
        {
            GuiMining ui = new GuiMining(this.miningBox, efolks);
            mc.displayGuiScreen(ui);
        }
        else if (this.vocation == Vocation.CROPFARMER)
        {
            GuiFarming ui = new GuiFarming(this.farmingBox, efolks.get(0));
            mc.displayGuiScreen(ui);
        }
        else if (this.vocation == Vocation.PATHBUILDER)
        {
            GuiPathBox ui = new GuiPathBox(this.pathBox, efolks);
            mc.displayGuiScreen(ui);
        }
        else      //all other jobs that employ via control panel
        {
            GuiControlBox ui = new GuiControlBox(this.controlBoxLocation, efolks.get(0));
            mc.displayGuiScreen(ui);
        }
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void keyTyped(char c, int i)
    {
        if (i == 1) //escape
        {
            mc.displayGuiScreen(null);
            mc.setIngameFocus();
            return;
        }

        //((GuiButton)buttonList.get(0)).enabled = theGuiTextField1.getText().trim().length() > 0;
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void updateScreen()
    {
        // theGuiTextField1.updateCursorCounter();
    }
}
