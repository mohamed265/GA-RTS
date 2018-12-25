/**
 * 
 */
package com.mohamed265.garts;

import java.util.Scanner;

import com.mohamed265.garts.ga.Scheduler;
import com.mohamed265.garts.utils.statics.Statics;

/**
 * @author Mohamed265
 *
 */
public class Main {

	public static void main(String[] args) {

		System.out.println("Hello GA-RTs");

		Scheduler scheduler = new Scheduler();
		Scanner scanner = new Scanner(System.in);

		while (Statics.isRunning) {

			System.out.println("1 to schedule task");
			System.out.println("2 to exit");
			int in = scanner.nextInt();

			if (in == 1) {
				readTask(scheduler, scanner);
			} else {
				Statics.isRunning = false;
				Statics.threads.shutdown();
				return;
			}
		}
	}

	private static void readTask(Scheduler scheduler, Scanner scanner) {

		System.out
				.println("please enter the following executionTime, communicationTime, num of parent tasks, tasks ids");
		int executionTime = scanner.nextInt();
		int communicationTime = scanner.nextInt();
		int numOfParents = scanner.nextInt();
		int[] parentsIds = new int[numOfParents];
		for (int i = 0; i < parentsIds.length; i++) {
			parentsIds[i] = scanner.nextInt();
		}
		scheduler.submitTask(executionTime, communicationTime, 0, 0, parentsIds);

	}

}
