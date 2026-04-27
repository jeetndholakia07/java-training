package models;

import java.sql.Timestamp;

public class BusinessEntity {
    private int id;
    private String rowGuid;
    private Timestamp modifiedDate;
    private String rowMd5;
    private boolean isDeleted;
    private Timestamp syncTs;
    private Timestamp startTs;
    private Timestamp finishTs;
    private boolean isActive;

    public BusinessEntity(){}

    public BusinessEntity(String rowguid, Timestamp Date, String row, boolean isDel, Timestamp Sync, Timestamp Start, Timestamp Finish, boolean isActive){
        this.rowGuid = rowguid;
        this.modifiedDate = Date;
        this.rowMd5 = row;
        this.isDeleted = isDel;
        this.syncTs = Sync;
        this.startTs = Start;
        this.finishTs = Finish;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getRowGuid() {
        return rowGuid;
    }

    public String getRowMd5() {
        return rowMd5;
    }

    public Timestamp getFinishTs() {
        return finishTs;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public Timestamp getStartTs() {
        return startTs;
    }

    public Timestamp getSyncTs() {
        return syncTs;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setFinishTs(Timestamp finishTs) {
        this.finishTs = finishTs;
    }

    public void setRowGuid(String rowGuid) {
        this.rowGuid = rowGuid;
    }

    public void setRowMd5(String rowMd5) {
        this.rowMd5 = rowMd5;
    }

    public void setStartTs(Timestamp startTs) {
        this.startTs = startTs;
    }

    public void setSyncTs(Timestamp syncTs) {
        this.syncTs = syncTs;
    }
}
