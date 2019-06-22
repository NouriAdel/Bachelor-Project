package STAMP;


import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javax.swing.*;

import com.sl.app.ConnectorPropertiesPanel;
import com.sl.app.DraggableLabel;
import com.sl.connector.*;
import com.sl.line.ConnectLine;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

public class TestMainExample extends JFrame {
    Canvas c = new Canvas();
    ConnectorPropertiesPanel props;
    static ArrayList<String> processControllers;
    static ArrayList<String> processes;
    static ArrayList<String> controllers;
    public TestMainExample(ArrayList<String> pc , ArrayList<String> p , ArrayList<String> c) {
        super("STAMP");
        controllers = c;
        processes = p;
        processControllers = pc;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());
        init();

        getContentPane().add(new JLabel("STAMP Heirarchy . You can drag the connected components to align the lines as desired"),
                             new GridBagConstraints(0, 0, 2, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
        getContentPane().add(new JLabel("CONTROLLERS"),
                new GridBagConstraints(0, 1, 2, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
        getContentPane().add(initConnectors(),
                             new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        getContentPane().add(props,
                             new GridBagConstraints(1, 2, 1, 1, 0, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, new Insets(5, 0, 5, 5), 0, 0));
        getContentPane().add(new JLabel("CONTROLLED PROCESSES"),
                new GridBagConstraints(0, 3, 2, 1, 1, 0, GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
//        getContentPane().add(c,
//                             new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    protected void init() {
        ConnectLine[] lines = new ConnectLine[5];
        lines[0] = new ConnectLine(new Point(200, 10), new Point(50, 300), ConnectLine.LINE_TYPE_SIMPLE, ConnectLine.LINE_START_HORIZONTAL, ConnectLine.LINE_ARROW_BOTH);
        lines[1] = new ConnectLine(new Point(200, 10), new Point(200, 150), ConnectLine.LINE_TYPE_SIMPLE, ConnectLine.LINE_START_HORIZONTAL, ConnectLine.LINE_ARROW_BOTH);
        lines[2] = new ConnectLine(new Point(50, 150), new Point(100, 100), ConnectLine.LINE_TYPE_SIMPLE, ConnectLine.LINE_START_HORIZONTAL, ConnectLine.LINE_ARROW_BOTH);
        lines[3] = new ConnectLine(new Point(150, 120), new Point(60, 70), ConnectLine.LINE_TYPE_SIMPLE, ConnectLine.LINE_START_HORIZONTAL, ConnectLine.LINE_ARROW_BOTH);

//        lines[1]=new ConnectLine(new Point(30,10), new Point(80,70),ConnectLine.LINE_TYPE_RECT_1BREAK, ConnectLine.LINE_START_HORIZONTAL,ConnectLine.LINE_ARROW_NONE);
//        lines[2]=new ConnectLine(new Point(50,20), new Point(100,100),ConnectLine.LINE_TYPE_RECT_1BREAK, ConnectLine.LINE_START_VERTICAL,ConnectLine.LINE_ARROW_NONE);
//        lines[3]=new ConnectLine(new Point(70,30), new Point(150,170),ConnectLine.LINE_TYPE_RECT_2BREAK, ConnectLine.LINE_START_HORIZONTAL,ConnectLine.LINE_ARROW_NONE);
//        lines[4]=new ConnectLine(new Point(100,50), new Point(200,270),ConnectLine.LINE_TYPE_RECT_2BREAK, ConnectLine.LINE_START_VERTICAL,ConnectLine.LINE_ARROW_NONE);
        c.setLines(lines, Color.blue);
    }

    protected ConnectorContainer initConnectors() {
    	int xc = 10;
    	int xp = 10;
        JConnector[] connectors = new JConnector[processes.size()];
        ConnectorContainer cc = new ConnectorContainer(connectors);
        cc.setLayout(null);
        int k=0;
        for (int i=0;i<controllers.size();i++){
        	String c = controllers.get(i);
        	JLabel a = new DraggableLabel(c);
        	a.setBounds(xc, 10, 100, 50);
        	xc = xc+180;
        	cc.add(a);
        	for (int j=0;j<processes.size();j++){
        		String p = processes.get(j);
        		if (processControllers.get(j).equals(c)){
        			JLabel b = new DraggableLabel(p);
        			b.setBounds(xp, 300, 100, 50);
        			xp = xp+180;
        			connectors[k] = new JConnector(a, b, ConnectLine.LINE_ARROW_SOURCE, JConnector.CONNECT_LINE_TYPE_RECTANGULAR, Color.red);
        	        props = new ConnectorPropertiesPanel(connectors[k]);
        	        k++;
        	        cc.add(b);
        		}
        	}
        }
        /*JLabel b1 = new DraggableLabel("Source 1");
        b1.setBounds(10, 10, 100, 50);
        JLabel b2 = new DraggableLabel("Dest 1");
        b2.setBounds(200, 20, 100, 50);
//        JLabel b3=new DraggableLabel("Source 2");
//        b3.setBounds(200,500,100,25);
//        JLabel b4=new DraggableLabel("Dest 2");
//        b4.setBounds(400,300,100,25);
        connectors[0] = new JConnector(b1, b2, ConnectLine.LINE_ARROW_SOURCE, JConnector.CONNECT_LINE_TYPE_RECTANGULAR, Color.red);
        props = new ConnectorPropertiesPanel(connectors[0]);
//        connectors[1]=new JConnector(b3, b4, ConnectLine.LINE_ARROW_DEST, Color.blue);
        ConnectorContainer cc = new ConnectorContainer(connectors);
        cc.setLayout(null);

        cc.add(b1);
        cc.add(b2);
        //cc.add(b3);
//        cc.add(b4);
*/
        cc.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        return cc;
    }

    public static void main(String[] args) {
        TestMainExample fr = new TestMainExample(processControllers , processes , controllers);
        fr.setVisible(true);
    }

    //temp class to test lines drawing
    protected static class Canvas extends JPanel {
        ConnectLine[] lines;
        Color color;
        public void setLines(ConnectLine[] lines, Color color) {
            this.lines = lines;
            this.color = color;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.black);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            g.setColor(color);
            for (int i = 0; i < lines.length; i++) {
                if (lines[i] != null) {
                    lines[i].paint( (Graphics2D) g);
                }
            }
        }
    }
}

