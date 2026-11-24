package br.upe;

import br.upe.ui.TelaEstatisticas;
import br.upe.ui.TelaPrincipal;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal pnlMain = new TelaPrincipal();

            JFrame frame = new JFrame("ToDo");
            frame.setContentPane(pnlMain.getPnlMain());

            // Menu principal
            JMenuBar barra = new JMenuBar();
            JMenu menuEstatisticas = new JMenu("Estatísticas");
            JMenuItem itemDesempenho = new JMenuItem("Desempenho...");
            itemDesempenho.addActionListener(e ->
                    new TelaEstatisticas(frame, pnlMain.getControlador().getEstatisticas()).setVisible(true));
            menuEstatisticas.add(itemDesempenho);
            barra.add(menuEstatisticas);
            frame.setJMenuBar(barra);

            frame.setMinimumSize(new Dimension(650, 450));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });
    }
}