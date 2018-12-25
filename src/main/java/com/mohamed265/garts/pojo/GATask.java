package com.mohamed265.garts.pojo;

import java.util.Arrays;
import java.util.Comparator;

import com.mohamed265.garts.utils.constants.TaskState;
import com.mohamed265.garts.utils.statics.Clock;
import com.mohamed265.garts.utils.statics.Statics;
import com.mohamed265.garts.utils.statics.Utilities;

public class GATask {

	// ----------- algorithm variables -----------

	private int executionTime;

	private int communicationTime;

	private int arrivalTime;

	private int startingTime;

	private int daadline;

	private int dlaxity;

	private int level; // depth

	private GATask[] parents;

	// ----------- implementation -----------

	private int taskId;

	private TaskState taskState;

	private RunnableTask runnableTask;

	private GAProcessor assignedProcessor;

	// ----------------------

	public GATask(RunnableTask runnableTask, int executionTime, int communicationTime, int daadline, int dlaxity,
			GATask... parents) {
		this.runnableTask = runnableTask;
		this.executionTime = executionTime;
		this.communicationTime = communicationTime;
		this.daadline = daadline;
		this.dlaxity = dlaxity;
		this.parents = parents;

		setTaskState(TaskState.NEW);
		this.level = calculateDepth();
		this.taskId = Utilities.generateTaskId();
		this.arrivalTime = Clock.getTime();
	}

	public int calculateDepth() {
		int level = 0;
		if (this.parents != null && this.parents.length > 0) {
			level = Arrays.stream(parents).max(Comparator.comparing(GATask::getLevel)).get().getLevel();
		}
		return level + 1;
	}

	public boolean isDependent() {
		return parents != null && parents.length > 0;
	}

	// ----------- implementation -----------

	public int getExecutionTime() {
		return executionTime;
	}

	public int getCommunicationTime() {
		return communicationTime;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public int getDaadline() {
		return daadline;
	}

	public int getDlaxity() {
		return dlaxity;
	}

	public int getLevel() {
		return level;
	}

	public RunnableTask getRunnableTask() {
		return runnableTask;
	}

	public GATask[] getParents() {
		return parents;
	}

	public int getTaskId() {
		return taskId;
	}

	public TaskState getTaskState() {
		return taskState;
	}

	public GAProcessor getAssignedProcessor() {
		return assignedProcessor;
	}

	public void setTaskToScheduled() {
		setTaskState(TaskState.SCHEDULED);
	}

	public void setTaskToRunning(GAProcessor assignedProcessor) {
		this.assignedProcessor = assignedProcessor;
		setTaskState(TaskState.RUNNING);
		startingTime = Clock.getTime();
	}

	public void setTaskToFinished() {
		Statics.waitingTasks.remove(this);
		setTaskState(TaskState.FINISHED);
	}

	private void setTaskState(TaskState taskState) {
		this.taskState = taskState;
	}

	public int getStartingTime() {
		return startingTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + taskId;
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
		GATask other = (GATask) obj;
		if (taskId != other.taskId)
			return false;
		return true;
	}

}
