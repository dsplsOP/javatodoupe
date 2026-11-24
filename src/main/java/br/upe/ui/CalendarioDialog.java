package br.upe.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class CalendarioDialog extends JDialog {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");
    private static final String[] DIAS = {"D", "S", "T", "Q", "Q", "S", "S"};

    private YearMonth mes;
    private LocalDate selecionada;
    private boolean confirmado = false;

    private final JLabel lblMes = new JLabel("", SwingConstants.CENTER);
    private final JPanel pnlDias = new JPanel(new GridLayout(0, 7, 2, 2));

    public CalendarioDialog(Window pai, LocalDate dataAtual) {
        super(pai, "Prazo da tarefa", ModalityType.APPLICATION_MODAL);
        this.selecionada = dataAtual;
        this.mes = YearMonth.from(dataAtual != null ? dataAtual : LocalDate.now());

        JButton btnAnterior = new JButton("<");
        JButton btnProximo = new JButton(">");
        btnAnterior.addActionListener(e -> { mes = mes.minusMonths(1); montarMes(); });
        btnProximo.addActionListener(e -> { mes = mes.plusMonths(1); montarMes(); });

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(btnAnterior, BorderLayout.WEST);
        topo.add(lblMes, BorderLayout.CENTER);
        topo.add(btnProximo, BorderLayout.EAST);

        JButton btnHoje = new JButton("Hoje");
        btnHoje.addActionListener(e -> escolher(LocalDate.now()));
        JButton btnRemover = new JButton("Remover prazo");
        btnRemover.addActionListener(e -> escolher(null));

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rodape.add(btnHoje);
        rodape.add(btnRemover);

        JPanel conteudo = new JPanel(new BorderLayout(5, 5));
        conteudo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        conteudo.add(topo, BorderLayout.NORTH);
        conteudo.add(pnlDias, BorderLayout.CENTER);
        conteudo.add(rodape, BorderLayout.SOUTH);

        setContentPane(conteudo);
        montarMes();
        setSize(340, 320);
        setLocationRelativeTo(pai);
    }

    private void montarMes() {
        pnlDias.removeAll();
        String nome = mes.getMonth().getDisplayName(TextStyle.FULL, PT_BR);
        lblMes.setText(Character.toUpperCase(nome.charAt(0)) + nome.substring(1) + " de " + mes.getYear());

        for (String d : DIAS) {
            pnlDias.add(new JLabel(d, SwingConstants.CENTER));
        }

        int deslocamento = mes.atDay(1).getDayOfWeek().getValue() % 7; // domingo = 0
        for (int i = 0; i < deslocamento; i++) {
            pnlDias.add(new JLabel(""));
        }

        LocalDate hoje = LocalDate.now();
        for (int dia = 1; dia <= mes.lengthOfMonth(); dia++) {
            LocalDate data = mes.atDay(dia);
            JButton btn = new JButton(String.valueOf(dia));
            btn.setMargin(new Insets(2, 2, 2, 2));
            if (data.equals(selecionada)) {
                btn.setBackground(new Color(100, 149, 237));
                btn.setOpaque(true);
            } else if (data.equals(hoje)) {
                btn.setBackground(new Color(255, 235, 150));
                btn.setOpaque(true);
            }
            btn.addActionListener(e -> escolher(data));
            pnlDias.add(btn);
        }
        pnlDias.revalidate();
        pnlDias.repaint();
    }

    private void escolher(LocalDate data) {
        this.selecionada = data;
        this.confirmado = true;
        dispose();
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public LocalDate getDataSelecionada() {
        return selecionada;
    }
}