package com.github.binhtran432k.plantumlpreviewer.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

class StatusPanel extends JPanel {
    private JLabel component;

    public StatusPanel(String componentStr) {
        component = new JLabel(componentStr);
        component.setFont(component.getFont().deriveFont(14f));
        component.setBorder(new EmptyBorder(3, 6, 3, 6));

        initUI();
    }

    public void setComponent(String text) {
        component.setText(text);
    }

    private void initUI() {
        setBackground(new Color(0xc1c1c1));
        setLayout(new BorderLayout());

        add(component);
    }
}
