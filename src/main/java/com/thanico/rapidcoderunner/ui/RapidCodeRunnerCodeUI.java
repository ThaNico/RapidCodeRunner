package com.thanico.rapidcoderunner.ui;

import com.thanico.rapidcoderunner.RapidCodeRunner;
import com.thanico.rapidcoderunner.RapidCodeRunnerGUI;
import com.thanico.rapidcoderunner.RunnerJava;
import com.thanico.rapidcoderunner.processing.utils.RapidCodeRunnerUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Class used to manage the code interface
 *
 * @author Nicolas
 *
 */
public class RapidCodeRunnerCodeUI {

	/**
	 * codeTextArea element id
	 */
	private static final String codeTextAreaId = "codeTextArea";

	/**
	 * btnCompile element id
	 */
	private final static String btnCompileId = "btnCompile";

	/**
	 * current scence
	 */
	private Scene scene;

	/**
	 * Constructor
	 *
	 * @param scene
	 */
	public RapidCodeRunnerCodeUI(Scene scene) {
		this.scene = scene;
	}

	/**
	 * Returns what needs to be displayed for the code UI
	 *
	 * @return
	 */
	public Tab getCodeUI() {
		return this.createCodeTab();
	}

	/**
	 * Create the code tab
	 *
	 * @return
	 */
	private Tab createCodeTab() {
		HBox hboxHeaderButtons = new HBox(5);
		hboxHeaderButtons.setAlignment(Pos.CENTER);
		hboxHeaderButtons.getChildren().addAll(this.createBtnCompile());

		VBox vboxCode = new VBox(10);
		vboxCode.setPadding(new Insets(0, 10, 0, 10));
		vboxCode.setAlignment(Pos.CENTER);

		vboxCode.getChildren().addAll(hboxHeaderButtons, this.createCodeTextArea());

		Tab codeTab = new Tab("CODE", vboxCode);
		codeTab.setClosable(false);
		return codeTab;
	}

	/**
	 * Button executing compile action<br>
	 * TODO make it clean
	 *
	 * @return
	 */
	private Node createBtnCompile() {
		Button btnCompile = new Button("COMPILE");
		btnCompile.setId(btnCompileId);
		btnCompile.setOnAction(new EventHandler<ActionEvent>() {

			/**
			 * Set the execution time label
			 *
			 * @param elementId
			 * @param execTime
			 */
			private void setExecutionTime(String elementId, long execTime) {
				Node execTimeLabel = RapidCodeRunnerCodeUI.this.getScene().lookup("#" + elementId + "_time");
				if (execTimeLabel != null) {
					String formattedTime = RapidCodeRunnerUtils.formatTime(execTime);
					((Label) execTimeLabel).setText("Action took " + formattedTime);
				}
			}

			@Override
			public void handle(ActionEvent event) {
				if (RapidCodeRunnerCodeUI.this.getScene() != null) {
					Node codeTextArea = RapidCodeRunnerCodeUI.this.getScene()
							.lookup("#" + RapidCodeRunnerCodeUI.codeTextAreaId);
					if (codeTextArea != null) {
						// Get the code and run it
						String codeContent = ((TextArea) codeTextArea).getText();

						RunnerJava runner = new RunnerJava(RapidCodeRunner.JAVA_HOME, codeContent);
						try {
							runner.compile();
							runner.runcode();
						} catch (Exception e) {
							// do nothing
						}

						// Print the compile status
						Node compileTextArea = RapidCodeRunnerCodeUI.this.getScene()
								.lookup("#" + RapidCodeRunnerResultUI.compileTextAreaId);
						if (compileTextArea != null) {
							((TextArea) compileTextArea).setText(runner.getCompileStatus().toString());
							this.setExecutionTime(RapidCodeRunnerResultUI.compileTextAreaId, runner.getCompileTime());
						}

						// Print the execution status
						Node runningTextArea = RapidCodeRunnerCodeUI.this.getScene()
								.lookup("#" + RapidCodeRunnerResultUI.runningTextAreaId);
						if (runningTextArea != null) {
							((TextArea) runningTextArea).setText(runner.getRunningStatus().toString());
							this.setExecutionTime(RapidCodeRunnerResultUI.runningTextAreaId, runner.getRunningTime());
						}

						// Change tab
						Node tabPane = RapidCodeRunnerCodeUI.this.getScene().lookup("#" + RapidCodeRunnerGUI.tabPaneId);
						if (tabPane != null) {
							SingleSelectionModel<Tab> selectionModel = ((TabPane) tabPane).getSelectionModel();
							selectionModel.select(1);
						}
					}
				}
			}
		});
		return btnCompile;
	}

	/**
	 * Create the code textArea
	 *
	 * @return
	 */
	private Node createCodeTextArea() {
		TextArea codeTextArea = new TextArea();
		codeTextArea.setPadding(new Insets(10));
		codeTextArea.setId(codeTextAreaId);
		codeTextArea.setText("System.out.println(\"Hello world\");");
		codeTextArea.setPrefHeight(this.getScene().heightProperty().doubleValue() - 100);
		return codeTextArea;
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	// Getters/Setters
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	private Scene getScene() {
		return this.scene;
	}
}
