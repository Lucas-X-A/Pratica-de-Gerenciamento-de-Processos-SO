import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    // Recursos compartilhados
    private static final StringBuffer buffer1 = new StringBuffer("Buffer1");
    private static final StringBuffer buffer2 = new StringBuffer("Buffer2");

    // Locks para garantir exclusão mútua
    private static final Lock bloqueioBuffer1 = new ReentrantLock();
    private static final Lock bloqueioBuffer2 = new ReentrantLock();

    // Códigos de cores ANSI para cada thread
    private static final String[] Cores = {
            "\u001B[31m", // Vermelho
            "\u001B[32m", // Verde
            "\u001B[33m", // Amarelo
            "\u001B[34m", // Azul
            "\u001B[35m"  // Magenta
    };
    private static final String resetar_Cor = "\u001B[0m"; // Resetar cor para o padrão

    public static void main(String[] args) {
        // Criar e iniciar 5 threads
        for (int i = 1; i <= 5; i++) {
            final int threadId = i;
            Thread thread = new Thread(() -> acessarRecurso(threadId));
            thread.start();
        }
    }

    private static void acessarRecurso(int threadId) {
        String cor = Cores[(threadId - 1) % Cores.length]; // Escolhe a cor com base no ID da thread

        try {
            // Tentar acessar o primeiro recurso (buffer1)
            exibirMensagem(cor, "Thread " + threadId + " tentando acessar Buffer1.");
            bloqueioBuffer1.lock(); // Lock para o buffer1
            try {
                exibirMensagem(cor, "Thread " + threadId + " acessou Buffer1.");
                consumirRecurso("Buffer1", threadId, cor);
            } finally {
                bloqueioBuffer1.unlock(); // Libera o lock
                exibirMensagem(cor, "Thread " + threadId + " liberou Buffer1.");
            }

            // Tentar acessar o segundo recurso (buffer2)
            exibirMensagem(cor, "Thread " + threadId + " tentando acessar Buffer2.");
            bloqueioBuffer2.lock(); // Lock para o buffer2
            try {
                exibirMensagem(cor, "Thread " + threadId + " acessou Buffer2.");
                consumirRecurso("Buffer2", threadId, cor);
            } finally {
                bloqueioBuffer2.unlock(); // Libera o lock
                exibirMensagem(cor, "Thread " + threadId + " liberou Buffer2.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Simula o consumo do recurso por 3 segundos
    private static void consumirRecurso(String resourceName, int threadId, String cor) {
        try {
            exibirMensagem(cor, "Thread " + threadId + " consumindo " + resourceName + ".");
            Thread.sleep(3000); // Simula consumo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            exibirMensagem(cor, "Thread " + threadId + " foi interrompida.");
        }
    }

    // Função para imprimir mensagens coloridas
    private static void exibirMensagem(String cor, String mensagem) {
        System.out.println(cor + mensagem + resetar_Cor);
    }
}
