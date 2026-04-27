import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import dao.BusinessEntityDAOImpl;
import models.BusinessEntity;
import utils.BusinessEntityParse;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    private static final int batchSize = 100;
    private static final int chunkSize = 500;
    private static final int threadCount = 5;
    private static final String filePath = "src/main/resources/SQLSERVER-Person_BusinessEntity-last-20777.csv";
    public static void main(String[] args){
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Future<?>> futures = new ArrayList<>();
        try(CSVReader reader = new CSVReader(new FileReader(filePath))){
            String[] parts;
            List<BusinessEntity> buffer = new ArrayList<>();
            reader.readNext();

            while((parts = reader.readNext())!=null){
               try{
                   BusinessEntity entity = BusinessEntityParse.parse(parts);
                   buffer.add(entity);
               }
               catch (Exception e){
                   System.out.println("Error parsing in line:" + String.join("", parts));
                   e.printStackTrace();
                   continue;
               }
               if(buffer.size()==chunkSize){
                   List <BusinessEntity> chunk = new ArrayList<>(buffer);
                   futures.add(executorService.submit(()->{
                       BusinessEntityDAOImpl dao = new BusinessEntityDAOImpl();
                       dao.batchInsert(chunk, batchSize);
                   }));
                   buffer.clear();
               }
           }

            if(!buffer.isEmpty()){
                List<BusinessEntity> chunk = new ArrayList<>(buffer);
                futures.add(executorService.submit(()->{
                    BusinessEntityDAOImpl dao = new BusinessEntityDAOImpl();
                    dao.batchInsert(chunk, batchSize);
                }));
            }

            for(Future<?>f : futures){
                try{
                    f.get();
                }
                catch (Exception e){
                    System.err.println("Thread failed: " + e.getMessage());
                }
            }
            System.out.println("Loaded data into database successfully.");
       }
       catch (CsvException | IOException e){
           System.out.println(e);
       }
        finally {
            executorService.shutdown();
        }
    }
}
