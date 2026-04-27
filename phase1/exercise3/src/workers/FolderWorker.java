package workers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderWorker implements Runnable {
    private String threadname;
    private String pathname;

    public FolderWorker(String name, String pathname) {
        this.threadname = name;
        this.pathname = pathname;
    }

    @Override
    public void run() {
        try {
            Path path = Paths.get(pathname);
            Files.createDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}