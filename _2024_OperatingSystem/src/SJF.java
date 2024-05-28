// Contributed By Others
// Scheduling Algorithm

import java.util.ArrayList;
import java.util.List;

public class SJF {
    public static ArrayList<Result> Run(ArrayList<Process> jobList) {
    	ArrayList<Result> resultList = new ArrayList<Result>();
    	int processingIdx = 0;
        int currentProcess = 0;
        int cpuTime = 0;
        int cpuDone = 0;
        int runTime = 0;

        List<ReadyQueueElement> readyQueue = new ArrayList<>();

        do {
            while (!jobList.isEmpty()) {
                Process frontJob = jobList.get(processingIdx);
                if (frontJob.arriveTime == runTime) {
                    readyQueue.add(new ReadyQueueElement(frontJob.processID, frontJob.burstTime, 0));
                    processingIdx++;
                    readyQueue.sort((o1, o2) -> {
                        if (o1.burstTime > o2.burstTime) return 1;
                        else if (o1.burstTime < o2.burstTime) return -1;
                        else return Integer.compare(o1.processID, o2.processID);
                    });
                } else {
                    break;
                }
            }

            if (currentProcess == 0) {
                if (!readyQueue.isEmpty()) {
                    ReadyQueueElement rq = readyQueue.get(0);
                    resultList.add(new Result(rq.processID, runTime, rq.burstTime, rq.waitingTime));
                    cpuDone = rq.burstTime;
                    cpuTime = 0;
                    currentProcess = rq.processID;
                    readyQueue.remove(0);
                }
            } else {
                if (cpuTime == cpuDone) {
                    currentProcess = 0;
                    continue;
                }
            }
            cpuTime++;
            runTime++;

            for (ReadyQueueElement readyQueueElement : readyQueue) {
                readyQueueElement.waitingTime++;
            }

        } while (!jobList.isEmpty() || !readyQueue.isEmpty() || currentProcess != 0);

        return resultList;
    }
}
