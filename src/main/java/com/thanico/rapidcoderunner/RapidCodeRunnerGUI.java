package com.thanico.rapidcoderunner;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
		scene = new Scene(root, screenWidth, screenHeight, Color.TRANSPARENT);

		TabPane tabPane = new TabPane();
		tabPane.setId(tabPaneId);
		tabPane.getTabs().add(this.createCodeTab());
		tabPane.getTabs().add(this.createResultTab());

		// bind to take available space
		BorderPane borderPane = new BorderPane();
		borderPane.prefHeightProperty().bind(scene.heightProperty());
		borderPane.prefWidthProperty().bind(scene.widthProperty());
		borderPane.setBackground(new Background(new BackgroundFill(Color.POWDERBLUE, null, null)));
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

		Tab codeTab = new Tab("Code", vboxCode);
		codeTab.setClosable(false);

		return codeTab;
	}

	/**
	 * Button executing compile action
	 *
	 * @return
	 */
	private Node createBtnCompile() {
		Button btnCompile = new Button("Compile");
		btnCompile.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (getScene() != null) {
					Node codeTextArea = getScene().lookup("#" + codeTextAreaId);
					if (codeTextArea != null) {
						// Get the run and run it
						String codeContent = ((TextArea) codeTextArea).getText();
						RunnerJava runner = new RunnerJava(RapidCodeRunner.JAVA_HOME, codeContent);
						runner.compile();
						runner.runcode();

						// Print the compile status
						Node compileTextArea = getScene().lookup("#" + compileTextAreaId);
						if (compileTextArea != null) {
							((TextArea) compileTextArea).setText(runner.getCompileStatus().toString());
						}

						// Print the execution status
						Node runningTextArea = getScene().lookup("#" + runningTextAreaId);
						if (runningTextArea != null) {
							((TextArea) runningTextArea).setText(runner.getRunningStatus().toString());
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
		codeTextArea.setMinHeight(getScene().heightProperty().doubleValue() - 100);
		return codeTextArea;
	}

	/**
	 * Create the result tab
	 *
	 * @return
	 */
	private Tab createResultTab() {
		Node leftSide = this.createResultTextArea(compileTextAreaId, "Compilation result");
		Node rightSide = this.createResultTextArea(runningTextAreaId, "Execution result");

		HBox hboxRes = new HBox(10);
		hboxRes.setPadding(new Insets(10));
		hboxRes.setAlignment(Pos.CENTER);
		hboxRes.getChildren().addAll(leftSide, rightSide);

		Tab execTab = new Tab("Result", hboxRes);
		execTab.setClosable(false);
		return execTab;
	}

	/**
	 * Create a result textarea
	 *
	 * @param elementId
	 *            element id
	 * @param elementLabel
	 *            element title above textarea
	 * @return
	 */
	private Node createResultTextArea(String elementId, String elementLabel) {
		VBox vboxRes = new VBox(10);
		vboxRes.setPadding(new Insets(0, 10, 0, 10));
		vboxRes.setAlignment(Pos.CENTER);

		TextArea resTextArea = new TextArea();
		resTextArea.setPadding(new Insets(10));
		resTextArea.setId(elementId);
		resTextArea.setPrefHeight(getScene().heightProperty().doubleValue() - 100);
		resTextArea.setEditable(false);
		resTextArea.setFocusTraversable(false);
		resTextArea.setPrefWidth(getScene().widthProperty().doubleValue() / 2);

		vboxRes.getChildren().addAll(new Label(elementLabel), resTextArea);

		return vboxRes;
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	// Getters/Setters
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	public static Scene getScene() {
		return scene;
	}

}
