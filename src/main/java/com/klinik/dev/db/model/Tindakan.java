package com.klinik.dev.db.model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.klinik.dev.contract.Comparable;
import com.klinik.dev.db.DB;
import lombok.Data;

import java.sql.SQLException;

/**
 * Created by khairulimam on 25/01/17.
 */

@DatabaseTable(tableName = "tindakan")
@Data
public class Tindakan implements Comparable {
  @DatabaseField(generatedId = true)
  private int id;
  @DatabaseField(width = 20)
  private String namaTindakan;
  @ForeignCollectionField
  private ForeignCollection<RiwayatTindakan> riwayatTindakans;
  @ForeignCollectionField
  private ForeignCollection<TindakanRule> tindakanrules;

  @Override
  public String toString() {
    return getNamaTindakan();
  }

  public static Dao<Tindakan, Integer> getDao() {
    try {
      return DaoManager.createDao(DB.getDB(), Tindakan.class);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public String toString_() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getNamaTindakan() + ":\n");
    tindakanrules.forEach(tindakanRule -> stringBuilder.append(tindakanRule.getRule() + ", "));
    stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "");
    return stringBuilder.toString();
  }

  @Override
  public int toBeCompared() {
    return this.id;
  }
}
