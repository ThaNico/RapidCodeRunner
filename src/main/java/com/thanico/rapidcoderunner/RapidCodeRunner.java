package com.thanico.rapidcoderunner;

public class RapidCodeRunner {
	public static void main(String[] args) {

		String JAVA_HOME = "C:\\Program Files\\Java\\jdk1.8.0_144";
		String JAVA_CODE = "System.out.println(\"Hello world\");";

		RunnerJava javaRunner = new RunnerJava(JAVA_HOME, JAVA_CODE);
		javaRunner.compile();
		javaRunner.runcode();

		/**
		 * Output example :
		 */
		// 2018-06-09 16:42:18:075 [INFO] Initializing runner java...
		// 2018-06-09 16:42:18:076 [INFO] Cleaning working directory...
		// 2018-06-09 16:42:18:079 [INFO] Cleaning working directory...OK
		// 2018-06-09 16:42:18:079 [INFO] Working directory set to:
		// E:\Projects\TestRunnerComp\RapidCodeRunner/runnerjava
		// 2018-06-09 16:42:18:079 [INFO] Writing source code in file...
		// 2018-06-09 16:42:18:080 [INFO] Writing source code in file...OK
		// 2018-06-09 16:42:18:080 [INFO] Initializing runner java...OK
		// 2018-06-09 16:42:18:080 [INFO] Compiling source code file...
		// 2018-06-09 16:42:18:426 [INFO] No standard output.
		// 2018-06-09 16:42:18:426 [INFO] No error output.
		// 2018-06-09 16:42:18:426 [INFO] Compiling source code file...OK
		// 2018-06-09 16:42:18:426 [INFO] Running code...
		// 2018-06-09 16:42:18:493 [INFO] Standard output :
		// Hello world
		// 2018-06-09 16:42:18:494 [INFO] No error output.
		// 2018-06-09 16:42:18:495 [INFO] Running code...OK

	}
}
