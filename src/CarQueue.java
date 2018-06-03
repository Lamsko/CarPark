import java.awt.Color;
import java.util.LinkedList;
import java.util.Random;

public class CarQueue implements Runnable {
	private Controller controller;
	private CarPark carPark;
	private LinkedList<Car> queue;
	private final int queueCapacity;
	private int queueNbr;
	private Thread thread;

	public CarQueue(Controller controller, CarPark carPark, int queueNbr, int size) {
		this.controller = controller;
		this.carPark = carPark;
		this.queueNbr = queueNbr;
		queue = new LinkedList<>();
		queueCapacity = size;
	}


	/**
	 * Retrieves the car first in queue.
	 * @return The car first in queue.
	 */
	public Car getCar() {
		Car car = null;
		if (queue.size() > 0) {
			car = queue.removeFirst();

			// Update GUI
			controller.setLblQueueSize(queueNbr, queue.size());
		}
		return car;
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public int getQueueNbr() {
		return queueNbr;
	}

	/**
	 * Creates and places new cars in the queue while there's still room in it.
	 */
	public void run() {
		Random rnd = new Random();
		int sleepTime;

		// While there's space in queue
		while (queue.size() < queueCapacity) {

			// Add car to queue
			queue.add(new Car(carPark));

			// Print to log
			controller.appendLogEntry("Samochód w kolejce do wjazdu", Color.YELLOW);

			// Update GUI
			controller.setLblQueueSize(getQueueNbr(), queue.size());

			// Put queue to sleep for some time
			sleepTime = rnd.nextInt(2000) + 500;
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}