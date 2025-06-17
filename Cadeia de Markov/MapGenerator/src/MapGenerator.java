import java.util.*; // Importa as bibliotecas necessárias, como List, ArrayList e Random

public class MapGenerator {

    // Lista de tipos de sala possíveis — são os "estados" da cadeia de Markov
    private static final String[] salas = {"Start", "Enemy", "Treasure", "Trap", "Boss", "Exit"};

    // Matriz de transição de estados: representa as probabilidades de ir de uma sala para outra
    private static final double[][] P = {
            {0.6, 0.2, 0.2, 0.0, 0.0},   // Probabilidades saindo de "Start"
            {0.4, 0.2, 0.2, 0.1, 0.1},   // de "Enemy"
            {0.3, 0.0, 0.3, 0.2, 0.2},   // de "Treasure"
            {0.3, 0.1, 0.4, 0.1, 0.1},   // de "Trap"
            {0.0, 0.0, 0.0, 0.0, 1.0},   // "Boss" sempre leva para "Exit"
            {0.0, 0.0, 0.0, 0.0, 1.0}    // "Exit" só leva a ela mesma (estado absorvente)
    };

    // Lista que guardará a sequência de salas do mapa gerado
    private List<String> mapa;

    // Construtor que gera o mapa ao criar um objeto da classe
    public MapGenerator(int maxSalas) {
        this.mapa = gerarMapa(maxSalas);
    }

    // Metodo privado que implementa a lógica de geração do mapa com base nas transições
    private List<String> gerarMapa(int maxSalas) {
        List<String> mapa = new ArrayList<>();
        Random rand = new Random();

        int estadoAtual = 0; // Começa em "Start" (índice 0 no array de salas)
        mapa.add(salas[estadoAtual]); // Adiciona "Start" como primeira sala

        // Enquanto não chegar em "Exit" e o número de salas for menor que o máximo permitido
        while (!salas[estadoAtual].equals("Exit") && mapa.size() < maxSalas) {
            // Escolhe próximo estado com base na linha da matriz de transição
            estadoAtual = escolherProximoEstado(P[estadoAtual], rand);
            mapa.add(salas[estadoAtual]); // Adiciona a nova sala à lista
        }

        return mapa;
    }

    // Dado um vetor de probabilidades, sorteia aleatoriamente o próximo estado
    private int escolherProximoEstado(double[] probabilidades, Random rand) {
        double r = rand.nextDouble(); // Número aleatório entre 0 e 1
        double acumulado = 0.0;

        // Vai acumulando as probabilidades até encontrar o intervalo correspondente ao número sorteado
        for (int i = 0; i < probabilidades.length; i++) {
            acumulado += probabilidades[i];
            if (r < acumulado) return i + 1; // +1 pois "Start" (índice 0) não está na matriz P
        }

        // Fallback de segurança (não deveria ocorrer se as probabilidades somarem 1)
        return probabilidades.length;
    }

    // Getter que permite acessar o mapa gerado de fora da classe
    public List<String> getMapa() {
        return mapa;
    }

    // Sobrescreve o metodo toString para retornar o mapa como uma string formatada

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"mapa\": [");

        // Itera pela lista de salas para montar a string JSON-like
        for (int i = 0; i < mapa.size(); i++) {
            sb.append("\"").append(mapa.get(i)).append("\"");
            if (i < mapa.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("]\n}");
        return sb.toString(); // Retorna a string completa
    }
}
