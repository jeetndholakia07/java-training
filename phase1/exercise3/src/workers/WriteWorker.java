package workers;

import java.io.*;

public class WriteWorker implements Runnable {
    private String outputPath;
    private String data;

    public WriteWorker(String data, String outputPath) {
        this.data = data;
        this.outputPath = outputPath;
    }

    public void writeFile(String filename, String output) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            writer.write(output);
        }
        catch (IOException e){
            System.out.println("Error writing file: ");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        writeFile(outputPath, data);
    }
}