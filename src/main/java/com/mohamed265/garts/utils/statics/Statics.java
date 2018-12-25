package com.mohamed265.garts.utils.statics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.mohamed265.garts.pojo.GAChromosome;
import com.mohamed265.garts.pojo.GAProcessor;
import com.mohamed265.garts.pojo.GATask;

public class Statics {

	public static boolean isRunning = true;

	public static GAChromosome scheduler = null;

	public final static List<GATask> allTasks = new ArrayList<>();

	public final static List<GATask> waitingTasks = new ArrayList<>();

	public final static int numOfProcessors = 3;

	public static AtomicInteger numOfAvailableProcessors = new AtomicInteger(numOfProcessors);

	public final static List<GAProcessor> gaProcessors = new ArrayList<>();

	public final static ExecutorService threads = Executors.newFixedThreadPool(numOfProcessors);

	static {
		for (int i = 0; i < numOfProcessors; i++) {
			gaProcessors.add(new GAProcessor());
		}
	}
}
