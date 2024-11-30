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

    public static void main(String[] args) {
        SimuladorEscalonamento simulador = new SimuladorEscalonamento();
        simulador.menu();
    }

    private void menu() {
        while (true) {
            System.out.println("\nSimulador de Escalonamento");
            System.out.println("1. Criar Processo");
            System.out.println("2. Configurar Quantum");
            System.out.println("3. Iniciar Escalonamento (Round Robin)");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            switch (opcao) {
                case 1 -> criarProcesso();
                case 2 -> configurarQuantum();
                case 3 -> iniciarEscalonamento();
                case 4 -> {
                    System.out.println("Encerrando simulador...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void criarProcesso() {
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
        System.out.println("Processo criado e adicionado à fila de prontos.");
    }

    private void configurarQuantum() {
        System.out.print("Defina o quantum (em ms): ");
        quantum = scanner.nextInt();
        System.out.println("Quantum configurado para " + quantum + " ms.");
    }

    private void iniciarEscalonamento() {
        if (filaProntos.isEmpty()) {
            System.out.println("Nenhum processo na fila de prontos.");
            return;
        }

        System.out.print("Escolha o algoritmo (1 - Round Robin, 2 - Prioridade): ");
        int escolha = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        if (escolha == 1) {
            iniciarEscalonamentoRoundRobin();
        } else if (escolha == 2) {
            iniciarEscalonamentoPrioridade();
        } else {
            System.out.println("Opção inválida. Voltando ao menu principal.");
        }
    }
    
    private void calcularMetricas(List<Processo> processos) {
        int somaTurnaround = 0;
        int somaEspera = 0;

        System.out.println("\nMétricas de Execução:");
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
}

