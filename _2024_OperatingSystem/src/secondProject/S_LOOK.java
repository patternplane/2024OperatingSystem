package secondProject;

public class S_LOOK implements DiskScheduler {
	private final int INIT_HEAD_POSITION;
	private int headPos;
	private HeadDirection headDirection;

	public S_LOOK(int InitHeadPosition, HeadDirection InitHeadDirection) {
		this.INIT_HEAD_POSITION = InitHeadPosition;
		this.headDirection = InitHeadDirection;
	}

	public Result[] run(DiskRequest[] requests,int startPos) throws Throwable {
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
					if (headDirection == HeadDirection.Increase)
						headDirection = HeadDirection.Decrease;
					else if (headDirection == HeadDirection.Decrease)
						headDirection = HeadDirection.Increase;
					else
						throw new Exception("Not Impleted Case!");
				}
			}
		}

		return result.toArray(new Result[result.size()]);
	}

	private QueueElement popNext(java.util.TreeSet<QueueElement> list) throws Throwable {
		if (headDirection == HeadDirection.Increase) {
			for (QueueElement r : list)
				if (headPos <= r.cylinder) {
					list.remove(r);
					return r;
				}
			return null;
		} else if (headDirection == HeadDirection.Decrease) {
			java.util.Iterator<QueueElement> desList = list.descendingIterator();
			while (desList.hasNext()) {
				QueueElement r = desList.next();
				if (r.cylinder <= headPos) {
					list.remove(r);
					return r;
				}
			}
			return null;
		} else {
			throw new Exception("Not Impleted Case!");
		}
	}
}
