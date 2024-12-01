import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    private static final StringBuffer buffer1 = new StringBuffer("Buffer1");
    private static final StringBuffer buffer2 = new StringBuffer("Buffer2");

    private static final Lock lockBuffer1 = new ReentrantLock();
    private static final Lock lockBuffer2 = new ReentrantLock();

    // HashMap para rastrear progresso de cada thread
    private static final ConcurrentHashMap<Integer, AtomicInteger> progressoThread = new ConcurrentHashMap<>();
    private static final int totalAcessoPorThread = 2; // Cada thread deve acessar os dois buffers

    private static final String[] Cores = {
            "\u001B[31m",
            "\u001B[32m",
            "\u001B[33m",
            "\u001B[34m",
            "\u001B[35m"
    };
    private static final String resetarCor = "\u001B[0m";

    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            final int threadId = i;
            progressoThread.put(threadId, new AtomicInteger(0));
            new Thread(() -> acessarRecurso(threadId)).start();
        }
    }

    private static void acessarRecurso(int threadId) {
        String cor = Cores[(threadId - 1) % Cores.length];

        while (true) {
            int progresso = progressoThread.get(threadId).get();
            if (progresso >= totalAcessoPorThread) {
                exibirMensagem(cor, "Thread " + threadId + " completou todos os acessos. Encerrando.");
                return;
            }

            // Tenta acessar ambos os buffers
            if (progresso == 0) {
                if (acessarBuffer(lockBuffer1, buffer1, "Buffer1", threadId, cor)) {
                    progressoThread.get(threadId).incrementAndGet();
                }
            } else if (progresso == 1) {
                if (acessarBuffer(lockBuffer2, buffer2, "Buffer2", threadId, cor)) {
                    progressoThread.get(threadId).incrementAndGet();
                }
            }
        }
    }

    private static boolean acessarBuffer(Lock lock, StringBuffer buffer, String nomeBuffer, int idThread, String cor) {
        exibirMensagem(cor, "Thread " + idThread + " tentando acessar " + nomeBuffer + ".");

        // Gera um tempo de espera aleatório entre 1 e 5 segundos
        long tempoRandom = ThreadLocalRandom.current().nextInt(1, 6);

        try {
            if (lock.tryLock(tempoRandom, TimeUnit.SECONDS)) {
                try {
                    exibirMensagem(cor, "Thread " + idThread + " acessou " + nomeBuffer + ".");
                    consumirRecurso(nomeBuffer, idThread, cor);
                } finally {
                    exibirMensagem(cor, "Thread " + idThread + " liberou " + nomeBuffer + ".");
                    lock.unlock(); // Liberar o lock depois da mensagem de liberação
                }
                return true;
            } else {
                exibirMensagem(cor, "Thread " + idThread + " não conseguiu acessar " + nomeBuffer + " (ocupado).");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            exibirMensagem(cor, "Thread " + idThread + " foi interrompida.");
        }

        return false;
    }

    private static void consumirRecurso(String bufferName, int threadId, String color) {
        try {
            exibirMensagem(color, "Thread " + threadId + " consumindo " + bufferName + ".");
            Thread.sleep(3000); // Simula o tempo de consumo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            exibirMensagem(color, "Thread " + threadId + " foi interrompida durante o consumo.");
        }
    }

    private static void exibirMensagem(String color, String message) {
        synchronized (System.out) { // Sincroniza mensagens para evitar mistura de prints
            System.out.println(color + message + resetarCor);
        }
    }
}
