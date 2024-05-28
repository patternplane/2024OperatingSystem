// Scheduling Algorithm

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Iterator;

class ReadyQueueElement_Priority extends ReadyQueueElement implements Comparable<ReadyQueueElement_Priority> {
	public int priority;
	public int arrivalTime;
	public int remainingTime;
	
	public ReadyQueueElement_Priority (int processID, int burstTime, int priority, int arrivalTime) {
		super(processID, burstTime, 0);
		this.priority = priority; 
		this.arrivalTime = arrivalTime;
		this.remainingTime = burstTime;
	}

	public int compareTo(ReadyQueueElement_Priority o) {
		int result1 = Integer.compare(o.priority, this.priority);
		if (result1 == 0)
			return Integer.compare(this.arrivalTime, o.arrivalTime);
		return result1;
	}
}

public class PriorityScheduling {
    public static ArrayList<Result> Run(ArrayList<Process> jobList, boolean isPreemptive) {
    	ArrayList<Result> resultList = new ArrayList<Result>(jobList.size());
    	
    	PriorityQueue<ReadyQueueElement_Priority> readyQueue = new PriorityQueue<ReadyQueueElement_Priority>();
    	Iterator<Process> jobItr = jobList.iterator();
    	Process nextJob = jobItr.next();
    	
    	int runTime = 0;
    	int startTime = 0;
    	int inRunningTime = 0;
    	ReadyQueueElement_Priority currentProcess = null;
    	while(true) {
    		while (nextJob != null && runTime == nextJob.arriveTime) {
    			readyQueue.add(
    					new ReadyQueueElement_Priority(
    							nextJob.processID, 
    							nextJob.burstTime,
    							nextJob.priority,
    							nextJob.arriveTime));
    			nextJob = (jobItr.hasNext() ? jobItr.next() : null);
    		}
    		if (readyQueue.isEmpty() && currentProcess == null) {
    			if (nextJob == null)
    				break;
    			else
    				runTime = nextJob.arriveTime;
    		}
    		else {
    			if (currentProcess == null
    					|| currentProcess.remainingTime == 0 
    					|| (isPreemptive && !readyQueue.isEmpty() && currentProcess.priority < readyQueue.peek().priority)) {
    				if (currentProcess != null) {
    					resultList.add(
        						new Result(
        								currentProcess.processID,
        								startTime,
        								inRunningTime,
        								currentProcess.waitingTime));
	    				if (currentProcess.remainingTime != 0) {
	        				currentProcess.waitingTime = 0;
	    					readyQueue.add(currentProcess);
	    				}
    				}
    				inRunningTime = 0;
    				startTime = runTime;
    				currentProcess = readyQueue.poll();
    			}
    			else {
    				int processingTime = 
        					(nextJob != null 
        					? Math.min(currentProcess.remainingTime, nextJob.arriveTime - runTime)
    						: currentProcess.remainingTime);
        			for (ReadyQueueElement_Priority waitingProcess : readyQueue)
        				waitingProcess.waitingTime += processingTime;
        			currentProcess.remainingTime -= processingTime;
        			runTime += processingTime;
        			inRunningTime += processingTime;
    			}
    		}
    	}
    	
    	return resultList;
    }
}
