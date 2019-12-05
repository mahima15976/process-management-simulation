/*
  	Midterm Project
  	Name: Mahima Shukla
   
*/
public class PreemptivePriorityScheduler extends Scheduler {
	private Integer t = 100; // Configurable time slice, t, set to 100 milliseconds

	@Override
	public void addProcess(Process process) {

		if (Pwait.size() == 0) {
			Pwait.add(process);
		} else {
			Boolean processAdded = false;
			for (int i = 0; i < Pwait.size(); ++i) {
				Process nextProcess = Pwait.get(i);
				if (nextProcess.getPriority() > process.getPriority()) {
					Pwait.add(i, process);
					processAdded = true;
					break;
				}
			}
			if (!processAdded) {
				Pwait.add(process);
			}
		}

	}

	public String getName() {
		return "Preemptive Priority ";
	}

	public Process getNextProcess(Integer processor) {
		Process currentProcess = Prun.get(processor);
		if (currentProcess != null) {
			Integer timeRemaining = currentProcess.getTimeRemaining(), timeTotal = currentProcess.getTimeTotal();
			Boolean preempted = false;
			if (Pwait.size() > 0) {
				Process highestPriority = Pwait.get(0);
				if (highestPriority.getPriority() < currentProcess.getPriority()) {
					// Get preempted
					addProcess(currentProcess);
					Prun.set(processor, null);
					preempted = true;
				}
			}
			if (!preempted && (timeTotal - timeRemaining) % t != 0) {
				// Continue the current process
				return currentProcess;
			}
			if (!preempted) {
				// Otherwise, attach it to the end of the list
				addProcess(currentProcess);
				Prun.set(processor, null);
			}
		}
		if (Pwait.size() == 0)
			return null;
		return Pwait.remove(0);
	}
}
