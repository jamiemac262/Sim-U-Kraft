/*package info.satscape.simukraft.common.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import info.satscape.simukraft.ModSimukraft;
import info.satscape.simukraft.common.FolkData;
import info.satscape.simukraft.common.CommonProxy.V3;
import info.satscape.simukraft.common.FolkData.FolkAction;
import info.satscape.simukraft.common.FolkData.GotoMethod;
import info.satscape.simukraft.common.jobs.Job.Vocation;
import info.satscape.simukraft.common.jobs.JobLivestockFarmer.Stage;

public class JobDairyFarmer extends Job {
	
	public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    transient public int runDelay = 1000;
    transient public long timeSinceLastRun = 0;
    private ArrayList<IInventory> farmChests = new ArrayList<IInventory>();
    private String[] cowNames=new String[6];
    
    
	public JobDairyFarmer(FolkData folk) {
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
            theFolk.gotoXYZ(theFolk.employedAt, GotoMethod.BEAM);
        }
	}
	
	private void createCowNames() {
		for(int i=0;i<6;i++) {
			cowNames[i]=FolkData.generateName(1, true, "");
		}
	}
	
	 @Override
	    public void onUpdate()
	    {
		 if (cowNames[0]==null || cowNames[0].contentEquals("")) {
			 createCowNames();
		 }
		 
		 super.onUpdate();

	        if (!ModSimukraft.isDayTime())
	        {
	            theStage = Stage.IDLE;
	        }

	        super.onUpdateGoingToWork(theFolk);

	        if (theStage == Stage.WAITINGFORMILKING)
	        {
	            runDelay = 40000;
	        } else {
	        	runDelay=10000;
	        }

	        if (System.currentTimeMillis() - timeSinceLastRun < runDelay)
	        {
	            return;
	        }

	        timeSinceLastRun = System.currentTimeMillis();

	        // ////////////////IDLE
	        if (theStage == Stage.IDLE && ModSimukraft.isDayTime())
	        {
	        }
	        else if (theStage == Stage.ARRIVEDATFARM)
	        {
	            stageArrived();
	        }
	        else if (theStage == Stage.WAITINGFORMILKING)
	        {
	            stageWaiting();
	        }
	        else if (theStage == Stage.MILKING)
	        {
	            stageMilking();
	        }
	        else if (theStage== Stage.STORINGMILK) {
	        	stageStoringMilk();
	        }
	        else if (theStage == Stage.CANTWORK)
	        {
	            stageCantWork();
	        }
	    }

	private void stageArrived() {
        vocation = theFolk.vocation;
        theStage = Stage.WAITINGFORMILKING;
        theFolk.statusText = "Warming up cow's udders";
        int count = 0;

        count = getAnimalCountInPen(theFolk.employedAt, EntityCow.class);

        if (count < 6)
        {
            spawnCows(theFolk.employedAt, 6 - count);
        }
	}
	
	private void stageWaiting() {
        theFolk.updateLocationFromEntity();
        double dist = theFolk.location.getDistanceTo(theFolk.employedAt);

        if (dist > 10)
        {
            theFolk.beamMeTo(theFolk.employedAt);
        }
        theFolk.statusText="Sterilizing empty bucket";
        theStage=Stage.MILKING;
	}
	 
	private void stageMilking() {
		Random rand=new Random();
		int c=rand.nextInt(6);
		theFolk.statusText="Milking "+ cowNames[c] +" the cow";
		theStage=Stage.STORINGMILK;
		theFolk.isWorking=true;
	}
	
	private void stageStoringMilk() {
		theFolk.statusText="Storing Milk in refrigerated chest";
		theFolk.isWorking=false;
		farmChests = inventoriesFindClosest(theFolk.employedAt, 5);
		if (farmChests.size()>0) {
			boolean ok=inventoriesPut(farmChests,new ItemStack(Items.milk_bucket,1) , true);
			if (!ok) {
				ModSimukraft.sendChat(theFolk.name+"'s dairy farm chests are full of milk!");
				theFolk.statusText = "Can't work, the chests are full";
				theStage=Stage.CANTWORK;
			} else {
				ModSimukraft.states.credits -= 0.05f;
		        theStage = Stage.WAITINGFORMILKING;
			}
		} else {
			theFolk.statusText="Who stole my dairy chests!";
			theStage=Stage.CANTWORK;
		}
		
	}
	
	private void stageCantWork() {
		//status set when stage is activated
	}
	
	
    public enum Stage
    {
        IDLE, ARRIVEDATFARM, WAITINGFORMILKING, MILKING, STORINGMILK, CANTWORK;
    }

	@Override
	public void onArrivedAtWork() {
        int dist = 0;
        dist = theFolk.location.getDistanceTo(theFolk.employedAt);

        if (dist <= 1)
        {
            theFolk.action = FolkAction.ATWORK;
            theFolk.stayPut = true;
            theFolk.statusText = "Arrived at the farm";
            theStage = Stage.ARRIVEDATFARM;
        }
        else
        {
            theFolk.gotoXYZ(theFolk.employedAt, null);
        }
	}

	@Override
	public void resetJob() {
		theStage = Stage.IDLE;
	}
	
	

    private void spawnCows(V3 controlBox, int count)
    {
        EntityAnimal newAnimal = null;

        for (int c = 1; c <= count; c++)
        {
          
            newAnimal = new EntityCow(jobWorld);
            newAnimal.setLocationAndAngles(controlBox.x+1, controlBox.y + 1, controlBox.z, 0f, 0f);

            if (!jobWorld.isRemote)
            {
                jobWorld.spawnEntityInWorld(newAnimal);
            }
        }
    }


}
*/