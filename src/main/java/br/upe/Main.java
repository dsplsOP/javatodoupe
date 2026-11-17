package br.upe;

import br.upe.ui.TelaPrincipal;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal pnlMain = new TelaPrincipal();

            JFrame frame = new JFrame("ToDo");
            frame.setContentPane(pnlMain.getPnlMain());

            // Define tamanho minimo e dimensiona adequadamente
            frame.setMinimumSize(new Dimension(650, 450));
            frame.pack();
            frame.setLocationRelativeTo(null); // Centraliza na tela
            frame.setVisible(true);

            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });
    }
}