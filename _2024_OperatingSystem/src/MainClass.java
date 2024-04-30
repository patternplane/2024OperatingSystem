
import java.util.ArrayList;

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SchedulingAlgorithm sa = new SchedulingAlgorithm();
		
		ArrayList<Process> p = new ArrayList<Process>();
		p.add(new Process(2,0,10,1));
		p.add(new Process(1,1,7,1));
		p.add(new Process(3,2,6,1));
		p.add(new Process(4,4,11,1));
		p.add(new Process(5,5,10,1));
		p.add(new Process(6,7,12,0));
		
		ArrayList<Result> result = sa.Run(p);
	}

}
