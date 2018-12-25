/**
 * 
 */
package com.mohamed265.garts.ga;

import java.util.ArrayList;

import com.mohamed265.garts.pojo.GAChromosome;
import com.mohamed265.garts.pojo.GAProcessor;
import com.mohamed265.garts.pojo.GATask;
import com.mohamed265.garts.pojo.RunnableTask;
import com.mohamed265.garts.utils.constants.ProcessorState;
import com.mohamed265.garts.utils.constants.TaskState;
import com.mohamed265.garts.utils.statics.Clock;
import com.mohamed265.garts.utils.statics.Factory;
import com.mohamed265.garts.utils.statics.Statics;

/**
 * @author Mohamed265
 *
 */
public class Scheduler {

	private Thread schedulUpdater;

	public Scheduler() {
		schedulUpdater = new Thread(() -> {
			while (Statics.isRunning) {
				System.out.println("Time now is: " + Clock.getTime());
				runTasks();
				try {
					Clock.getTimeUnit().sleep(1);
					Clock.incrementTime();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		schedulUpdater.start();
	}

	private void runTasks() {
		if (Statics.scheduler != null) {

			for (int i = 0; i < Statics.scheduler.getProcessors().length; i++) {

				GAProcessor gaProcessor = Statics.scheduler.getProcessors()[i];

				GATask gaTask = Statics.scheduler.getTasks()[i];

				if (gaProcessor.getProcessorState().equals(ProcessorState.IDEAL)) {

					gaProcessor.submitTask(gaTask);

					Statics.scheduler.removeSchedulAt(i);

					i--;
				}
			}
		}
	}

	public void submitTask(int executionTime, int communicationTime, int daadline, int dlaxity, int... parentsIds) {

		RunnableTask runnableTask = Factory.getRunnableTask(executionTime);

		GATask[] parents = getParents(parentsIds);

		GATask gaTask = new GATask(runnableTask, executionTime, communicationTime, daadline, dlaxity, parents);

		runOrSchedualTask(gaTask);

	}

	private void runOrSchedualTask(GATask gaTask) {

		if (Statics.numOfAvailableProcessors.get() != 0) {
			if (!gaTask.isDependent()) {
				GAProcessor gaProcessor = getAvailableProcessor();
				Statics.allTasks.add(gaTask);
				gaProcessor.submitTask(gaTask);
			} else {
				if (isReady(gaTask.getParents())) {
					GAProcessor gaProcessor = getAvailableProcessor();
					Statics.allTasks.add(gaTask);
					gaProcessor.submitTask(gaTask);
				} else {
					Statics.waitingTasks.add(gaTask);
					Statics.allTasks.add(gaTask);
				}
			}
		} else {

			Statics.waitingTasks.add(gaTask);
			Statics.allTasks.add(gaTask);

			SchedulingGA schedulingGA = new SchedulingGA();
			GAChromosome gaChromosome = schedulingGA.startGA();
			Statics.scheduler = gaChromosome;
		}

	}

	private boolean isReady(GATask[] parents) {
		boolean ready = true;
		if (parents != null && parents.length > 0) {
			for (GATask gaTask : parents) {
				ready &= gaTask.getTaskState().equals(TaskState.FINISHED);
			}
		}
		return ready;
	}

	private GAProcessor getAvailableProcessor() {
		for (GAProcessor gaProcessor : Statics.gaProcessors) {
			if (gaProcessor.getProcessorState().equals(ProcessorState.IDEAL))
				return gaProcessor;
		}
		return null;
	}

	private GATask[] getParents(int... parentsIds) {
		if (parentsIds != null && parentsIds.length > 0) {

			ArrayList<GATask> list = new ArrayList<>();

			for (int id : parentsIds) {

				GATask task = getTaskById(id);

				if (task != null)
					list.add(task);

			}

			GATask[] parents = new GATask[list.size()];

			for (int i = 0; i < parents.length; i++) {
				GATask gaTask = parents[i];
				parents[i] = gaTask;
			}

			return parents;
		}
		return null;
	}

	private GATask getTaskById(int taskId) {
		for (GATask task : Statics.allTasks) {
			if (task.getTaskId() == taskId)
				return task;
		}
		return null;
	}
}
