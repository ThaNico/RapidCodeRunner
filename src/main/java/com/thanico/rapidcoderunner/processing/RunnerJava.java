package com.thanico.rapidcoderunner.processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.thanico.rapidcoderunner.ui.lang.RapidCodeRunnerMessages;

/**
 * Class used to compile and run java code
 *
 * @author Nicolas
 *
 */
public class RunnerJava {
	/**
	 * has the compile method been fully executed ?
	 */
	private boolean compiled = false;

	/**
	 * the directory containing source file + running file
	 */
	private String workingDirectory = null;

	/**
	 * The path to java
	 */
	private String JAVA_HOME = null;

	/**
	 * code runner exec
	 */
	protected final static String JAVA_PATH = "/bin/java.exe";

	/**
	 * code compiler exec
	 */
	protected final static String JAVAC_PATH = "/bin/javac.exe";

	/**
	 * filename + classname
	 */
	protected final static String CODEFILE_NAME = "ExampleProgram";

	/**
	 * source code extension
	 */
	protected final static String CODEFILE_EXT = ".java";

	/**
	 * run code extension
	 */
	private final static String RUNFILE_EXT = ".class";

	/**
	 * simple logger
	 */
	private org.slf4j.Logger Log = org.slf4j.LoggerFactory.getLogger(this.getClass());

	/**
	 * Compilation status
	 */
	private StringBuilder compileStatus = new StringBuilder();

	/**
	 * Compilation time
	 */
	private long compileTime;

	/**
	 * Running status
	 */
	private StringBuilder runningStatus = new StringBuilder();

	/**
	 * Execution time
	 */
	private long runningTime;

	/**
	 * New line
	 */
	private final static String NL = "\n";

	protected RunnerJava() {

	}

	/**
	 * Constructor
	 *
	 * @param CODE
	 *                 the java code (no main/class needed)
	 */
	public RunnerJava(String JAVA_HOME, String code) {
		this.Log.info("Initializing runner java...");
		if (JAVA_HOME == null || JAVA_HOME.isEmpty()) {
			this.Log.error(RapidCodeRunnerMessages.ERR_NO_JAVA_HOME_DEFINED);
			this.getCompileStatus().append(RapidCodeRunnerMessages.ERR_NO_JAVA_HOME_DEFINED + NL);
		}

		this.setJavaHome(JAVA_HOME);

		// cleaning compile/run directory + setting WD
		this.cleanBefore();
		if (this.getWorkingDirectory() == null) {
			this.Log.error("Unable to find current working directory, program will stop.");
			throw new RuntimeException();
		}

		// writing source code in file
		this.writeSource(
				"class " + CODEFILE_NAME + " { public static void main(String[] args) {\r\n" + code + "\r\n } }");
		this.Log.info("Initializing runner java...OK");
	}

	/**
	 * Clean the compile/run directory before compiling/running<br>
	 * And calls setWorkingDirectory to a valid non null directory
	 */
	private void cleanBefore() {
		this.Log.info("Cleaning working directory...");
		String WD = System.getProperty("user.dir");
		if (WD == null || WD.isEmpty()) {
			this.Log.error("Please check the user.dir system property.");
			return;
		}

		WD += "/runnerjava";
		File dir = new File(WD);
		if (!dir.exists()) {
			dir.mkdir();
		} else {
			if (dir.listFiles().length > 0) {
				this.removeWildcardFiles(dir, "*" + CODEFILE_EXT);
				this.removeWildcardFiles(dir, "*" + RUNFILE_EXT);
			}
		}
		this.Log.info("Cleaning working directory...OK");
		this.setWorkingDirectory(WD);
	}

