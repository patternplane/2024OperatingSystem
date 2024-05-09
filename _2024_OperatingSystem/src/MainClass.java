
import java.util.ArrayList;

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//SchedulingAlgorithm sa = new SchedulingAlgorithm();
		PriorityScheduling sa = new PriorityScheduling(); 
		
		ArrayList<Process> p = new ArrayList<Process>();
		p.add(new Process(2,0,10,1));
		p.add(new Process(1,1,7,3));
		p.add(new Process(3,2,6,5));
		p.add(new Process(4,4,11,6));
		p.add(new Process(5,5,10,1));
		p.add(new Process(6,7,12,0));
		
		ArrayList<Result> result = sa.Run(p, false);
		for (Result r : result)
			System.out.println("ID : " + r.processID + " startP : " + r.startP + " Burst : " + r.burstTime + " waiting : " + r.waitingTime);
	}

}
