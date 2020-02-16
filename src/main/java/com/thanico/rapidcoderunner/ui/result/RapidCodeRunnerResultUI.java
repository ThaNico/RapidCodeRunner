package com.thanico.rapidcoderunner.ui.result;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Class used to manage the code result interface
 *
 * @author Nicolas
 *
 */
public class RapidCodeRunnerResultUI {
	/**
	 * compileTextArea element id
	 */
	public final static String compileTextAreaId = "compileTextArea";

	/**
	 * runningTextArea element id
	 */
	public final static String runningTextAreaId = "runningTextArea";

	/**
	 * current scence
	 */
	private Scene scene;

	/**
	 * Constructor
	 *
	 * @param scene
	 */
	public RapidCodeRunnerResultUI(Scene scene) {
		this.scene = scene;
	}

	/**
	 * Returns what needs to be displayed for the result UI
	 *
	 * @return
	 */
	public Tab getResultUI() {
		return this.createResultTab();
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
	 *                         element id
	 * @param elementLabel
	 *                         element title above textarea
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
		resTextArea.setPrefHeight(this.getScene().heightProperty().doubleValue() - 100);
		resTextArea.setEditable(false);
		resTextArea.setFocusTraversable(false);
		resTextArea.setPrefWidth(this.getScene().widthProperty().doubleValue() / 2);
		return resTextArea;
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	// Getters/Setters
	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	private Scene getScene() {
		return this.scene;
	}
}
