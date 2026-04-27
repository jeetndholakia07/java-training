package utils;

import models.BusinessEntity;

import java.sql.Timestamp;

public class BusinessEntityParse{
    public static BusinessEntity parse(String[] parts){
        if(parts.length<9){
            throw new RuntimeException("Invalid CSV Format.");
        }
        BusinessEntity entity = new BusinessEntity();
        entity.setRowGuid(parts[1]);
        entity.setModifiedDate(parseTimestamp(parts[2]));
        entity.setRowMd5(parts[3]);
        entity.setDeleted(parseBoolean(parts[4]));
        entity.setSyncTs(parseTimestamp(parts[5]));
        entity.setStartTs(parseTimestamp(parts[6]));
        entity.setFinishTs(parseTimestamp(parts[7]));
        entity.setActive(parseBoolean(parts[8]));
        return entity;
    }

    private static Timestamp parseTimestamp(String value){
        try{
            if(value==null || value.isEmpty()) return null;
            return Timestamp.valueOf(value);
        }
        catch (Exception e){
            throw new RuntimeException("Invalid timestamp: " + value, e);
        }
    }

    private static boolean parseBoolean(String value){
        return "1".equals(value) || Boolean.parseBoolean(value);
    }
}
