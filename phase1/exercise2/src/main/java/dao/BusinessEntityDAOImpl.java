package dao;

import models.BusinessEntity;
import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BusinessEntityDAOImpl implements BusinessEntityDAO{
    private final String INSERT_SQL = "INSERT INTO business_entity(rowguid, ModifiedDate, __ROW_MD5, __DLH_IS_DELETED, __DLH_SYNC_TS, __DLH_START_TS, __DLH_FINISH_TS, __DLH_IS_ACTIVE)" +
    "VALUES(?,?,?,?,?,?,?,?)";
    @Override
    public void createBusinessEntity(BusinessEntity entity) {
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_SQL);
        ){
            ps.setString(1,entity.getRowGuid());
            ps.setTimestamp(2,entity.getModifiedDate());
            ps.setString(3,entity.getRowMd5());
            ps.setBoolean(4, entity.isDeleted());
            ps.setTimestamp(5,entity.getSyncTs());
            ps.setTimestamp(6, entity.getStartTs());
            ps.setTimestamp(7, entity.getFinishTs());
            ps.setBoolean(8, entity.isActive());

            ps.executeUpdate();
        }
        catch (SQLException e){
            System.out.println(e);
        }
    }

    @Override
    public void batchInsert(List<BusinessEntity> entities, int batchSize) {
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
                int count = 0;

                for (BusinessEntity entity : entities) {
                    ps.setString(1, entity.getRowGuid());
                    ps.setTimestamp(2, entity.getModifiedDate());
                    ps.setString(3, entity.getRowMd5());
                    ps.setBoolean(4, entity.isDeleted());
                    ps.setTimestamp(5, entity.getSyncTs());
                    ps.setTimestamp(6, entity.getStartTs());
                    ps.setTimestamp(7, entity.getFinishTs());
                    ps.setBoolean(8, entity.isActive());

                    ps.addBatch();
                    count++;

                    if (count % batchSize == 0) {
                        ps.executeBatch();
                        conn.commit();
                    }
                }

                ps.executeBatch();
                conn.commit();
            }

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }
}
