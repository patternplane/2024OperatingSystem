import java.util.ArrayList;
import java.util.EnumSet;

public class Tester2 {
	public void Test() {
		TestSupporter ts = new TestSupporter();
		
		ArrayList<Process>[] jobsData = ts.readFilesFromDialog();
		
		EnumSet<PrintType> printType = EnumSet.of(PrintType.SUMMARY_INFO);
		
		int timeSlice1 = 6;
		int timeSlice2 = 6;
		boolean isPreemptive = true;
		
		int currentFile = 1;
		for (ArrayList<Process> jobs : jobsData) {
			System.out.println("\n\nFile : " + currentFile++);
			
			ts.printResult(jobs.size(), printType, "FCFS" , FCFS.Run(jobs));
			ts.printResult(jobs.size(), printType, "SJF" , SJF.Run(jobs));
			ts.printResult(jobs.size(), printType, "RoundRobin" , RoundRobin.Run(jobs, timeSlice1));
			ts.printResult(jobs.size(), printType, "Priority" , PriorityScheduling.Run(jobs, isPreemptive));
			ts.printResult(jobs.size(), printType, "New" , NewScheduling.Run(jobs, timeSlice2));
		}
	}
}
