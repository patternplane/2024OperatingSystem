package secondProject;

public class S_FCFS implements DiskScheduler {
	private final int INIT_HEAD_POSITION;
	private int headPos;
	private final int MAX_WAITING_SIZE;

	public S_FCFS(int InitHeadPosition, int maxWaitingSize) {
		this.INIT_HEAD_POSITION = InitHeadPosition;
		this.MAX_WAITING_SIZE = maxWaitingSize;
	}

	public Result[] run(DiskRequest[] requests,int startPos) {
		headPos =  startPos;//new java.util.Random().nextInt(0,200);//INIT_HEAD_POSITION;
		
		java.util.ArrayList<Result> result = new java.util.ArrayList<Result>();
		java.util.Queue<QueueElement> waitingQueue = new java.util.LinkedList<QueueElement>();
		int nextRequestIdx = 0;
		int runTime = 0;

		while (true) {
			while (nextRequestIdx < requests.length) { // && waitingQueue.size() < MAX_WAITING_SIZE
				waitingQueue.add(new QueueElement(requests[nextRequestIdx].arrivalTime, requests[nextRequestIdx].cylinder)); // acceptedTime = runTime
				nextRequestIdx++;
			}
			if (waitingQueue.isEmpty()) {
				if (nextRequestIdx >= requests.length)
					break;
				else
					runTime = requests[nextRequestIdx].arrivalTime;
			}
			else {
				QueueElement currentItem = waitingQueue.poll();
				runTime += Math.abs(headPos - currentItem.cylinder);
				headPos = currentItem.cylinder;
				result.add(new Result(currentItem.cylinder, runTime - currentItem.acceptedTime));
			}
		}

		return result.toArray(new Result[result.size()]);
	}
}
