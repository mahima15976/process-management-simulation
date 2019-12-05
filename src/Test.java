/*
  	Midterm Project
  	Name: Mahima Shukla
   
*/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class Test {
	public static final Integer n = 20; // Number of processes
	public static final Integer m = 4;
	public static final Integer c_s = 15;
	public static Integer PART2 = 0;

	public static void main(String[] args) {
		// checking input
		if (args.length > 0 && args[0].equals("-PART2")) {
			PART2 = 1;
		}
		// Creating ExponentialRandom object for random time
		ExponentialRandom E_R = new ExponentialRandom();

		// Creating processes
		Queue<Process> allProcesses = new PriorityQueue<Process>(n, new Comparator<Process>() {
			public int compare(Process left, Process right) {
				if (left.getRequestedTime() < right.getRequestedTime())
					return -1;
				else if (left.getRequestedTime() == right.getRequestedTime())
					return 0;
				return 1;
			}
		});
		Random random = new Random();
		for (Integer i = 0; i < n; ++i) {
			Integer t = random.nextInt(300) + 100;
			Integer p = random.nextInt(5);
			Integer reqAt = 0;
			if (i > n / 10) {
				reqAt = E_R.nextInt();
			}
			Process process = new Process(i, t, p, reqAt);
			allProcesses.add(process);
		}
		// Create schedulers
		List<Scheduler> schedulers = new ArrayList<Scheduler>();

		// Add schedulers to the list
		schedulers.addAll(Arrays.asList(new FirstComeScheduler(), new ShortestJobFirstScheduler(),
				(new ShortestJobFirstScheduler()).enablePreemption(), new RoundRobinScheduler(),
				new PreemptivePriorityScheduler()));

		for (Scheduler scheduler : schedulers) {
			Queue<Process> currentProcesses = new PriorityQueue<Process>(allProcesses);
			if (PART2 == 0) {
				scheduler.addProcesses(currentProcesses);
			} else {
				addProcessesRequestedNow(allProcesses, currentProcesses, scheduler, 0);
			}
			System.out.println("For scheduling algorithm: " + scheduler.getName());
			while (scheduler.hasUnfinishedProcesses() || (PART2 == 1 && currentProcesses.size() > 0)) {
				scheduler.tick();
				if (PART2 == 1) {
					addProcessesRequestedNow(allProcesses, currentProcesses, scheduler, scheduler.getTime());
				}

			}
			System.out.println();
			// Print final results
			scheduler.printResults();
			for (Process process : allProcesses) {
				process.reset();
			}

		}

	}

	public static void addProcessesRequestedNow(Queue<Process> allProcesses, Queue<Process> currentProcesses,
			Scheduler scheduler, Integer time) {
		Process nextProcess = currentProcesses.peek();
		if (nextProcess == null)
			return;
		while (nextProcess.getRequestedTime() <= time) {
			nextProcess = currentProcesses.remove();
			scheduler.addProcess(nextProcess);

			nextProcess = currentProcesses.peek();
			if (nextProcess == null)
				break;

		}
	}
}