package info.satscape.simukraft.common.blocks;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.satscape.simukraft.client.Gui.GuiBuildingConstructor;
import info.satscape.simukraft.common.CommonProxy;
import info.satscape.simukraft.common.FolkData;
import info.satscape.simukraft.common.CommonProxy.V3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockConstructorBox extends Block
{
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    /** string containing +x -x +z -z  */
    public String buildDirection = "";

    /** int is the Block Id */
    public BlockConstructorBox(int blockId)
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        icons = new IIcon[1];
        icons[0] = iconRegister.registerIcon("satscapesimukraft:blockConstruction");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)      // getBlockTextureFromSideAndMetadata
    {
        return icons[0];
    }

    @Override
    public void onBlockDestroyedByPlayer(World par1World, int par2, int par3,
                                         int par4, int par5)
    {
        if (!par1World.isRemote)
        {
            par1World.playSoundEffect(par2, par3, par4, "satscapesimukraft:powerdown", 1f, 1f);
        }

        FolkData theFolk = FolkData.getFolkByEmployedAt(new V3((double)par2, (double)par3, (double)par4
                           , par1World.provider.dimensionId));

        if (theFolk != null)
        {
            theFolk.selfFire();
        }

        super.onBlockDestroyedByPlayer(par1World, par2, par3, par4, par5);
    }

    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
            par1World.playSoundEffect(par2, par3, par4, "satscapesimukraft:constructoractivated", 1f, 1f);
        }

        super.onBlockAdded(par1World, par2, par3, par4);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World par1World, int par2, int par3,
                                    int par4, EntityPlayer thePlayer, int par6, float par7,
                                    float par8, float par9)
    {
        par1World.playSoundEffect(par2, par3, par4, "satscapesimukraft:computer", 1f, 1f);
        int px = (int) Math.floor(thePlayer.posX);
        int py = (int) Math.floor(thePlayer.posY);
        int pz = (int) Math.floor(thePlayer.posZ);
        int ex = par2;
        int ey = par3;
        int ez = par4;

        if (par4 == pz)  //first block should be on X axis
        {
            if (px < par2)
            {
                buildDirection = "-x";
            }
            else
            {
                buildDirection = "+x";
            }
        }
        else if (par2 == px)    //first block should be on Z axis
        {
            if (pz < par4)
            {
                buildDirection = "-z";
            }
            else
            {
                buildDirection = "+z";
            }
        }

        V3 loc = new V3((double)par2, (double)par3, (double)par4, thePlayer.dimension);
        Minecraft mc = Minecraft.getMinecraft();
        GuiBuildingConstructor ui = new GuiBuildingConstructor(loc, buildDirection, null); // xyz of box and xyz of 1st build block
        mc.displayGuiScreen(ui);
        return true;
    }
}
