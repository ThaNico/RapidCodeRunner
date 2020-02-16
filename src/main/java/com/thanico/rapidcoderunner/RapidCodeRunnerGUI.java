package com.thanico.rapidcoderunner;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.thanico.rapidcoderunner.ui.RapidCodeRunnerCodeUI;
import com.thanico.rapidcoderunner.ui.RapidCodeRunnerResultUI;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RapidCodeRunnerGUI extends Application {

	/**
	 * tabPane element id
	 */
	public final static String tabPaneId = "tabPane";

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
		Scene scene = new Scene(root, screenWidth, screenHeight);
		scene.getStylesheets().add("css/rcr.css");

		TabPane tabPane = new TabPane();
		tabPane.setId(tabPaneId);

		RapidCodeRunnerCodeUI codeUI = new RapidCodeRunnerCodeUI(scene);
		tabPane.getTabs().add(codeUI.getCodeUI());

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
