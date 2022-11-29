package pc;

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;

import Producer_Consumer.consumer;

//Kareem Hossam Mahmoud 20205002
//Donia Waleed Gamal Hagag 20205010
//Emad Eldin Ali Hany 20205004

public class pc extends Thread {

	static String Fname;
	static long elem_count = 0;
	static long largePrime = 0;
	static long larger = 0;

	public class GUI extends JFrame {

//////////////////* timer *///////////////////
		static Long time = 0L;

		public static void main(String[] args) throws InterruptedException {
//////////////////* Creating the frame and the panel *////////////////////////
			JFrame frame = new JFrame("20205002_20205010_20205004");
			JPanel panel = new JPanel();
//////////////////* creating the labels */////////////////////////////////////
			JLabel N = new JLabel("N");
			JLabel Buffer_s = new JLabel("Buffer Size");
			JLabel Output_file = new JLabel("Output file");
			JLabel The_largest_prime_number = new JLabel("The largest prime number");
			JLabel N_of_elements = new JLabel("# of elements (prime numbers) generated");
			JLabel time_elapsed = new JLabel("Time elapsed since the start of processing");
			Dimension size_N = N.getPreferredSize();
			Dimension size_Buffer = Buffer_s.getPreferredSize();
			Dimension size_out = Output_file.getPreferredSize();
			N.setBounds(250, 51, 100, size_N.height);
			Buffer_s.setBounds(250, 88, 100, size_Buffer.height);
			Output_file.setBounds(250, 135, 100, size_out.height);
			The_largest_prime_number.setBounds(20, 264, 200, 20);
			N_of_elements.setBounds(20, 332, 250, 20);
			time_elapsed.setBounds(20, 396, 250, 20);
//////////////////* creating text fields ~input boxes~ *///////////////////////
			JTextField N_input = new JTextField();
			JTextField Buffer_input = new JTextField();
			JTextField out_input = new JTextField();
			N_input.setBounds(20, 51, 200, 20);
			Buffer_input.setBounds(20, 88, 200, 20);
			out_input.setBounds(20, 135, 200, 20);
/////////////* creating the button *//////////////////////////////////////////
			JButton Button = new JButton();
			Button.setText("Start producer");
			Button.setBounds(20, 174, 150, 20);
/////////////* creating Textpane -output places- *///////////////////////////
			JTextPane large = new JTextPane();
			JTextPane elements = new JTextPane();
			JTextPane time_ela = new JTextPane();
			large.setBounds(300, 264, 200, 20);
			elements.setBounds(300, 332, 200, 20);
			time_ela.setBounds(300, 396, 200, 20);
			large.setText("largest prime");
			large.setEditable(false);
			elements.setText("# of elements");
			elements.setEditable(false);
			time_ela.setText("Time elapsed");
			time_ela.setEditable(false);
/////////////* adding all elements to the panel *///////////////////////////
			panel.setLayout(null);
			panel.add(large);
			panel.add(elements);
			panel.add(time_ela);
			panel.add(Button);
			panel.add(N_input);
			panel.add(Buffer_input);
			panel.add(out_input);
			panel.add(N);
			panel.add(Buffer_s);
			panel.add(Output_file);
			panel.add(The_largest_prime_number);
			panel.add(N_of_elements);
			panel.add(time_elapsed);
/////////////* setting the frame and adding the panel *///////////////////////////
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			frame.add(panel);
			frame.setSize(547, 600);
			frame.setVisible(true);
/////////////* time elapsed method -timer works when you hit the button- *///////////////////////////
			Timer timer = new Timer();
			TimerTask timertask = new TimerTask() {
				public void run() {
					if (larger != largePrime) {
						time++;
						time_ela.setText(time + "ms");
					}
					large.setText(String.valueOf(largePrime));
					elements.setText(String.valueOf(elem_count));

				}
			};
/////////////*Actions done on pressing the button*///////////////////////////
			Button.addActionListener(e -> {
				timer.scheduleAtFixedRate(timertask, 0, 1);
				Fname = out_input.getText();

				final pcb pcb1 = new pcb(Integer.parseInt(Buffer_input.getText()), Integer.parseInt(N_input.getText()));
				Thread t1 = new Thread(new Runnable() {
					public void run() {
						try {
							pcb1.produce();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});

				Thread t2 = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							pcb1.consume();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});

				/////////// *getting the prime from end to stop the timer*//////////////////
				for (int i = Integer.parseInt(N_input.getText()); i > 0; i--) {
					boolean isItPrime = true;
					for (int j = 2; j <= i / 2; j++) {
						if ((i % j) == 0) {
							isItPrime = false;
							break;
						}
					}
					if (isItPrime) {
						larger = i;
						break;
					}
				}
				/////////////////// *Starting the threads*//////////////////////////////
				t1.start();
				t2.start();

			}); // End Of Button

		}// End of Main

/////////////////////////////*Producer_Consumer Main Code*////////////////////////////////////////
		static public class pcb extends consumer {
			int buffer;
			int n;

			public pcb(int size, int n) {
				buffer = size;
				this.n = n;
			}

			LinkedList<Integer> list = new LinkedList<>();

///////////////////////*Prime Number Check function*//////////////
			boolean checkForPrime(int num) {
				boolean isItPrime = true;

				if (num < 1) {
					isItPrime = false;
					return isItPrime;
				} else {
					for (int i = 2; i <= num / 2; i++) {
						if ((num % i) == 0) {
							isItPrime = false;
							break;
						}
					}
					return isItPrime;
				}
			}

///////////////////////*Producer function*///////////////////////
			@Override
			public void produce() throws InterruptedException {
				int value = 0;
				while (value <= n) {
					synchronized (this) {

						while (list.size() == buffer) {
							wait();
						}
						if (checkForPrime(value)) {
							list.add(value);
						}

						notify();
						value++;
					}

				}

			}

///////////////////////*Consumer function*///////////////////////
			@Override
			public void consume() throws InterruptedException {
				while (true) {
					synchronized (this) {
						while (list.size() == 0) {
							wait();
						}

						int value = list.removeFirst();
						char c = '"';
						try (FileWriter fw = new FileWriter(Fname, Charset.forName("UTF-8"), true);
								BufferedWriter bw = new BufferedWriter(fw);
								PrintWriter out = new PrintWriter(bw)) {
							if (value != 1) {
								out.write(",");
							}
							out.write(c);
							out.print(value);
							out.write(c);
							elem_count++;
							largePrime = value;
						} catch (IOException e) {
						}
						notify();

					}

				} // End of While
			}// End Of Consume()
		}// End of Class pcb

	}// End of Class GUI
}// End of Class pc