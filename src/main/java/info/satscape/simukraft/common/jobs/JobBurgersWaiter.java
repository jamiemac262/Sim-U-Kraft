package info.satscape.simukraft.common.jobs;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import info.satscape.simukraft.ModSimukraft;
import info.satscape.simukraft.common.Building;
import info.satscape.simukraft.common.FolkData;
import info.satscape.simukraft.common.CommonProxy.V3;
import info.satscape.simukraft.common.FolkData.FolkAction;
import info.satscape.simukraft.common.jobs.Job.Vocation;
import info.satscape.simukraft.common.jobs.JobBurgersManager.Stage;

public class JobBurgersWaiter extends Job {

	/*
	Specialblock meta values for this job:
	0=Raw materials chest waypoint
	1=Frycook waypoint
	2=Waiter waypoint
	*/
	public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    public int runDelay = 1000;
    private long timeSinceLastRun=0;
    private Building theStore=null;
    
	public JobBurgersWaiter(FolkData folk) {
      theFolk = folk;

        if (theStage == null)
        {
            theStage = Stage.IDLE;
        }

        if (theFolk == null)
        {
            return;   // is null when first employing
        }

        if (theFolk.destination == null)
        {
            theFolk.gotoXYZ(theFolk.employedAt, null);
        }
	}

	 public enum Stage
	    {
	        IDLE, ARRIVEDATSTORE, SERVING
	    }
	    
	    @Override
	    public void onUpdate()
	    {
	        super.onUpdate();
	        if(theStore ==null) {
	        	theStore=Building.getBuilding(theFolk.employedAt);
	        }
	        if (theStore==null) {
	        	//wait until building has loaded
	        	return;
	        }

	        if (!ModSimukraft.isDayTime())
	        {
	            theStage = Stage.IDLE;
	        }

	        super.onUpdateGoingToWork(theFolk);

	        if (theStage == Stage.ARRIVEDATSTORE)
	        {
	            theFolk.action = FolkAction.ATWORK;
	            runDelay = 11000;
	        } else if (theStage==Stage.SERVING) {
	        	runDelay=45000;
	        }
	        else
	        {
	            runDelay = 5000;
	        }
	        if (System.currentTimeMillis() - timeSinceLastRun < runDelay)
	        {
	            return;
	        }
	      

	        // ////////////////IDLE
	        if (theStage == Stage.IDLE && ModSimukraft.isDayTime())
	        {
	        }
	        else if (theStage == Stage.ARRIVEDATSTORE)
	        {
	        	theStage=Stage.SERVING;
	        	theFolk.statusText="Serving customers";
	        	
	        }else if (theStage==Stage.SERVING) {
	        	stageServing();
	        }


	        if (!ModSimukraft.isDayTime())
	        {
	            theStage = Stage.IDLE;
	        }

	        timeSinceLastRun = System.currentTimeMillis();
	    }

	    
		private void stageServing() {
			ArrayList<V3> serve=theStore.getSpecialBlocks(2);
			if (serve.isEmpty()) { return; }
			ArrayList<IInventory> theChests=inventoriesFindClosest(serve.get(0), 3);
			if (theChests.isEmpty()) { return; }
			
			theFolk.gotoXYZ(serve.get(0), null);
            try {theFolk.destination.destinationAcc=0.3d;}catch(Exception e){} //NPE if already there
            
			ItemStack is=inventoriesGet(theChests, null, true, false,-1); //doesn't actually pull from inventory
			if(is==null) {
				theFolk.statusText="Wishing we had more customers";
				return;
			} else {
				if (is.getItem()== ModSimukraft.itemFood) {
					is=new ItemStack(is.getItem(),1,is.getItemDamage());
					inventoriesGet(theChests, is, false, true,-1);
					theFolk.statusText="Just sold "+is.getDisplayName();
					int r=new Random().nextInt(ModSimukraft.theFolks.size()-1);
					FolkData folk=ModSimukraft.theFolks.get(r);
					if (folk.levelFood<10) {folk.levelFood++;}
					folk.saveThisFolk();
					ModSimukraft.log.info("JobBurgersWaiter: Just fed "+folk.name);
					ModSimukraft.states.credits -=0.45;
				} else {
					theFolk.statusText="Who put "+is.getDisplayName()+" in my chest, folks can't eat that!";
				}
			}
			
			
		}


		@Override
		public void onArrivedAtWork() {
	        int dist = 0;
	        dist = theFolk.location.getDistanceTo(theFolk.employedAt);

	        if (dist <= 1)
	        {
	            theFolk.action = FolkAction.ATWORK;
	            theFolk.stayPut = true;
	            theFolk.statusText = "Arrived at the store";
	            theStage = Stage.ARRIVEDATSTORE;
	            ArrayList<V3> back=theStore.getSpecialBlocks(2);
	            if (!back.isEmpty()) {
	            	theFolk.gotoXYZ(back.get(0), null);
	            }

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

}
