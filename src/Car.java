import java.util.Random;

public class Car implements Runnable {
	private int timer;
	private CarPark carPark;
	private int parkingSpot;
	private Random random;
	private static final int minTime = 40;
	private static final int maxTime = 50;

	public Car(CarPark carPark) {
		this.carPark = carPark;
		this.random = new Random();
		generateParkingTime();
	}


	private void generateParkingTime() {
		this.timer = random.nextInt(maxTime) + minTime;
	}

	/**
	 * Zmniejsza licznik parkowania o 1 przy każdym wywołaniu.
	 *
	 * Zwraca True jeśli czas parkowania zakończony.
	 */
	public boolean tick() {
		return (timer--) <= 0;
	}

	public void setParkingSpot(int spot) {
		this.parkingSpot = spot;
	}

	public int getParkingSpot() {
		return parkingSpot;
	}

	@Override
	public void run() {
		// Zaparkuj samochod
		carPark.park(this);
	}
}