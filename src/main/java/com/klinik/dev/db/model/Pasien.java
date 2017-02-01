package com.klinik.dev.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.klinik.dev.bussiness.BRule;
import com.klinik.dev.contract.Comparable;
import com.klinik.dev.enums.AGAMA;
import com.klinik.dev.enums.STATUS;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by khairulimam on 23/01/17.
 */
@DatabaseTable(tableName = "pasien")
@Data
public class Pasien implements Comparable {
    @DatabaseField(generatedId = true, width = 4)
    private int noRekamMedis;
    @DatabaseField(width = 30)
    private String nama;
    @DatabaseField(width = 10)
    private String namaPanggilan;
    @DatabaseField(dataType = DataType.DATE_TIME)
    private DateTime tglLahir;
    @DatabaseField
    private String alamat;
    @DatabaseField(dataType = DataType.ENUM_STRING, unknownEnumName = "BELUM_MENIKAH", width = 20)
    private STATUS status;
    @DatabaseField(width = 15)
    private String noTelepon;
    @DatabaseField(width = 20)
    private String pekerjaan;
    @DatabaseField(dataType = DataType.ENUM_STRING, unknownEnumName = "_", width = 30)
    private AGAMA agama;
    @DatabaseField(dataType = DataType.DATE_TIME)
    private DateTime tglRegister;
    @DatabaseField(dataType = DataType.DATE_TIME)
    private DateTime checkupTerakhir;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    private Tindakan tindakan;
    @ForeignCollectionField
    private ForeignCollection<RiwayatTindakan> riwayatTindakans;

    public DateTime getCheckupTerakhir() {
        return checkupTerakhir.withTimeAtStartOfDay();
    }

    public Pasien getPasien() {
        return this;
    }

    public String getJadwalSelanjutnya() {
        if (tindakan == null)
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        List<BRule> bRules = tindakan.getTindakan().getBRules();
        for (BRule bRule : bRules) {
            stringBuilder.append(String.format("(%s) tgl %s\n", bRule.getRuleName(), bRule.toStringDate(checkupTerakhir)));
        }
        return stringBuilder.toString();
    }

    @Override
    public int toBeCompared() {
        return this.noRekamMedis;
    }
}
