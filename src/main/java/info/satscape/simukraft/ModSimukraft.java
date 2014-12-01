package info.satscape.simukraft;

import info.satscape.simukraft.client.EventSounds;
import info.satscape.simukraft.client.Gui.GuiFarming;
import info.satscape.simukraft.client.Gui.GuiHUD;
import info.satscape.simukraft.client.Gui.GuiRunMod;
import info.satscape.simukraft.common.Building;
import info.satscape.simukraft.common.CommonProxy;
import info.satscape.simukraft.common.CommonTickHandler;
import info.satscape.simukraft.common.CourierTask;
import info.satscape.simukraft.common.EntityAlignBeam;
import info.satscape.simukraft.common.EntityConBox;
import info.satscape.simukraft.common.EntityFolk;
import info.satscape.simukraft.common.EntityWindmill;
import info.satscape.simukraft.common.FarmingBox;
import info.satscape.simukraft.common.FluidMilk;
import info.satscape.simukraft.common.FolkData;
import info.satscape.simukraft.common.FolkEntity;
import info.satscape.simukraft.common.GameStates;
import info.satscape.simukraft.common.MiningBox;
import info.satscape.simukraft.common.PathBox;
import info.satscape.simukraft.common.PricesForBlocks;
import info.satscape.simukraft.common.Relationship;
//import info.satscape.simukraft.common.TileEntityWindmill;
import info.satscape.simukraft.common.CommonProxy.Commodity;
import info.satscape.simukraft.common.CommonProxy.V3;
import info.satscape.simukraft.common.blocks.BlockCheeseBlock;
import info.satscape.simukraft.common.blocks.BlockCompositeBrick;
import info.satscape.simukraft.common.blocks.BlockConstructorBox;
import info.satscape.simukraft.common.blocks.BlockControlBox;
import info.satscape.simukraft.common.blocks.BlockFarmingBox;
import info.satscape.simukraft.common.blocks.BlockFluidMilk;
import info.satscape.simukraft.common.blocks.BlockLightBox;
import info.satscape.simukraft.common.blocks.BlockMarker;
import info.satscape.simukraft.common.blocks.BlockMiningBox;
//import info.satscape.simukraft.common.blocks.BlockWindmill;
import info.satscape.simukraft.common.items.ItemBlockLightBox;
import info.satscape.simukraft.common.items.ItemBlockWindmill;
import info.satscape.simukraft.common.items.ItemGranulesGold;
import info.satscape.simukraft.common.items.ItemGranulesIron;
import info.satscape.simukraft.common.items.ItemSUKFood;
import info.satscape.simukraft.common.items.ItemWindmillBase;
import info.satscape.simukraft.common.items.ItemWindmillSails;
import info.satscape.simukraft.common.items.ItemWindmillVane;
import info.satscape.simukraft.common.jobs.Job.Vocation;
//import info.satscape.simukraft.common.jobs.JobSoldier;









import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

/// Sim-U-Kraft Minecraft Mod by Scott Hather AKA "Satscape"
/// v0.1.0 - RML version first released 2nd Jan 2012
/// v0.9.0 - Forge version released 6th November 2012
/// This mod is now open-source, feel free to do as you want with this, the code is not a good example of
/// how to write Java, as I don't do this as my day job! MC 1.7+ has broke a few things too, Audio/sound effects will
/// need re-writing as it's changed a lot and there's the whole lack of SMP, there is some PacketHandling, oh and
/// PacketHandling is changed in MC1.7+ too, so you'll need to re-write that.....Good luck!
/// You can use this for personal use or re-distribute your own version, let me know on www.facebook.com/simukraft/ and I'll 
/// re-post it so the 1200 Facebook fans get to know.
/// Minecraft modding has been fun, but I just don't have the time at the moment,
/// All the best,
/// Scott Hather
/// http://satscape.ucoz.co.uk/index/sim_u_kraft/0-5
/// satscapeminecraft@gmail.com <<< if I get too much spam for putting this here, I'll have to close the account.


@Mod(modid = "satscapesimukraft", name = "Sim-U-Kraft Reloaded", version = "0.0.1 Alpha")

public class ModSimukraft
{
    public static String version = "0.0.1 Alpha";
    public static final String MODID = "satscapesimukraft";
    @SidedProxy(clientSide = "info.satscape.simukraft.client.ClientProxy",
                serverSide = "info.satscape.simukraft.common.CommonProxy")
    public static CommonProxy proxy;
    
    @Instance(MODID)
    public static ModSimukraft modInstance;
    public static SimpleNetworkWrapper network;
    public static Logger log = Logger.getLogger("Sim-U-Kraft");

    /** used to detect when we are in-world (not main menu) and when player changes worlds/maps */
    public static String currentSavePath = "";

    /** Block instances and IDs */
    public static int constructorBlockId = 0;
    public static Block buildingConstructor;
    public static int controlBlockId = 0;
   public static Block controlBox;
    static int markerBlockId = 0;
    static Block marker;
    public static int miningBlockId = 0;
    static Block miningBox;
    public static int farmingBlockId = 0;
    static Block farmingBox;
    public static int lightboxId = 0;
    public static Block lightBox;
    static Block lightBoxRed;
    static Block lightBoxOrange;
    static Block lightBoxYellow;
    static Block lightBoxGreen;
    static Block lightBoxBlue;
    static Block lightBoxPurple;
    static Block pathConstructor;
     public static int pathConstructorId = 0;
	public static Block windmill;
	public static int windmillId;
    public static Item itemGranulesIron;
    public static int itemGranulesIronId;
    public static Item itemGranulesGold;
    public static int itemGranulesGoldId;
    public static Item itemWindmillBase;
    public static int itemWindmillBaseId;
    public static Item itemWindmillVane;
    public static int itemWindmillVaneId;
    public static Item itemWindmillSails;
    public static int itemWindmillSailsId;
    //public static Item itemGranulesCopper;
    //public static int itemGranulesCopperId;
    //public static Item itemGranulesTin;
    //public static int itemGranulesTinId;
    public static int itemFoodId;
    public static Item itemFood;
    public static Item itemFoodCheese;
    public static Item itemFoodFries;
    public static Item itemFoodBurger;
    public static Item itemFoodCheeseburger;
    public static Item itemBlockLightBox;
    public static Block blockCompositeBrick;
    public static int blockCompositeBrickId;
    public static Block blockCheese;
    public static int blockCheeseId;
    public static Fluid SUKfluidMilk;
    public static Block blockFluidMilk;
    public static int blockFluidMilkId;
    
    
    /** all the folk's data (used to construct and maintain an EntityFolk) */
    public static ArrayList<FolkData> theFolks = new ArrayList<FolkData>();
 
    /** all the building objects */
    public static ArrayList<Building> theBuildings = new ArrayList<Building>();

    /** all courier tasks */
    public static ArrayList<CourierTask> theCourierTasks = new ArrayList<CourierTask>();

    /** all courier points */
    public static ArrayList<V3> theCourierPoints = new ArrayList<V3>();

    /** all the mining Boxes */
    public static ArrayList<MiningBox> theMiningBoxes = new ArrayList<MiningBox>();

    /** all the farming Boxes */
    public static ArrayList<FarmingBox> theFarmingBoxes = new ArrayList<FarmingBox>();

