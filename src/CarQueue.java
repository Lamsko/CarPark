import java.awt.*;
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
	 * Zwraca pierwszy samochod z kolejki
	 */
	public Car getCar() {
		Car car = null;
		if (queue.size() > 0) {
			car = queue.removeFirst();

			// Aktualizacja GUI
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
	 * Tworzy nowe samochody w kolejce dopoki jest w niej miejsce
	 */
	public void run() {
		Random rnd = new Random();
		int sleepTime;

		// Dopoki jest miejsce w kolejce
		while (queue.size() < queueCapacity) {

			// Dodaj samochod
			queue.add(new Car(carPark));

			// Wypisz w logu
			controller.appendLogEntry("Samochód w kolejce do wjazdu", Color.YELLOW);

			// Aktualizacja GUI
			controller.setLblQueueSize(getQueueNbr(), queue.size());

			// Odczekaj chwile
			sleepTime = rnd.nextInt(2000) + 500;
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}