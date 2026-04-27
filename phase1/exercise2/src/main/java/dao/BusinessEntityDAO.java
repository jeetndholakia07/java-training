package dao;

import models.BusinessEntity;
import java.util.List;

public interface BusinessEntityDAO {
    public void createBusinessEntity(BusinessEntity entity);
    public void batchInsert(List<BusinessEntity> entities, int batchSize);
}