package com.thanico.rapidcoderunner.ui;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.thanico.rapidcoderunner.ui.code.RapidCodeRunnerCodeUI;
import com.thanico.rapidcoderunner.ui.result.RapidCodeRunnerResultUI;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main UI
 *
 * @author Nicolas
 *
 */
public class RapidCodeRunnerUI extends Application {

	/**
	 * CSS file for the UI
	 */
	private static final String CSS_FILE_PATH = "css/rcr.css";

	/**
	 * tabPane element id
	 */
	public final static String tabPaneId = "tabPane";

	/**
	 * UI creation
	 */
	@Override
	public void start(Stage primaryStage) {
		// get the default screen size
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int screenWidth = gd.getDisplayMode().getWidth() - 100;
		int screenHeight = gd.getDisplayMode().getHeight() - 100;

		// Create the scene with css styling
		Group root = new Group();
		Scene scene = new Scene(root, screenWidth, screenHeight);
		scene.getStylesheets().add(CSS_FILE_PATH);

		// Init the tab pane
		TabPane tabPane = new TabPane();
		tabPane.setId(tabPaneId);

		// Add the code tab
		RapidCodeRunnerCodeUI codeUI = new RapidCodeRunnerCodeUI(scene, System.getenv("JAVA_HOME"));
		tabPane.getTabs().add(codeUI.getCodeUI());

		// Add the compile result tab
		RapidCodeRunnerResultUI resultUI = new RapidCodeRunnerResultUI(scene);
		tabPane.getTabs().add(resultUI.getResultUI());

		// bind to take available space
		BorderPane borderPane = new BorderPane();
		borderPane.prefHeightProperty().bind(scene.heightProperty());
		borderPane.prefWidthProperty().bind(scene.widthProperty());
		borderPane.setCenter(tabPane);

		root.getChildren().add(borderPane);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
