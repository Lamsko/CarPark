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

	/**
	 * Gives the car a parking time between minTime and maxTime.
	 */
	private void generateParkingTime() {
		this.timer = random.nextInt(maxTime) + minTime;
	}

	/**
	 * Decreases parking timer by one every time it's called.
	 *
	 * @return True if parking timer is expired.
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
		// Park the car
		carPark.park(this);
	}
}