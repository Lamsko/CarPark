import java.awt.Color;

public class Controller {
	private GUI gui;

	public Controller(GUI gui) {
		this.gui = gui;
	}

	public void setLblQueueSize(int queue, int size) {
		gui.setLblQueueSize(queue, size);
	}


	public void appendLogEntry(String logEntry, Color color) {
		gui.appendLogEntry(logEntry, color);
	}

	public void setCarParkCapacity(int capacity) {
		gui.setCarParkCapacity(capacity);
	}
}