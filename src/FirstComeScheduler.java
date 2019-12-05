/*
  	Midterm Project
  	Name: Mahima Shukla
   
*/
class FirstComeScheduler extends Scheduler {
	public FirstComeScheduler() {
		super();
	}

	public String getName() {
		return "First Come First Serve (FCFS)";
	}

	public Process getNextProcess(Integer processor) {
		if (Prun.get(processor) != null) {
			return Prun.get(processor);
		} else {
			if (Pwait.size() > 0) {
				return Pwait.remove(0);
			} else {
				return null;
			}
		}
	}
}

