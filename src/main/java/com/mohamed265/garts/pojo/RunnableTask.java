/**
 * 
 */
package com.mohamed265.garts.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohamed265
 *
 */
public abstract class RunnableTask implements Runnable {

	private Object taskResult;

	private int executionTime;

	private List<Object> parentsResults;

	public RunnableTask(int executionTime) {
		this.parentsResults = new ArrayList<>();
		this.executionTime = executionTime;
	}

	public abstract void run();

	public void addResult(Object result) {
		parentsResults.add(result);
	}

	public void addResults(List<Object> results) {
		parentsResults.addAll(results);
	}

	public Object getTaskResult() {
		return taskResult;
	}

	public int getExecutionTime() {
		return executionTime;
	}
}
