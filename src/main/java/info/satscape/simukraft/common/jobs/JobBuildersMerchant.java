/*package info.satscape.simukraft.common.jobs;

import info.satscape.simukraft.ModSimukraft;
import info.satscape.simukraft.common.FolkData;
import info.satscape.simukraft.common.FolkData.FolkAction;
import info.satscape.simukraft.common.jobs.Job.Vocation;
import info.satscape.simukraft.common.jobs.JobMiner.Stage;

import java.io.Serializable;

public class JobBuildersMerchant extends Job implements Serializable
{
    private static final long serialVersionUID = 1177112214324279141L;

    public Vocation vocation = null;
    public FolkData theFolk = null;
    public Stage theStage;
    transient public int runDelay = 1000;
    transient public long timeSinceLastRun = 0;

    public JobBuildersMerchant() {}

    public JobBuildersMerchant(FolkData folk)
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

    public void resetJob()
    {
        theStage = Stage.IDLE;
    }

    public enum Stage
    {
        IDLE, INSTORE;
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

        if (theStage == Stage.INSTORE)
        {
            runDelay = 10000;
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
        else if (theStage == Stage.INSTORE)
        {
            theFolk.statusText = "Serving customers";
            theFolk.updateLocationFromEntity();
            double dist = theFolk.location.getDistanceTo(theFolk.employedAt);

            if (dist > 5 && theFolk.destination == null)
            {
                theFolk.gotoXYZ(theFolk.employedAt, null);
            }

            if (dist <= 5)
            {
                theFolk.stayPut = true;
            }
        }
    }

    @Override
    public void onArrivedAtWork()
    {
        int dist = 0;
        theFolk.updateLocationFromEntity();
        dist = theFolk.location.getDistanceTo(theFolk.employedAt);

        if (dist <= 1)
        {
            theFolk.action = FolkAction.ATWORK;
            theFolk.stayPut = true;
            theFolk.statusText = "Arrived at the store";
            theStage = Stage.INSTORE;
        }
        else
        {
            theFolk.gotoXYZ(theFolk.employedAt, null);
        }
    }
}
*/