package br.upe.ui;

import br.upe.model.Estatisticas;
import br.upe.model.Prioridade;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class TelaEstatisticas extends JDialog {

    public TelaEstatisticas(Window pai, Estatisticas est) {
        super(pai, "Estatísticas de Desempenho", ModalityType.APPLICATION_MODAL);

        JPanel grade = new JPanel(new GridLayout(0, 2, 10, 8));
        grade.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        linha(grade, "Total de tarefas cadastradas:", String.valueOf(est.getTotal()));
        linha(grade, "Tarefas concluídas:", String.valueOf(est.getConcluidas()));
        linha(grade, "Tarefas pendentes:", String.valueOf(est.getPendentes()));
        linha(grade, "Porcentagem de conclusão:", String.format("%.1f%%", est.getPercentualConclusao()));
        linha(grade, "Tarefas atrasadas:", String.valueOf(est.getAtrasadas()));
        linha(grade, "Concluídas dentro do prazo:", String.valueOf(est.getConcluidasNoPrazo()));
        linha(grade, "Concluídas fora do prazo:", String.valueOf(est.getConcluidasForaDoPrazo()));

        Map<Prioridade, Integer> dist = est.getDistribuicaoPorPrioridade();
        linha(grade, "Prioridade BAIXA:", String.valueOf(dist.get(Prioridade.BAIXA)));
        linha(grade, "Prioridade MÉDIA:", String.valueOf(dist.get(Prioridade.MEDIA)));
        linha(grade, "Prioridade ALTA:", String.valueOf(dist.get(Prioridade.ALTA)));

        JProgressBar barra = new JProgressBar(0, 100);
        barra.setValue((int) Math.round(est.getPercentualConclusao()));
        barra.setStringPainted(true);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        JPanel rodape = new JPanel();
        rodape.add(btnFechar);

        JPanel conteudo = new JPanel(new BorderLayout(5, 10));
        conteudo.add(grade, BorderLayout.NORTH);
        JPanel meio = new JPanel(new BorderLayout());
        meio.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        meio.add(barra, BorderLayout.CENTER);
        conteudo.add(meio, BorderLayout.CENTER);
        conteudo.add(rodape, BorderLayout.SOUTH);

        setContentPane(conteudo);
        pack();
        setLocationRelativeTo(pai);
    }

    private void linha(JPanel painel, String titulo, String valor) {
        painel.add(new JLabel(titulo));
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(lblValor.getFont().deriveFont(Font.BOLD));
        painel.add(lblValor);
    }
}