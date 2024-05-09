
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

public class SchedulingAlgorithm
{
    public static ArrayList<Result> Run(ArrayList<Process> jobList)
    {
        int currentProcess = 0;
        int cpuTime = 0;
        int cpuDone = 0;

        int runTime = 0;
        
        ArrayList<Result> resultList = new ArrayList<Result>();

        Queue<ReadyQueueElement> readyQueue = new LinkedList<ReadyQueueElement>();
        int unProcessedIdx = 0;
        do
        {
            for (; unProcessedIdx < jobList.size(); unProcessedIdx++)
            {
                Process frontJob = jobList.get(unProcessedIdx);
                if (frontJob.arriveTime == runTime)
                {
                    readyQueue.add(new ReadyQueueElement(frontJob.processID, frontJob.burstTime, 0));
                }
                else
                {
                    break;
                }
            }

            if (currentProcess == 0)
            {
                if (readyQueue.size() != 0)
                {
                    ReadyQueueElement rq = readyQueue.remove();
                    resultList.add(new Result(rq.processID, runTime, rq.burstTime, rq.waitingTime));
                    cpuDone = rq.burstTime;
                    cpuTime = 0;
                    currentProcess = rq.processID;
                }
            }
            else
            {
                if (cpuTime == cpuDone)
                {
                    currentProcess = 0;
                    continue;
                }
            }

            cpuTime++;
            runTime++;

            for(ReadyQueueElement rm : readyQueue)
            {
                rm.waitingTime++;
            }

        } while (unProcessedIdx < jobList.size() || readyQueue.size() != 0 || currentProcess != 0);

        return resultList;
    }
}
