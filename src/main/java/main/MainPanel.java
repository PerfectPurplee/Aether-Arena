package main;

import javax.swing.*;
import java.awt.*;

import static main.GameEngine.*;

public class MainPanel extends JPanel {

//    sposob na pobranie rozmiaru glownego ekranu.
//    Trzeba napisac metode skalujaca, tak zeby kod dzialal na wqhd.
//    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();



    public MainPanel() {

        this.setBackground(Color.white);
        this.setPreferredSize(new Dimension(1800,900));
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }
}
