import java.awt.*;

public class CarPark implements Runnable {
	private Controller controller;
	private Car[] parkedCars;       // Samochody aktualnie zaparkowane na parkingu
	private Status[] statuses;      // Status miejsc parkingowych (EMPTY, FILLED)
	private CarQueue queue;          // Kolejka do wjazdu
	private int garageCount;        // Ilosc samochodow na parkingu
	private boolean operational;     // Informacja o tym czy parking działa
	private int capacity;            // Pojemność garażu
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
	 * Otwarcie parkingu
	 */
	public void open() {
		// Inicjalizacja
		garageCount = 0;
		initStatuses();
		parkedCars = new Car[capacity];

		// Wysyłamy pojemność do GUI
		controller.setCarParkCapacity(capacity);

		// Utworzenie kolejki i start watku

			queue = new CarQueue(controller, this,1, 10);
			queue.start();

		// Start watku parkingu
		operational = true;
		start();
	}

	/**
	 * Ustawienie statusu miejsc parkingowych na EMPTY
	 */
	private void initStatuses() {
		statuses = new Status[capacity];
		for (int i = 0; i < capacity; i++) {
			statuses[i] = Status.EMPTY;
		}
	}

	/**
	 * Sprawdzamy ilosc dostepnych miejsc parkingowych
	 * -1 gdy brak
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
	 * Parkujemy samochod w garażu
	 */
	public void park(Car car) {
		synchronized (mutex) {
			// Oznaczenie miejsca jako FILLED
			int spot = car.getParkingSpot();
			statuses[spot] = Status.FILLED;
			parkedCars[spot] = car;
			garageCount++;

			// Wypis w logu
			controller.appendLogEntry("Samochód wjechał do garażu", Color.GREEN);

			// Aktualizacja GUI
			controller.setLblQueueSize(0, garageCount);
		}
	}

	/**
	 * Wyjeżdżamy samochodem z parkingu
	 */
	public void unpark(Car car) {
		synchronized (mutex) {
			// Wyjazd samochodu
			int spot = car.getParkingSpot();
			statuses[spot] = Status.EMPTY;
			parkedCars[spot] = null;
			garageCount--;

			// Wpis w logu
			controller.appendLogEntry("Samochód wyjechał z garażu", Color.RED);

			// Aktualizacja GUI
			controller.setLblQueueSize(0, garageCount);
		}
	}

	/**
	 * Petla po zaparkowanych samochodach odliczajaca czas do wyjazdu i wyparkowujaca samochody z licznikiem 0
	 */
	public void checkCars() {
		// Petla po wszystkich miejscach
		for (int i = 0; i < capacity; i++) {

			// Jeśli miejsce zajete
			if (statuses[i] == Status.FILLED) {

				// Pobieramy samochod
				Car c = parkedCars[i];

				// i zmniejszamy licznik i sprawdzamy czy nalezy wyparkowac samochod
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
	 * Jeśli uruchomiony sprawdza czy jest miejsce w garażu
	 * Pobiera nastepny samochod z kolejki i parkuje samochod
	 * Nastepnie sprawdza czy jest samochod do wyparkowania
	 */
	public void run() {
		Car car;
		int next;

		while (operational) {

			// Pobiera wolne miejsce
			next = getAvailableSpace();

			// Jeśli jest wolne miejsce
			if (next != -1) {

				// Pobiera samochod z kolejki
				car = queue.getCar();
				if (car != null) {
					// Parkuje samochod
					car.setParkingSpot(next);
					car.run();
				}
			}

			// Sprawdzamy status i odparkowywujemy jesli potrzeba
			checkCars();

			// Odczekujemy chwile
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}