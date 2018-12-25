/**
 * 
 */
package com.mohamed265.garts.utils.statics;

import java.util.Arrays;

import com.mohamed265.garts.pojo.GAProcessor;
import com.mohamed265.garts.pojo.GATask;

/**
 * @author Mohamed265
 *
 */
public class Utilities {

	public static int calculateTotalCommunicationDelay(GAProcessor processor, GATask[] tasks) {

		if (tasks != null && tasks.length > 0) {

			return Arrays.stream(tasks).filter(t -> !t.getAssignedProcessor().equals(processor))
					.mapToInt(t -> t.getCommunicationTime()).sum();
		}

		return 0;
	}

	private static int taskId = 1;

	public static synchronized int generateTaskId() {
		return taskId++;
	}

	private static int processorId = 0;

	public static synchronized int generateProcessorId() {
		return processorId++;
	}
}