    /** all the relationships */
    public static ArrayList<Relationship> theRelationships = new ArrayList<Relationship>();

    /** all the path constructor boxes  */
      public static ArrayList<PathBox> thePathBoxes=new ArrayList<PathBox>();

    /** contains all the game states and settings for this level they are playing */
    public static GameStates states = new GameStates();

    /** arraylist of commodities that banks are current selling, updates each morning with new items */
    public static ArrayList<Commodity> theCommodities = new ArrayList<Commodity>();

    /** used to upgrade crop farms in the update() call */
    public static FarmingBox farmToUpgrade = null;
    public static int farmToUpgradeCounter = 0;
    private static ArrayList<V3> farmToUpgradePoints = null;
    static public boolean isDay = true;

    public static ArrayList<V3> demolishBlocks = new ArrayList<V3>();
    public static World demolishWorld = null;

    ////config file settings
    public static Configuration config;
    public static int configPopulationLimit = 100;
    public static int configLumberArea = 30;
    public static boolean configDisableBeamEffect = false;
    public static boolean configFolkTalking = true;
    public static boolean configEnableMarkerAlignmentBeams = true;
    public static boolean configUseExpensiveRecipies = false;
    public static int configMaterialReminderInterval = 3;
    public static int configHUDoffset = 0;
    public static boolean configStopRain = false;
    public static boolean configFolkTalkingEnglish = true;
    public static String[] configMaleNames;
    public static String[] configFemaleNames;
    public static String[] configSurnames;
    
    
    public static enum GameMode
    {
        DONOTRUN, NORMAL, CREATIVE, HARDCORE;
    }
    public static GameMode gameMode = null;

