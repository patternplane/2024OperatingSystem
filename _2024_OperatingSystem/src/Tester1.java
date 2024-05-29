import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Scanner;

public class Tester1 {
	public void Test() {
ArrayList<Process> p = new ArrayList<Process>();
		
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
        scanner.close();
        
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
		
		TestSupporter ts = new TestSupporter();
		ts.printResult(p.size(), printType, "RoundRobin", RoundRobin.Run(p, timeSlice));
		ts.printResult(p.size(), printType, "SJF", SJF.Run(p));
		ts.printResult(p.size(), printType, "FCFS", FCFS.Run(p));
		ts.printResult(p.size(), printType, "Priority", PriorityScheduling.Run(p, isPreemptive));
		ts.printResult(p.size(), printType, "New", NewScheduling.Run(p, timeSlice2));
	}
}
