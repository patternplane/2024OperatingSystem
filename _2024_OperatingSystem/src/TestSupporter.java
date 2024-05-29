import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.EnumSet;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

enum PrintType {
	DETAILED_INFO,
	SUMMARY_INFO,
	PROCESS_DETAILED_INFO;
}

public class TestSupporter {
	public ArrayList<Process>[] readFilesFromDialog() {

		JFrame frame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        
        ArrayList<ArrayList<Process>> jobsList = new ArrayList<ArrayList<Process>>(); 
        
		int result = fileChooser.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			ArrayList<Process> processList;
		    File[] selectedFile = fileChooser.getSelectedFiles();
		    for (File f : selectedFile){
		    	processList = readFile(f.getPath());
		    	if (processList != null) {
		    		System.out.println("File : " + f.getName());
		    		jobsList.add(processList);
		    	}
		    }
		}
		
		fileChooser.setVisible(false);
		frame.setVisible(false);
		frame.dispose();
		
		return jobsList.toArray(new ArrayList[jobsList.size()]);
	}
	
	// Used when read Jobs From Extern File
	public ArrayList<Process> readFile(String path) {
		try {
			ArrayList<Process> result = new ArrayList<Process>();
			
			java.io.FileReader f = new java.io.FileReader(path);
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
	public void outToFile(ArrayList<Result> result) {
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
	
	// print results to Console
	public ArrayList<Result> printResult(int itemsCount, EnumSet<PrintType> printType, String algoName, ArrayList<Result> result) {
		
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
