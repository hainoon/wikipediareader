/**
 * 
 */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import model.ModelLadefortschritt;
import model.Statusmeldungen;

/**
 * Ladeanzeige für den Fortschritt bei Artikelwechsel
 * 
 * @author marcel
 *
 */
public class Ladeanzeige implements Observer {
	private JFrame frame;
	private JProgressBar haupt;
	private JProgressBar teil;
	private JTextArea status;
	private JLabel uhr;
	private Timer timer;
	private TimerTask task;
	private JPanel details;
	private JLabel aktuellerTeilschritt;
	private JLabel aktuellerHauptschritt;
	private JLabel anzahlTeilschritte;
	private JLabel anzahlHautschritte;

	public Ladeanzeige() {
		frame = new JFrame();
		JPanel main = new JPanel(new GridLayout(2, 1));
		JPanel fortschritt = new JPanel(new BorderLayout());

		JPanel links = new JPanel(new GridLayout(3, 1));
		links.add(new JLabel("aktueller Vorgang"));
		links.add(new JLabel("gesamter Vorgang"));
		links.add(new JLabel("Details"));
		JPanel bars = new JPanel(new GridLayout(3, 1));
		haupt = new JProgressBar(0, 100);
		haupt.setValue(0);
		teil = new JProgressBar(0, 100);
		teil.setValue(0);
		details = new JPanel();
		JLabel dauer = new JLabel("Zeit: ");
		uhr = new JLabel();
		JLabel teilschritt = new JLabel("Teilschritt: ");
		aktuellerTeilschritt = new JLabel("0");
		JLabel slash1 = new JLabel(" / ");
		anzahlTeilschritte = new JLabel("1");
		JLabel hauptschritt = new JLabel("Hauptschritt: ");
		aktuellerHauptschritt = new JLabel("0");
		JLabel slash2 = new JLabel(" / ");
		anzahlHautschritte = new JLabel("1");

		details.add(dauer);
		details.add(uhr);
		details.add(teilschritt);
		details.add(aktuellerTeilschritt);
		details.add(slash1);
		details.add(anzahlTeilschritte);
		details.add(hauptschritt);
		details.add(aktuellerHauptschritt);
		details.add(slash2);
		details.add(anzahlHautschritte);

		bars.add(teil);
		bars.add(haupt);
		bars.add(details);

		fortschritt.add(links, BorderLayout.WEST);
		fortschritt.add(bars, BorderLayout.CENTER);

		status = new JTextArea();
		status.setEditable(false);
		JScrollPane scroll = new JScrollPane(status);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		main.add(fortschritt);
		main.add(scroll);

		frame.add(main);
		frame.setVisible(false);
		frame.setSize(new Dimension(600, 300));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void timer() {
		final long startzeit = System.currentTimeMillis();
		System.out.println(startzeit);
		task = new TimerTask() {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");

			public void run() {
				date.setTime(System.currentTimeMillis() - startzeit);
				uhr.setText(dateFormat.format(date));
			}
		};
		timer = new Timer();
		timer.schedule(task, 0, 1000);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof ModelLadefortschritt) {
			assert arg instanceof Statusmeldungen : "Fehlerhafte Observerbenachrichtigung";
			switch (((Statusmeldungen) arg)) {
			case Ladevorgang1:
				frame.setVisible(true);
				timer();
				break;
			case Ladevorgang0:
				reset();
				break;
			case Hauptschritt:
				String aktuelleHauptoperation = ((ModelLadefortschritt) o).getAktuelleHauptoperation();
				// System.out.println(aktuelleHauptoperation);
				status.append(aktuelleHauptoperation + "\n");
				aktuellerHauptschritt.setText(((ModelLadefortschritt) o).getAktuellerHauptschritt() + "");
				anzahlHautschritte.setText(((ModelLadefortschritt) o).getAnzahlHauptschritte() + "");
				break;
			case Teilschritt:
				String aktuelleTeiloperation = ((ModelLadefortschritt) o).getAktuelleTeiloperation();
				// System.out.println(aktuelleTeiloperation);
				status.append(aktuelleTeiloperation + "\n");
				double p = ((ModelLadefortschritt) o).getProzessfortschritt();
				haupt.setValue((int) (p * 100.0));
				double p2 = ((ModelLadefortschritt) o).getTeilprozessfortschritt();
				teil.setValue((int) (p2 * 100));
				aktuellerTeilschritt.setText(((ModelLadefortschritt) o).getAktuellerTeilschritt() + "");
				anzahlTeilschritte.setText(((ModelLadefortschritt) o).getAnzahlTeilschritte() + "");
				break;
			default:
				System.err.println("Fehler");
			}
			status.setCaretPosition(status.getText().length());
		}
	}

	private void reset() {
		frame.setVisible(false);
		haupt.setValue(0);
		teil.setValue(0);
		status.setText("");
		task.cancel(); // TODO Timer richtig beenden
		timer.purge();
		timer.cancel();

		// TODO Daten reseten sollte eigentlich übers Modell gehen
	}

}
