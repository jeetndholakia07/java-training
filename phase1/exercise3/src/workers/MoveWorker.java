package workers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public  class MoveWorker implements Runnable{
    private String threadname;
    private String inputPath;
    private String outputPath;

    public MoveWorker(String name, String inputPath, String outputPath){
        this.threadname = name;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    public void moveFile(String inputPath, String outputPath) throws IOException {
        Path source = Paths.get(inputPath);
        Path destination = Paths.get(outputPath);
        Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }
    @Override
    public void run(){
        try{
            moveFile(inputPath,outputPath);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}