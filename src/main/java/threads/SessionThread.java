package threads;

public class SessionThread implements Runnable {

	private static Boolean shouldStop = false;

	private void init() {
		System.out.println("[Session] Thread started.");
	}
	
	@Override
	public void run() {
		init();
		//while (!shouldStop) {
			// SESSION IS TO BE ADDED


			//NOT IMPLEMENTED YET!
		//}
		System.out.println("[Session] Thread stopped.");
	}

	public static Boolean getShouldStop() {
		return shouldStop;
	}

	public static void setShouldStop(Boolean shouldStop) {
		SessionThread.shouldStop = shouldStop;
	}


}