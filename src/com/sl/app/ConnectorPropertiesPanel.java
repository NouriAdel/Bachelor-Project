package com.sl.app;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import com.sl.connector.JConnector;
import java.awt.event.*;
import com.sl.line.ConnectLine;

public class ConnectorPropertiesPanel extends JPanel {
    JComboBox cbxType = new JComboBox(new String[] {"Simple", "Rectangular"});
    JComboBox cbxArrow = new JComboBox(new String[] {"No arrow", "Source", "Dest", "Both"});
    JButton btnColor = new JButton("...");
    JConnector connector;
    public ConnectorPropertiesPanel(JConnector connector) {
        this.connector = connector;
        setLayout(new GridBagLayout());
        add(new JLabel("Line type:"), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        add(cbxType, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));

        add(new JLabel("Line arrow:"), new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        add(cbxArrow, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));

        add(new JLabel("Line color:"), new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        add(btnColor, new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));

        add(new JLabel(" "), new GridBagConstraints(0, 7, 2, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

        setColor(connector.getLineColor());
        if (connector.getLineType() == ConnectLine.LINE_TYPE_SIMPLE) {
            cbxType.setSelectedIndex(0);
        }
        else {
            cbxType.setSelectedIndex(1);
        }
        switch (connector.getLineArrow()) {
            case ConnectLine.LINE_ARROW_NONE:
                cbxArrow.setSelectedIndex(0);
                break;
            case ConnectLine.LINE_ARROW_SOURCE:
                cbxArrow.setSelectedIndex(1);
                break;
            case ConnectLine.LINE_ARROW_DEST:
                cbxArrow.setSelectedIndex(2);
                break;
            case ConnectLine.LINE_ARROW_BOTH:
                cbxArrow.setSelectedIndex(3);
                break;
        }

        initListeners();
    }

    protected void setColor(Color c) {
        btnColor.setBackground(c);
    }

    protected void initListeners() {
        cbxType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cbxType.getSelectedIndex() == 0) {
                    connector.setLineType(ConnectLine.LINE_TYPE_SIMPLE);
                }
                else {
                    connector.setLineType(ConnectLine.LINE_TYPE_RECT_1BREAK);
                }
                getTopLevelAncestor().repaint();
            }
        });
        cbxArrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch (cbxArrow.getSelectedIndex()) {
                    case 0:
                        connector.setLineArrow(ConnectLine.LINE_ARROW_NONE);
                        break;
                    case 1:
                        connector.setLineArrow(ConnectLine.LINE_ARROW_SOURCE);
                        break;
                    case 2:
                        connector.setLineArrow(ConnectLine.LINE_ARROW_DEST);
                        break;
                    case 3:
                        connector.setLineArrow(ConnectLine.LINE_ARROW_BOTH);
                        break;
                }
                getTopLevelAncestor().repaint();
            }
        });

        btnColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(ConnectorPropertiesPanel.this, "Select line color", connector.getLineColor());
                if (newColor != null) {
                    setColor(newColor);
                    connector.setLineColor(newColor);
                    getTopLevelAncestor().repaint();
                }
            }
        });
    }
}
