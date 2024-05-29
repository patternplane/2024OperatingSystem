// Random Job Generator

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class JobGenerator {
	
	public void makeToFile(String path, ArrayList<Process> jobs) {
		StringBuilder str = new StringBuilder();
		
		Iterator<Process> itr = jobs.iterator();
		while (itr.hasNext()) {
			Process p = itr.next();
			str
			.append("process ")
			.append(p.processID).append(" ")
			.append(p.arriveTime).append(" ")
			.append(p.burstTime).append(" ")
			.append(p.priority);
			if (itr.hasNext())
				str.append("\n");
		}

		try {
			FileWriter fout = new FileWriter(path);
			fout.write(str.toString());
			fout.close();
		} catch (IOException e) {}
	}

	public ArrayList<Process> OrderedPriorityProcessGenerator(int size, int interval, boolean ascending) {
		Random r = new Random();
		
		int minPriority = 1;
		int maxPriority = 2000;
		
		ArrayList<Process> jobs = new ArrayList<Process>(size);
		int arrivalTime = 0;
		int processID = 1;
		int priority = (ascending? minPriority : maxPriority);
		int orderItv = (int)(2*(maxPriority - minPriority)/(double)size);
		if (orderItv == 0)
			orderItv = 1;
		while (size-- > 0 && (minPriority <= priority && priority <= maxPriority)) {
			jobs.add(new Process(processID++, arrivalTime, r.nextInt(60,80), priority));
			priority += r.nextInt(orderItv) * (ascending? 1 : -1);
			arrivalTime += r.nextInt(interval);
		}
		
		return jobs;
	} 
	
	public ArrayList<Process> OrderedBurstProcessGenerator(int size, int startBurst, int addValue) {
		Random r = new Random();
		
		ArrayList<Process> jobs = new ArrayList<Process>(size);
		int arrivalTime = 0;
		int processID = 1;
		while (size-- > 0 && startBurst > 0) {
			jobs.add(new Process(processID++, arrivalTime, startBurst, r.nextInt(100)));
			startBurst += addValue;
			arrivalTime += r.nextInt(startBurst*2);
		}
		
		return jobs;
	} 
	
	public ArrayList<Process> intervaledRandomicProcessGenerator(int size, int interval, int minBurst, int maxBurst) {
		Random r = new Random();
		
		ArrayList<Process> jobs = new ArrayList<Process>(size);
		int arrivalTime = 0;
		int processID = 1;
		while (size-- > 0) {
			jobs.add(new Process(processID++, arrivalTime, r.nextInt(minBurst, maxBurst), r.nextInt(100)));
			arrivalTime += r.nextInt(interval);
		}
		
		return jobs;
	}
}