	/**
	 * Removes files according to a wildcard
	 *
	 * @param dir
	 *                     the directory
	 * @param wildcard
	 *                     the wildcard (*.java for ex)
	 */
	private void removeWildcardFiles(File dir, String wildcard) {
		FileFilter fileFilter = new WildcardFileFilter(wildcard);
		File[] files = dir.listFiles(fileFilter);
		for (int i = 0; i < files.length; i++) {
			if (this.Log.isDebugEnabled()) {
				this.Log.debug(wildcard + " : Removing file " + files[i]);
			}
			files[i].delete();
		}
	}

	/**
	 * Writes source code to file
	 *
	 * @param source
	 *                   the source code
	 */
	private void writeSource(String source) {
		this.Log.info("Writing source code in file...");
		String filename = this.getWorkingDirectory() + "/" + CODEFILE_NAME + CODEFILE_EXT;
		// Create the file
		File classfile = new File(filename);
		try {
			classfile.createNewFile();
		} catch (IOException e) {
			classfile = null;
			this.handleException(e, false);
		} finally {
			if (classfile == null || !classfile.exists()) {
				this.Log.error("Cannot create source code file.");
				throw new RuntimeException();
			}
		}

		// Then write inside
		try (PrintWriter out = new PrintWriter(filename)) {
			out.print(source);
		} catch (FileNotFoundException e) {
			this.Log.error("The source code file does not exist (" + filename + ").");
			this.handleException(e, true);
		}
		this.Log.info("Writing source code in file...OK");
	}

	/**
	 * Compiles before execution
	 */
	public void compile() throws RuntimeException {
		this.Log.info("Compiling source code file...");

		String[] execJavac = this.getJavaCompileCommand();

		try {
			long startTime = System.currentTimeMillis();
			Process proc = Runtime.getRuntime().exec(execJavac);
			this.readProcessStream(proc, this.getCompileStatus());

			// TODO : check if measurement can be more effective
			this.setCompileTime(System.currentTimeMillis() - startTime);
		} catch (IOException e) {
			this.getCompileStatus().append("Error while executing compile command." + NL);
			this.getCompileStatus().append(e.getMessage() + NL);
			this.Log.error("Error while executing compile command.");
			this.handleException(e, true);
		}

		this.Log.info("Compiling source code file...OK");
		this.setCompiled(true);
	}

	/**
	 * Gets the array needed for the java compile command
	 *
	 * @return
	 */
	protected String[] getJavaCompileCommand() {
		// program params :
		// 0 : program exe
		// 1 : file full path + name to compile
		String[] execJavac = { this.JAVA_HOME + JAVAC_PATH,
				this.getWorkingDirectory() + "/" + CODEFILE_NAME + CODEFILE_EXT };
		if (this.Log.isDebugEnabled()) {
			this.Log.debug("Compile command : " + execJavac[0] + " " + execJavac[1]);
		}
		return execJavac;
	}

	/**
	 * Runs the code
	 */
	public void runcode() throws RuntimeException {
		this.Log.info("Running code...");

		if (!this.isCompiled()) {
			this.getRunningStatus().append(RapidCodeRunnerMessages.ERR_CODE_NOT_COMPILED + NL);
			this.Log.error(RapidCodeRunnerMessages.ERR_CODE_NOT_COMPILED);
			throw new RuntimeException(RapidCodeRunnerMessages.ERR_CODE_NOT_COMPILED);
		}

		File runfile = new File(this.getWorkingDirectory() + "/" + CODEFILE_NAME + RUNFILE_EXT);
		if (!runfile.exists()) {
			this.getRunningStatus().append(RapidCodeRunnerMessages.ERR_CLASS_FILE_MISSING + NL);
			this.Log.error(RapidCodeRunnerMessages.ERR_CLASS_FILE_MISSING);
			throw new RuntimeException(RapidCodeRunnerMessages.ERR_CLASS_FILE_MISSING);
		}

		String[] execJava = this.getJavaRunCommand();

		try {
			long startTime = System.currentTimeMillis();
			Process proc = Runtime.getRuntime().exec(execJava);
			this.readProcessStream(proc, this.getRunningStatus());

			// TODO : check if measurement can be more effective
			this.setRunningTime(System.currentTimeMillis() - startTime);
		} catch (IOException e) {
			this.getRunningStatus().append("Error while executing run command, please check logs." + NL);
			this.Log.error("Error while executing run command.");
			this.handleException(e, true);
		}

		this.Log.info("Running code...OK");
	}

