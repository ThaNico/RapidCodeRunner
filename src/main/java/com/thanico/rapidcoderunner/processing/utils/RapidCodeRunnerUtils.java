package com.thanico.rapidcoderunner.processing.utils;

import java.util.concurrent.TimeUnit;

public class RapidCodeRunnerUtils {
	/**
	 * Formats time :<br>
	 * if time >= 60000 then prints : Xmn XXsec<br>
	 * if time >= 10000 then prints : Xsec Xms<br>
	 * else : Xms
	 *
	 * @param execTime
	 * @return
	 */
	public static String formatTime(long execTime) {
		String formattedTime;
		if (execTime >= 60000) {
			formattedTime = String.format("%dmn %02dsec", TimeUnit.MILLISECONDS.toMinutes(execTime),
					TimeUnit.MILLISECONDS.toSeconds(execTime)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(execTime)));
		} else if (execTime >= 10000) {
			formattedTime = String.format("%dsec %dms", TimeUnit.MILLISECONDS.toSeconds(execTime),
					execTime - (1000 * TimeUnit.MILLISECONDS.toSeconds(execTime)));
		} else {
			formattedTime = execTime + "ms";
		}
		return formattedTime;
	}
}
