package STAMP;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

import com.sl.app.TestApplication;

import components.Component;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class MainExample extends Application{
	
	String losses;
	String hazards;
	String constraints;
	ArrayList <String> control=new ArrayList();
	ArrayList <String> process=new ArrayList();
	ArrayList <String> processControl = new ArrayList();
	ArrayList <String> ucas = new ArrayList();
	ArrayList <String> scenarios = new ArrayList();
	
	public static void main (String[]args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
	
		primaryStage.setTitle("STPA method");
		Button Controller = new Button ("Controller");
		Button ControlledProcess = new Button ("Controlled Process");
		Button STAMP = new Button ("Show STAMP Hierarchy");
		
		Label STPASteps = new Label("There FOUR steps to apply STPA method :");
		Label Step1 = new Label("-The first step is to : Define purpose of the analysis");
		Label Step2 = new Label("-The second step is to : Model the control structure");
		Label Step3 = new Label("-The third step is to : Identify unsafe control actions");
		Label Step4 = new Label("-The fourth step is to : Identify loss scenarios");
		Label Step1_1 = new Label("1.1)Identify losses");
		Label Step1_2 = new Label("1.2)Identify system-level hazards");
		Label Step1_3 = new Label("1.3)Identify system-level safety constraints");
		Label Step1_4 = new Label("1.4)Refine hazards (optional)");
		Button losses = new Button("Identify Losses");
		Button hazards = new Button("Identify Hazards");
		Button constraints = new Button("Identify Constraints");
		Button uca = new Button("Identify UCA");
		Button scenarios = new Button("Identify Scenarios");
		Button uca2 = new Button("Identify UCA 2");
		Button constAction = new Button("Show Constraining Control Actions");
		Label res = new Label("The Result");
		Button uca3 = new Button("Identify UCA 3");
		Button scenarios2 = new Button("Show Scenarios");
		
		losses.setOnAction(e->identifyLosses());
		hazards.setOnAction(e->identifyHazards());
		constraints.setOnAction(e->identifyConstraints());
		uca.setOnAction(e->ucas());
		scenarios.setOnAction(e->lossScens());
		uca2.setOnAction(e->ucas2());
		constAction.setOnAction(e->showAction());
		uca3.setOnAction(e->ucas3());
		scenarios2.setOnAction(e->showScen());
		
		Controller.setOnAction(e -> showController());
		ControlledProcess.setOnAction(e -> showControlledProcess());
		STAMP.setOnAction(e -> showSTAMP());
		
		VBox pane = new VBox(10);
		pane.setPadding(new Insets (10));
		//pane.getChildren().add(Controller);
		pane.getChildren().addAll(STPASteps,Step1 , Step1_1 ,losses , Step1_2 , hazards , Step1_3 , constraints , Step1_4 ,Step2 , Controller,ControlledProcess,STAMP,Step3 ,uca ,uca2 ,uca3 , Step4 , scenarios ,scenarios2 ,res ,constAction);
		Scene scene = new Scene (pane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	

	

	

	private void showController(){
		/*//Stage stage = null;
		Button constraints2 = new Button("Identify Constraints");
		VBox pane2 = new VBox(10);
		pane2.setPadding(new Insets (10));
		//pane.getChildren().add(Controller);
		pane2.getChildren().addAll(constraints2);
		pane2.setVisible(true);
		Scene scene2 = new Scene (pane2);
		stage.setScene(scene2);
		stage.show();*/
		
		  EventQueue.invokeLater(new Runnable()
	        {
	            @Override
	            public void run()
	            {
	                JFrame frame = new JFrame("Controller");
	                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	                try 
	                {
	                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	                } catch (Exception e) {
	                   e.printStackTrace();
	                }
	                JPanel panel = new JPanel();
	                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	                panel.setOpaque(true);
	                JTextArea textArea = new JTextArea(15, 50);
	                textArea.setWrapStyleWord(true);
	                textArea.setEditable(false);
	                textArea.setFont(Font.getFont(Font.SANS_SERIF));
	                JScrollPane scroller = new JScrollPane(textArea);
	                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	                JPanel inputpanel = new JPanel();
	                inputpanel.setLayout(new FlowLayout());
	                JTextField input = new JTextField(20);
	                JButton button = new JButton("Enter");
	                DefaultCaret caret = (DefaultCaret) textArea.getCaret();
	                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	                panel.add(scroller);
	                inputpanel.add(input);
	                inputpanel.add(button);
	                panel.add(inputpanel);
	                frame.getContentPane().add(BorderLayout.CENTER, panel);
	                frame.pack();
	                frame.setLocationByPlatform(true);
	                frame.setVisible(true);
	                frame.setResizable(false);
	                input.requestFocus();
	                textArea.setText("Enter the controllers : ");
	                
	                button.addActionListener(new ActionListener(){
	                	public void  actionPerformed(ActionEvent e){
	                		String a = input.getText();
	                		textArea.append("\n"+a);
	                		textArea.setLineWrap(true);
	                		control.add(a);
	                		System.out.println(control);
	                	}
	                });
	            }
	        });
		  
	}
	
	private void showControlledProcess(){
		
		  EventQueue.invokeLater(new Runnable()
	        {
	            @Override
	            public void run()
	            {
	                JFrame frame = new JFrame("Controlled Processes");
	                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	                try 
	                {
	                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	                } catch (Exception e) {
	                   e.printStackTrace();
	                }
	                JPanel panel = new JPanel();
	                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	                panel.setOpaque(true);
	                JTextArea textArea = new JTextArea(15, 50);
	                textArea.setWrapStyleWord(true);
	                textArea.setEditable(false);
	                textArea.setFont(Font.getFont(Font.SANS_SERIF));
	                JScrollPane scroller = new JScrollPane(textArea);
	                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	                JPanel inputpanel = new JPanel();
	                inputpanel.setLayout(new FlowLayout());
	                JTextField input = new JTextField(20);
	                JButton button = new JButton("Enter");
	                DefaultCaret caret = (DefaultCaret) textArea.getCaret();
	                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	                panel.add(scroller);
	                inputpanel.add(input);
	                inputpanel.add(button);
	                panel.add(inputpanel);
	                frame.getContentPane().add(BorderLayout.CENTER, panel);
	                frame.pack();
	                frame.setLocationByPlatform(true);
	                frame.setVisible(true);
	                frame.setResizable(false);
	                input.requestFocus();
	                
	                JComboBox combo = new JComboBox();
	                inputpanel.add(combo);
	                for (String a : control){
	                	combo.addItem(a);
	                }
	                
	                textArea.setText("Enter the controlled processes : ");
	                
	                button.addActionListener(new ActionListener(){
	                	public void  actionPerformed(ActionEvent e){
	                		String a = input.getText();
	                		String b = (String) combo.getSelectedItem();
	                		textArea.append("\n"+"Contoller : "+b+"  ,  "+ "Controlled Process : "+ a );
	                		textArea.setLineWrap(true);
	                		process.add(a);
	                		processControl.add(b);
	                		System.out.println(process);
	                		System.out.println(processControl);
	                	}
	                });
	            }
	        });
		
	}
	
	private void showSTAMP(){
		
		TestMainExample t = new TestMainExample(processControl , process , control);
		t.show();
		
		 /* EventQueue.invokeLater(new Runnable()
	        {
	            @Override
	            public void run()
	            {
	                JFrame frame = new JFrame("Test");
	                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	                try 
	                {
	                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	                } catch (Exception e) {
	                   e.printStackTrace();
	                }
	                JPanel panel = new JPanel();
	                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	                panel.setOpaque(true);
	                //JTextArea textArea = new JTextArea(15, 50);
	                //textArea.setWrapStyleWord(true);
	                //textArea.setEditable(false);
	                //textArea.setFont(Font.getFont(Font.SANS_SERIF));
	                //JScrollPane scroller = new JScrollPane(textArea);
	                //scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	                //scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	                JPanel inputpanel = new JPanel();
	                inputpanel.setLayout(new FlowLayout());
	                //JTextField input = new JTextField(20);
	               // JButton button = new JButton("Enter");
	                //DefaultCaret caret = (DefaultCaret) textArea.getCaret();
	                //caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	                //panel.add(scroller);
	                //inputpanel.add(input);
	               // inputpanel.add(button);
	                JLabel c = new JLabel("Controllers");
	                inputpanel.add(c);
	                for (String a : process){
	                	JLabel l = new JLabel();
	                	l.setText(a);
	                	inputpanel.add(l);
	                }
	                JLabel a = new JLabel("hhh");
	                JLabel b = new JLabel("jjj");
	                inputpanel.add(a);
	                inputpanel.add(b);
	                Pane main = new Pane();
	                Label aa = new Label("hhh");
	                Label bb = new Label("jjj");
	                main.getChildren().add(aa);
	                main.getChildren().add(bb);
	                
	                //main.getChildren().add(a);
	                //main.getChildren().add(b);
	                Platform.runLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							drawLineBetween2Label(main, aa, bb);
							
						}
					});
	                
	                panel.add(inputpanel);
	                frame.getContentPane().add(BorderLayout.CENTER, panel);
	                frame.pack();
	                frame.setLocationByPlatform(true);
	                frame.setVisible(true);
	                frame.setResizable(false);
	              //  input.requestFocus();
	            }
	        });
		*/
	}
	
/*public void initialize() {
		
		Platform.runLater(new Runnable() {
			public void run() {
				drawLineBetween2Label(mainPane, mainPump, mainPipe);
				drawLineBetween2Label(mainPane, mainPipe, valveA);
				drawLineBetween2Label(mainPane, mainPipe, valveB);
				drawLineBetween2Label(mainPane, valveA, pipeA);
				drawLineBetween2Label(mainPane, valveB, pipeB);
			}
		});
			
		
	}*/
	
/*public Line drawLineBetween2Label(Pane commonAncestor, Label upperLabel, Label lowerLabel) {
		
		Bounds bounds1 = getRelativeBounds(upperLabel, commonAncestor);
		Bounds bounds2 = getRelativeBounds(lowerLabel, commonAncestor);
		
		Bounds upperLabelToCommonAnscestor = bounds1;
		Bounds lowerLabelToCommonAnscestor = bounds2;
		
//		if (bounds1.getMaxY() < bounds2.getMaxY()) {
//			upperLabelToCommonAnscestor = bounds1;
//			lowerLabelToCommonAnscestor = bounds2;
//		}
//		else {
//			upperLabelToCommonAnscestor = bounds2;
//			lowerLabelToCommonAnscestor = bounds1;
//		}

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
*/	
	private void identifyLosses(){
		
		/*Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Step 1.1 : Identify Losses");
		alert.setHeaderText("What are the losses you're mostly concerned with ?");*/
		
		TextInputDialog loss = new TextInputDialog("Enter the considered losses");
		loss.setTitle("Step 1.1 : Identify Losses");
		loss.setHeaderText("What are the losses you're mostly concerned with ?");
		loss.showAndWait();
		losses = loss.getEditor().getText();
		System.out.println(losses);
	}
	
	private void identifyHazards(){
		
		TextInputDialog hazard = new TextInputDialog("Enter the considered hazards");
		hazard.setTitle("Step 1.2 : Identify Hazards");
		hazard.setHeaderText("What are the system-level hazards you're mostly concerned with ?");
		hazard.showAndWait();
		hazards = hazard.getEditor().getText();
		System.out.println(hazards);
		
		/*TestMainExample t = new TestMainExample(processControl , process , control);
		t.show();*/
	}

	private void identifyConstraints(){
		
		TextInputDialog constraint = new TextInputDialog("Enter the considered constraints");
		constraint.setTitle("Step 1.3 : Identify Constraints");
		constraint.setHeaderText("What are the system-level safety constraints you're mostly concerned with ?");
		constraint.showAndWait();
		constraints = constraint.getEditor().getText();
		System.out.println(constraints);
	}
	
	private void ucas(){
		/*//Stage stage = null;
		Button constraints2 = new Button("Identify Constraints");
		VBox pane2 = new VBox(10);
		pane2.setPadding(new Insets (10));
		//pane.getChildren().add(Controller);
		pane2.getChildren().addAll(constraints2);
		pane2.setVisible(true);
		Scene scene2 = new Scene (pane2);
		stage.setScene(scene2);
		stage.show();*/
		
		  EventQueue.invokeLater(new Runnable()
	        {
	            @Override
	            public void run()
	            {
	                JFrame frame = new JFrame("Unsafe Control Actions");
	                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	                try 
	                {
	                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	                } catch (Exception e) {
	                   e.printStackTrace();
	                }
	                JPanel panel = new JPanel();
	                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	                panel.setOpaque(true);
	                JTextArea textArea = new JTextArea(15, 50);
	                textArea.setWrapStyleWord(true);
	                textArea.setEditable(false);
	                textArea.setFont(Font.getFont(Font.SANS_SERIF));
	                JScrollPane scroller = new JScrollPane(textArea);
	                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	                JPanel inputpanel = new JPanel();
	                inputpanel.setLayout(new FlowLayout());
	                JTextField input = new JTextField(20);
	                JButton button = new JButton("Enter");
	                DefaultCaret caret = (DefaultCaret) textArea.getCaret();
	                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	                panel.add(scroller);
	                inputpanel.add(input);
	                inputpanel.add(button);
	                panel.add(inputpanel);
	                frame.getContentPane().add(BorderLayout.CENTER, panel);
	                frame.pack();
	                frame.setLocationByPlatform(true);
	                frame.setVisible(true);
	                frame.setResizable(false);
	                input.requestFocus();
	                textArea.setText("Enter the UCAs : ");
	                
	                button.addActionListener(new ActionListener(){
	                	public void  actionPerformed(ActionEvent e){
	                		String a = input.getText();
	                		textArea.append("\n"+a);
	                		textArea.setLineWrap(true);
	                		ucas.add(a);
	                		System.out.println(ucas);
	                	}
	                });
	            }
	        });
		  
	}
	
	private void lossScens(){
		/*//Stage stage = null;
		Button constraints2 = new Button("Identify Constraints");
		VBox pane2 = new VBox(10);
		pane2.setPadding(new Insets (10));
		//pane.getChildren().add(Controller);
		pane2.getChildren().addAll(constraints2);
		pane2.setVisible(true);
		Scene scene2 = new Scene (pane2);
		stage.setScene(scene2);
		stage.show();*/
		
		  EventQueue.invokeLater(new Runnable()
	        {
	            @Override
	            public void run()
	            {
	                JFrame frame = new JFrame("Loss Scenarios");
	                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	                try 
	                {
	                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	                } catch (Exception e) {
	                   e.printStackTrace();
	                }
	                JPanel panel = new JPanel();
	                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	                panel.setOpaque(true);
	                JTextArea textArea = new JTextArea(15, 50);
	                textArea.setWrapStyleWord(true);
	                textArea.setEditable(false);
	                textArea.setFont(Font.getFont(Font.SANS_SERIF));
	                JScrollPane scroller = new JScrollPane(textArea);
	                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	                JPanel inputpanel = new JPanel();
	                inputpanel.setLayout(new FlowLayout());
	                JTextField input = new JTextField(20);
	                JButton button = new JButton("Enter");
	                DefaultCaret caret = (DefaultCaret) textArea.getCaret();
	                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	                panel.add(scroller);
	                inputpanel.add(input);
	                inputpanel.add(button);
	                panel.add(inputpanel);
	                frame.getContentPane().add(BorderLayout.CENTER, panel);
	                frame.pack();
	                frame.setLocationByPlatform(true);
	                frame.setVisible(true);
	                frame.setResizable(false);
	                input.requestFocus();
	                textArea.setText("Enter the Loss Scenarios : ");
	                
	                button.addActionListener(new ActionListener(){
	                	public void  actionPerformed(ActionEvent e){
	                		String a = input.getText();
	                		textArea.append("\n"+a);
	                		textArea.setLineWrap(true);
	                		scenarios.add(a);
	                		System.out.println(scenarios);
	                	}
	                });
	            }
	        });
		  
	}
	
	private void showAction() {
		// TODO Auto-generated method stub
		
		EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame frame = new JFrame("Constraining Control Actions");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                try 
                {
                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                   e.printStackTrace();
                }
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setOpaque(true);
                JTextArea textArea = new JTextArea(15, 50);
                textArea.setWrapStyleWord(true);
                textArea.setEditable(false);
                textArea.setFont(Font.getFont(Font.SANS_SERIF));
                JScrollPane scroller = new JScrollPane(textArea);
                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                JPanel inputpanel = new JPanel();
                inputpanel.setLayout(new FlowLayout());
                //JTextField input = new JTextField(20);
                //JButton button = new JButton("Enter");
                DefaultCaret caret = (DefaultCaret) textArea.getCaret();
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
                panel.add(scroller);
                //inputpanel.add(input);
                //inputpanel.add(button);
                panel.add(inputpanel);
                frame.getContentPane().add(BorderLayout.CENTER, panel);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                frame.setResizable(false);
                //input.requestFocus();
                textArea.setText("Here are the Constraining Actions : ");
                
                for (int i=0;i<i1.size();i++) {
                	textArea.append("\n"+i1.get(i)+" ");
                	textArea.append(i7.get(i).equals("+")? "must not"+" ":"must"+" ");
                	textArea.append(i3.get(i)+" "+i4.get(i)+" "+i5.get(i));
                }
                
//                button.addActionListener(new ActionListener(){
//                	public void  actionPerformed(ActionEvent e){
//                		String a = input.getText();
//                		textArea.append("\n"+a);
//                		textArea.setLineWrap(true);
//                		scenarios.add(a);
//                		System.out.println(scenarios);
//                	}
//                });
            }
        });
		
		
	}

	private void ucas2() {
		// TODO Auto-generated method stub
		
		ArrayList<String> cases = new ArrayList<>(Arrays.asList("Provide a situation when not applying the control signal causes hazard","Provide a situation when applying the control signal causes hazard" , "What happens when the control signal is applied too early/too late?" , "What happens when the control signal is stopped too soon/applied too long ?" ));
		
		for (int i=0;i<control.size();i++) {
			for (int j=0;j<4;j++) {
				TextInputDialog uca2 = new TextInputDialog("Enter the considered UCAs");
				uca2.setTitle("Unsafe Control Actions");
				uca2.setHeaderText("For the controller : "+control.get(i)+" , "+cases.get(j));
				uca2.showAndWait();
				String u = uca2.getEditor().getText();
				//ucas.add(u);
				scenarios.add(u);
				System.out.println(u);
			}
		}
	}
	
	boolean windowclosed;
	ArrayList<String> i1=new ArrayList<String>();
	ArrayList<String> i2=new ArrayList<String>();
	ArrayList<String> i3=new ArrayList<String>();
	ArrayList<String> i4=new ArrayList<String>();
	ArrayList<String> i5=new ArrayList<String>();
	ArrayList<String> i6=new ArrayList<String>();
	ArrayList<String> i7=new ArrayList<String>();
	int k=3;
	private void ucas3() {
		// TODO Auto-generated method stub
		ArrayList<String> cases = new ArrayList<>(Arrays.asList("Provide a situation when not applying the control signal causes hazard","Provide a situation when applying the control signal causes hazard" , "What happens when the control signal is applied too early/too late?" , "What happens when the control signal is stopped too soon/applied too long ?" ));
//		System.out.println(cases);
//		System.out.println(cases.size());
		for (int i=0;i<control.size();i++) {
			for (int j=0;j<=3;j++) {
				//windowclosed = false;
//				TextInputDialog uca2 = new TextInputDialog("Enter the considered UCAs");
//				uca2.setTitle("Unsafe Control Actions");
//				uca2.setHeaderText("For the controller : "+control.get(i)+" , "+cases.get(j));
//				uca2.showAndWait();
//				String u = uca2.getEditor().getText();
//				//ucas.add(u);
//				scenarios.add(u);
//				System.out.println(u);
				
				EventQueue.invokeLater(new Runnable()
		        {
		            @Override
		            public void run()
		            {
		                JFrame frame = new JFrame("UCA");
		                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		                try 
		                {
		                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		                } catch (Exception e) {
		                   e.printStackTrace();
		                }
		                JPanel panel = new JPanel();
		                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		                panel.setOpaque(true);
		                JTextArea textArea = new JTextArea(15, 50);
		                textArea.setWrapStyleWord(true);
		                textArea.setEditable(false);
		                textArea.setFont(Font.getFont(Font.SANS_SERIF));
		                JScrollPane scroller = new JScrollPane(textArea);
		                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		                JPanel inputpanel = new JPanel();
		                inputpanel.setLayout(new FlowLayout());
		                JTextField input1 = new JTextField(20);
		                
		                JTextField input2 = new JTextField(10);
		                JTextField input3 = new JTextField(20);
		                JTextField input4 = new JTextField(20);
		                JTextField input5 = new JTextField(20);
		                JTextField input6 = new JTextField(20);
		                JTextField input7 = new JTextField(10);
		               
		                JButton button = new JButton("Enter");
		                DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		                panel.add(scroller);
		                inputpanel.add(input1);
		                
		                inputpanel.add(input2);
		                inputpanel.add(input3);
		                inputpanel.add(input4);
		                inputpanel.add(input5);
		                inputpanel.add(input6);
		                inputpanel.add(input7);
		                
		                inputpanel.add(button);
		                panel.add(inputpanel);
		                frame.getContentPane().add(BorderLayout.CENTER, panel);
		                frame.pack();
		                frame.setLocationByPlatform(true);
		                frame.setVisible(true);
		                frame.setResizable(false);
		                input1.requestFocus();
		                textArea.setText("Every UCA contains five parts: For example : " + "\n"+"UCA-2: BSCU Autobrake provides Brake command during a normal takeoff [H-4.3]" + "\n"+ "<Source>           <Type>     <Control Action>             <Context>        <Link to Hazards>"+"\n"+cases.get(k));
		                k--;
		                
		                button.addActionListener(new ActionListener(){
		                	public void  actionPerformed(ActionEvent e){
//		                		String a = input1.getText();
//		                		textArea.append("\n"+a);
//		                		textArea.setLineWrap(true);
//		                		scenarios.add(a);
//		                		System.out.println(scenarios);
		                		String s1 = input1.getText();
		                		String s2 = input2.getText();
		                		String s3 = input3.getText();
		                		String s4 = input4.getText();
		                		String s5 = input5.getText();
		                		String s6 = input6.getText();
		                		String s7 = input7.getText();
		                		i1.add(s1);
		                		i2.add(s2);
		                		i3.add(s3);
		                		i4.add(s4);
		                		i5.add(s5);
		                		i6.add(s6);
		                		i7.add(s7);
		                		//textArea.append("\n"+s1+" "+s2+" "+s3+" "+s4+" "+s5+" "+s6+" "+s7);
		                		scenarios.add("\n"+s1+" "+s2+" "+s3+" "+s4+" "+s5+" "+s6);
		                		frame.dispose();
		                		
		                	}
		                });
		                
//		                frame.addWindowListener(new WindowAdapter() {
//
//		                    @Override
//		                    public void windowClosing(WindowEvent e) {
//		                        System.out.println("A is closing");
//		                        windowclosed=true;
//		                    }});
		                
		            }
		        });
			  
				
//				Timer timer = new Timer("Timer");
//				while(!windowclosed) {
//					TimerTask task = new TimerTask() {
//				        public void run() {
//				            System.out.println("Task performed on: " + new Date() + "n" +
//				              "Thread's name: " + Thread.currentThread().getName());
//				        }
//				    };
//				    
//				     
//				    long delay = 1000L;
//				    timer.schedule(task, delay);
//			}
//				timer.cancel();
//				
		}
		}
	}
	
	private void method() {
		EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame frame = new JFrame("Loss Scenarios");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                try 
                {
                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                   e.printStackTrace();
                }
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setOpaque(true);
                JTextArea textArea = new JTextArea(15, 50);
                textArea.setWrapStyleWord(true);
                textArea.setEditable(false);
                textArea.setFont(Font.getFont(Font.SANS_SERIF));
                JScrollPane scroller = new JScrollPane(textArea);
                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                JPanel inputpanel = new JPanel();
                inputpanel.setLayout(new FlowLayout());
                JTextField input = new JTextField(20);
                JButton button = new JButton("Enter");
                DefaultCaret caret = (DefaultCaret) textArea.getCaret();
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
                panel.add(scroller);
                inputpanel.add(input);
                inputpanel.add(button);
                panel.add(inputpanel);
                frame.getContentPane().add(BorderLayout.CENTER, panel);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                frame.setResizable(false);
                input.requestFocus();
                textArea.setText("Enter the Loss Scenarios : ");
                
                button.addActionListener(new ActionListener(){
                	public void  actionPerformed(ActionEvent e){
                		String a = input.getText();
                		textArea.append("\n"+a);
                		textArea.setLineWrap(true);
                		scenarios.add(a);
                		System.out.println(scenarios);
                	}
                });
            }
        });
	  
	}
	
	private void showScen() {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame frame = new JFrame("Loss Scenarios");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                try 
                {
                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                   e.printStackTrace();
                }
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setOpaque(true);
                JTextArea textArea = new JTextArea(15, 50);
                textArea.setWrapStyleWord(true);
                textArea.setEditable(false);
                textArea.setFont(Font.getFont(Font.SANS_SERIF));
                JScrollPane scroller = new JScrollPane(textArea);
                scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                JPanel inputpanel = new JPanel();
                inputpanel.setLayout(new FlowLayout());
                //JTextField input = new JTextField(20);
                //JButton button = new JButton("Enter");
                DefaultCaret caret = (DefaultCaret) textArea.getCaret();
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
                panel.add(scroller);
                //inputpanel.add(input);
                //inputpanel.add(button);
                panel.add(inputpanel);
                frame.getContentPane().add(BorderLayout.CENTER, panel);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                frame.setResizable(false);
                //input.requestFocus();
                textArea.setText("Here are the Loss Scenarios : ");
                
                for (int i=0;i<scenarios.size();i++) {
                	textArea.append("\n"+scenarios.get(i));
                	
                }
                System.out.println(scenarios);
                
//                button.addActionListener(new ActionListener(){
//                	public void  actionPerformed(ActionEvent e){
//                		String a = input.getText();
//                		textArea.append("\n"+a);
//                		textArea.setLineWrap(true);
//                		scenarios.add(a);
//                		System.out.println(scenarios);
//                	}
//                });
            }
        });
		
		
	}
	
}
