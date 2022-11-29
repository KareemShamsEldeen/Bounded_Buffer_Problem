package Producer_Consumer;

import java.util.LinkedList;

import Semaphore.Mysemaphore;

public class producer extends Thread {

	buffer buf;
	int n;
	Mysemaphore empty;
	Mysemaphore full;
	Mysemaphore mutex;
	private int inptr = 0;
	int store[];
	int value = 0;
	LinkedList<Integer> list = new LinkedList<>();

///////////////////////*Constructors*///////////////////////
	public producer()// Default Constructor
	{

	}

	public producer(int[] array, int n, Mysemaphore emptyy, Mysemaphore fulll, Mysemaphore mutexx)// parameterized
																									// Constructor
	{
		this.n = n;
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

///////////////////////*Producer function*///////////////////////
	public void produce() throws InterruptedException {
		int value = 0;
		while (value <= n) {
			synchronized (this) {

				while (list.size() == store.length) {
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

}
