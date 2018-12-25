/**
 * 
 */
package com.mohamed265.garts.pojo;

import static com.mohamed265.garts.utils.statics.Statics.gaProcessors;
import static com.mohamed265.garts.utils.statics.Statics.waitingTasks;

import java.util.List;
import java.util.stream.Collectors;

import com.mohamed265.garts.utils.statics.Clock;

/**
 * @author Mohamed265
 *
 */
public class GAChromosome {

	private GAProcessor[] processors;

	private GATask[] tasks;

	private int scheduleLength = 0;

	private double fitnessValue;

	public GAChromosome() {

	}

	public GAChromosome(GAProcessor[] processors, GATask[] tasks) {
		this.processors = processors;
		this.tasks = tasks;
	}

	public void calculateFitnessValue(double a) {
		calculateScheduleLength();
		fitnessValue = (a / (double) scheduleLength);
	}

	public void calculateScheduleLength() {

		List<Integer> readyTime = gaProcessors.stream()
				.map(processor -> processor.getReadyAtTime() == 0 ? 0 : processor.getReadyAtTime() - Clock.getTime())
				.collect(Collectors.toList());

		List<Integer> finishTime = waitingTasks.stream().map(task -> 0).collect(Collectors.toList());

		for (int i = 0; i < tasks.length; i++) {

			GATask gaTask = tasks[i];

			GAProcessor gaProcessor = processors[i];

			int dat = calculateDAT(gaProcessor, gaTask.getParents(), finishTime);

			int st = Math.max(readyTime.get(gaProcessor.getProcessorId()), dat);

			int ft = st + gaTask.getExecutionTime();

			readyTime.set(gaProcessor.getProcessorId(), ft);

			scheduleLength = Math.min(scheduleLength, ft);
		}

	}

	private int calculateDAT(GAProcessor processor, GATask[] parents, List<Integer> finishTime) {
		int dat = 0;

		if (parents != null && parents.length > 0) {

			for (int i = 0; i < parents.length; i++) {

				int parentIndex = getParentIndex(parents[i]);

				int parentTaskFinishTime = parentIndex != -1 ? finishTime.get(parentIndex) : 0;

				if (parentIndex != -1 && processor.equals(getParentProcessor(parentIndex))) {
					dat = Math.max(parentTaskFinishTime, dat);
				} else {
					dat = Math.max(parents[i].getCommunicationTime() + parentTaskFinishTime, dat);
				}
			}
		}
		return dat;
	}

	private int getParentIndex(GATask gaTask) {
		for (int i = 0; i < tasks.length; i++) {
			if (tasks[i].getTaskId() == gaTask.getTaskId()) {
				return i;
			}
		}
		return -1;
	}

	public void removeSchedulAt(int index) {
		GAProcessor[] processors = new GAProcessor[this.processors.length - 1];

		GATask[] tasks = new GATask[this.tasks.length - 1];

		for (int i = 0, j = 0; i < tasks.length; i++) {
			if (i != index) {
				processors[j] = this.processors[i];
				tasks[j] = this.tasks[i];
				j++;
			}
		}

		this.processors = processors;
		this.tasks = tasks;
	}

	private GAProcessor getParentProcessor(int parentIndex) {
		return processors[parentIndex];
	}

	public GAProcessor[] getProcessors() {
		return processors;
	}

	public GATask[] getTasks() {
		return tasks;
	}

	public int getScheduleLength() {
		return scheduleLength;
	}

	public double getFitnessValue() {
		return fitnessValue;
	}

	public void setProcessors(GAProcessor[] processors) {
		this.processors = processors;
	}

	public void setTasks(GATask[] tasks) {
		this.tasks = tasks;
	}

}
