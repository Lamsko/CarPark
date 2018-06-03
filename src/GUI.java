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
	private JLabel lblQueue;
	private JLabel lblCarParkSize;      // Size of car allow
	private JLabel lblQueueSize;     // Size of entrance 1
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
		frame.setTitle("Symulator garażu");
		initializeGUI();					// Fill in components
		frame.setVisible(true);
		frame.setResizable(false);			// Prevent user from change size
		frame.setLocationRelativeTo(null);	// Start middle screen
		CarPark carPark = new CarPark(controller, 10);
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
				(Color.black), "Garaż"));


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
		JLabel lblCarPark = new JLabel("Zaparkowane: ");
		lblCarParkCapacity = new JLabel("Pojemność : ");
		lblCarPark.setHorizontalAlignment(JLabel.CENTER);
		lblCarParkCapacity.setHorizontalAlignment(JLabel.CENTER);
		lblCarParkSize = new JLabel("0");
		lblCarParkSize.setHorizontalAlignment(JLabel.CENTER);
		lblCarParkSize.setFont(lblCarParkSize.getFont().deriveFont(fontSize));
		pnlCarPark.add(lblCarPark, BorderLayout.CENTER);
		pnlCarPark.add(lblCarParkSize, BorderLayout.CENTER);
		pnlCarPark.add(lblCarParkCapacity, BorderLayout.SOUTH);

		// Create queue panels
		JPanel pnlQueue = new JPanel();
		pnlQueue.setLayout(new GridBagLayout());

		// Queue 1
		lblQueue = new JLabel("Kolejka: ");
		lblQueueSize = new JLabel("0");
		lblQueueSize.setFont(lblQueueSize.getFont().deriveFont(fontSize));
		pnlQueue.add(lblQueue);
		pnlQueue.add(lblQueueSize);


		// Fill Main panels
		left.add(new JPanel());
		left.add(new JPanel());
		left.add(pnlQueue);
		left.add(pnlCarPark);
		left.add(new JPanel());
		left.add(new JPanel());
		right.add(pnlLog);
	}

	public void setLblQueueSize(int queue, int size) {
		// Get correct label
		JLabel lbl;
		if (queue == 0) lbl = lblCarParkSize;
		else lbl = lblQueueSize;

		String carGrammar;
		if (size == 1 ) {
			carGrammar = "samochód";
		}
		else if (size == 2 || size == 3 || size == 4){
			carGrammar = "samochody";
		}
		else {
			carGrammar = "samochodów";
		}

		SwingUtilities.invokeLater(() -> lbl.setText(size + " " + carGrammar));
	}

	public void setCarParkCapacity(int capacity) {
		SwingUtilities.invokeLater(() -> lblCarParkCapacity.setText("Pojemność: " + capacity));
	}

	public void setQueueName(int queue, String queueName) {
		JLabel lbl;
		lbl = lblQueue;
		final JLabel finalLbl = lbl;
		SwingUtilities.invokeLater(() -> finalLbl.setText(queueName + " wjazd: "));
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