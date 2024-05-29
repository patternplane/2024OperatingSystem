// Contributed By Others
// Scheduling Algorithm

import java.util.ArrayList;
import java.util.List;

public class RoundRobin {
    public static ArrayList<Result> Run(List<Process> jobList, int timeSlice) {
    	ArrayList<Result> resultList = new ArrayList<Result>();
        
        int processingIdx = 0;
        int currentProcess = 0;
        int cpuTime = 0;
        int cpuDone = 0;

        int runTime = 0;
        ReadyQueueElement rq = null;
        List<ReadyQueueElement> readyQueue = new ArrayList<>();

        do {
            while (jobList.size() != processingIdx) {
                Process frontJob = jobList.get(processingIdx);
                if (frontJob.arriveTime == runTime) {
                    readyQueue.add(new ReadyQueueElement(frontJob.processID, frontJob.burstTime, 0));
                    processingIdx++;

                } else {
                    break;
                }
            }

            if (currentProcess == 0) {
                if (readyQueue.size() != 0) {
                    rq = readyQueue.get(0);
                    cpuDone = rq.burstTime;
                    cpuTime = 0;
                    currentProcess = rq.processID;
                    readyQueue.remove(0);
                }
            } else {
                if (cpuTime == cpuDone) {
                    currentProcess = 0;
                    resultList.add(new Result(rq.processID, runTime - cpuDone, cpuDone, rq.waitingTime));
                    continue;
                }
                if (cpuTime == timeSlice) {
                    resultList.add(new Result(rq.processID, runTime - cpuTime, cpuTime, rq.waitingTime));
                    readyQueue.add(new ReadyQueueElement(rq.processID, cpuDone - cpuTime, 0));
                    rq = readyQueue.get(0);
                    cpuDone = rq.burstTime;
                    cpuTime = 0;
                    currentProcess = rq.processID;
                    readyQueue.remove(0);
                }
            }
            cpuTime++;
            runTime++;

            for (ReadyQueueElement readyQueueElement : readyQueue) {
                readyQueueElement.waitingTime++;
            }

        } while (jobList.size() != processingIdx || readyQueue.size() != 0 || currentProcess != 0);

        return resultList;
    }
}