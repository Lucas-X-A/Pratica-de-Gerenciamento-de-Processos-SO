# Pratica-de-Gerenciamento-de-Processos-SO
Atividade 1. Cinco threads concorrendo simultaneamente a dois recursos compartilhados (ex.: variáveis globais, buffers, etc.), as quais somente uma thread por vez pode acessar cada recurso compartilhado (pode utilizar qualquer técnica de exclusão mútua). Nesse caso, deve ser demonstrado (com logs, prints, graficamente, etc.) que as condições de corrida existem e que a exclusão mútua de fato ocorre. Como sugestão, considerar a permuta de uma thread para outra a cada 3 segundos (nesse intervalo, uma thread pode estar "consumindo" o recurso enquanto as demais aguardam, seja de forma bloqueada ou como espera ocupada), como no exemplo do produtor/consumidor (porém aqui com dois buffers).
Atividade 2.  Um "mini" simulador (pode se basear nessa ferramenta: https://sourceforge.net/projects/oscsimulator/) de escalonamento preemptivo de processos, onde seja possível um usuário (não precisa de interface gráfica, pode ser linha de comando):

Criar processos indicando: ID, Nome, prioridade, processo I/O bound ou CPU/bound, tempo de CPU total (ex.: em unidades inteiras de tempo, por exemplo, 1 a 10 ms). A cada criação, o processo deve ser inserido na fila de "pronto" para ser escalonado conforme algoritmo de escalonamento;
Escolher uma de duas opções de algoritmo de escalonamento implementadas (se em dupla escolher uma por integrante);
Selecionar o tempo de quantum da preempção (ex.: em unidades inteiras de tempo, por exemplo, 1 a 10 ms)
Mostrar a lista de processos na fila de "prontos" dinamicamente (atualizar conforme escalonamento);
Iniciar a execução e escalonamento de processos, mostrando (com logs, prints, graficamente, etc.) ao usuário qual processo está ativo na CPU (por quanto tempo), a preempção do processo e quais estão aguardando, indicando sempre a ordem de execução dos algoritmos.
Ao final da execução, indicar o tempo de turnaround de cada processo e o tempo médio de espera de todos os processos
