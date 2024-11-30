import java.util.*;

class Processo {
    int id;
    String nome;
    int prioridade;
    boolean ioBound; // true para I/O-bound, false para CPU-bound
    int tempoTotalCPU;
    int tempoRestante;
    int tempoEspera = 0;
    int tempoTurnaround = 0;

    public Processo(int id, String nome, int prioridade, boolean ioBound, int tempoTotalCPU) {
        this.id = id;
        this.nome = nome;
        this.prioridade = prioridade;
        this.ioBound = ioBound;
        this.tempoTotalCPU = tempoTotalCPU;
        this.tempoRestante = tempoTotalCPU;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Nome: %s, Prioridade: %d, Tipo: %s, Tempo Restante: %d ms",
                id, nome, prioridade, ioBound ? "I/O-bound" : "CPU-bound", tempoRestante);
    }
}

public class SimuladorEscalonamento {
    private static final Scanner scanner = new Scanner(System.in);
    private final Queue<Processo> filaProntos = new LinkedList<>();
    private int quantum = 1;

    // Códigos de cores ANSI
    private static final String[] Cores = {
            "\u001B[31m", // Vermelho
            "\u001B[32m", // Verde
            "\u001B[33m", // Amarelo
            "\u001B[34m", // Azul
            "\u001B[35m"  // Magenta
    };
    private static final String resetar_Cor = "\u001B[0m"; // Resetar cor para o padrão

    public static void main(String[] args) {
        SimuladorEscalonamento simulador = new SimuladorEscalonamento();
        simulador.menu();
    }

    private void menu() {
        String cor = "\u001B[35m";
        while (true) {
            exibirMensagemColorida(cor,"\nSimulador de Escalonamento");
            exibirMensagemColorida(cor,"1. Criar Processo");
            exibirMensagemColorida(cor,"2. Configurar Quantum");
            exibirMensagemColorida(cor,"3. Iniciar Escalonamento");
            exibirMensagemColorida(cor,"4. Sair");
            exibirMensagemColorida(cor,"Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            switch (opcao) {
                case 1 -> criarProcesso();
                case 2 -> configurarQuantum();
                case 3 -> iniciarEscalonamento();
                case 4 -> {
                    exibirMensagemColorida(cor,"Encerrando simulador...");
                    return;
                }
                default -> exibirMensagemColorida(cor,"Opção inválida.");
            }
        }
    }

