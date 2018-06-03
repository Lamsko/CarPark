import java.awt.Color;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarPark implements Runnable {
	private Controller controller;
	private Car[] parkedCars;       // Cars currently parked in the parking house
	private Status[] statuses;      // The parking spots' statuses
	private CarQueue queue;
	private int garageCount;        // Number of cars in the parking house
	private boolean operational;     // The parking house's operational statuses
	private int capacity;
	private final Object mutex;
	private Thread thread;

	public CarPark(Controller controller, int capacity) {
		this.controller = controller;
		this.capacity = capacity;
		this.mutex = new Object();
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * Opens up the car park for business.
	 */
	public void open() {
		// Initialize operational values
		garageCount = 0;
		initStatuses();
		parkedCars = new Car[capacity];

		// Send capacity to GUI
		controller.setCarParkCapacity(capacity);

		// Create queue, set their names and start their threads

			queue = new CarQueue(controller, this,1, 10);
			queue.start();

		// Start car park thread
		operational = true;
		start();
	}

	/**
	 * Fills the status array with EMPTY statuses
	 */
	private void initStatuses() {
		statuses = new Status[capacity];
		for (int i = 0; i < capacity; i++) {
			statuses[i] = Status.EMPTY;
		}
	}

	/**
	 * Check if there's an available parking spot.
	 * @return The available parking spot if there is one, otherwise -1.
	 */
	public int getAvailableSpace() {
		for (int i = 0; i < capacity; i++) {
			if (statuses[i] == Status.EMPTY) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Parks the provided car in the car park.
	 * @param car The car to be parked.
	 */
	public void park(Car car) {
		synchronized (mutex) {
			// Mark the spot as filled
			int spot = car.getParkingSpot();
			statuses[spot] = Status.FILLED;
			parkedCars[spot] = car;
			garageCount++;

			// Print to log
			controller.appendLogEntry("Samochód wjechał do garażu", Color.GREEN);

			// Update GUI
			controller.setLblQueueSize(0, garageCount);
		}
	}

	/**
	 * Unparks the provided car from the car park.
	 * @param car The car to be unparked.
	 */
	public void unpark(Car car) {
		synchronized (mutex) {
			// Departure car
			int spot = car.getParkingSpot();
			statuses[spot] = Status.EMPTY;
			parkedCars[spot] = null;
			garageCount--;

			// Print to log
			controller.appendLogEntry("Samochód wyjechał z garażu", Color.RED);

			// Update GUI
			controller.setLblQueueSize(0, garageCount);
		}
	}

	/**
	 * Loops through the filled parking spots and unparks the cars with expired parking times.
	 */
	public void checkCars() {
		// Loop through all parking spots
		for (int i = 0; i < capacity; i++) {

			// If the spot if filled
			if (statuses[i] == Status.FILLED) {

				// Get car
				Car c = parkedCars[i];

				// and the parking time of the car in that spot has expired
				if (c != null) {
					if (c.tick()) {
						// Unpark car
						unpark(parkedCars[i]);
					}
				}
			}

		}
	}

	/**
	 * Checks if there's space in the car park,
	 * then retrieves a car from one of the four queue,
	 * then parks the car though an executor service that calls the cars run method.
	 * Finally checks if there's any cars to unpark.
	 */
	public void run() {
		Car car;
		int next;
		ExecutorService executor = Executors.newFixedThreadPool(10);

		while (operational) {

			// Get open parking spot
			next = getAvailableSpace();

			// If there's an open parking spot
			if (next != -1) {

				// Get car from the randomized queue
				car = queue.getCar();
				if (car != null) {
					// Park the car
					car.setParkingSpot(next);
					executor.execute(car);
				}
			}

			// Check car statues (and unpark if needed)
			checkCars();

			// Wait a bit
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}