package WorkerImpl;

import java.util.ArrayList;
import java.util.List;

public class MoveImpl {
    private String name;
    private String inputPath;
    private String outputPath;
    private int totalFiles;
    private int chunkSize;

    public MoveImpl(String name, String inputPath, String outputPath, int totalFiles, int chunkSize){
        this.name = name;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.totalFiles = totalFiles;
        this.chunkSize = chunkSize;
    }

    public void moveFiles(){
        List<Thread> moveFiles = new ArrayList<>();
        for(int i=1; i<=totalFiles; i+=chunkSize){
            for(int j=i; j<=i+chunkSize-1; j++){
                Thread t = new Thread(new workers.MoveWorker(String.format("%s-%d",name, j),String.format("%s/%d.txt",inputPath, j),String.format("%s/%d-%d/%d.txt",outputPath, i,i+chunkSize-1,j)));
                moveFiles.add(t);
            }
        }
        for(Thread t: moveFiles){
            t.start();
        }
        for(Thread t: moveFiles){
            try{
                t.join();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println("Moved " + totalFiles + " files successfully");
    }
}