	/**
	 * Gets the array needed for the java run command
	 *
	 * @return
	 */
	protected String[] getJavaRunCommand() {
		// program params :
		// 0 : program exe
		// 1 : -cp (classpath)
		// 2 : path containing classfile
		// 3 : classname
		String[] execJava = { this.JAVA_HOME + JAVA_PATH, "-cp", this.getWorkingDirectory(), CODEFILE_NAME };
		if (this.Log.isDebugEnabled()) {
			this.Log.debug(
					"Compile command : " + execJava[0] + " " + execJava[1] + " " + execJava[2] + " " + execJava[3]);
		}
		return execJava;
	}

	/**
	 * Reads the output of a process (compile/run)
	 *
	 * @param proc
	 *                 the process
	 */
	private void readProcessStream(Process proc, StringBuilder builder) {
		boolean hasOutput = false;
		String s = null;
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		try {
			while ((s = stdInput.readLine()) != null) {
				if (!hasOutput) {
					builder.append("Standard output : " + NL);
					this.Log.info("Standard output : ");
					hasOutput = true;
				}
				builder.append(s + NL);
				System.out.println(s);
			}
		} catch (IOException e) {
			builder.append("Error while retrieving standard output." + NL);
			this.Log.error("Error while retrieving standard output.");
			this.handleException(e, false);
		}
		if (!hasOutput) {
			builder.append("No standard output." + NL);
			this.Log.info("No standard output.");
		}

		hasOutput = false;
		s = null;
		BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

		try {
			while ((s = stdError.readLine()) != null) {
				if (!hasOutput) {
					builder.append("Error output : " + NL);
					this.Log.info("Error output : ");
					hasOutput = true;
				}
				builder.append(s + NL);
				System.out.println(s);
			}
		} catch (IOException e) {
			builder.append("Error while retrieving error output." + NL);
			this.Log.error("Error while retrieving error output.");
			this.handleException(e, false);
		}
		if (!hasOutput) {
			this.Log.info("No error output.");
		}
	}

	/**
	 * Exception log handling
	 *
	 * @param e
	 *              exception
	 */
	private void handleException(Exception e, boolean throwRuntimeException) {
		this.Log.error("Error message : " + e.getMessage());
		if (this.Log.isDebugEnabled()) {
			e.printStackTrace();
		} else {
			this.Log.error("Please enable debug mode to see the stacktrace.");
		}

		if (throwRuntimeException) {
			throw new RuntimeException(e);
		}
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	// Getters/Setters
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	public String getWorkingDirectory() {
		return this.workingDirectory;
	}

	protected void setWorkingDirectory(String workingDirectory) {
		this.Log.info("Working directory set to: " + workingDirectory);
		this.workingDirectory = workingDirectory;
	}

	protected void setJavaHome(String JAVA_HOME) {
		this.JAVA_HOME = JAVA_HOME;
	}

	protected boolean isCompiled() {
		return this.compiled;
	}

	protected void setCompiled(boolean compiled) {
		this.compiled = compiled;
	}

	public StringBuilder getCompileStatus() {
		return this.compileStatus;
	}

	public StringBuilder getRunningStatus() {
		return this.runningStatus;
	}

	public long getCompileTime() {
		return this.compileTime;
	}

	private void setCompileTime(long compileTime) {
		this.compileTime = compileTime;
	}

	public long getRunningTime() {
		return this.runningTime;
	}

	private void setRunningTime(long runningTime) {
		this.runningTime = runningTime;
	}

}
