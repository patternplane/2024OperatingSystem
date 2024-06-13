package secondProject;

public class S_CLOOK implements DiskScheduler {
	private final int INIT_HEAD_POSITION;
	private int headPos;

	public S_CLOOK(int InitHeadPosition) {
		this.INIT_HEAD_POSITION = InitHeadPosition;
	}

	public Result[] run(DiskRequest[] requests,int startPos) {
		headPos = startPos;//new java.util.Random().nextInt(0,200);//headPos = INIT_HEAD_POSITION;

		java.util.ArrayList<Result> result = new java.util.ArrayList<Result>();

		java.util.TreeSet<QueueElement> waitingQueue = new java.util.TreeSet<QueueElement>((o1, o2) -> {
			return o1.cylinder < o2.cylinder ? -1 : (o1.cylinder > o2.cylinder ? 1 : (Integer.compare(o1.id, o2.id)));
		});
		int nextRequestIdx = 0;
		int runTime = 0;

		while (true) {
			while (nextRequestIdx < requests.length && runTime >= requests[nextRequestIdx].arrivalTime) {
				waitingQueue.add(new QueueElement(requests[nextRequestIdx].arrivalTime, requests[nextRequestIdx].cylinder, nextRequestIdx)); // accepted Time = runTime
				nextRequestIdx++;
			}
			if (waitingQueue.isEmpty()) {
				if (nextRequestIdx >= requests.length)
					break;
				else
					runTime = requests[nextRequestIdx].arrivalTime;
			} else {
				QueueElement currentItem = popNext(waitingQueue);
				if (currentItem != null) {
					runTime += Math.abs(currentItem.cylinder - headPos);
					headPos = currentItem.cylinder;
					result.add(new Result(currentItem.cylinder, runTime - currentItem.acceptedTime));
				} else if (!waitingQueue.isEmpty()) {
					runTime += Math.abs(waitingQueue.first().cylinder - headPos)/4; // 처리없이 이동만 하므로 충분히 짧을테니, 무시한다면??
					headPos = waitingQueue.first().cylinder;
				}
			}
		}

		return result.toArray(new Result[result.size()]);
	}

	private QueueElement popNext(java.util.TreeSet<QueueElement> list) {
		for (QueueElement r : list)
			if (headPos <= r.cylinder) {
				list.remove(r);
				return r;
			}
		return null;
	}
}
