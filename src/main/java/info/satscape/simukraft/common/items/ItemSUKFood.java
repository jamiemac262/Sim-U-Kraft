package info.satscape.simukraft.common.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

public class ItemSUKFood extends ItemFood {

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	
	public ItemSUKFood(int par1) {
		super(par1, 6, false);    // 6 half-hearts when ate    0.6f is default for ItemFood
		this.setCreativeTab(CreativeTabs.tabFood);
		setHasSubtypes(true);
	}
	
	public static final String[] names = new String[] {"foodCheese", "foodBurger", "foodFries","foodCheeseburger"};
	
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister)
	{
	       icons = new IIcon[names.length];
	             
	       for(int i = 0; i < icons.length; i++)
	       {
	           icons[i] = par1IconRegister.registerIcon("satscapesimukraft:" + names[i]);
	       }
	}
	
	@Override
	public IIcon getIconFromDamage(int meta)
	{
		if (meta >=0 && meta < names.length) {
			return icons[meta];
		} else {
			return null;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
	    for (int x = 0; x < names.length; x++)
	    {
	        par3List.add(new ItemStack(this, 1, x));
	    }
	}
	
	
	 @Override
	    public String getUnlocalizedName(ItemStack is)
	    {
	        if (is.getItemDamage() == 0)
	        {
	            return "item.foodCheese";
	        }
	        else if (is.getItemDamage() == 1)
	        {
	            return "item.foodBurger";
	        }
	        else if (is.getItemDamage() == 2)
	        {
	            return "item.foodFries";
	        }
	        else if (is.getItemDamage() == 3)
	        {
	            return "item.foodCheeseburger";
	        }
	        else
	        {
	            return null;
	        }
	    }


	 
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {

		par3List.add("A tasty snack for folks");
		super.addInformation(par1ItemStack, par2EntityPlayer,par3List , par4);
	}


	@Override
	    public int getMetadata(int par1)
	    {
	        return par1;
	    }

}
