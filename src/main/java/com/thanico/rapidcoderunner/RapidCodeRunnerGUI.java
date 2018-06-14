package com.thanico.rapidcoderunner;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RapidCodeRunnerGUI extends Application {

	/**
	 * UI scene
	 */
	private static Scene scene;

	/**
	 * codeTextArea element id
	 */
	private final static String codeTextAreaId = "codeTextArea";

	/**
	 * tabPane element id
	 */
	private final static String tabPaneId = "tabPane";

	/**
	 * compileTextArea element id
	 */
	private final static String compileTextAreaId = "compileTextArea";

	/**
	 * runningTextArea element id
	 */
	private final static String runningTextAreaId = "runningTextArea";

	/**
	 * btnCompile element id
	 */
	private final static String btnCompileId = "btnCompile";

	/**
	 * Main
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}

	/**
	 * UI creation
	 */
	@Override
	public void start(Stage primaryStage) {
		// get the default screen size
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int screenWidth = gd.getDisplayMode().getWidth() - 100;
		int screenHeight = gd.getDisplayMode().getHeight() - 100;

		Group root = new Group();
		scene = new Scene(root, screenWidth, screenHeight);
		scene.getStylesheets().add("css/rcr.css");

		TabPane tabPane = new TabPane();
		tabPane.setId(tabPaneId);
		tabPane.getTabs().add(this.createCodeTab());
		tabPane.getTabs().add(this.createResultTab());

		// bind to take available space
		BorderPane borderPane = new BorderPane();
		borderPane.prefHeightProperty().bind(scene.heightProperty());
		borderPane.prefWidthProperty().bind(scene.widthProperty());
		borderPane.setCenter(tabPane);

		root.getChildren().add(borderPane);

		primaryStage.setScene(scene);
		primaryStage.show();
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
				Node execTimeLabel = getScene().lookup("#" + elementId + "_time");
				if (execTimeLabel != null) {
					// TODO clean that too
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
					((Label) execTimeLabel).setText("Action took " + formattedTime);
				}
			}

			@Override
			public void handle(ActionEvent event) {
				if (getScene() != null) {
					Node codeTextArea = getScene().lookup("#" + codeTextAreaId);
					if (codeTextArea != null) {
						// Get the run and run it
						String codeContent = ((TextArea) codeTextArea).getText();

						RunnerJava runner = new RunnerJava(RapidCodeRunner.JAVA_HOME, codeContent);
						try {
							runner.compile();
							runner.runcode();
						} catch (Exception e) {
							// do nothing
						}

						// Print the compile status
						Node compileTextArea = getScene().lookup("#" + compileTextAreaId);
						if (compileTextArea != null) {
							((TextArea) compileTextArea).setText(runner.getCompileStatus().toString());
							this.setExecutionTime(compileTextAreaId, runner.getCompileTime());
						}

						// Print the execution status
						Node runningTextArea = getScene().lookup("#" + runningTextAreaId);
						if (runningTextArea != null) {
							((TextArea) runningTextArea).setText(runner.getRunningStatus().toString());
							this.setExecutionTime(runningTextAreaId, runner.getRunningTime());
						}

						// Change tab
						Node tabPane = getScene().lookup("#" + tabPaneId);
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
		codeTextArea.setPrefHeight(getScene().heightProperty().doubleValue() - 100);
		return codeTextArea;
	}

	/**
	 * Create the result tab
	 *
	 * @return
	 */
	private Tab createResultTab() {
		Node leftSide = this.createResultBox(compileTextAreaId, "COMPILATION");
		Node rightSide = this.createResultBox(runningTextAreaId, "EXECUTION");

		HBox hboxRes = new HBox(10);
		hboxRes.setPadding(new Insets(10));
		hboxRes.setAlignment(Pos.CENTER);
		hboxRes.getChildren().addAll(leftSide, rightSide);

		Tab execTab = new Tab("RESULT", hboxRes);
		execTab.setClosable(false);
		return execTab;
	}

	/**
	 * Create a result box
	 *
	 * @param elementId
	 *            element id
	 * @param elementLabel
	 *            element title above textarea
	 * @return
	 */
	private Node createResultBox(String elementId, String elementLabel) {
		VBox vboxRes = new VBox(10);
		vboxRes.setPadding(new Insets(0, 10, 0, 10));
		vboxRes.setAlignment(Pos.CENTER);

		TextArea resTextArea = this.createResultTextArea(elementId);

		Label resLabel = new Label(elementLabel);
		resLabel.setId(elementId + "_label");
		resLabel.setPrefWidth(resTextArea.getPrefWidth());

		Label timeLabel = new Label();
		timeLabel.setId(elementId + "_time");
		timeLabel.setPrefWidth(resTextArea.getPrefWidth());

		vboxRes.getChildren().addAll(resLabel, resTextArea, timeLabel);
		return vboxRes;
	}

	/**
	 * Create a result textarea
	 *
	 * @param elementId
	 * @return
	 */
	private TextArea createResultTextArea(String elementId) {
		TextArea resTextArea = new TextArea();
		resTextArea.setPadding(new Insets(10));
		resTextArea.setId(elementId);
		resTextArea.setPrefHeight(getScene().heightProperty().doubleValue() - 100);
		resTextArea.setEditable(false);
		resTextArea.setFocusTraversable(false);
		resTextArea.setPrefWidth(getScene().widthProperty().doubleValue() / 2);
		return resTextArea;
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	// Getters/Setters
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	public static Scene getScene() {
		return scene;
	}

}
