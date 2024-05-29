
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;
import java.util.Scanner;

public class MainClass {
	
	// Used when read Jobs From Extern File
	static ArrayList<Process> readFile() {
		try {
			ArrayList<Process> result = new ArrayList<Process>();
			
			java.io.FileReader f = new java.io.FileReader(".\\input.txt");
			java.io.BufferedReader br = new java.io.BufferedReader(f);
			String line;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(" ");
				result.add(
						new Process(
								Integer.parseInt(tokens[1]),
								Integer.parseInt(tokens[2]),
								Integer.parseInt(tokens[3]),
								Integer.parseInt(tokens[4])));
			}
			
			br.close();
			f.close();
			
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	// Used When Export ResultData To File
	static void outToFile(ArrayList<Result> result) {
		try {            
			FileWriter fw = new FileWriter(new File(".\\output.txt"));            
			BufferedWriter writer = new BufferedWriter(fw);
			
			for (Result r : result) {
				writer.write(Integer.toString(r.processID));
				writer.write(' ');
				writer.write(Integer.toString(r.startP));
				writer.write(' ');
				writer.write(Integer.toString(r.burstTime));
				writer.write(' ');
				writer.write(Integer.toString(r.waitingTime));
				writer.write('\n');
			}
			
			writer.close();
			fw.close();
		} catch (Exception e) {
			
		}
	}

	public static void main(String[] args) {
		
		ArrayList<Process> p;
		p = new ArrayList<Process>();
		
		// Test 1
		p.add(new Process(1,0,6,0));
		p.add(new Process(2,3,3,0));
		p.add(new Process(3,6,3,0));
		
		
		/* Test 2
		p.add(new Process(2,0,10,4));
		p.add(new Process(1,1,1,5));
		p.add(new Process(3,2,6,4));
		p.add(new Process(4,4,11,6));
		p.add(new Process(5,5,10,1));
		p.add(new Process(6,7,12,0));*/

		/* Test 3
		p.add(new Process(2,0,10,1)); 
		p.add(new Process(1,1,7,1)); 
		p.add(newProcess(3,2,6,1)); 
		p.add(new Process(4,4,11,1)); 
		p.add(newProcess(5,5,10,1));
		p.add(new Process(6,7,12,0));
		 */

		// Test 4
		//JobGenerator j = new JobGenerator();
		//p = j.intervaledRandomicProcessGenerator(10, 50, 100, 500);
		// 1. 오름차순 burst j.OrderedBurstProcessGenerator(1000, 5, 2, +5);
		// 2. 내림차순 burst j.OrderedBurstProcessGenerator(1000, 5, 5005, -5);
		// 3. 오름차순 priority j.OrderedPriorityProcessGenerator(1000, 5, true);
		// 4. 내림차순 priority j.OrderedPriorityProcessGenerator(1000, 5, false);
		// 5. 밀도 낮은 arrival j.intervaledRandomicProcessGenerator(1000, 100, 1, 50);
		// 6. 밀도 높은 arrival j.intervaledRandomicProcessGenerator(1000, 1, 1, 50);
		// 7. 밀도 중간 arrival j.intervaledRandomicProcessGenerator(1000, 50, 1, 50);
		// 8. 무거운 burst j.intervaledRandomicProcessGenerator(1000, 50, 100, 500);
		//j.makeToFile(".\\test.txt", p);
		
		// Test 5
		//p = readFile();
		
		/* print Jobs
		for (int i = 0; i < p.size(); i++)
			System.out.println("p.add(new Process(" + p.get(i).processID
					+ "," + p.get(i).arriveTime
					+ "," + p.get(i).burstTime
					+ "," + p.get(i).priority + "));");
		System.out.println("-------- BackupLine -----------");
		*/

		// Round Robin Time Slice
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Time Slice Size :");
        int timeSlice = scanner.nextInt();
        
        // New Algorithm Time Slice
        int timeSlice2 = 3;
        
        // Priority Algorithm isPreemptive
        boolean isPreemptive = true;

		// result print type on console 
		EnumSet<PrintType> printType = EnumSet.noneOf(PrintType.class);
		//printType.add(PrintType.DETAILED_INFO); // can toggle : Show All Work Result
		printType.add(PrintType.SUMMARY_INFO); // can toggle : Show Summay Information
		printType.add(PrintType.PROCESS_DETAILED_INFO); // can toggle : Show Result per Process
        
		p.sort((p1, p2) -> Integer.compare(p1.arriveTime, p2.arriveTime));
		
		printResult(p.size(), printType, "RoundRobin", RoundRobin.Run(p, timeSlice));
		printResult(p.size(), printType, "SJF", SJF.Run(p));
		printResult(p.size(), printType, "FCFS", FCFS.Run(p));
		printResult(p.size(), printType, "Priority", PriorityScheduling.Run(p, isPreemptive));
		printResult(p.size(), printType, "New", NewScheduling.Run(p, timeSlice2));
	}

	enum PrintType {
		DETAILED_INFO,
		SUMMARY_INFO,
		PROCESS_DETAILED_INFO;
	}
	
	static ArrayList<Result> printResult(int itemsCount, EnumSet<PrintType> printType, String algoName, ArrayList<Result> result) {
		
		System.out.println("알고리즘 : " + algoName);
		
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
			long totalWaitingTime = 0;
			long totalBurstTime = 0;
			for (Result r : result) {
				totalWaitingTime += r.waitingTime;
				totalBurstTime += r.burstTime;
			}
			
			System.out.printf("전체 실행시간 : %d, 평균 대기시간 : %f, 평균 실행시간 : %f\n"
					, (result.get(result.size()-1).startP + result.get(result.size()-1).burstTime)
					, (float)(totalWaitingTime / (double)itemsCount)
					, ((totalWaitingTime + totalBurstTime) / (double)itemsCount));
		}
		System.out.println("======================");
		
		return result;
	}
}
