package com.klinik.dev.contractmorph;

import com.j256.ormlite.dao.Dao;
import com.klinik.dev.Util;
import com.klinik.dev.contract.OnOkFormContract;
import lombok.Data;
import tray.notification.NotificationType;

import java.sql.SQLException;

/**
 * Created by khairulimam on 29/01/17.
 */
@Data
public class OkSimpan implements OnOkFormContract {
    private Dao tableDao;

    @Override
    public void onPositive(Object object) {
        try {
            int create = tableDao.create(object);
            if (create == 1)
                Util.showNotif("Sukses", "Berhasil menanmbahkan data", NotificationType.INFORMATION);
        } catch (SQLException e) {
            Util.showNotif("Sukses", e.getMessage(), NotificationType.ERROR);
            e.printStackTrace();
        }
    }
}