    private void criarProcesso() {
        String cor = "\u001B[33m";
        System.out.print("ID do processo: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        System.out.print("Nome do processo: ");
        String nome = scanner.nextLine();

        System.out.print("Prioridade do processo (1 a 10): ");
        int prioridade = scanner.nextInt();

        System.out.print("Tipo do processo (1 para I/O-bound, 0 para CPU-bound): ");
        boolean ioBound = scanner.nextInt() == 1;

        System.out.print("Tempo total de CPU (em ms): ");
        int tempoTotalCPU = scanner.nextInt();

        filaProntos.add(new Processo(id, nome, prioridade, ioBound, tempoTotalCPU));
        exibirMensagemColorida(cor,"Processo criado e adicionado à fila de prontos.");
    }

    private void configurarQuantum() {
        String cor = "\u001B[33m";
        exibirMensagemColorida(cor,"Defina o quantum (em ms): ");
        quantum = scanner.nextInt();
        exibirMensagemColorida(cor,"Quantum configurado para " + quantum + " ms.");
    }

    private void iniciarEscalonamento() {
        String cor = "\u001B[33m";
        if (filaProntos.isEmpty()) {
            exibirMensagemColorida(cor,"Nenhum processo na fila de prontos.");
            return;
        }

        exibirMensagemColorida(cor,"Escolha o algoritmo (1 - Round Robin, 2 - Prioridade): ");
        int escolha = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        if (escolha == 1) {
            iniciarEscalonamentoRoundRobin();
        } else if (escolha == 2) {
            iniciarEscalonamentoPrioridade();
        } else {
            exibirMensagemColorida(cor,"Opção inválida. Voltando ao menu principal.");
        }
    }

    private void iniciarEscalonamentoRoundRobin() {
        String cor = "\u001B[33m";
        exibirMensagemColorida(cor,"\nIniciando escalonamento Round Robin");
        List<Processo> listaProcessos = new ArrayList<>(filaProntos);
        int tempoTotal = 0;

        while (!filaProntos.isEmpty()) {
            Processo processo = filaProntos.poll();
            int tempoExecucao = Math.min(quantum, processo.tempoRestante);

            System.out.printf("Executando %s (ID: %d) por %d ms.\n",
                    processo.nome, processo.id, tempoExecucao);

            processo.tempoRestante -= tempoExecucao;
            tempoTotal += tempoExecucao;

            // Atualizar tempo de espera dos processos aguardando
            for (Processo p : filaProntos) {
                p.tempoEspera += tempoExecucao;
            }

            // Exibir o estado atual da fila
            exibirEstadoFila(processo);

            if (processo.tempoRestante > 0) {
                filaProntos.add(processo); // Reinsere na fila
            } else {
                processo.tempoTurnaround = tempoTotal;
                System.out.printf("%s (ID: %d) concluído.\n", processo.nome, processo.id);
            }
        }

        calcularMetricas(listaProcessos);
    }

    private void iniciarEscalonamentoPrioridade() {
        String cor = "\u001B[33m";
        exibirMensagemColorida(cor,"\nIniciando escalonamento por Prioridade");
        List<Processo> listaProcessos = new ArrayList<>(filaProntos);
        listaProcessos.sort(Comparator.comparingInt(p -> p.prioridade));

        int tempoTotal = 0;
        for (Processo processo : listaProcessos) {
            System.out.printf("Executando %s (ID: %d) com prioridade %d.\n",
                    processo.nome, processo.id, processo.prioridade);

            tempoTotal += processo.tempoTotalCPU;
            processo.tempoEspera = tempoTotal - processo.tempoTotalCPU;
            processo.tempoTurnaround = tempoTotal;

            System.out.printf("%s (ID: %d) concluído.\n", processo.nome, processo.id);
            // Exibir o estado atual da fila
            exibirEstadoFila(processo);
        }

        calcularMetricas(listaProcessos);
    }

    private void exibirEstadoFila(Processo processoAtivo) {
        String cor = "\u001B[33m";
        exibirMensagemColorida(cor,"\nEstado Atual da Fila:");
        System.out.printf("Processo ativo: %s (ID: %d)\n", processoAtivo.nome, processoAtivo.id);

        if (filaProntos.isEmpty()) {
            System.out.println("Nenhum processo aguardando.");
        } else {
            exibirMensagemColorida(cor,"Processos aguardando:");
            for (Processo p : filaProntos) {
                System.out.printf("- %s (ID: %d) com %d ms restantes.\n", p.nome, p.id, p.tempoRestante);
            }
        }
    }

    private void calcularMetricas(List<Processo> processos) {
        String cor = "\u001B[33m";
        int somaTurnaround = 0;
        int somaEspera = 0;

        exibirMensagemColorida(cor,"\nMétricas de Execução:");
        for (Processo p : processos) {
            somaTurnaround += p.tempoTurnaround;
            somaEspera += p.tempoEspera;
            System.out.printf("Processo %s (ID: %d): Turnaround = %d ms, Espera = %d ms.\n",
                    p.nome, p.id, p.tempoTurnaround, p.tempoEspera);
        }

        double mediaTurnaround = (double) somaTurnaround / processos.size();
        double mediaEspera = (double) somaEspera / processos.size();

        System.out.printf("\nTempo médio de Turnaround: %.2f ms\n", mediaTurnaround);
        System.out.printf("Tempo médio de Espera: %.2f ms\n", mediaEspera);
    }

    // Função para imprimir mensagens coloridas
    private static void exibirMensagemColorida(String cor, String mensagem) {
        System.out.println(cor + mensagem + resetar_Cor);
    }
}
