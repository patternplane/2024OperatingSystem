// Scheduling Algorithm

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Iterator;

class ReadyQueueElement_New extends ReadyQueueElement implements Comparable<ReadyQueueElement_New> {
	public int remainingTime;
	public int arrivalTime;
	
	public ReadyQueueElement_New (int processID, int burstTime, int arrivalTime, int waitingTime) {
		super(processID, burstTime, waitingTime);
		this.remainingTime = burstTime;
		this.arrivalTime = arrivalTime;
	}

	public int compareTo(ReadyQueueElement_New o) {
		int result1 = Integer.compare(this.remainingTime, o.remainingTime);
		if (result1 == 0)
			return Integer.compare(this.arrivalTime, o.arrivalTime);
		else
			return result1; 
	}
}

public class NewScheduling {
    public static ArrayList<Result> Run(ArrayList<Process> jobList, int timeQuantum) {
    	if (timeQuantum < 1) {
    		System.out.println("Invalid Time Quantum! : " + timeQuantum);
    		return null;
    	}
    	
    	ArrayList<Result> resultList = new ArrayList<Result>(jobList.size());
    	
    	PriorityQueue<ReadyQueueElement_New> readyQueue = new PriorityQueue<ReadyQueueElement_New>();
    	Queue<ReadyQueueElement_New> newQueue = new LinkedList<ReadyQueueElement_New>();
    	Iterator<Process> jobItr = jobList.iterator();
    	Process nextJob = jobItr.next();
    	
    	int runTime = 0;
    	int startTime = 0;
    	int inRunningTime = 0;
    	ReadyQueueElement_New currentProcess = null;
        
    	while(true) {
    		while (nextJob != null && nextJob.arriveTime <= runTime) {
    			newQueue.add(
    					new ReadyQueueElement_New(
    							nextJob.processID, 
    							nextJob.burstTime,
    							nextJob.arriveTime,
    							runTime - nextJob.arriveTime));
    			nextJob = (jobItr.hasNext() ? jobItr.next() : null);
    		}
    		if (readyQueue.isEmpty() && currentProcess == null) {
    			if (newQueue.isEmpty()) {
	    			if (nextJob == null)
	    				break;
	    			else
	    				runTime = nextJob.arriveTime;
    			}
    			else
    				while (!newQueue.isEmpty())
    					readyQueue.add(newQueue.remove());
    		}
    		else {
    			if (currentProcess != null) {
    				Result lastResult = (resultList.isEmpty() ? null : resultList.get(resultList.size() - 1));
    				if (lastResult != null && lastResult.processID == currentProcess.processID) {
    					lastResult.burstTime += inRunningTime;
    					lastResult.waitingTime += currentProcess.waitingTime;
    				}
    				else
    					resultList.add(
    						new Result(
    								currentProcess.processID,
    								startTime,
    								inRunningTime,
    								currentProcess.waitingTime));
    				if (currentProcess.remainingTime != 0) {
        				currentProcess.waitingTime = 0;
    					newQueue.add(currentProcess);
    				}
				}
				inRunningTime = 0;
				startTime = runTime;
				currentProcess = readyQueue.poll();
				
    			if (currentProcess != null) {
					int processingTime;
					if (readyQueue.isEmpty() && newQueue.isEmpty()) {
						if (nextJob == null)
							processingTime = currentProcess.remainingTime;
						else
                            processingTime = Math.min(currentProcess.remainingTime, currentProcess.remainingTime <= timeQuantum * 2 ? currentProcess.remainingTime : timeQuantum);
					}
					else 
						processingTime = currentProcess.remainingTime <= timeQuantum*2 ? currentProcess.remainingTime : timeQuantum;
					
	    			for (ReadyQueueElement_New waitingProcess : readyQueue)
	    				waitingProcess.waitingTime += processingTime;
	    			for (ReadyQueueElement_New waitingProcess : newQueue)
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
