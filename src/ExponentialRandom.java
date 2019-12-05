/*
  	Midterm Project
  	Name: Mahima Shukla
   
*/
public class ExponentialRandom {
	public int nextInt() {
		double lambda = 0.90;
		double r = Math.random();
		double s;

		while (true) {
			s = -Math.log(r) / lambda;
			if (s > 7999) {
				continue;
			}
			break;
		}
		return (int) Math.floor(s);
	}
}
