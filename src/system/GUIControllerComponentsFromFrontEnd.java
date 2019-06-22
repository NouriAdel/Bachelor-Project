package system;

import java.util.ArrayList;
import components.Component;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
import system.Controllers.SystemController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import system.Controllers.*;

public class GUIControllerComponentsFromFrontEnd {
	@FXML
	private HBox hbox;
	
	private HBox secondaryOperatorsHbox;
	private HBox secondaryEventsHbox;

	@FXML
	private VBox vbox;
	
	@FXML
	private VBox eventListVBox;

	@FXML
	private Label topEvent;

	@FXML
	private Pane treePane;

	@FXML
	private Label operator;
	
	@FXML
	private Tab resultTab;
	
	@FXML
	private AnchorPane resultPane;
	
	@FXML
	private AnchorPane eventListresultPane;
	
	public ArrayList<Label> eventListLabelList;
	public ArrayList<Label> eventListSecondaryLabelList;	
	
	public ArrayList<TreeEvent> list;
	public ArrayList<Label> labelList;
	public ArrayList<Label> secondaryLabelList;
	public ArrayList<ComponentLabel> allComponents;
	
	public Label [] secondaryLabels;
	public int itr;
	
	public String topEventLabel; 
	public ArrayList<Line> connectingLines;
	
	@FXML
	private HomeController homeViewController;
	
	@FXML
	private SystemController systemViewController;
	
	@FXML
	private CreateComponentController createComponentViewController;
	
	@FXML
	private ModifyComponentController modifyComponentViewController;	

	@FXML
	private ScrollPane scrollPane;
	
	public  void reConstructComponentsControllerTest(ArrayList<TreeEvent> list, String topEventText) {
		
		this.list = list;
		this.topEventLabel = topEventText;
		
		secondaryLabels = new Label[1000];
		itr = 0;
		
		resultPane.getChildren().clear();
		vbox.getChildren().clear();
		vbox.getChildren().add(topEvent);
		vbox.getChildren().add(operator);
		hbox.getChildren().clear();
		vbox.getChildren().add(hbox);
		resultPane.getChildren().add(vbox);


		reinitialize();
		
	}
	
