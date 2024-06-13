package secondProject;

public class Tool {
	public double getAverageWaitingTime(Result[] result) {
		long totalWaitingTime = 0;
		for (Result r : result)
			totalWaitingTime += r.waitingTime;
		return totalWaitingTime / (double)result.length;
	}
	
	public double getSTDEVWaitingTime(Result[] result) {
		double value = 0.0;
		double avg = getAverageWaitingTime(result);
		
		for (Result r : result)
			value += Math.pow((r.waitingTime-avg), 2);
		return Math.sqrt(value / result.length); 
	}
	
	public double getAverage(int[] data) {
		long total = 0;
		for (int r : data)
			total += r;
		return total / (double)data.length;
	}
	
	public double getSTDEV(int[] data) {
		double value = 0.0;
		double avg = getAverage(data);
		
		for (int r : data)
			value += Math.pow((r-avg), 2);
		return Math.sqrt(value / data.length); 
	}
	
	public void printResult(Result[] results) {
		for (Result r : results)
			System.out.println(r.cylinder + " : " + r.waitingTime + " Time");
	}
}
