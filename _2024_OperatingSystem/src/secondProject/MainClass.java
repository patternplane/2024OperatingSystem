package secondProject;

import java.util.ArrayList;
import java.util.List;

public class MainClass {
	static public void main(String[] args) throws Throwable {
		test2();
	}
	
	static private void test2() throws Throwable {
		
		RequestGenerator g = new RequestGenerator();
		
		List<Integer>[][] data = new ArrayList[6][200];
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[i].length; j++)
				data[i][j] = new ArrayList<Integer>();
		
		System.out.println("실험조건 : ??");
		
		DiskScheduler[] sch = new DiskScheduler[6];
		sch[0] = new S_FCFS(53, 10);
		sch[1] = new S_SSTF(53);
		sch[2] = new S_SCAN(53, HeadDirection.Increase);
		sch[3] = new S_CSCAN(53);
		sch[4] = new S_LOOK(53, HeadDirection.Increase);
		sch[5] = new S_CLOOK(53);

		// 랜덤한 m개 요청을 n회 한 후의 총 결과 수집
		if (false) {
			for (int i = 0; i < 100000; i++) {
				DiskRequest[] testValue = g.randomGenerate(10);
				for (int schIdx = 0; schIdx < sch.length; schIdx++) {
					for (int pos = 0; pos < 200; pos++) {
						Result[] re = sch[schIdx].run(testValue,pos);
						for (Result r : re) {
							data[schIdx][r.cylinder].add(r.waitingTime);
							
							if (false && schIdx == 2 && i == 0) {
								//System.out.println(r.cylinder);
								System.out.println(r.waitingTime);
								System.out.println(re.length);
							}
						}
					}
				}
			}
		}
		
		// 몇가지 랜덤 상황을 겹치는 경우 상정
		if (true ) {
			for (int i = 0; i < 10000; i++) {
				
				DiskRequest[] testValue1 = g.randomGenerate_SameArrival(4);
				DiskRequest[] testValue2 = g.randomGenerate_center(0);
				DiskRequest[] testValue = new DiskRequest[testValue1.length + testValue2.length];
				for (int j = 0; j < testValue1.length; j++)
					testValue[j] = testValue1[j];
				for (int j = 0; j < testValue2.length; j++) {
					testValue2[j].arrivalTime += 5;
					testValue[testValue1.length + j] = testValue2[j];
				}
				

					for (int schIdx = 0; schIdx < sch.length; schIdx++) {
						Result[] re = sch[schIdx].run(testValue, 100);
						for (Result r : re) {
							data[schIdx][r.cylinder].add(r.waitingTime);
						}
					
				}
			}
		}


		Tool t = new Tool();
		
		// 요청의 위치별 평균과 편차 계산		
		if (true) {
			System.out.println("\tFCFS\t" + "\tSSTF\t" + "\tSCAN\t" + "\tCSCAN\t" + "\tLOOK\t" + "\tCLOOK");
			for (int i = 0; i < 200; i++)  {
				System.out.print(i + "\t");
				for (int j = 0; j < 6; j++){
					int[] a = new int[data[j][i].size()];
					for (int k = 0; k < a.length; k++)
						a[k] = data[j][i].get(k);
					System.out.print(t.getAverage(a) + "\t" + t.getSTDEV(a) + "\t");
				}
				System.out.println();
			}
		}
	}
	
	static private void test1() throws Throwable {

		java.util.ArrayList<DiskRequest> requestsList = new java.util.ArrayList<DiskRequest>();
		DiskRequest[] requests;
		
		int arrive = 0;
		requestsList.add(new DiskRequest(arrive, 98));
		requestsList.add(new DiskRequest(arrive, 183));
		requestsList.add(new DiskRequest(arrive, 37));
		requestsList.add(new DiskRequest(arrive, 122));
		requestsList.add(new DiskRequest(arrive, 14));
		requestsList.add(new DiskRequest(arrive, 124));
		requestsList.add(new DiskRequest(arrive, 65));
		requestsList.add(new DiskRequest(arrive, 67));
		
		RequestGenerator g = new RequestGenerator();
		g.randomGenerate(6);
		
		requests = requestsList.toArray(new DiskRequest[requestsList.size()]);
		
		DiskScheduler[] ds = new DiskScheduler[6];
		ds[0] = new S_FCFS(53, 10);
		ds[1] = new S_SSTF(53);
		ds[2] = new S_SCAN(53, HeadDirection.Decrease);
		ds[3] = new S_CSCAN(53);
		ds[4] = new S_LOOK(53, HeadDirection.Decrease);
		ds[5] = new S_CLOOK(53);
		
		Tool tool = new Tool();

		Result[] results;
		for (DiskScheduler schd : ds) {
			System.out.println(schd.getClass().getSimpleName() + "======");
			results = schd.run(g.randomGenerate(1000), 100); // requests
			System.out.println("avg : " + tool.getAverageWaitingTime(results));
			System.out.println("std : " + tool.getSTDEVWaitingTime(results));
			//tool.printResult(results);
		}
	}
}
