import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;

public class GUI {
	private Controller controller;
	private JFrame frame;               // Główne okno
	private JLabel lblQueue;
	private JLabel lblCarParkSize;      // Rozmiar parkingu
	private JLabel lblQueueSize;     // Rozmiar kolejki
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
		initializeGUI();					// Wypełnia komponenty
		frame.setVisible(true);
		frame.setResizable(false);			// Zablokowanie zmiany rozmiaru
		frame.setLocationRelativeTo(null);	// Wyśrodkowanie
		CarPark carPark = new CarPark(controller, 10); //utworzenie garażu
		carPark.open();
	}

	public void initializeGUI() {
		float fontSize = 15f;

		// Główne panele
		JPanel left = new JPanel();
		left.setLayout(new GridLayout(3, 3));
		JPanel right = new JPanel();
		right.setPreferredSize(new Dimension(250, 800));
		right.setLayout(new GridLayout(0, 1));

		// Dodanie paneli do głównego okna
		frame.add(left, BorderLayout.CENTER);
		frame.add(right, BorderLayout.EAST);

		// Panel garażu
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


		// Panel garażu
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

		// Panel koleki
		JPanel pnlQueue = new JPanel();
		pnlQueue.setLayout(new GridBagLayout());

		// Kolejka
		lblQueue = new JLabel("Kolejka: ");
		lblQueueSize = new JLabel("0");
		lblQueueSize.setFont(lblQueueSize.getFont().deriveFont(fontSize));
		pnlQueue.add(lblQueue);
		pnlQueue.add(lblQueueSize);


		// Wypełniamy główny Panel
		left.add(new JPanel());
		left.add(new JPanel());
		left.add(pnlQueue);
		left.add(pnlCarPark);
		left.add(new JPanel());
		left.add(new JPanel());
		right.add(pnlLog);
	}

	public void setLblQueueSize(int queue, int size) {
		// Pobieramy odpowiednią etykietę
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
	* Dodawanie wpisów do logu
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