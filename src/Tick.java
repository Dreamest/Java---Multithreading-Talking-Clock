
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.application.Platform;

public class Tick implements Runnable {
	private int sleepTime = 1000;
	private ClockPane clockPane;
	private boolean suspended = false;
	private boolean resume = false;
	private Lock lock = new ReentrantLock();
	private Condition stop = lock.newCondition();

	public Tick(ClockPane clockPane) {
		this.clockPane = clockPane;
	}

	@Override
	public void run() {
		moveClock();
	}

	public void moveClock() {
		while (true) {
			lock.lock();
			if (suspended)
				try {
					stop.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			else {
				resume = true;
				clockPane.setCurrentTime();
				if (clockPane.getSecond() == 0) {
					announceTime(clockPane.getHour(), clockPane.getMinute());
				}
				Platform.runLater(() -> clockPane.paintClock());
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		}
	}

	private void announceTime(int hour, int minute) {
		new Thread(new AnnounceTimeOnSeparateThread(hour, minute)).start();
	}

	public synchronized void pause() {
		suspended = true;
		resume = false;
	}

	public void play() {
		if (resume)
			return;
		lock.lock();
		suspended = false;
		stop.signal();
		lock.unlock();
	}
}
