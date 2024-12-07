package filesystem;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class Journal implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient List<String> logs; // Logs na memória
    private String logFilePath;

    public Journal(String logFilePath) {
        this.logFilePath = "C:\\Users\\joaom\\Downloads\\Sistema_Arquivos4\\journal.txt";
        this.logs = new ArrayList<>();
        loadLogsFromFile();
    }

    public void logOperation(String operation, String details) {
        String logEntry = operation + ": " + details;
        logs.add(logEntry); // Adiciona à lista de logs
        if (logs.size() > 1000) { // Mantém no máximo 10.000 logs na memória
            logs = logs.subList(logs.size() - 10000, logs.size());
        }

        saveLogsToFile();
    }

    // Retorna os últimos 10 logs, ou todos se houver menos de 10
    public List<String> getLogs() {
        loadLogsFromFile(); // Atualiza os logs ao pegar
        int startIndex = Math.max(0, logs.size() - 10);
        return new ArrayList<>(logs.subList(startIndex, logs.size())); // Retorna os 10 logs mais recentes
    }

    private void saveLogsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath))) {
            for (String log : logs) {
                writer.write(log);
                writer.newLine();
            }
            writer.flush();
            System.out.println("Logs salvos no arquivo '" + logFilePath + "'"); // Confirmação no terminal
        } catch (IOException e) {
            System.err.println("Erro ao salvar logs no arquivo '" + logFilePath + "': " + e.getMessage());
        }
    }


    private void loadLogsFromFile() {
        File file = new File(logFilePath);
        if (!file.exists()) {
            return; // Se o arquivo não existe, não há logs para carregar
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            logs.clear(); // Limpa os logs antes de carregar novos
            String line;
            while ((line = reader.readLine()) != null) {
                logs.add(line); // Adiciona as linhas do arquivo aos logs na memória
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar logs do arquivo '" + logFilePath + "': " + e.getMessage());
        }
    }




    // Serialização customizada para ignorar logs na memória
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject(); // Serializa os campos padrão
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject(); // Desserializa os campos padrão
        if (logFilePath == null || logFilePath.isEmpty()) {
            logFilePath = "journal.txt"; // Caminho padrão para o arquivo de logs
        }
        logs = new ArrayList<>(); // Inicializa a lista de logs vazia
        loadLogsFromFile(); // Carrega os logs do arquivo
    }
}

