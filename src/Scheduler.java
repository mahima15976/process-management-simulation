/*
  	Midterm Project
  	Name: Mahima Shukla
   
*/
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

abstract class Scheduler {
	// Other variables
	List<Process> Pwait = new ArrayList<Process>(), Prun = new ArrayList<Process>(), // Indexed

			finishedProcesses = new ArrayList<Process>();
	List<Integer> waitingTimes = new ArrayList<Integer>();
	Integer time = 0, waitingTime = 0, maxTime = 0, averageTime = 0, minTime = 0;

	public Scheduler() {
		// Initialize data indexed by CPU #
		for (Integer i = 0; i < Test.m; ++i) {
			Prun.add(null);
			waitingTimes.add(0);
		}
	}

	public void addProcess(Process process) {
		Pwait.add(new Process(process));
	}

	public void addProcesses(Collection<Process> processes) {
		for (Process process : processes) {
			addProcess(process);
		}
	}

	public Boolean hasWaitingProcesses() {
		return Pwait.size() > 0;
	}

	public Boolean hasRunningProcesses() {
		for (Process process : Prun) {
			if (process != null) {
				return true;
			}
		}
		return false;
	}

	public Boolean hasUnfinishedProcesses() {

		return hasWaitingProcesses() || hasRunningProcesses();
	}

	// Returns the current process for a certain processor
	abstract Process getNextProcess(Integer processor);

	// Returns the name of the scheduler
	abstract String getName();

	// Increments to the next step
	public void tick() {
		for (Integer i = 0; i < Test.m; ++i) {
			Integer waitingTime = waitingTimes.get(i);
			Process currentProcess = Prun.get(i);
			if (waitingTime > 0) {
				// The CPU is currently waiting
				waitingTimes.set(i, --waitingTime);
				currentProcess.pause();
				if (waitingTime == 0) {
					System.out.printf("[time %dms] Process %d created (requires %dms CPU time, priority is %d)\n", time,
							currentProcess.getId(), currentProcess.getTimeRemaining(), currentProcess.getPriority());
				}
				continue;
			}
			if (currentProcess == null) {

			} else {
				currentProcess.run();
				// If this process has terminated
				if (currentProcess.getTimeRemaining() == 0) {

					currentProcess.setTimeTotal();

					System.out.printf(
							"[time %dms] Process %d completed its CPU burst (turnaround time %dms, initial wait time %dms, total wait time %dms)\n",
							time, currentProcess.getId(), currentProcess.getTimeTotal(),
							currentProcess.getTimeInitiallyWaiting(), currentProcess.getTimeWaiting());
					// Remove the process from the set of running processes
					Prun.set(i, null);
					// Add it to the set of finished processes
					finishedProcesses.add(currentProcess);
				}
			}
			Process nextProcess = getNextProcess(i);
			if (nextProcess == null) {
				// CPU is idle
				continue;
			}
			if (nextProcess != currentProcess) {
				if (currentProcess != null) {
					// A process was just running, we need a context switch
					System.out.printf("[time %dms] Context switch (swapping out process %d for process %d in CPU %s)\n",
							time, currentProcess.getId(), nextProcess.getId(), Character.toString((char) (65 + i)));
					waitingTimes.set(i, Test.c_s);
				} else {
					// This is the first process so start it now
					System.out.printf("[time %dms] Process %d created (requires %dms CPU time, priority is %d)\n", time,
							nextProcess.getId(), nextProcess.getTimeRemaining(), nextProcess.getPriority());
				}
				Prun.set(i, nextProcess);
			}
		}
		for (Process process : Pwait) {
			process.pause();
		}
		time++;
	}

	public Integer getTime() {
		return time;
	}

	public void printResults() {
		int minTurnaroundTime = Integer.MAX_VALUE;
		double avgTurnaroundTime = 0;
		int maxTurnaroundTime = 0;
		int minInitialWaitTime = Integer.MAX_VALUE;
		double avgInitialWaitTime = 0;
		int maxInitialWaitTime = 0;

		int minTotalWaitTime = Integer.MAX_VALUE;
		double avgTotalWaitTime = 0;
		int maxTotalWaitTime = 0;
		for (Process process : finishedProcesses) {

			avgTurnaroundTime += process.getTimeTotal();
			avgInitialWaitTime += process.getTimeInitiallyWaiting();
			avgTotalWaitTime += process.getTimeWaiting();

			if (process.getTimeTotal() > maxTurnaroundTime) {
				maxTurnaroundTime = process.getTimeTotal();
			}
			if (process.getTimeTotal() < minTurnaroundTime) {
				minTurnaroundTime = process.getTimeTotal();
			}
			if (process.getTimeInitiallyWaiting() > maxInitialWaitTime) {
				maxInitialWaitTime = process.getTimeInitiallyWaiting();
			}
			if (process.getTimeInitiallyWaiting() < minInitialWaitTime) {
				minInitialWaitTime = process.getTimeInitiallyWaiting();
			}
			if (process.getTimeWaiting() > maxTotalWaitTime) {
				maxTotalWaitTime = process.getTimeWaiting();
			}
			if (process.getTimeWaiting() < minTotalWaitTime) {
				minTotalWaitTime = process.getTimeWaiting();
			}
		}
		int finishedProcessesSize = finishedProcesses.size();
		avgTurnaroundTime /= finishedProcessesSize;
		avgInitialWaitTime /= finishedProcessesSize;
		avgTotalWaitTime /= finishedProcessesSize;
		System.out.printf("Number of CPUs: %d\n", Test.m);
		System.out.printf("Turnaround time: min %dms; avg %.3fms; max %dms\n", minTurnaroundTime, avgTurnaroundTime,
				maxTurnaroundTime);
		System.out.printf("Initial wait time: min %dms; avg %.3fms; max %dms\n", minInitialWaitTime, avgInitialWaitTime,
				maxInitialWaitTime);
		System.out.printf("Total wait time: min %dms; avg %.3fms; max %dms\n\n", minTotalWaitTime, avgTotalWaitTime,
				maxTotalWaitTime);
	}
}
