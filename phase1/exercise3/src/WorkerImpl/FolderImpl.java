package WorkerImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FolderImpl {
    private String name;
    private String pathname;
    private int totalFiles;
    private int chunkSize;

    public FolderImpl(String name, String pathname){
        this.name = name;
        this.pathname = pathname;
    }

    public FolderImpl(String name, String pathname, int totalFiles, int chunkSize){
        this.name = name;
        this.pathname = pathname;
        this.totalFiles = totalFiles;
        this.chunkSize = chunkSize;
    }

    public void createFolder(){
        try{
            Path path = Paths.get(pathname);
            Files.createDirectory(path);
            System.out.println("Created folder " + name + " successfully.");
        }
        catch (IOException e){
           e.printStackTrace();
        }
    }

    public void createFolders(){
        List<Thread> folders = new ArrayList<>();
        for(int i=1;i<=totalFiles;i+=chunkSize){
            Thread t = new Thread(new workers.FolderWorker(String.format("%s-%d",name, i),String.format("%s/%d-%d",pathname,i,i+chunkSize-1)));
            folders.add(t);
        }
        for(Thread t: folders){
                t.start();
        }
        for(Thread t: folders){
            try{
                t.join();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println("Created " + name + " folders" + " successfully");
    }
}
