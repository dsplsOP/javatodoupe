package br.upe.persistence;

import br.upe.model.Prioridade;
import br.upe.model.Tarefa;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Salva e carrega as tarefas em um arquivo de texto (UTF-8).
 * Cada tarefa ocupa uma linha, com os campos separados por TAB:
 * descricao, categoria, prioridade, finalizada, dataCriacao,
 * dataFinalizacao, dataVencimento, ordem
 *
 * Por padrão o arquivo fica em: <pasta do usuário>/.todo-upe/tarefas.txt
 */
public class TarefaRepositorio {

    private static final String SEPARADOR = "\t";
    private static final int TOTAL_CAMPOS = 8;

    private final Path arquivo;

    public TarefaRepositorio() {
        this(Paths.get(System.getProperty("user.home"), ".todo-upe", "tarefas.txt"));
    }

    public TarefaRepositorio(Path arquivo) {
        this.arquivo = arquivo;
    }

    public Path getArquivo() {
        return arquivo;
    }

    public List<Tarefa> carregar() {
        List<Tarefa> tarefas = new ArrayList<>();
        if (!Files.exists(arquivo)) {
            return tarefas; // primeira execução: ainda não há nada salvo
        }
        try {
            for (String linha : Files.readAllLines(arquivo, StandardCharsets.UTF_8)) {
                if (linha.isBlank()) {
                    continue;
                }
                try {
                    tarefas.add(linhaParaTarefa(linha));
                } catch (RuntimeException e) {
                    System.err.println("Linha inválida ignorada em " + arquivo + ": " + linha);
                }
            }
        } catch (IOException e) {
            System.err.println("Não foi possível ler " + arquivo + ": " + e.getMessage());
        }
        return tarefas;
    }

    public void salvar(List<Tarefa> tarefas) {
        List<String> linhas = new ArrayList<>();
        for (Tarefa tarefa : tarefas) {
            linhas.add(tarefaParaLinha(tarefa));
        }
        try {
            Files.createDirectories(arquivo.getParent());
            // Grava num arquivo temporário e só depois troca, para nunca
            // deixar o arquivo principal pela metade se algo falhar.
            Path temporario = arquivo.resolveSibling(arquivo.getFileName() + ".tmp");
            Files.write(temporario, linhas, StandardCharsets.UTF_8);
            Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Não foi possível salvar em " + arquivo + ": " + e.getMessage());
        }
    }

    // ---------- Conversão Tarefa <-> linha de texto ----------

    private String tarefaParaLinha(Tarefa t) {
        String[] campos = new String[TOTAL_CAMPOS];
        campos[0] = escapar(t.getDescricao());
        campos[1] = escapar(t.getCategoria());
        campos[2] = (t.getPrioridade() == null) ? "" : t.getPrioridade().name();
        campos[3] = String.valueOf(Boolean.TRUE.equals(t.isFinalizada()));
        campos[4] = dataParaTexto(t.getDataCriacao());
        campos[5] = dataParaTexto(t.getDataFinalizacao());
        campos[6] = dataParaTexto(t.getDataVencimento());
        campos[7] = String.valueOf(t.getOrdem());
        return String.join(SEPARADOR, campos);
    }

    private Tarefa linhaParaTarefa(String linha) {
        String[] campos = linha.split(SEPARADOR, -1);
        if (campos.length != TOTAL_CAMPOS) {
            throw new IllegalArgumentException("Quantidade de campos inesperada");
        }
        Prioridade prioridade = campos[2].isEmpty() ? null : Prioridade.valueOf(campos[2]);
        int ordem = Integer.parseInt(campos[7]);

        Tarefa tarefa = new Tarefa(desescapar(campos[0]), desescapar(campos[1]), ordem, prioridade);
        tarefa.setFinalizada(Boolean.parseBoolean(campos[3]));
        tarefa.setDataCriacao(textoParaData(campos[4]));
        tarefa.setDataFinalizacao(textoParaData(campos[5]));
        tarefa.setDataVencimento(textoParaData(campos[6]));
        return tarefa;
    }

    private String dataParaTexto(LocalDate data) {
        return (data == null) ? "" : data.toString();
    }

    private LocalDate textoParaData(String texto) {
        return texto.isEmpty() ? null : LocalDate.parse(texto);
    }

    // Protege TAB, quebra de linha e barra invertida dentro dos textos
    private String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("\\", "\\\\")
                .replace("\t", "\\t")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private String desescapar(String texto) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == '\\' && i + 1 < texto.length()) {
                char proximo = texto.charAt(++i);
                switch (proximo) {
                    case 't': sb.append('\t'); break;
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    default: sb.append(proximo);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
