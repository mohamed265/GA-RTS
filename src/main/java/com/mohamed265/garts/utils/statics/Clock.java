/**
 * 
 */
package com.mohamed265.garts.utils.statics;

import java.util.concurrent.TimeUnit;

/**
 * @author Mohamed265
 *
 */
public class Clock {

	private static int time = 0;

	private static TimeUnit timeUnit = TimeUnit.SECONDS;

	public static int getTime() {
		return time;
	}

	public static void incrementTime() {
		time++;
	}

	public static TimeUnit getTimeUnit() {
		return timeUnit;
	}
}
