package secondProject;

public class RequestGenerator {
	public DiskRequest[] randomGenerate(int size) {
		java.util.Random r = new java.util.Random();
		//r.setSeed(50015010);
		
		DiskRequest[] requests = new DiskRequest[size];
		int arrivalTime = 0;
		for (int i = 0; i < size; i++) {
			requests[i] = new DiskRequest(arrivalTime, r.nextInt(0, 200));
			arrivalTime += r.nextInt(0, 50);
		}
		
		return requests;
	}
	
	public DiskRequest[] randomGenerate_SameArrival(int size) {
		java.util.Random r = new java.util.Random();
		//r.setSeed(50015010);
		
		DiskRequest[] requests = new DiskRequest[size];
		for (int i = 0; i < size; i++) {
			requests[i] = new DiskRequest(0, r.nextInt(0, 200));
		}
		
		return requests;
	}
	
	public DiskRequest[] randomGenerate_density(int size) {
		java.util.Random r = new java.util.Random();
		//r.setSeed(50015010);
		
		DiskRequest[] requests = new DiskRequest[size];
		for (int i = 0; i < size; i++) {
			requests[i] = new DiskRequest(0, (r.nextInt(0, 100)<5 ? ((r.nextInt(0, 2) == 0) ? r.nextInt(0, 40) :r.nextInt(160, 200)) : r.nextInt(80, 120)));
		}
		
		return requests;
	}
	
	public DiskRequest[] randomGenerate_center(int size) {
		java.util.Random r = new java.util.Random();
		//r.setSeed(50015010);
		
		DiskRequest[] requests = new DiskRequest[size];
		for (int i = 0; i < size; i++) {
			requests[i] = new DiskRequest(0,  r.nextInt(80, 120));
		}
		
		return requests;
	}

	public DiskRequest[] randomGenerate_outer(int size) {
		java.util.Random r = new java.util.Random();
		//r.setSeed(50015010);
		
		DiskRequest[] requests = new DiskRequest[size];
		for (int i = 0; i < size; i++) {
			requests[i] = new DiskRequest(0,  (r.nextInt(0, 2) == 0 ? r.nextInt(0, 20) : r.nextInt(180, 200)));
		}
		
		return requests;
	}

	public DiskRequest[] randomGenerate_near(int size) {
		java.util.Random r = new java.util.Random();
		//r.setSeed(50015010);
		
		DiskRequest[] requests = new DiskRequest[size];
		int value = 100;
		for (int i = 0; i < size; i++) {
			if (r.nextInt(0, 100) < 30) 
				requests[i] = new DiskRequest(0,  (r.nextInt(0, 2) == 0 ? r.nextInt(0, 20) : r.nextInt(180, 200)));
			else {
				value += (r.nextInt(0, 2) == 0 ? -1 : 1) * r.nextInt(4,7);
				if (value < 0)
					value = 30;
				else if (value >= 200)
					value = 170;
				requests[i] = new DiskRequest(0,  value);
			}
		}
		
		return requests;
	}
}
