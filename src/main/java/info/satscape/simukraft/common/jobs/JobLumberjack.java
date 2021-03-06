/*package info.satscape.simukraft.common.jobs;

import info.satscape.simukraft.ModSimukraft;
import info.satscape.simukraft.common.Building;
import info.satscape.simukraft.common.CommonProxy;
import info.satscape.simukraft.common.FolkData;
import info.satscape.simukraft.common.CommonProxy.V3;
import info.satscape.simukraft.common.FolkData.FolkAction;
import info.satscape.simukraft.common.FolkData.GotoMethod;
import info.satscape.simukraft.common.jobs.Job.Vocation;
import info.satscape.simukraft.common.jobs.JobMiner.Stage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

public class JobLumberjack extends Job implements Serializable
{
    private static final long serialVersionUID = -1177112207904887741L;

    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    transient public int runDelay = 1000;
    transient public long timeSinceLastRun = 0;

    private transient ArrayList<IInventory> millChests = new ArrayList<IInventory>();
    private transient V3 foundWoodAt = new V3();
    private transient Building lumbermill = null;
    private transient long startedGoing = 0l;
    private transient boolean isChopping = false;
    private transient float pay = 0f;
    private transient boolean onRoute = false;

    public JobLumberjack() {}
    public JobLumberjack(FolkData folk)
    {
        theFolk = folk;

        if (theStage == null)
        {
            theStage = Stage.IDLE;
        }

        if (theFolk == null)
        {
            return;   // is null when first employing, this is for next day(s)
        }

        if (theFolk.destination == null)
        {
            theFolk.gotoXYZ(theFolk.employedAt, null);
        }
    }

    public enum Stage
    {
        IDLE, ARRIVEDATMILL, SCANFORTREE, GOTOTREE, CHOPPINGTREE, RETURNWOOD;
    }

    public void resetJob()
    {
        theStage = Stage.IDLE;
        theFolk.isWorking = false;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (!ModSimukraft.isDayTime())
        {
            theStage = Stage.IDLE;
        }

        super.onUpdateGoingToWork(theFolk);

        if (System.currentTimeMillis() - timeSinceLastRun < runDelay)
        {
            return;
        }

        timeSinceLastRun = System.currentTimeMillis();

        // ////////////////IDLE
        if (theStage == Stage.IDLE && ModSimukraft.isDayTime())
        {
            theStage = Stage.SCANFORTREE;
        }
        else if (theStage == Stage.ARRIVEDATMILL)
        {
            theStage = Stage.SCANFORTREE;
        }
        else if (theStage == Stage.SCANFORTREE)
        {
            stageScanForTree();
        }
        else if (theStage == Stage.GOTOTREE)
        {
            pickUpSaplings();
            stageGotoTree();
        }
        else if (theStage == Stage.CHOPPINGTREE)
        {
            stageChoppingTree();
            pickUpSaplings();
        }
        else if (theStage == Stage.RETURNWOOD)
        {
            stageReturnWood();
            pickUpSaplings();
        }
    }

    private void stageScanForTree()
    {
        theFolk.action = FolkAction.ATWORK;
        theFolk.isWorking = false;
        V3 searchXYZ = null;
        lumbermill = Building.getBuilding(theFolk.employedAt);
        V3 ts=null;
        
        try
        {
            if (lumbermill.lumbermillMarker != null)
            {
                searchXYZ = lumbermill.lumbermillMarker; // use a marker if there is one
            }
            else if (theFolk.employedAt != null)
            {
                searchXYZ = theFolk.employedAt.clone();
            }
            else
            {
                searchXYZ = theFolk.location.clone();
            }
            
            ts = searchXYZ.clone();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        
        V3 searchpos;

        if (ts != null)
        {
            searchpos = ts.clone();
        }
        else
        {
            searchpos = theFolk.location.clone();
        }

        try
        {
            foundWoodAt = findClosestBlockType(searchpos, Block.wood, ModSimukraft.configLumberArea, false);
            foundWoodAt.theDimension = jobWorld.provider.dimensionId;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        theStage = Stage.GOTOTREE;
        onRoute = false;

        if (foundWoodAt == null)
        {
            ModSimukraft.sendChat(theFolk.name + " could not find any wood in the area.");
            theFolk.selfFire();
            return;
        }
    }

    private void stageGotoTree()
    {
        theFolk.isWorking = false;

        if (!onRoute)
        {
            theFolk.statusText = "Going to tree...";
            theFolk.gotoXYZ(foundWoodAt, null);
            startedGoing = System.currentTimeMillis();
            onRoute = true;
        }
        else
        {
            if (theFolk.gotoMethod == GotoMethod.WALK)
            {
                theFolk.updateLocationFromEntity();
            }

            double dist = theFolk.location.getDistanceTo(foundWoodAt);

            if (dist < 7)
            {
                theStage = Stage.CHOPPINGTREE;
                theFolk.stayPut = true;
                step = 1;
            }
            else
            {
                if (theFolk.destination == null && theFolk.theEntity != null)
                {
                    //theFolk.gotoXYZ(foundWoodAt);
                }

                if (System.currentTimeMillis() - startedGoing > 25000)
                {
                    //fuck it, close enough
                    theStage = Stage.CHOPPINGTREE;
                    theFolk.stayPut = true;
                    theFolk.destination = null;
                    step = 1;
                }
            }
        }
    }

    private void stageChoppingTree()
    {
        int count;

        if (step == 1)
        {
            theFolk.statusText = "Choppy Choppy tree!";
            theFolk.isWorking = true;

            ///find the bottom of the trunk
            for (int down = 0; down < 20; down++)
            {
                int x = foundWoodAt.x.intValue();
                int y = foundWoodAt.y.intValue() - 1;
                int z = foundWoodAt.z.intValue();

                if (jobWorld == null)
                {
                    theFolk.selfFire();
                    return;
                }

                if (jobWorld.getBlockId(x, y, z) != Block.wood.blockID)
                {
                    break;
                }
                else
                {
                    foundWoodAt.y = (double) y;
                }
            }

            step = 2;
        }
        else if (step == 2)
        {
            if (jobWorld.getBlockId(foundWoodAt.x.intValue(), foundWoodAt.y.intValue(),
                                    foundWoodAt.z.intValue()) == Block.wood.blockID)
            {
                Thread t = new Thread(new Runnable()
                {
                    public void run()
                    {
                        isChopping = true;

                        for (int d = 0; d < 12; d++)
                        {
                            try
                            {
                                mc.theWorld.playSound(theFolk.location.x, theFolk.location.y, theFolk.location.z, "step.wood", 1f, 1f, false);
                            }
                            catch (Exception e) {}

                            if (theFolk.theEntity != null)
                            {
                                theFolk.theEntity.swingProgress = 0.3f;

                                try
                                {
                                    Thread.sleep(100);
                                }
                                catch (Exception e) {}

                                theFolk.theEntity.swingProgress = 0.7f;

                                try
                                {
                                    Thread.sleep(100);
                                }
                                catch (Exception e) {}
                            }
                        }

                        isChopping = false;
                    }
                });
                t.start();
                step = 3;
            }
            else
            {
                //no more tree left
                step = 4;
            }
        }
        else if (step == 3)
        {
            if (isChopping)
            {
                return;
            }

            ArrayList<ItemStack> log = this.translateBlockWhenMined(jobWorld, foundWoodAt);
            jobWorld.setBlock(foundWoodAt.x.intValue(), foundWoodAt.y.intValue(), foundWoodAt.z.intValue(), 0, 0, 0x03);

            if (log != null)
            {
                for (int l = 0; l < log.size(); l++)
                {
                    ItemStack isl = log.get(l);
                    theFolk.inventory.add(isl);
                }
            }

            count = getInventoryCount(theFolk, Block.wood.blockID);
            theFolk.statusText = "Got " + count + " logs so far";
            theFolk.stayPut = false;
            foundWoodAt.y = foundWoodAt.y + 1;
            step = 2;
        }
        else if (step == 4)
        {
            if (theFolk.isSpawned())
            {
                count = getInventoryCount(theFolk, Block.sapling.blockID);

                if (count > 0)
                {
                    for (int i = 0; i < theFolk.inventory.size(); i++)
                    {
                        ItemStack fis = theFolk.inventory.get(i);

                        if (fis != null && fis.getItem().itemID == Block.sapling.blockID)
                        {
                            theFolk.inventory.remove(i);
                            plantSapling(fis);
                            break;
                        }
                    }
                }
            }
            else
            {
                //they're de-spawned, so are the saplings, so just plant one anyway
                plantSapling(new ItemStack(Block.sapling));
            }

            count = getInventoryCount(theFolk, Block.wood.blockID);

            if (count < 12)
            {
                theStage = Stage.SCANFORTREE;
            }
            else
            {
                theStage = Stage.RETURNWOOD;
                step = 1;
            }
        }
    }

    private void stageReturnWood()
    {
        theFolk.isWorking = false;

        if (step == 1)
        {
            theFolk.statusText = "Delivering wood back to base";
            theFolk.gotoXYZ(theFolk.employedAt, null);
            step = 2;
        }
        else if (step == 2)
        {
            if (theFolk.gotoMethod == GotoMethod.WALK)
            {
                theFolk.updateLocationFromEntity();
            }

            int dist = theFolk.location.getDistanceTo(theFolk.employedAt);

            if (dist <= 1)
            {
                step = 3;
            }
            else
            {
                if (theFolk.destination == null  && theFolk.theEntity != null)
                {
                    //step=1;
                }
            }
        }
        else if (step == 3)
        {
            theFolk.stayPut = true;
            int count = getInventoryCount(theFolk, Block.wood.blockID);
            millChests = inventoriesFindClosest(theFolk.employedAt, 6);
            inventoriesTransferFromFolk(theFolk.inventory, millChests, new ItemStack(Block.wood));
            pay = (float)count * 0.03f;
            ModSimukraft.states.credits -= pay;
            ModSimukraft.sendChat(theFolk.name + " has delivered " + count + " logs at the lumbermill");
            theStage = Stage.SCANFORTREE;
            step = 1;
        }
    }

    @Override
    public void onArrivedAtWork()
    {
        int dist = 0;
        dist = theFolk.location.getDistanceTo(theFolk.employedAt);

        if (dist <= 1)
        {
            theFolk.action = FolkAction.ATWORK;
            theFolk.stayPut = true;
            theFolk.statusText = "I'm a lumberjack, and I'm ok";
            theStage = Stage.ARRIVEDATMILL;
        }
        else
        {
            theFolk.gotoXYZ(theFolk.employedAt, null);
        }
    }

    private void pickUpSaplings()
    {
        if (!theFolk.isSpawned())
        {
            return;
        }

        List list1 = jobWorld.getEntitiesWithinAABBExcludingEntity(
                         theFolk.theEntity,
                         AxisAlignedBB.getBoundingBox(theFolk.theEntity.posX, theFolk.theEntity.posY, theFolk.theEntity.posZ
                                 , theFolk.theEntity.posX + 1.0D, theFolk.theEntity.posY + 1.0D, theFolk.theEntity.posZ + 1.0D)
                         .expand(3D, 4D, 3D));
        Iterator iterator1 = list1.iterator();

        if (!list1.isEmpty())
        {
            do
            {
                if (!iterator1.hasNext())
                {
                    break;
                }

                Entity entity1 = (Entity) iterator1.next();

                if (!(entity1 instanceof EntityItem))
                {
                    continue;
                }

                EntityItem entityitem = (EntityItem) entity1;
                ItemStack is = entityitem.getEntityItem();

                try
                {
                    int ID = is.getItem().itemID;

                    if (ID == 6)
                    {
                        theFolk.inventory.add(new ItemStack(Block.sapling, is.getItemDamage(), 1));
                        entityitem.setDead();
                    }
                }
                catch (Exception e) {}
            }
            while (true);
        }
    }

    private void plantSapling(ItemStack is)
    {
        if (theFolk.isSpawned())
        {
            if (jobWorld.getBlockId((int)theFolk.theEntity.posX, (int)theFolk.theEntity.posY, (int)theFolk.theEntity.posZ) == 0)
            {
                jobWorld.setBlock((int)theFolk.theEntity.posX, (int)theFolk.theEntity.posY, (int)theFolk.theEntity.posZ,
                                  is.itemID, is.getItemDamage(), 0x03);
            }
        } else {
        	jobWorld.setBlock(theFolk.location.x.intValue(),theFolk.location.y.intValue(),theFolk.location.z.intValue()
        			,Block.sapling.blockID,0,0x03);
        }
    }
}
*/