package info.satscape.simukraft.common.blocks;

import info.satscape.simukraft.ModSimukraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidMilk extends BlockFluidClassic {
	private IIcon icons[];
	
	public BlockFluidMilk(int id) {
		super(ModSimukraft.SUKfluidMilk, Material.water);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    	icons=new IIcon[2];
    	icons[0] = iconRegister.registerIcon("satscapesimukraft:milk_still");
    	icons[1] = iconRegister.registerIcon("satscapesimukraft:milk_flow");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
    	if (meta>=1) {
    		return icons[1];
    	} else {
    		return icons[0];
    	}
    }

   
    

}
