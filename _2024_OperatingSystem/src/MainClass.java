
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;

public class MainClass {

	public static void main(String[] args) {
		
		ArrayList<Process> p;
		
		/*
		p = new ArrayList<Process>();
		p.add(new Process(1,0,6,0));
		p.add(new Process(2,3,3,0));
		p.add(new Process(3,6,3,0));
		
		p.add(new Process(2,0,10,4));
		p.add(new Process(1,1,1,5));
		p.add(new Process(3,2,6,4));
		p.add(new Process(4,4,11,6));
		p.add(new Process(5,5,10,1));
		p.add(new Process(6,7,12,0));
		*/

		p = randomicProcessGenerator(1, 50);
		//p = BigProcess(50);
		for (int i = 0; i < p.size(); i++)
			System.out.println("p.add(new Process(" + p.get(i).processID
					+ "," + p.get(i).arriveTime
					+ "," + p.get(i).burstTime
					+ "," + p.get(i).priority + "));");
		System.out.println("-------- BackupLine -----------");
		
		EnumSet<PrintType> printType = EnumSet.noneOf(PrintType.class);
		//printType.add(PrintType.DETAILED_INFO);
		printType.add(PrintType.SUMMARY_INFO);
		//printType.add(PrintType.PROCESS_DETAILED_INFO);
		
		printResult(RoundRobin.SchedulingAlgorithm.Run(new ArrayList<Process>(p), new ArrayList<Result>()), p.size(), printType);
		printResult(SJF.SchedulingAlgorithm.Run(new ArrayList<Process>(p), new ArrayList<Result>()), p.size(), printType);
		printResult(SchedulingAlgorithm.Run(p), p.size(), printType);
		printResult(PriorityScheduling.Run(p, true), p.size(), printType);
		printResult(NewScheduling.Run(p, 6), p.size(), printType);
	}
	
	static ArrayList<Process> BigProcess(int size) {
		Random r = new Random();
		
		ArrayList<Process> jobs = new ArrayList<Process>(size);
		int arrivalTime = 0;
		int processID = 1;
		while (size-- > 0) {
			jobs.add(new Process(processID++, arrivalTime, r.nextInt(60, 80), r.nextInt(30)));
			arrivalTime += r.nextInt(20);
		}
		
		return jobs;
	}
	
	static ArrayList<Process> randomicProcessGenerator(int seed, int size) {
		Random r = new Random();
		long longSeed = ((long)seed << 32) + seed;  
		long newSeed = r.nextLong() + longSeed;
		newSeed ^= r.nextInt();
		newSeed -= seed;
		newSeed += r.nextLong();
		r.setSeed(newSeed);
		
		ArrayList<Process> jobs = new ArrayList<Process>(size);
		int arrivalTime = 0;
		int processID = 1;
		while (size-- > 0) {
			jobs.add(new Process(processID++, arrivalTime, r.nextInt(50) + 1, r.nextInt(30)));
			arrivalTime += r.nextInt(20);
		}
		
		return jobs;
	}

	enum PrintType {
		DETAILED_INFO,
		SUMMARY_INFO,
		PROCESS_DETAILED_INFO;
	}
	
	static void printResult(ArrayList<Result> result, int itemsCount, EnumSet<PrintType> printType) {
		
		if (printType.contains(PrintType.DETAILED_INFO))
			for (Result r : result)
				System.out.println("ID : " + r.processID + " startP : " + r.startP + " Burst : " + r.burstTime + " waiting : " + r.waitingTime);
		
		if (printType.contains(PrintType.PROCESS_DETAILED_INFO)) {

			Result[] processResult = new Result[itemsCount];
			for (int i = 0; i < itemsCount; i++)
				processResult[i] = new Result(i+1, -1, 0, 0);
			
			for (Result r : result) {
				processResult[r.processID - 1].burstTime += r.burstTime;
				processResult[r.processID - 1].waitingTime += r.waitingTime;
			}

			System.out.println("프로세스별 실행시간, 대기시간 : ");
			for (Result r : processResult)
				System.out.println("PID : " + r.processID
						+ ", TurnaroundTime : " + (r.burstTime + r.waitingTime)
						+ ", WaitingTime : " + r.waitingTime);
		}
		
		if (printType.contains(PrintType.SUMMARY_INFO)) {
			int totalWaitingTime = 0;
			int totalBurstTime = 0;
			for (Result r : result) {
				totalWaitingTime += r.waitingTime;
				totalBurstTime += r.burstTime;
			}
			
			System.out.println("전체 실행시간 : " + (result.get(result.size()-1).startP + result.get(result.size()-1).burstTime)
					+ ", 평균 대기시간 : " + (totalWaitingTime / (double)itemsCount)
					+ ", 평균 실행시간 : " + ((totalWaitingTime + totalBurstTime) / (double)itemsCount));
		}
		System.out.println("======================");
	}
}
