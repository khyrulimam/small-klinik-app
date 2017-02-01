package com.klinik.dev.db.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.klinik.dev.contract.Searchable;
import lombok.Data;

/**
 * Created by khairulimam on 25/01/17.
 */
@DatabaseTable(tableName = "riwayat_tindakan")
@Data
public class RiwayatTindakan implements Searchable {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Pasien pasien;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Tindakan tindakan;
    @DatabaseField
    private boolean status;

    @Override
    public int getInt() {
        return this.id;
    }
}