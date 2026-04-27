import WorkerImpl.FolderImpl;
import WorkerImpl.WriteImpl;
import WorkerImpl.MoveImpl;

public class main {
    public static void main(String[] args){
        final int totalFiles = 1000;
        final int chunkSize = 100;
        FolderImpl dataFolder = new FolderImpl("data","public/data");
        dataFolder.createFolder();

        FolderImpl outputFolder = new FolderImpl("output","public/output");
        outputFolder.createFolder();

        WriteImpl writer = new WriteImpl("write", "public/file.txt", "public/data", totalFiles, chunkSize);
        writer.writeFiles();

        FolderImpl DataFolders = new FolderImpl("folders","public/output",totalFiles, chunkSize);
        DataFolders.createFolders();

        MoveImpl moveToDir = new MoveImpl("moveDir", "public/data", "public/output", totalFiles, chunkSize);
        moveToDir.moveFiles();
    }
}