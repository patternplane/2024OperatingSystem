package secondProject;

public class S_SSTF implements DiskScheduler {
	private final int INIT_HEAD_POSITION;
	private int headPos;
	
	public S_SSTF(int InitHeadPosition) {
		this.INIT_HEAD_POSITION = InitHeadPosition;
	}

	public Result[] run(DiskRequest[] requests, int startPos) {
		headPos = startPos;//new java.util.Random().nextInt(0,200);// headPos = INIT_HEAD_POSITION;

		java.util.ArrayList<Result> result = new java.util.ArrayList<Result>();

		java.util.LinkedList<QueueElement> waitingQueue = new java.util.LinkedList<QueueElement>();
		int nextRequestIdx = 0;
		int runTime = 0;

		while (true) {
			while (nextRequestIdx < requests.length && runTime >= requests[nextRequestIdx].arrivalTime) {
				waitingQueue.add(new QueueElement(requests[nextRequestIdx].arrivalTime, requests[nextRequestIdx].cylinder)); // accepted Time = runTime
				nextRequestIdx++;
			}
			if (waitingQueue.isEmpty()) {
				if (nextRequestIdx >= requests.length)
					break;
				else
					runTime = requests[nextRequestIdx].arrivalTime;
			} else {
				QueueElement currentItem = popNext(waitingQueue);
				runTime += Math.abs(currentItem.cylinder - headPos);
				headPos = currentItem.cylinder;
				result.add(new Result(currentItem.cylinder, runTime - currentItem.acceptedTime));
			}
		}

		return result.toArray(new Result[result.size()]);
	}

	private QueueElement popNext(java.util.LinkedList<QueueElement> list) {
		QueueElement minItem = list.getFirst();
		for (QueueElement r : list)
			if (Math.abs(minItem.cylinder - headPos) > Math.abs(r.cylinder - headPos))
				minItem = r;
		list.remove(minItem);
		return minItem;
	}
}
