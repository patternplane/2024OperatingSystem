import java.util.ArrayList;
import java.util.EnumSet;

public class Tester2 {
	public void Test() {
		TestSupporter ts = new TestSupporter();
		
		ArrayList<Process>[] jobsData = ts.readFilesFromDialog();
		
		EnumSet<PrintType> printType = EnumSet.of(PrintType.SUMMARY_INFO);
		
		int timeSlice1 = 800;
		int timeSlice2 = 12;
		
		int currentFile = 1;
		for (ArrayList<Process> jobs : jobsData) {
			ArrayList<Result> r = ts.getPerProcessResult(NewScheduling.Run(jobs, timeSlice2));
			System.out.print(ts.getAverageWaitingTime(r));
			System.out.print("\t");
			System.out.println(ts.getSTDEVWaitingTime(r));
			
			//System.out.println("\n\nFile : " + currentFile);
			
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
