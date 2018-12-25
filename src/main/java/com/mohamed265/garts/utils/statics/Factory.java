/**
 * 
 */
package com.mohamed265.garts.utils.statics;

import static com.mohamed265.garts.utils.statics.Statics.gaProcessors;
import static com.mohamed265.garts.utils.statics.Statics.waitingTasks;
import static com.mohamed265.garts.utils.statics.Statics.numOfProcessors;

import java.util.Arrays;
import java.util.Random;

import com.mohamed265.garts.pojo.GAChromosome;
import com.mohamed265.garts.pojo.GAProcessor;
import com.mohamed265.garts.pojo.GATask;
import com.mohamed265.garts.pojo.RunnableTask;

/**
 * @author Mohamed265
 *
 */
public class Factory {

	public final static Random random = new Random();

	public static RunnableTask getRunnableTask(int executionTime) {

		return new RunnableTask(executionTime) {
			@Override
			public void run() {
				try {
					// the following simulate the execution of task
					Clock.getTimeUnit().sleep(executionTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
	}

	public static GAChromosome generateChromosome() {

		int numOfTasks = waitingTasks.size();

		GAProcessor[] processors = new GAProcessor[numOfTasks];

		for (int i = 0; i < numOfTasks; i++) {
			processors[i] = gaProcessors.get(random.nextInt(numOfProcessors));
		}

		GATask[] tasks = new GATask[numOfTasks];

		for (int i = 0; i < numOfTasks; i++) {
			tasks[i] = waitingTasks.get(i);
		}

		Arrays.sort(tasks, (t1, t2) -> t1.getLevel() - t2.getLevel());

		return new GAChromosome(processors, tasks);
	}
}
