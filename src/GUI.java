import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class GUI {
	private Controller controller;
	private JFrame frame;               // The Main windows
	private JLabel lblQueueOne;
	private JLabel lblQueueTwo;
	private JLabel lblQueueThree;
	private JLabel lblQueueFour;
	private JLabel lblCarParkSize;      // Size of car allow
	private JLabel lblQueueOneSize;     // Size of entrance 1
	private JLabel lblQueueTwoSize;     // Size of entrance 2
	private JLabel lblQueueThreeSize;   // Size of entrance 3
	private JLabel lblQueueFourSize;    // Size of entrance 4
	private JLabel lblCarParkCapacity;
	private JTextPane log;              // Log

	public GUI () {
		this.controller = new Controller(this);
	}

	public void startProgram() {
		frame = new JFrame();
		frame.setBounds(0, 0, 800, 400);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setTitle("Car Park with Threadpool");
		initializeGUI();					// Fill in components
		frame.setVisible(true);
		frame.setResizable(false);			// Prevent user from change size
		frame.setLocationRelativeTo(null);	// Start middle screen
		CarPark carPark = new CarPark(controller, 50);
		carPark.open();
	}

	public void initializeGUI() {
		float fontSize = 15f;

		// Create Main panels
		JPanel left = new JPanel();
		left.setLayout(new GridLayout(3, 3));
		JPanel right = new JPanel();
		right.setPreferredSize(new Dimension(250, 800));
		right.setLayout(new GridLayout(0, 1));

		// Add panels to frame
		frame.add(left, BorderLayout.CENTER);
		frame.add(right, BorderLayout.EAST);

		// Car Park Panel
		JPanel pnlCarPark = new JPanel();
		pnlCarPark.setLayout(new BorderLayout());
		pnlCarPark.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder
				(Color.black), "Car Park"));


		// Log
		JPanel pnlLog = new JPanel();
		pnlLog.setLayout(new BorderLayout());
		pnlLog.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder
				(Color.black), "Log"));
		log = new JTextPane();
		log.setBackground(Color.BLACK);
		JScrollPane scroll = new JScrollPane(log);
		pnlLog.add(scroll, BorderLayout.CENTER);


		// Car Park Panel
		JLabel lblCarPark = new JLabel("Parked: ");
		lblCarParkCapacity = new JLabel("Capacity : ");
		lblCarPark.setHorizontalAlignment(JLabel.CENTER);
		lblCarParkCapacity.setHorizontalAlignment(JLabel.CENTER);
		lblCarParkSize = new JLabel("0");
		lblCarParkSize.setHorizontalAlignment(JLabel.CENTER);
		lblCarParkSize.setFont(lblCarParkSize.getFont().deriveFont(fontSize));
		pnlCarPark.add(lblCarPark, BorderLayout.CENTER);
		pnlCarPark.add(lblCarParkSize, BorderLayout.CENTER);
		pnlCarPark.add(lblCarParkCapacity, BorderLayout.SOUTH);

		// Create queue panels
		JPanel pnlQueueOne = new JPanel();
		JPanel pnlQueueTwo = new JPanel();
		JPanel pnlQueueThree = new JPanel();
		JPanel pnlQueueFour = new JPanel();
		pnlQueueOne.setLayout(new GridBagLayout());
		pnlQueueTwo.setLayout(new GridBagLayout());
		pnlQueueThree.setLayout(new GridBagLayout());
		pnlQueueFour.setLayout(new GridBagLayout());

		// Queue 1
		lblQueueOne = new JLabel("Queue 1: ");
		lblQueueOneSize = new JLabel("0");
		lblQueueOneSize.setFont(lblQueueOneSize.getFont().deriveFont(fontSize));
		pnlQueueOne.add(lblQueueOne);
		pnlQueueOne.add(lblQueueOneSize);


		// Queue 2
		lblQueueTwo = new JLabel("Queue 2: ");
		lblQueueTwoSize = new JLabel("0");
		lblQueueTwoSize.setFont(lblQueueTwoSize.getFont().deriveFont(fontSize));
		pnlQueueTwo.add(lblQueueTwo);
		pnlQueueTwo.add(lblQueueTwoSize);


		// Queue 3
		lblQueueThree = new JLabel("Queue 3: ");
		lblQueueThreeSize = new JLabel("0");
		lblQueueThreeSize.setFont(lblQueueThreeSize.getFont().deriveFont(fontSize));
		pnlQueueThree.add(lblQueueThree);
		pnlQueueThree.add(lblQueueThreeSize);


		// Queue 4
		lblQueueFour = new JLabel("Queue 4: ");
		lblQueueFourSize = new JLabel("0");
		lblQueueFourSize.setFont(lblQueueFourSize.getFont().deriveFont(fontSize));
		pnlQueueFour.add(lblQueueFour);
		pnlQueueFour.add(lblQueueFourSize);

		// Fill Main panels
		left.add(new JPanel());
		left.add(pnlQueueTwo);
		left.add(new JPanel());
		left.add(pnlQueueOne);
		left.add(pnlCarPark);
		left.add(pnlQueueThree);
		left.add(new JPanel());
		left.add(pnlQueueFour);
		left.add(new JPanel());
		right.add(pnlLog);
	}

	public void setLblQueueSize(int queue, int size) {
		// Get correct label
		JLabel lbl;
		if (queue == 0) lbl = lblCarParkSize;
		else if (queue == 1) lbl = lblQueueOneSize;
		else if (queue == 2) lbl = lblQueueTwoSize;
		else if (queue == 3) lbl = lblQueueThreeSize;
		else lbl = lblQueueFourSize;

		// Fix "car" and "cars" grammar
		String carGrammar = size == 1 ? "car" : "cars";

		SwingUtilities.invokeLater(() -> lbl.setText(size + " " + carGrammar));
	}

	public void setCarParkCapacity(int capacity) {
		SwingUtilities.invokeLater(() -> lblCarParkCapacity.setText("Capacity: " + capacity));
	}

	public void setQueueName(int queue, String queueName) {
		JLabel lbl;
		if (queue == 1) lbl = lblQueueOne;
		else if (queue == 2) lbl = lblQueueTwo;
		else if (queue == 3) lbl = lblQueueThree;
		else lbl = lblQueueFour;
		final JLabel finalLbl = lbl;
		SwingUtilities.invokeLater(() -> finalLbl.setText(queueName + " Entrance: "));
	}

	/**
	 * Appends the provided string to the log, colored with the provided color.
	 * @param msg The string to be logged.
	 * @param c The color of the log entry.
	 */
	public void appendLogEntry(String msg, Color c) {
		SwingUtilities.invokeLater(() -> {
			StyleContext sc = StyleContext.getDefaultStyleContext();
			AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
			aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
			aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
			int len = log.getDocument().getLength();
			log.setCaretPosition(len);
			log.setCharacterAttributes(aset, false);
			log.replaceSelection(msg + "\n");
		});
	}
}