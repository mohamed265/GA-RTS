package com.mohamed265.garts.pojo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.mohamed265.garts.utils.constants.ProcessorState;
import com.mohamed265.garts.utils.statics.Clock;
import com.mohamed265.garts.utils.statics.Statics;
import com.mohamed265.garts.utils.statics.Utilities;

public class GAProcessor {

	private int processorId;

	private int readyAtTime;

	private ProcessorState processorState;

	public GAProcessor() {
		this.processorId = Utilities.generateProcessorId();
		setProcessorIdeal();
	}

	public void submitTask(GATask gaTask) {

		Statics.threads.execute(() -> {

			System.out.println("processor: " + getProcessorId() + ", start execute task: " + gaTask.getTaskId()
					+ ", at time: " + Clock.getTime());

			gaTask.setTaskToRunning(this);

			setProcessorRunning();

			try {
				RunnableTask runnableTask = gaTask.getRunnableTask();

				// get all task results before execution
				int totalComunicationTime = Utilities.calculateTotalCommunicationDelay(this, gaTask.getParents());

				readyAtTime = Clock.getTime() + totalComunicationTime + gaTask.getExecutionTime();

				// the following to simulate the cost of communication between processors
				Clock.getTimeUnit().sleep(totalComunicationTime);

				if (gaTask.getParents() != null && gaTask.getParents().length > 0) {

					List<Object> results = Arrays.stream(gaTask.getParents())
							.map(parentTask -> parentTask.getRunnableTask().getTaskResult())
							.collect(Collectors.toList());
					runnableTask.addResults(results);
				}

				// after finish communication
				// start the task execution
				runnableTask.run();

			} catch (Exception e) {
				e.printStackTrace();
			}

			gaTask.setTaskToFinished();

			setProcessorIdeal();

			System.out.println("processor: " + getProcessorId() + ", finish execute task: " + gaTask.getTaskId()
					+ ", at time: " + Clock.getTime());
		});
	}

	public int getProcessorId() {
		return processorId;
	}

	private void setProcessorIdeal() {
		setProcessorState(ProcessorState.IDEAL);
		Statics.numOfAvailableProcessors.incrementAndGet();
		readyAtTime = 0;
	}

	private void setProcessorRunning() {
		setProcessorState(ProcessorState.RUNNING);
		Statics.numOfAvailableProcessors.decrementAndGet();
	}

	private void setProcessorState(ProcessorState processorState) {
		this.processorState = processorState;
	}

	public ProcessorState getProcessorState() {
		return processorState;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + processorId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GAProcessor other = (GAProcessor) obj;
		if (processorId != other.processorId)
			return false;
		return true;
	}

	public int getReadyAtTime() {
		return readyAtTime;
	}

	public void setReadyAtTime(int readyAtTime) {
		this.readyAtTime = readyAtTime;
	}
}
