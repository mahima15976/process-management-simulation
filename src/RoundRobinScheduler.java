/*
  	Midterm Project
  	Name: Mahima Shukla
   
*/
class RoundRobinScheduler extends Scheduler {
	private Integer t = 100; // Configurable time slice, t, set to 100 milliseconds

	public String getName() {
		return "Round-Robin (RR)";
	}

	public Process getNextProcess(Integer processor) {
		Process currentProcess = Prun.get(processor);
		if (currentProcess != null) {
			Integer timeRemaining = currentProcess.getTimeRemaining(), timeTotal = currentProcess.getTimeTotal();
			if ((timeTotal - timeRemaining) % t > 0) {
				return currentProcess;
			}
			// Otherwise, stick it on the end of the list
			addProcess(currentProcess);
			Prun.set(processor, null);
		}
		if (Pwait.size() == 0)
			return null;
		return Pwait.remove(0);
	}
}
