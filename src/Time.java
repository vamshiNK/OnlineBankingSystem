public class Time {
	static long startTime,elapsedTime;
	Time(long startTime,long elapsedTime)
	{
		this.startTime=startTime;
		this.elapsedTime=elapsedTime;
	}
	void Secends()
	{
		//startTime = System.currentTimeMillis();

		// Loop for 10 seconds
		for (int i = 1; i <= 10; i++) {
			// Sleep for 1 second (1000 milliseconds)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Calculate and print the elapsed time in seconds
		//	elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
		//	System.out.println("Elapsed Time: " + elapsedTime + " seconds");
		}
	}
	
}