    public static int getGameModeNumber()
    {
        if (gameMode == GameMode.DONOTRUN)
        {
            return -1;
        }
        else if (gameMode == GameMode.NORMAL)
        {
            return 0;
        }
        else if (gameMode == GameMode.CREATIVE)
        {
            return 1;
        }
        else if (gameMode == GameMode.HARDCORE)
        {
            return 2;
        }

        return 0;
    }
    public static void setGameModeFromNumber(int gm)
    {
        if (gm == -1)
        {
            gameMode = GameMode.DONOTRUN;
        }
        else if (gm == 0)
        {
            gameMode = GameMode.NORMAL;
        }
        else if (gm == 1)
        {
            gameMode = GameMode.CREATIVE;
        }
        else if (gm == 2)
        {
            gameMode = GameMode.HARDCORE;
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt)
    {
    	log.setLevel(Level.INFO);
    	
    	FolkEntity.mainRegistry();
        
    	
    	FMLCommonHandler.instance().bus().register(new CommonTickHandler());
    	network = NetworkRegistry.INSTANCE.newSimpleChannel("satscapesimukraft");
        config = new Configuration(evt.getSuggestedConfigurationFile());

        try
        {
			// TODO: Change lightbox ID, as it clashes with Tinkers Construct
		
            config.load();
            
            //Block IDs from the config file
           /* constructorBlockId = config.getBlock("ConstructorBox", 3210).getInt();
            controlBlockId = config.getBlock("ControlBox", 3211).getInt();
            markerBlockId = config.getBlock("Marker", 3212).getInt();
            miningBlockId = config.getBlock("MiningBox", 3213).getInt();
            farmingBlockId = config.getBlock("FarmingBox", 3214).getInt();
            lightboxId = config.getBlock("LightBox", 3215).getInt();
            //pathConstructorId=config.getBlock("PathConstructor", 3216).getInt();
            windmillId = config.getBlock("WindmillBox",3243).getInt(3243);
            itemGranulesGoldId=config.getItem("granulesGold", 3244).getInt(3244);
            itemGranulesIronId=config.getItem("granulesIron", 3245).getInt(3245);
            itemWindmillBaseId=config.getItem("WindmillBase", 3246).getInt(3246);
            itemWindmillVaneId=config.getItem("WindmillVane",3247).getInt(3247);
            itemWindmillSailsId=config.getItem("WindmillSails",3248).getInt(3248);
            //itemGranulesCopperId=config.getItem("granulesCopper", 3249).getInt(3249);
            //itemGranulesTinId=config.getItem("granulesTin", 3250).getInt(3250);
            itemFoodId=config.getItem("itemFood", 3251).getInt(3251);
            blockCompositeBrickId=config.getBlock("CompositeBrick", 3252).getInt(3252);
            blockCheeseId=config.getBlock("BlockCheese",3253).getInt(3253);
            blockFluidMilkId=config.getBlock("fluidMilk", 3254).getInt(3254);
       */     
            //other settings
            Property p;
            p = config.get("Settings", "DisableBeamingEffect", false);
            configDisableBeamEffect = p.getBoolean(false);
            p.comment = "This enables or disables the beaming effect (purple particles) - Set to true to turn them off.";
            p = config.get("Settings", "FolkTalking", true);
            configFolkTalking = p.getBoolean(false);
            p.comment = "If the folks BLARG talking gets annoying, set this to false";
            p = config.get("Settings", "FolkTalkingEnglish", true);
            configFolkTalkingEnglish = p.getBoolean(true);
            p.comment = "If the folks ENGLISH talking gets annoying, set this to false";
            p = config.get("Settings", "LumbermillArea", 40);
            configLumberArea = p.getInt();
            p.comment = "The radius in blocks that the lumberjack will look for trees from the starting point, don't set this too high, otherwise it will slow down MC every time they scan for the nearest tree, so 30 to 1000 should be ok (1000 is 1 Kilometre)";
            p = config.get("Settings", "PopulationLimit", 200);
            configPopulationLimit = p.getInt();
            p.comment = "Limit the population from growing beyond this number if you have an older computer";
            p = config.get("Settings", "EnableMarkerAlignmentBeams", true);
            configEnableMarkerAlignmentBeams = p.getBoolean(true);
            p.comment = "When placing a marker it fires out 4 alignment beams, setting this to false will turn those beams off";
            p = config.get("Settings", "UseExpensiveRecipes", false);
            configUseExpensiveRecipies = p.getBoolean(false);
            p.comment = "If you think the mining/farming boxes are too cheap/overpowered, set this to true to make the recipies require diamond tools instead of stone tools";
            p = config.get("Settings", "MaterialReminderInterval", 3);
            configMaterialReminderInterval = p.getInt(3);
            p.comment = "When a builder runs out of materials, they will let you know about it every 3 minutes, set to 0 for no further reminders.";

            if (configMaterialReminderInterval <= 0)
            {
                configMaterialReminderInterval = 2000;
            }

            p = config.get("Settings", "HUDoffset", 0);
            configHUDoffset = p.getInt(0);
            p.comment = "This positions the HUD (population and money text at the top of the screen) - default is 0, which is the top, value is in pixels, so setting 320 will display it 320 pixels from the top of the screen. Alter this to suit your screen resolution and avoid clashing with other text, setting to minus 10 will display it offscreen.";
            
            p = config.get("Settings", "StopRain", false);
            configStopRain = p.getBoolean(false);
            p.comment = "This is just a personal mod :-) If you too find it rains ALL THE F***ING TIME in your world and it annoys you/causes lag, set this to true and you'll only have brief showers instead";
       
            ///////////// Random name generator stuff
            p= config.get("Names", "MaleNames", "Aaron, Adam, Alan, Albatrude, Alexander, Amaranth, Andrew, Angelo, Baldric, Bartholomew, Basher, Beau, Ben, Benie, Bennie, Bill, Blaize, Bob, Boots, Brad, Bradley, Breaker, Brian, Bruce, Butler, Cable, Caeser, Carlos, Carrington, Cassius, Clarence, CrazyDave, Dan, Darren, Darth, David, Derek, Dorian, Dougal, Drake, Drakkar, Draven, Earl, Ed, Edward, Fane, Fark, Fernando, Frank, Frankie, Fred, Gabe, Gary, Ged, Gerry, Glynne, Godfrey, Grendel, Grunter, Happy, Harry, Hercules, Horatio, Howard, Ike, Isaac, Jack, James, Jay, Jean-Luc, Jens, Jeremy, Jerry, Jessie, Jesus, Jim, Jimmy, Joe, John, Jose, Jose, Joseph, Juan, Justin, Justin, Kellam, Ken, Kevin, Knuckles, Lars, Lazarus, Lewis, Loki, Lorenzo, Louis,Lumpy, Lynk, Malcolm, Markus, Martin, Maximus, Michael,Mozart, Noire, Norman, Notch, Obsidian, Olaf, Oswaldo, Oxnard, Ozzy, Perkin, Pete, Philip, Pumpkin, Ralph, Randy, Red, Reks, Rick, Rogue, Romeo, Roy,Samuel, Schmitty, Scott, Sean, Seifer, Seth, Seymour, Sheldon, Sid, Simon, Slash, Spud, Steele, Stephen, Steve, Steven, Storm, Stryker, Tazer, Thunder, Tidus, Todd, Tom, Uther, Valen, Vance, Velderveer, Victor, Virion, Wayne, Wendle, William, Willie, Wolfgang, Wyatt, Xensor, Yoda, Zac, Zander, Zelroth, Zero, Zorro");
            String temp= p.getString();
            p.comment="These are the male first names used by the random name generator, keep the format the same or bad things will happen.";
            configMaleNames=temp.split(",");
            
            p= config.get("Names", "FemaleNames", "Adele, Agnes, Alice, Alouette, Amelia, Angela, Anne, Annette, Annie, Anthuria, Audrey, Belladonna, Bellinda, Beryl, Betty, BigDoris, Blossom, Bluebell, Breezy, Bridget,Bubbles, Bunty, Chalice, Charlotte, Chibi, ChiChi, Chlodeswinthe, Cinnamon, Coco, Connie, Cosette, Cressida, Cynthia, Daphne, Dimpleblossom, Dimples, Druscilla, Elizabeth, Elphina, Ermengarde, Essence, Fe Fe, Finola, Floris, Foofi, Forsythia, Foxglove, Francesca, Freesia, Frida, Funnysplash, Gardenia, Georgette, Giggles, Gladys, Glimmer, Gossamer, Gwendoline, Hannah, Harriet,  Hayley, Hazel, Heidi, Helga, Hilary, Himiltrud, Honor, Honoria, Hortensia, Hyacinth, Imeena, Iris, Jane, Janet, Joanne, Juliet, Kali, Karen, Kate, Kathy, Katrina, Kay, Kerry, Kristy, Lavinia, Leeta, LiloLil, Lisa, Lizette, Lobelia, Louise, Lucretia, Lumiona, Luna, Lurleen, Lyndis, Lynette, Macey, Madonna, Maggie, Maple, Marie, Marilee,  Martha, Mary, Maude, Maureen,  Maxine, Maya, Michelle, Mikki, Mildred,  Millie, Minnie, Morningpuff, Morticia, Myrtle, Mystery, Neen, Nicolette, Nightshade, Nina, Ninja, Odette, Olive, Pansy, Pansy, Paprika, Patricia, Peachy, Pearl, Persephone, Phoebe, Pinky, Plumeria, Poppy, Posy, Primrose, Priscilla, Queenie, Quintessa, Rebbeca, Rhonda, Ronni, Rosa, Rosette, Ruby, Scarlet, Schmarina, Semolina, Serena, Severa, Sharron, Sheila, Subrina, Sunflower, Susan, Susie, Suzette, Suzie, Talula, Tamara, Tammie, Tansy, Tera, Tessa, Tiffaney, Tourmaline, Trina, Trinity, Trish, Trudi, Truffles, Tulipdance, Twinkleboots, Ukara, Ursula, Velocity, Velvet, Vervain, Violet, Violet, Wilma, Winterwillow, Xyla, Yvette");
            temp= p.getString();
            p.comment="These are the female first names used by the random name generator, keep the format the same or bad things will happen.";
            configFemaleNames=temp.split(",");
            
            p= config.get("Names", "LastNames", "Acorn, Aferditie, Alebuckle, AnchorArms,  Anvilbrow, Arsette, Arsing, Astley, Bacon, Bailey, Beiber, Bijoux, Bilberry, Binkydiggle, Bitterpool, Blonk, Blueberry, Booth, Boothby, Boozewob, Brandybuck, Brocx, Bugger, Bumbletoad, Bumfondle, Bunce, Buntflog, Button, Butts, Claus, Clinton, Clutz, Cox, Creaper, Cupid, Curlynoggin, Dalek, Dapplewink, Dent, Derp, Derpy, Diaper, Diggle, Dimfury, Dimplegourd, Dimplehorn, Donglefart, Dover, Dugbloron, Dulek, Dumbledug, Eaglefeathers, Easyrider, Featherbottom, Featheroak, Firsty, Fitzwilliam, Flashheart, Flickersand, Flintrock, Freckles, Fumblemore, Garlicfeet, Gawkroger, Giggerty, Glitterbreath, Goodbody, Gravy, Grayblade, Griffin, Griswold, Grubb, Handy, Hather, Havealot, Head, Hogpen, Hunt, Jabberwocky, Jaffa, Jibberjabba, Jigglybop, Jingles, Jones, Kegbuster, Kettle, Kitchen, Kneebiter, LaForce, Laforge, Lister, Loordes, MacArse, Maplebutton, Marblemantle, Mayflower, McBucket, McBurp, McCoy, McDonald, McFries, McNugget, Merry, Moist, Moneypenny, Mucus, Mugwort, Nabaztag, Neon, Nibbles, Oaktoes, O'Brian, O'Leary, O'Mygod, O'Notch, O'Reily, Pebble,  Peculier, Persson, Picard, Plank, Plop, Plumdrop, Plunder, Plunder, Potter, Power, Reed, Riker, Rumble, Shadespyre, Shakespeare, Sherman, Silverwood, Smith, Snot, Sparklebutter, Spitznoggle, Steelfinger, Strider, Stumbletoe, Tate, Testificate, Thornburrow, Twinklefig, Twistybees, Underwood, Vader, Walsch, Windywings, Winterbottom, Wonker, Yenocheq, Yog, Zaragamba,Flooberwag,Norsepapper");
            temp= p.getString();
            p.comment="These are the last names used by the random name generator, keep the format the same or bad things will happen.";
            configSurnames=temp.split(",");
            

        }
        catch (Exception e)
        {
            log.severe("Could not allocate block/item ID - " + e.toString());
        }
        finally
        {
            config.save();
        }

        File check = new File(getSimukraftFolder());

        if (!check.exists())
        {
            System.out.println("Sim-U-Kraft error - Mod not correctly installed, ./minecraft/mods/Simukraft/ folder is missing - copy this file from the zip provided");
        }

        SUKfluidMilk=new FluidMilk();  //fluids before blocks
        this.blockFluidMilk=new BlockFluidMilk(blockFluidMilkId);
        
        lightBox = new BlockLightBox(lightboxId).
                setStepSound(Block.soundTypeWood).setHardness(2F).setResistance(1.0F).setTickRandomly(true).setBlockName("SUKlightBox");
        
        buildingConstructor = new BlockConstructorBox(constructorBlockId).
        setStepSound(Block.soundTypeWood).setHardness(2F).setResistance(1.0F).setBlockName("SUKconstructorBox");
        
        controlBox = new BlockControlBox(controlBlockId).
        setStepSound(Block.soundTypeWood).setHardness(10F).setResistance(1.0F).setBlockName("SUKcontrol");
        
        marker = new BlockMarker(markerBlockId).
        setStepSound(Block.soundTypeWood).setHardness(2F).setResistance(1.0F).setBlockName("SUKmarker");
        
        miningBox = new BlockMiningBox(miningBlockId).
        setStepSound(Block.soundTypeWood).setHardness(2F).setResistance(1.0F).setBlockName("SUKmining");
        
        farmingBox = new BlockFarmingBox(farmingBlockId).
        setStepSound(Block.soundTypeWood).setHardness(2F).setResistance(1.0F).setBlockName("SUKfarming");
        
        itemFood = new ItemSUKFood(itemFoodId).setUnlocalizedName("SUKfood");
        blockCompositeBrick = new BlockCompositeBrick(blockCompositeBrickId,Material.rock).
                setStepSound(Block.soundTypeStone).setHardness(8F).setResistance(7.0F).setBlockName("SUKcompositebrick");
        
        blockCheese=new BlockCheeseBlock(Material.cactus).
        		setStepSound(Block.soundTypeCloth).setHardness(0.1F).setResistance(0.5f).setBlockName("SUKblockCheese");
        
       // windmill = new BlockWindmill(windmillId, false).setStepSound(Block.soundTypeWood).setHardness(3F).setResistance(3.0F).setBlockName("SUKWindmill");
        
        //ItemBlock itemLightBox = new ItemBlockLightBox(lightBox).setUnlocalizedName("SUKitemLightBox");
        //GameRegistry.registerItem(itemLightBox, itemLightBox.getUnlocalizedName());
        
        /*TODO: re-Implement -- causes crash at getUnlocalizedName*/
        //ItemBlock itemWindmill = new ItemBlockWindmill(windmill).setUnlocalizedName("SUKitemWindmill");
        //GameRegistry.registerItem(itemWindmill, itemWindmill.getUnlocalizedName());
        //pathConstructor = new BlockPathConstructor(pathConstructorId).
        //	setStepSound(Block.soundWoodFootstep).setHardness(2F).setResistance(1.0F).setBlockName("SUKpathConstructor");

        
		
        itemGranulesGold=new ItemGranulesGold(); 
		LanguageRegistry.addName(itemGranulesGold, "Gold granules"); 
		
		itemGranulesIron=new ItemGranulesIron(); 
		LanguageRegistry.addName(itemGranulesIron, "Iron granules"); 
		
	//	itemGranulesCopper=new ItemGranulesCopper(itemGranulesCopperId); 
	//	LanguageRegistry.addName(itemGranulesCopper, "dustCopper"); 
		
	//	itemGranulesTin=new ItemGranulesTin(itemGranulesTinId); 
	//	LanguageRegistry.addName(itemGranulesTin, "Tin granules"); 
        
		itemWindmillBase=new ItemWindmillBase(itemWindmillBaseId); 
		LanguageRegistry.addName(itemWindmillBase, "Windmill base"); 
		
		itemWindmillVane=new ItemWindmillVane(itemWindmillVaneId); 
		LanguageRegistry.addName(itemWindmillVane, "Windmill vane"); 
		
		itemWindmillSails=new ItemWindmillSails(itemWindmillSailsId); 
		LanguageRegistry.addName(itemWindmillBase, "Windmill sails"); 
		
		
	//	OreDictionary.registerOre("dustCopper", itemGranulesCopper);
	//	OreDictionary.registerOre("dustTin", itemGranulesTin);
        
        MinecraftForge.EVENT_BUS.register(new EventSounds());
    }

    @EventHandler
    public void initLoad(FMLInitializationEvent event)
    {    
        GameRegistry.registerBlock(buildingConstructor, "SUKconstructorBox");
        GameRegistry.registerBlock(controlBox, "SUKcontrol");
        GameRegistry.registerBlock(marker, "SUKmarker");
        GameRegistry.registerBlock(miningBox, "SUKmining");
        GameRegistry.registerBlock(farmingBox, "SUKfarming");
        GameRegistry.registerBlock(blockCompositeBrick,"SUKcompositebrick");
        GameRegistry.registerBlock(blockCheese,"SUKcheeseblock");
        GameRegistry.registerBlock(blockFluidMilk,"fluidMilk");
        //GameRegistry.registerBlock(pathConstructor,"SUKpathConstructor");
        //GameRegistry.registerTileEntity(TileEntityWindmill.class, "tileentitywindmill");
        LanguageRegistry.addName(buildingConstructor, "Sim-U-Building Constructor Box");
        LanguageRegistry.addName(controlBox, "Sim-U-Control Box");
        LanguageRegistry.addName(marker, "Sim-U-Marker");
        LanguageRegistry.addName(miningBox, "Sim-U-Mining Box");
        LanguageRegistry.addName(farmingBox, "Sim-U-Farming Box");
        //TODO: LanguageRegistry.addName(windmill, "Sim-U-Windmill");
        LanguageRegistry.addName(blockCompositeBrick, "Composite Brick");
        LanguageRegistry.addName(blockCheese,"Block of Cheese");
        LanguageRegistry.addName(blockFluidMilk, "Milk");
        LanguageRegistry.addName(new ItemStack(itemFood, 1, 0), "Cheese slice");
        LanguageRegistry.addName(new ItemStack(itemFood, 1, 1), "a Hamburger");
        LanguageRegistry.addName(new ItemStack(itemFood, 1, 2), "Fries");
        LanguageRegistry.addName(new ItemStack(itemFood, 1, 3), "a Cheeseburger");
        
        //LanguageRegistry.addName(pathConstructor, "Sim-U-Path Constructor");
        //LanguageRegistry.addName(lightBox, "Sim-U-Light Box");  multiblock now
        LanguageRegistry.instance().addStringLocalization("tile.blockSUKLight.white.name", "Sim-U-Light (white)");
        LanguageRegistry.instance().addStringLocalization("tile.blockSUKLight.red.name", "Sim-U-Light (red)");
        LanguageRegistry.instance().addStringLocalization("tile.blockSUKLight.orange.name", "Sim-U-Light (orange)");
        LanguageRegistry.instance().addStringLocalization("tile.blockSUKLight.yellow.name", "Sim-U-Light (yellow)");
        LanguageRegistry.instance().addStringLocalization("tile.blockSUKLight.green.name", "Sim-U-Light (green)");
        LanguageRegistry.instance().addStringLocalization("tile.blockSUKLight.blue.name", "Sim-U-Light (blue)");
        LanguageRegistry.instance().addStringLocalization("tile.blockSUKLight.purple.name", "Sim-U-Light (purple)");
        LanguageRegistry.instance().addStringLocalization("tile.blockSUKLight.rainbow.name", "Sim-U-Light (rainbow)");
        GameRegistry.addRecipe(new ItemStack(buildingConstructor, 1), new Object[]
                               {
                                   "PPP", "CWC", "CCC",
                                   'C', Blocks.cobblestone,
                                   'P', Blocks.planks,
                                   'W', Blocks.crafting_table
                               });
        GameRegistry.addRecipe(new ItemStack(marker, 3), new Object[]
                               {
                                   "G", "S",
                                   'S', Items.stick,
                                   'G', new ItemStack(Items.dye, 1, 11)
                               });

        if (configUseExpensiveRecipies)
        {
            GameRegistry.addRecipe(new ItemStack(miningBox, 1), new Object[]
                                   {
                                       "PPP", "CWC", "CCC",
                                       'C', Blocks.cobblestone,
                                       'P', Blocks.planks,
                                       'W', Items.diamond_pickaxe
                                   });
            GameRegistry.addRecipe(new ItemStack(farmingBox, 1), new Object[]
                                   {
                                       "PPP", "CWC", "CCC",
                                       'C', Blocks.cobblestone,
                                       'P', Blocks.planks,
                                       'W', Items.diamond_hoe
                                   });
        }
        else
        {
            GameRegistry.addRecipe(new ItemStack(miningBox, 1), new Object[]
                                   {
                                       "PPP", "CWC", "CCC",
                                       'C', Blocks.cobblestone,
                                       'P', Blocks.planks,
                                       'W', Items.stone_pickaxe
                                   });
            GameRegistry.addRecipe(new ItemStack(farmingBox, 1), new Object[]
                                   {
                                       "PPP", "CWC", "CCC",
                                       'C', Blocks.cobblestone,
                                       'P', Blocks.planks,
                                       'W', Items.stone_hoe
                                   });
        }

        GameRegistry.addRecipe(new ItemStack(lightBox, 2), new Object[]
                               {
                                   "LL", "LL",
                                   'L', Blocks.torch
                               });
        GameRegistry.addShapelessRecipe(new ItemStack(lightBox, 1, 1), new Object[]
                                        {
                                            lightBox, new ItemStack(Items.dye, 1, 1)
                                        });
        GameRegistry.addShapelessRecipe(new ItemStack(lightBox, 1, 2), new Object[]
                                        {
                                            lightBox, new ItemStack(Items.dye, 1, 14)
                                        });
        GameRegistry.addShapelessRecipe(new ItemStack(lightBox, 1, 3), new Object[]
                                        {
                                            lightBox, new ItemStack(Items.dye, 1, 11)
                                        });
        GameRegistry.addShapelessRecipe(new ItemStack(lightBox, 1, 4), new Object[]
                                        {
                                            lightBox, new ItemStack(Items.dye, 1, 10)  //lime
                                        });
        GameRegistry.addShapelessRecipe(new ItemStack(lightBox, 1, 5), new Object[]
                                        {
                                            lightBox, new ItemStack(Items.dye, 1, 4)
                                        });
        GameRegistry.addShapelessRecipe(new ItemStack(lightBox, 1, 6), new Object[]
                                        {
                                            lightBox, new ItemStack(Items.dye, 1, 5)
                                        });
        GameRegistry.addShapelessRecipe(new ItemStack(lightBox, 1, 7), new Object[]
                                        {
                                            lightBox, new ItemStack(Items.dye, 1, 1),
                                            new ItemStack(Items.dye, 1, 14),
                                            new ItemStack(Items.dye, 1, 11),
                                            new ItemStack(Items.dye, 1, 10),
                                            new ItemStack(Items.dye, 1, 4),
                                            new ItemStack(Items.dye, 1, 5)
                                        });
        
        GameRegistry.addRecipe(new ItemStack(blockCheese,1), new Object[] {
        	"CCC","CCC","CCC",
        	'C', new ItemStack(itemFood,1,0)
        });
        GameRegistry.addShapelessRecipe(new ItemStack(itemFood,9,0), new Object[] {
        	new ItemStack(blockCheese)
        });
        
        /*
        GameRegistry.addRecipe(new ItemStack(pathConstructor, 1), new Object[]{
            "PPP", "CWC", "CCC",
            'C', Blocks.cobblestone,
            'P', Blocks.planks,
            'W', buildingConstructor
        });
         */
        
        GameRegistry.addRecipe(new ItemStack(blockCompositeBrick, 1), new Object[]{
            "CSC", "SIS", "CSC",
            'C', Blocks.hardened_clay,   
            'S', Blocks.stone,
            'I', Blocks.iron_bars
        });  
        

        GameRegistry.addRecipe(new ItemStack(itemWindmillBase), new Object[]
		    {
		     " C ", 
		     "CCC",
		     "CCC",  
		     'C', blockCompositeBrick
		    });
        
        for (int c=0;c<16;c++) {
	        GameRegistry.addRecipe(new ItemStack(itemWindmillVane,1,c), new Object[]
			    {
			     "WWW",
			     "SSS",  
			     'S', Items.stick, 'W', new ItemStack(Blocks.wool,1,c)
			    });
        }
        
        for(int c=0;c<16;c++) {
	        GameRegistry.addRecipe(new ItemStack(itemWindmillSails,1,c), new Object[]
			    {
			     " V ", 
			     "VPV",
			     " V ",  
			     'V', new ItemStack(itemWindmillVane,1,c) , 'P', Blocks.planks
			    });
        }
        
        for(int c=0;c<16;c++) {
        GameRegistry.addRecipe(new ItemStack(windmill,1,c), new Object[]
		    {
		     "S", 
		     "B", 
		     'S', new ItemStack(itemWindmillSails,1,c), 'B', itemWindmillBase
		    });
       }
        
        GameRegistry.addSmelting(itemGranulesGold, new ItemStack(Items.gold_ingot), 0.1f);
        GameRegistry.addSmelting(itemGranulesIron, new ItemStack(Items.iron_ingot), 0.1f);
        
       
        // ENTITIES:
        EntityRegistry.registerGlobalEntityID(EntityAlignBeam.class,
                "AlignBeam", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityAlignBeam.class,"AlignBeam", 0, this, 250, 10, false);
        
        EntityRegistry.registerGlobalEntityID(EntityFolk.class,
                "Folk", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityFolk.class, "Folk", 1, this, 250, 2, true);
        
        EntityRegistry.registerGlobalEntityID(EntityConBox.class,
                "ConBox", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityConBox.class, "ConBox", 2, this, 250, 2, true);
        
        EntityRegistry.registerGlobalEntityID(EntityWindmill.class,
                "SUKWindmill", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityWindmill.class,"SUKWindmill", 3, this, 250, 1, false);
        
        
        proxy.registerRenderInfo();
        proxy.registerMisc();
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new GuiHUD(Minecraft.getMinecraft()));
      }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
      event.registerServerCommand(new SimukraftCommand());
    }
    private static GuiRunMod runModui = null;
    
    public static void resetAndLoadNewWorld() {
    	Side side = cpw.mods.fml.common.FMLCommonHandler.instance().getEffectiveSide();
    	log.info("RESETTING and loading world on "+side.toString()+" SIDE"); 
    	ModSimukraft.theBuildings.clear();
        ModSimukraft.theCourierPoints.clear();
        ModSimukraft.theCourierTasks.clear();
        ModSimukraft.theMiningBoxes.clear();
        ModSimukraft.theFarmingBoxes.clear();
        ModSimukraft.theFolks.clear();
        ModSimukraft.theRelationships.clear();
        
        File f=new File(ModSimukraft.getSavesDataFolder() + "settings.sk2");
        if (!f.exists()) {
        	f=new File(ModSimukraft.getSavesDataFolder() + "settings.suk");
        }
        if (f.exists())
        {
            ModSimukraft.states.loadStates();

            try
            {
                ModSimukraft.setGameModeFromNumber(ModSimukraft.states.gameModeNumber);
            }
            catch (Exception e)
            {
                ModSimukraft.setGameModeFromNumber(0);
            }
        }
        else
        {
            ModSimukraft.states = new GameStates();
            ModSimukraft.states.saveStates();
        }

        if (ModSimukraft.states == null)
        {
            new File(ModSimukraft.getSavesDataFolder() + "settings.sk2").delete();
            ModSimukraft.states = new GameStates();
            ModSimukraft.states.saveStates();
            ModSimukraft.sendChat("Your Sim-U-Kraft settings file was corrupted, I had to make a new one");
        }

        if (ModSimukraft.states.gameModeNumber == -1)
        {
            if (runModui == null)
            {
                GuiRunMod runModui = new GuiRunMod();
                Minecraft.getMinecraft().displayGuiScreen(runModui);
            }

            return;
        }

        if (ModSimukraft.states.gameModeNumber >= 0)
        {
            ModSimukraft.proxy.ranStartup = true;
        }

        ModSimukraft.sendChat("Welcome to Sim-U-Kraft " + ModSimukraft.version);
        ModSimukraft.theFolks.clear();
        Building.initialiseAllBuildings(); //load ALL buildings off disk and init them
        Building.loadAllBuildings();	   //load buildings in this world
        CourierTask.loadCourierTasksAndPoints();
        MiningBox.loadMiningBoxes();
        FarmingBox.loadFarmingBoxes();
        //PathBox.loadPathBoxes();
        FolkData.loadAndSpawnFolks();
        Relationship.loadRelationships();
        ModSimukraft.updateCheck();
        ModSimukraft.isDay = ModSimukraft.isDayTime();
        Building.checkTennants();
        ModSimukraft.proxy.ranStartup = true;
    }
 

    /** helper function to send chat to all players in all worlds/dimensions */
    public static void sendChat(String theText)
    {
        for (World w : MinecraftServer.getServer().worldServers)
        {
            if (!w.isRemote)
            {
                for (int i = 0; i < w.playerEntities.size(); i++)
                {
                    EntityPlayer p = (EntityPlayer) w.playerEntities.get(i);
                    p.addChatMessage(new ChatComponentText(theText));
                }
            }
        }
    }

    /** gets the .minecraft/saves/CURRENTWORLD/simukraft/   folder as a string  */
    public static String getSavesDataFolder()
    {
        String worldname = MinecraftServer.getServer().getFolderName();
        String strmc = new File(".").getAbsolutePath();
        strmc = strmc.substring(0, strmc.length() - 1);
        File test = new File(strmc + "saves"); //       .bodge()     :-)
        String ret = "";

        if (test.exists())   /// CLIENT SIDE
        {
            ret = new File(strmc + File.separator + "saves"
                           + File.separator + worldname + File.separator + "simukraft" + File.separator).getAbsolutePath()
            + File.separator;
        }
        else     // SERVER SIDE
        {
            strmc = strmc + worldname + File.separator + "simukraft" + File.separator;
            ret = new File(strmc).getAbsolutePath();
        }

        File f = new File(ret);

        if (!f.exists())
        {
            f.mkdirs();
        }

        return ret;
    }

    /** get the .minecraft/mods/Simukraft/  folder (contains building text files) */
    public static String getSimukraftFolder()
    {
        try
        {
            String strmc = new File(".").getAbsolutePath();
            strmc = strmc.substring(0, strmc.length() - 1);
            return new File(strmc +
                            File.separator + "mods" + File.separator + "Simukraft").getAbsolutePath();
        }
        catch (Exception e)
        {
            return "";
        }
    }

    /** returns true when it is daytime in the OVERWORLD, ignores other world times */
    public static boolean isDayTime()
    {
        if (MinecraftServer.getServer().worldServers[0].getWorldInfo().getWorldTime() % 24000 <= 11999)
        {
            return true;
        }
        else
        {
            return false;
        }

        //return MinecraftServer.getServer().worldServers[0].isDaytime();  //<- doesn't work during storm
    }

    /** returns a nicely formatted money value */
    public static String displayMoney(float moneyin)
    {
        DecimalFormat myFormatter = new DecimalFormat("#,##0.00");
        String output = myFormatter.format(moneyin);
        return output;
    }

    public static void updateCheck()
    {
		//REMOVED
    }

  
        public void ThreadUpdate()
        {
            //start();
        }

        public void run()
        
        {
        	/*TODO: RE-IMPLEMENT*/
            try
            {
                Thread.sleep(15000);
                File check = new File(ModSimukraft.getSimukraftFolder() + "/buildings/");

                if (!check.exists())
                {
                    ModSimukraft.sendChat(ModSimukraft.getSimukraftFolder() + "/buildings/  folder is missing, Sim-U-Kraft is not correctly installed, please copy the simukraft folder AND the zip file.");
                    return;
                }
            }
                //String baseURL = "http://satscape.no-ip.info:7254/simukraftstore/";


                //// check for new version
               // String ver = downloadFile(baseURL + "simukraft-version.txt", ModSimukraft.getSimukraftFolder()
                                         // + File.separator + "simukraft.txt");

                //if (ver != null)
                //{
                //    ver = ver.trim();
//
                   // if (!ver.contentEquals(""))
                    //{
                      //  if (!version.contentEquals(ver))
                      //  {
                      //      ModSimukraft.sendChat("**** NEW update of Sim-U-Kraft available (from " + version + " to " + ver + ") at satscape.wordpress.com/simukraft");
                      //  }

                     //   Long now = System.currentTimeMillis();
                       // ModSimukraft.states.lastUpdateCheck = now;
                       // ModSimukraft.states.saveStates();
                    //}
             //   }

                //////get new buildings
               // int high = getHighestPKID("residential");
               /// int o = getHighestPKID("other");

               // if (o > high)
               // {
              //     high = o;
              //  }

                
                
               // String newbs = downloadFile(baseURL + "backend.php?cmd=getnew&n=" + high + "&i=" + getTheirId() + "&v=" + version, ModSimukraft.getSimukraftFolder()
              //                              + File.separator + "simukraft.txt");

               // if (newbs.length() == 0)
               // {
               //     return;   //No new buildings or server is fucked
               // }

               // String[] items = newbs.split("!END"); //['pk'].['title'].['author'].['type']

               // for (int i = 0; i < items.length - 1; i++)
               // {
                //    String[] fields = items[i].split("!F");
                //    String url = baseURL + "catalogue/PKID" + fields[0] + "-" + fields[1] + ".txt";
                //    String local = ModSimukraft.getSimukraftFolder() + "/buildings/" + fields[3] +
                //                   "/PKID" + fields[0] + "-" + fields[1] + ".txt";
                //    String ret = downloadFile(url, local);

                 //   if (!ret.contentEquals(""))
                 //   {
                  //      url = baseURL + "backend.php?cmd=got&pk=" + fields[0];
                  //      downloadFile(url, ModSimukraft.getSimukraftFolder() + File.separator + "cache.txt");
                  //      ModSimukraft.sendChat("Sim-U-Kraft: Downloaded new building - '" + fields[1] + "' by " + fields[2] +
                      //                        " (" + fields[3] + ")");
                  //  }
               // }
          //  }
          catch (Exception e)
            {
                e.printStackTrace();
            }
      //  }
   //
       // int highest = 0;
       // int m1 = 0;
       // File actual = new File(ModSimukraft.getSimukraftFolder() + File.separator + "buildings" + File.separator + type +
       //                        File.separator);

      //  for (File f : actual.listFiles())
      //  {
        //    if (f.getName().startsWith("PKID"))
       //     {
          //      m1 = f.getName().indexOf("-");

           //     if (m1 > 0)
           //     {
               //     String id = f.getName().substring(4, m1);

               //     if (Integer.parseInt(id) > highest)
               //     {
                //        highest = Integer.parseInt(id);
                //    }
              //  }
           // }
        //}

        //return highest;
   // }
      //  String ret = "";
      //  log.info("Downloading file " + url);
       // url = url.replace(" ", "%20");

       // try
       //{
            //java.io.BufferedInputStream in = new java.io.BufferedInputStream(new java.net.URL(url).openStream());
            //java.io.FileOutputStream fos = new java.io.FileOutputStream(localFile);
           // java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
          // byte[] data = new byte[4096];
            //int x = 0;

            //while ((x = in.read(data, 0, 4096)) >= 0)
            //{
            //    bout.write(data, 0, x);
            //}

           // bout.flush();
            //ret = new String(data);
            //bout.close();
            //in.close();
        //}
        //catch (Exception e)
        //{
           // ret = "";
          //  e.printStackTrace();
        //}

        //return ret;*/
    }

    public static void dayTransitionHandler()
    {
        if (isDayTime() && isDay == false)   /// Day transisition
        {
            isDay = true;
            log.info("Night to day transition");
            World world = proxy.getClientWorld();

            if (world != null)
            {
                EntityPlayer p = Minecraft.getMinecraft().thePlayer;

                if (p != null)
                {
                    proxy.getClientWorld().playSound(p.posX, p.posY, p.posZ, "satscapesimukraft:rooster", 1.0f, 1.0f, false);
                }
            }

            ModSimukraft.states.dayOfWeek++;

            if (ModSimukraft.states.dayOfWeek > 6)
            {
                ModSimukraft.states.dayOfWeek = 0;
                int homeless=0;
                for(FolkData f:theFolks) {
                	if (f.getHome()==null) {
                		homeless++;
                	}
                }
                if (homeless>1) {
                	sendChat("There is a demand for more residential housing, you have "+homeless+" folks without a home.");
                }
                
            }

            evolveFolks();

            if (theFolks.size() > 1)
            {
                Random rand = new Random();
                int f1 = rand.nextInt(theFolks.size());
                int f2 = f1;

                while (f2 == f1)
                {
                    f2 = rand.nextInt(theFolks.size());
                }

                FolkData folk1 = theFolks.get(f1);
                FolkData folk2 = theFolks.get(f2);
                Relationship.meddleWithRelationship(folk1, folk2);
            }
        }

        if (!isDayTime() && isDay == true)
        {
            isDay = false;
            log.info("Day to Night transition");

            if (theFolks.size() > 1)
            {
                Random rand = new Random();
                int f1 = rand.nextInt(theFolks.size());
                int f2 = f1;

                while (f2 == f1)
                {
                    f2 = rand.nextInt(theFolks.size());
                }

                FolkData folk1 = theFolks.get(f1);
                FolkData folk2 = theFolks.get(f2);
                Relationship.meddleWithRelationship(folk1, folk2);
            }
            
            for(FolkData folk:theFolks) {
            	folk.destination=null;
            	if (folk.theEntity !=null) {
            		folk.theEntity.getNavigator().clearPathEntity();
            	}
            }
            
        }
    }

    //age them etc
    private static void evolveFolks()
    {
        if (theFolks.size() <= 0)
        {
            return;
        }

        FolkData folk;
        Random rand = new Random();
        log.info("evolving folks");
        //collect rent
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {

                try
                {
                    Thread.sleep(3000);
                }
                catch (Exception e) {}

                float totalRent = 0f;

                if (gameMode != GameMode.CREATIVE)
                {
                    for (int b = 0; b < theBuildings.size(); b++)
                    {
                        Building building = (Building) theBuildings.get(b);

                        if (building.type.contentEquals("residential") && building.tennants.size() > 0)
                        {
                            if (building.rent == null || building.rent == 0f)
                            {
                                building.rent = 1f;
                            }

                            log.info("Building rent for " + building.displayNameWithoutPK + ": " + building.rent + "(" +
                                building.blocksInBuilding + ")");
                            totalRent += building.rent;
                        }
                    }
                }

                if (totalRent > 0f)
                {
                    sendChat("Collected " + displayMoney(totalRent) + " Sim-u-credits in rent today.");
                    states.credits += totalRent;
                    EntityPlayer p = Minecraft.getMinecraft().thePlayer;

                    if (p != null)
                    {
                        proxy.getClientWorld().playSound(p.posX, p.posY, p.posZ, "satscapesimukraft:cash", 1f, 1f, false);
                    }
                }
                else if (gameMode != GameMode.CREATIVE)
                {
                    sendChat("No rent collected today, you should hire a folk to build a residential house.");
                }
            }
        });
        t.start();

        for (int f = 0; f < theFolks.size(); f++)
        {
            folk = (FolkData) theFolks.get(f);
            //reset their greet status
            folk.greetedToday = false;
            /*TODO: re-implement*/
           // folk.shaggingStage = -1.0f; //not had today

            if (folk.pregnancyStage > 0.0f) //increase pregnancy - birth is in FolkData
            {
                folk.pregnancyStage += 0.1f;
            }
            
            //Age them
            int currentAge = folk.age;
            if (currentAge >= 18)
            {
                if (states.dayOfWeek == 6)  //on saturday morning
                {
                    folk.age++;
                }
            }
            else
            {
                if (states.dayOfWeek == 3 || states.dayOfWeek == 6) //age kids twice a week
                {
                    folk.age++;

                    if (currentAge == 17 && folk.age == 18) //now an adult (skin changes automatically)
                    {
                        folk.evictThem();// out of parents house
                        sendChat(folk.name+" is now 18 years old, they'll start looking for a house and you can now employ them too.");
                    }
                }
            }
            
            ///kill them when they are over 110 (1 in 10 random)
            if (folk.age > 110 && rand.nextInt(10) == 5)
            {
                sendChat(folk.name + " is old and not feeling very well...oh no!");
                folk.eventDied(DamageSource.generic);
            }
        }

        if (gameMode != GameMode.CREATIVE)
        {
            ////age them one year per game week
            int fl = rand.nextInt(theFolks.size());

            for (int f = 0; f < theFolks.size(); f++)
            {
                folk = (FolkData) theFolks.get(f);

                if (f == fl)
                {
                    folk.levelFood--;

                    if (folk.levelFood == 0)
                    {
                        sendChat(folk.name + " is VERY hungry, you should build a farm, grocery, bakery or throw some food at them.");
                    }
                }
            }

            /// pay the soldiers
            for (int f = 0; f < theFolks.size(); f++)
            {
                folk = (FolkData) theFolks.get(f);

                if (folk.theirJob != null)
                {
                    if (folk.vocation == Vocation.SOLDIER)
                    {
                        //JobSoldier job = (JobSoldier)folk.theirJob;
                       // float pay = job.kills * 0.20f;

                        //if (job.kills > 0)
                       // {
                        //    ModSimukraft.sendChat("Paid " + folk.name + " " + ModSimukraft.displayMoney(pay) +
                       //                           " Sim-u-credits for killing " + job.kills + " hostile mobs yesterday.");
                        //    states.credits -= pay;
                       //     job.kills = 0;
                       // }
                    }
                }
            }

            // fluctuate block prices (Builders merchant)
            boolean updown = rand.nextBoolean();
            PricesForBlocks.adjustPrice(Blocks.planks, updown);
            updown = rand.nextBoolean();
            PricesForBlocks.adjustPrice(Blocks.cobblestone, updown);
            updown = rand.nextBoolean();
            PricesForBlocks.adjustPrice(Blocks.stone, updown);
            updown = rand.nextBoolean();
            PricesForBlocks.adjustPrice(Blocks.glass, updown);
            updown = rand.nextBoolean();
            PricesForBlocks.adjustPrice(Blocks.wool, updown);
            updown = rand.nextBoolean();
            PricesForBlocks.adjustPrice(Blocks.brick_block, updown);
            updown = rand.nextBoolean();
            PricesForBlocks.adjustPrice(Blocks.stonebrick, updown);
            updown = rand.nextBoolean();
            PricesForBlocks.adjustPrice(Blocks.fence, updown);
        } //end of non-creative mode stuff

        states.saveStates();
        Commodity.refreshAvailableCommoditities();
    }

    public static void demolishBlocks()
    {
        if (demolishBlocks.size() < 1)
        {
            return;
        }

        int count = demolishBlocks.size();

        if (count > 10)
        {
            count = 10;
        }

        for (int i = 0; i < count; i++)
        {
            V3 blockLoc = demolishBlocks.get(0);
            Block block;

            try
            {
            	block =Block.getBlockById(blockLoc.blockID);
            	block.dropBlockAsItem(demolishWorld, blockLoc.x.intValue(), blockLoc.y.intValue() + 10 + new Random().nextInt(20), blockLoc.z.intValue()
                                      , 0, 0);
                demolishBlocks.remove(0);
            }
            catch (Exception e) {}

            
        }
    }

    /** called from commonTickHandler() every tick to upgrade a farm from one level to the next */
    public static void upgradeFarm()
    {

        if (farmToUpgrade.level == 0)
        {
            farmToUpgrade.level = 1;
        }

        if (farmToUpgrade.level == 1) //upgrade to level 2 (fence and lights)
        {
            if (farmToUpgradePoints == null)
            {
                farmToUpgradePoints = farmToUpgrade.getPerimeterPoints();
            }

            V3 point = farmToUpgradePoints.get(farmToUpgradeCounter);
            World theWorld = MinecraftServer.getServer().worldServerForDimension(point.theDimension);
            /////place fencing if the area is clear
            Block id = theWorld.getBlock(point.x.intValue(), point.y.intValue(), point.z.intValue());
            boolean destroy=false;
            if (id != Blocks.air) {
            	TileEntity te=theWorld.getTileEntity(point.x.intValue(), point.y.intValue(), point.z.intValue());
            	if (te ==null) {
            		destroy=true;
            	} else {
            		if (!(te instanceof IInventory)) {
            			destroy=true;
            		}
            	}
            } else { destroy=true; }
            
        	if (destroy){
        		theWorld.setBlock(point.x.intValue(), point.y.intValue(), point.z.intValue(),
                        Blocks.fence, 0, 0x03);
        		theWorld.markBlockForUpdate(point.x.intValue(), point.y.intValue(), point.z.intValue());
        	}
            
            

            //// place lights every 6 blocks
            if (farmToUpgradeCounter % 6 == 0)
            {
                theWorld.setBlock(point.x.intValue(), point.y.intValue() - 1, point.z.intValue(),
                                  Block.getBlockById(lightboxId), 0, 0x03);
                theWorld.markBlockForUpdate(point.x.intValue(), point.y.intValue() - 1, point.z.intValue());
            }
        }
        else if (farmToUpgrade.level == 2)   //upgrade to level 3 (irrigation)
        {
            if (farmToUpgradePoints == null)
            {
                farmToUpgradePoints = farmToUpgrade.getSoilBlockPoints();
            }

            V3 point = farmToUpgradePoints.get(farmToUpgradeCounter);
            World theWorld = MinecraftServer.getServer().worldServerForDimension(point.theDimension);

            if (point.x.intValue() % 5 == 0 && point.z.intValue() % 5 == 0)
            {
                theWorld.setBlock(point.x.intValue(), point.y.intValue() - 1, point.z.intValue(), Blocks.water, 0, 0x03);
                theWorld.setBlock(point.x.intValue(), point.y.intValue() - 2, point.z.intValue(), Block.getBlockById(lightboxId), 0, 0x03);
                theWorld.markBlockForUpdate(point.x.intValue(), point.y.intValue() - 1, point.z.intValue());
                theWorld.markBlockForUpdate(point.x.intValue(), point.y.intValue() - 2, point.z.intValue());
            }
        }

        farmToUpgradeCounter++;

        if (farmToUpgradeCounter > farmToUpgradePoints.size() - 1)
        {
            farmToUpgrade.level++;
            farmToUpgradePoints = null;
            farmToUpgrade = null;
            farmToUpgradeCounter = 0;
            log.info("Finished farm upgrade");
            return;
        }
    }

    protected final static String[] dow = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    public static String getDayOfWeek()
    {
        return dow[states.dayOfWeek];
    }
    
    public static ArrayList<String> loadSK2(String fullFilename) {
    	ArrayList<String> ret=new ArrayList<String>();
    	try {
    		BufferedReader br = new BufferedReader(new FileReader(fullFilename));

            String line = br.readLine();

            while (line != null) {
            	ret.add(line);
                line = br.readLine();
            }
            br.close();
            
    	}catch(Exception e) {e.printStackTrace();}
    	
        return ret;
    }
    
    public static void changeGamemode(int num){
    	
    	states.gameModeNumber = num;
    	
    }
    
    public static void saveSK2(String fullFilename, ArrayList<String> strings) {
    	try {
    	BufferedWriter bw=new BufferedWriter(new FileWriter(fullFilename));
    	for(String line:strings) {
    		bw.write(line+"\r\n");
    	}
    	bw.close();
    	
    	}catch(Exception e) {e.printStackTrace(); }
    }
    

}
