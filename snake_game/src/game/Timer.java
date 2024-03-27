package game;

public class Timer extends Thread {
	private int hour 	= 0;
	private int minute	= 0;
	private int second	= 0;
	private String timeAsString = "";

	public static final int ONE_SECOND_IN_MILI = 1000;

	public String getCurrentTime() {
		return this.timeAsString;
	}
	
	public void reset() {
		hour = 0;
		minute = 0;
		second = 0;
		timeAsString = "";
	}

	public void run() {
		while (true) {
			updateTimeAsString();
			second++;

			try {
				Thread.sleep(ONE_SECOND_IN_MILI);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	public void updateTimeAsString() {
		timeAsString = "";

		if (second == 60) {
			second = 0;
			minute++;
		}
		
		if (minute == 60) {
			minute = 0;
			hour++;
		}
	
		if (hour > 0)
			timeAsString = hour + "시간 ";

		if (minute > 0)
			timeAsString += minute + "분 ";
		
		timeAsString += second + "초";
	}
}