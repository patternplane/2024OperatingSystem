import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Scanner;

public class Tester2 {
	public void Test() {
		TestSupporter ts = new TestSupporter();
		
		ArrayList<Process>[] jobsData = ts.readFilesFromDialog();
		
		EnumSet<PrintType> printType = EnumSet.of(PrintType.SUMMARY_INFO);

		// Time Slice
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Time Slice Size :");
        int timeSlice = scanner.nextInt();
        scanner.close();
		
		int currentFile = 1;
		ArrayList<Result> r;
		for (ArrayList<Process> jobs : jobsData) {
			System.out.println("File : " + currentFile);
			
			System.out.println("FCFS Avg & StDev of Waiting Times : ");
			r = ts.getPerProcessResult(FCFS.Run(jobs));
			System.out.print(ts.getAverageWaitingTime(r));
			System.out.print("\t");
			System.out.println(ts.getSTDEVWaitingTime(r));

			System.out.println("SJF Avg & StDev of Waiting Times : ");
			r = ts.getPerProcessResult(SJF.Run(jobs));
			System.out.print(ts.getAverageWaitingTime(r));
			System.out.print("\t");
			System.out.println(ts.getSTDEVWaitingTime(r));

			System.out.println("Priority(P) Avg & StDev of Waiting Times : ");
			r = ts.getPerProcessResult(PriorityScheduling.Run(jobs, true));
			System.out.print(ts.getAverageWaitingTime(r));
			System.out.print("\t");
			System.out.println(ts.getSTDEVWaitingTime(r));
			
			System.out.println("Priority(NP) Avg & StDev of Waiting Times : ");
			r = ts.getPerProcessResult(PriorityScheduling.Run(jobs, false));
			System.out.print(ts.getAverageWaitingTime(r));
			System.out.print("\t");
			System.out.println(ts.getSTDEVWaitingTime(r));
			
			System.out.println("RR Avg & StDev of Waiting Times : ");
			r = ts.getPerProcessResult(RoundRobin.Run(jobs, timeSlice));
			System.out.print(ts.getAverageWaitingTime(r));
			System.out.print("\t");
			System.out.println(ts.getSTDEVWaitingTime(r));
			
			System.out.println("Hybrid SJF RR Avg & StDev of Waiting Times : ");
			r = ts.getPerProcessResult(NewScheduling.Run(jobs, timeSlice));
			System.out.print(ts.getAverageWaitingTime(r));
			System.out.print("\t");
			System.out.println(ts.getSTDEVWaitingTime(r));
			
			
			//ts.outToFile(".\\result"+currentFile+"_FCFS.txt", ts.getPerProcessResult(ts.printResult(jobs.size(), printType, "FCFS" , FCFS.Run(jobs))));
			//ts.outToFile(".\\result"+currentFile+"_SJF.txt", ts.getPerProcessResult(ts.printResult(jobs.size(), printType, "SJF" , SJF.Run(jobs))));
			//ts.outToFile(".\\result"+currentFile+"_RR.txt", ts.getPerProcessResult(ts.printResult(jobs.size(), printType, "RoundRobin" , RoundRobin.Run(jobs, timeSlice1))));
			//ts.outToFile(".\\result"+currentFile+"_Priority_true.txt", ts.getPerProcessResult(ts.printResult(jobs.size(), printType, "Priority" , PriorityScheduling.Run(jobs, true))));
			//ts.outToFile(".\\result"+currentFile+"_Priority_false.txt", ts.getPerProcessResult(ts.printResult(jobs.size(), printType, "Priority" , PriorityScheduling.Run(jobs, false))));
			//ts.outToFile(".\\result"+currentFile+"_New.txt", ts.getPerProcessResult(ts.printResult(jobs.size(), printType, "New" , NewScheduling.Run(jobs, timeSlice2))));
			
			currentFile++;
		}
	}
}
