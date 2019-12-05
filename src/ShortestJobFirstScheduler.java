/*
  	Midterm Project
  	Name: Mahima Shukla
   
*/
class ShortestJobFirstScheduler extends Scheduler {
	private Boolean preemption = false;

	public String getName() {
		return "Shortest Job First (SJF) with" + (preemption ? " preemption" : "out preemption");
	}

	public ShortestJobFirstScheduler enablePreemption() {
		preemption = true;
		return this;
	}

	public ShortestJobFirstScheduler disablePreemption() {
		preemption = false;
		return this;
	}

	public Process getNextProcess(Integer processor) {
		Process currentProcess = Prun.get(processor);
		if (currentProcess != null && preemption == false) {
			return currentProcess;
		}
		if (Pwait.size() == 0) {
			return null;
		}
		Integer shortestProcess = 0;
		Integer shortestTime = Pwait.get(0).getTimeRemaining();
		for (Integer i = 0; i < Pwait.size(); ++i) {
			Process process = Pwait.get(i);
			if (process.getTimeRemaining() < shortestTime) {
				shortestTime = process.getTimeRemaining();
				shortestProcess = i;
			}
		}
		if (currentProcess != null && preemption) {
			if (shortestTime < currentProcess.getTimeRemaining()) {
				addProcess(currentProcess);
				Prun.set(processor, null);
			} else {
				return currentProcess;
			}
		}
		return Pwait.remove((int) shortestProcess);
	}
}

