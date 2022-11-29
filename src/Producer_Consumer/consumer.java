package Producer_Consumer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.LinkedList;

import Semaphore.Mysemaphore;

public class consumer extends producer {
	int n;
	Mysemaphore empty;
	Mysemaphore full;
	Mysemaphore mutex;
	private int outptr = 0;
	int store[];
	LinkedList<Integer> list = new LinkedList<>();

///////////////////////*Constructors*///////////////////////
	public consumer()// Default Constructor
	{
	}

	public consumer(int[] array, int n, Mysemaphore emptyy, Mysemaphore fulll, Mysemaphore mutexx)// parameterized
																									// Constructor
	{
		store = new int[array.length];
		store = array;
		empty = new Mysemaphore(emptyy.value);
		full = new Mysemaphore(fulll.value);
		mutex = new Mysemaphore(mutexx.value);
	}

///////////////////////*Wait and Signal*///////////////////////
	public void Signal(Mysemaphore S) {
		notify();
		S.value++;
	}

	public void Wait(Mysemaphore S) throws InterruptedException {
		if (S.value <= 0) {
			wait();
		}
		S.value--;
	}

///////////////////////*Consumer function*///////////////////////
	public void consume() throws InterruptedException {
		while (true) {
			synchronized (this) {
				while (list.size() == 0) {
					wait();
				}

				int value = list.removeFirst();
				char c = '"';
				try (FileWriter fw = new FileWriter("write.txt", Charset.forName("UTF-8"), true);
						BufferedWriter bw = new BufferedWriter(fw);
						PrintWriter out = new PrintWriter(bw)) {
					out.write(c);
					out.print(value);
					out.write(c);
					out.write(",");
				} catch (IOException e) {
				}
				notify();
			}
		}
	}

///////////////////////*Prime Number Check function*///////////////////////
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
}