	public void setButtonsActions() {
		homeViewController.createCompBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				homeViewController.setVisible(false);
				modifyComponentViewController.setVisible(false);
				createComponentViewController.setVisible(true);
				
			}
            
        });
		
		homeViewController.modifyCompBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				homeViewController.setVisible(false);
				modifyComponentViewController.setVisible(true);
				createComponentViewController.setVisible(false);
				
				ComponentLabel tobeModifiedComponent = ((Component) homeViewController.modifyCompCombobox.getValue()).label;	
				
				modifyComponentViewController.preModifyComponent(tobeModifiedComponent, allComponents);
			}
            
        });
				
		createComponentViewController.createCompCancelBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				initialVisibility();	
				
			}
            
        });
		
		createComponentViewController.createCompCreateBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {			
				ComponentLabel comp = createComponentViewController.onCreateComponent();
				System.out.println("New component" + comp.component);
				System.out.println(allComponents);
				allComponents.add(comp);
				homeViewController.modifyCompCombobox.getItems().add(comp.component);
//				homeViewController.initializeTopEventCheckBoxes(allComponents);
//				homeViewController.addTopEventCheckBoxForComponent(comp.component);
				systemViewController.viewNewComponent(comp);
				
				comp.setOnMousePressed(event -> {
					comp.mouseX = event.getSceneX();
					comp.mouseY = event.getSceneY();
		        });

		        comp.setOnMouseDragged(event -> {
		           double deltaX = event.getSceneX() - comp.mouseX;
		           double deltaY = event.getSceneY() - comp.mouseY;
		           comp.relocate(comp.getLayoutX() + deltaX, comp.getLayoutY() + deltaY);
		           comp.mouseX = event.getSceneX();
		           comp.mouseY = event.getSceneY();
		           drawConnections();
		        });
				
				initialVisibility();	
			}
            
        });
		
		modifyComponentViewController.modifyCompCancelBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				initialVisibility();			
			}
            
        });
		
		modifyComponentViewController.modifyCompmodifyBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {			
				Component component = modifyComponentViewController.onModifyComponent();
				homeViewController.modifyCompCombobox.getItems().remove(component);
				homeViewController.modifyCompCombobox.getItems().add(component);
//				homeViewController.initializeTopEventCheckBoxes(allComponents);
//				homeViewController.addTopEventCheckBoxForComponent(component);
				initialVisibility();	
				drawConnections();
//				Component [] systemComponents = new Component[allComponents.size()];
//				int i = 0;
//				for (ComponentLabel compLabel: allComponents) {
//					systemComponents[i++] = compLabel.component;
//				}
//				list = WBSListOfListsApproach.testCheckBeforeInjecting(systemComponents);
//				System.out.println("All components: " + allComponents);
//				System.out.println(list);
//				reConstructComponentsControllerTest(list, "Top Event");
			}
            
        });
		
		homeViewController.generateTreeBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				System.out.println("Generating tree ...");
				
				String topEventText = "";
				
				Component [] systemComponents = new Component[allComponents.size()];
				int i = 0;
				for (ComponentLabel compLabel: allComponents) {
					Component component = compLabel.component;
					if (component.isTopLevelComponent) {
						if (topEventText.length() > 0)
							topEventText += " and " + component.name;
						else 
							topEventText += "Top Event: " + component.name;
					}
					systemComponents[i++] = component;
				}
				
				list = NewFrontEndInput.prepareTreeFromFrontEndComponents(systemComponents);
				System.out.println("All components: " + allComponents);
				System.out.println(list);
				reConstructComponentsControllerTest(list, topEventText);
			}
            
        });
		
	}
	
	public void initialVisibility() {
		homeViewController.setVisible(true);
		modifyComponentViewController.setVisible(false);
		createComponentViewController.setVisible(false);
		
	}
	
	public void reinitialize() {
		
		System.out.println("New list: " + list);
		
		initialVisibility();
//		setButtonsActions();
//		allComponents = new ArrayList<ComponentLabel>(); // srsly?!!!

		int padding = 10;
		labelList = new ArrayList();
		secondaryLabelList = new ArrayList();

		// since we know that it's the top level event and the operation is OR

		operator.setText("OR");
		operator.setStyle("-fx-background-color: #effcfa; -fx-padding: 10px; ");
		operator.autosize();

		topEvent.setText(topEventLabel);
		topEvent.setStyle("-fx-background-color: #effcfa; -fx-padding: 10px; ");
		topEvent.autosize();

		// spacing can be adjusted according to the number of elements in the list for
		// example
		hbox.setStyle("-fx-spacing: 10;");
		vbox.setStyle("-fx-spacing: 30;");
		vbox.setAlignment(Pos.CENTER);
		
		secondaryOperatorsHbox = new HBox();
		secondaryOperatorsHbox.setStyle("-fx-spacing: 150;");
		secondaryOperatorsHbox.setAlignment(Pos.CENTER);
		
		secondaryEventsHbox = new HBox();
		secondaryEventsHbox.setStyle("-fx-spacing: 30;");
		secondaryEventsHbox.setAlignment(Pos.CENTER);

		for (TreeEvent event : list) {
			
			int eventSize = event.componentNames.length; 
		
			Label label = new Label(event.description);
			label.setWrapText(true);
//			label.autosize();
			label.setStyle("-fx-background-color: #effcfa; -fx-padding: 10px; -fx-text-wrap: true;");
			label.setPrefSize(150, 120);
			label.setTextAlignment(TextAlignment.CENTER);
			labelList.add(label);
			
			if (eventSize > 1) {
				Label secondaryOperator = new Label(event.operator);
				customizeOperatorLabel(secondaryOperator);
				
				Label secondarylabel = new Label("test");
				
				int minIndex = itr;
				int maxIndex = itr + eventSize;
				
				for (int i = 0; i < eventSize; i++) {
					secondarylabel = new Label("loss of: " + event.componentNames[i] + " due to: " + event.componentFailures[i]);
					customizeSecondaryLabel(secondarylabel);
					secondaryLabelList.add(secondarylabel);
					secondaryLabels[itr++] = secondarylabel;
					
					
					
				}
				
				Platform.runLater(new Runnable() {
					public void run() {		
						drawLineBetween2Label(resultPane, label, secondaryOperator);	
						
						for (int i = minIndex; i < maxIndex; i++) {
							drawLineBetween2Label(resultPane, secondaryOperator, secondaryLabels[i]);
						}
						
					}
				});
			
				secondaryOperatorsHbox.getChildren().add(secondaryOperator);
				secondaryEventsHbox.getChildren().addAll(secondaryLabelList);
				secondaryLabelList.clear();
				
			}
			
			
			
			Platform.runLater(new Runnable() {
				public void run() {				
					drawLineBetween2Label(resultPane, operator, label);
				}
			});
		}	
		hbox.getChildren().addAll(labelList);
		vbox.getChildren().add(secondaryOperatorsHbox);
		vbox.getChildren().add(secondaryEventsHbox);
		
		
		Platform.runLater(new Runnable() {
			public void run() {				
				drawLineBetween2Label(resultPane, topEvent, operator);
			}
		});

		
		// Add the events in the events list tab
		// VBox eventListVBox = new VBox();
		eventListVBox.setStyle("-fx-spacing: 10;");
		eventListVBox.setAlignment(Pos.CENTER);

		Label topEventlabel = new Label("Top Event: " + this.topEventLabel);

		customizeEventListTopEvent(topEventlabel);
		eventListVBox.getChildren().add(topEventlabel);

		// Label topEventOperator = new Label("OR");
		// customizeEventListLabel(topEventOperator);
		// eventListVBox.getChildren().add(topEventOperator);

		for (TreeEvent event : list) {

			int eventSize = event.componentNames.length;

			Label label = new Label("\t -> " + event.description);
			customizeEventListLabel1(label);
			eventListLabelList.add(label);
			eventListVBox.getChildren().add(label);

			if (eventSize > 1) {
				Label secondarylabel = new Label("test");
				for (int i = 0; i < eventSize; i++) {
					secondarylabel = new Label("\t \t - " + "loss of: " + event.componentNames[i] + " due to: "
							+ event.componentFailures[i]);
					customizeEventListLabel2(secondarylabel);

					eventListVBox.getChildren().add(secondarylabel);

				}
			}
		} 
	}
	
	public void initialize() {		
		initialVisibility();
		setButtonsActions();
		allComponents = new ArrayList<ComponentLabel>();
		connectingLines = new ArrayList();
		eventListLabelList = new ArrayList();
	}
	
	public void drawConnections() {

		systemViewController.mainPain.getChildren().removeAll(connectingLines);
		connectingLines = new ArrayList();

		ComponentLabel[] componentLabels = allComponents.toArray(new ComponentLabel[allComponents.size()]);

		for (int i = 0; i < componentLabels.length; i++) {
			ComponentLabel compLabel1 = componentLabels[i];
			Component comp = componentLabels[i].component;
			for (int j = 0; j < comp.inputPorts.length; j++) {
				if (comp.inputPorts[j] != null) {
					ComponentLabel compLabel2 = comp.inputPorts[j].label;

					Platform.runLater(new Runnable() {
						public void run() {
							Line line = drawLineBetween2Label(systemViewController.mainPain, compLabel2, compLabel1);
							connectingLines.add(line);
						}
					});
				}
				
			}
			
			for (int j = 0; j < comp.outputPorts.length; j++) {
				if (comp.outputPorts[j] != null) {
					ComponentLabel compLabel2 = comp.outputPorts[j].label;

					Platform.runLater(new Runnable() {
						public void run() {
							Line line = drawLineBetween2Label(systemViewController.mainPain, compLabel2, compLabel1);
							connectingLines.add(line);
						}
					});
				}
			}
		}

	}

	public void customizeEventListTopEvent(Label label) {
		label.setWrapText(true);
		label.setStyle("-fx-background-color: #93adab; -fx-padding: 5px; -fx-text-wrap: true;");
		label.setPrefSize(1200, 30);
		label.setTextAlignment(TextAlignment.CENTER);
	}

	public void customizeEventListLabel1(Label label) {
		label.setWrapText(true);
		label.setStyle("-fx-background-color: #d7e2e1; -fx-padding: 5px; -fx-text-wrap: true;");
		label.setPrefSize(1200, 30);
		label.setTextAlignment(TextAlignment.CENTER);
	}

	public void customizeEventListLabel2(Label label) {
		label.setWrapText(true);
		label.setStyle("-fx-background-color: #e6f2f0; -fx-padding: 5px; -fx-text-wrap: true;");
		label.setPrefSize(1200, 30);
		label.setTextAlignment(TextAlignment.CENTER);
	}
	
	public void customizeLabel(Label label) {
		label.setWrapText(true);
		label.setStyle("-fx-background-color: #effcfa; -fx-padding: 10px; -fx-text-wrap: true;");
		label.setPrefSize(150, 120);
		label.setTextAlignment(TextAlignment.CENTER);
	}
	
	public void customizeOperatorLabel(Label label) {
		label.setWrapText(true);
		label.setStyle("-fx-background-color: #effcfa; -fx-padding: 10px; -fx-text-wrap: true;");
		label.setPrefSize(50, 50);
		label.setTextAlignment(TextAlignment.CENTER);
	}
	
	public void customizeSecondaryLabel(Label label) {
		label.setWrapText(true);
		label.setStyle("-fx-background-color: #effcfa; -fx-padding: 10px; -fx-text-wrap: true;");
		label.setPrefSize(100, 100);
		label.setTextAlignment(TextAlignment.CENTER);
	}

	public Line drawLineBetween2Label(Pane commonAncestor, Label upperLabel, Label lowerLabel) {
		
		Bounds bounds1 = getRelativeBounds(upperLabel, commonAncestor);
		Bounds bounds2 = getRelativeBounds(lowerLabel, commonAncestor);
		
		Bounds upperLabelToCommonAnscestor;
		Bounds lowerLabelToCommonAnscestor;
		
		if (bounds1.getMaxY() < bounds2.getMaxY()) {
			upperLabelToCommonAnscestor = bounds1;
			lowerLabelToCommonAnscestor = bounds2;
		}
		else {
			upperLabelToCommonAnscestor = bounds2;
			lowerLabelToCommonAnscestor = bounds1;
		}

		Point2D operatorCenter = getCenter(upperLabelToCommonAnscestor);
		Point2D labelCenter = getCenter(lowerLabelToCommonAnscestor);

		double maxYL1 = upperLabelToCommonAnscestor.getMaxY();
		double minYl2 = lowerLabelToCommonAnscestor.getMinY();

		Line levelToOperator = new Line(operatorCenter.getX(), maxYL1, labelCenter.getX(), minYl2);
		commonAncestor.getChildren().add(levelToOperator);

		return levelToOperator;
	}
	// https://stackoverflow.com/questions/43115807/how-to-draw-line-between-two-nodes-placed-in-different-panes-regions/43119383

	private Bounds getRelativeBounds(Node node, Node relativeTo) {
		Bounds nodeBoundsInScene = node.localToScene(node.getBoundsInLocal());
		return relativeTo.sceneToLocal(nodeBoundsInScene);
	}

	private Point2D getCenter(Bounds b) {
		return new Point2D(b.getMinX() + b.getWidth() / 2, b.getMinY() + b.getHeight() / 2);
	}
	
}
