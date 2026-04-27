package WorkerImpl;

import workers.WriteWorker;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WriteImpl {
    private String inputPath;
    private String outputPath;
    private int totalFiles, chunkSize;
    private int threadCount;

    public WriteImpl(String inputPath, String outputPath, int totalFiles, int chunkSize, int threadCount){
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.totalFiles = totalFiles;
        this.chunkSize = chunkSize;
        this.threadCount = threadCount;
    }

    public String readFile(String filename) {
        String result="";
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            StringBuilder data = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                data.append(line).append(System.lineSeparator());
            }
            result = data.toString();
        }
        catch (IOException e){
            System.out.println("Error reading file: ");
            e.printStackTrace();
        }
        return result;
    }

    public void writeFiles(){
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        String data = readFile(inputPath);
        for (int i = 1; i <= totalFiles; i++) {
            executor.submit(new WriteWorker(
                    data,
                    String.format("%s/%d.txt", outputPath, i)
            ));
        }

        executor.shutdown();

        try {
            if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("Created " + totalFiles + " files successfully");
    }
}
