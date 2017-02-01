package com.klinik.dev.controller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.klinik.dev.Util;
import com.klinik.dev.contract.OnOkFormContract;
import com.klinik.dev.db.DB;
import com.klinik.dev.db.model.Rule;
import com.klinik.dev.db.model.Tindakan;
import com.klinik.dev.enums.OPERATION_TYPE;
import com.klinik.dev.events.EventBus;
import com.klinik.dev.events.RuleEvent;
import com.klinik.dev.events.TindakanEvent;
import com.sun.istack.internal.Nullable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.Data;
import tray.notification.NotificationType;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by khairulimam on 29/01/17.
 */
@Data
public class TindakanAndRuleFormDialog implements Initializable {

    private Dao ruleDao = DaoManager.createDao(DB.getDB(), Rule.class);
    private Dao tindakanDao = DaoManager.createDao(DB.getDB(), Tindakan.class);

    public enum METODE {
        SIMPAN_RULE,
        SIMPAN_TINDAKAN
    }

    public TindakanAndRuleFormDialog() throws SQLException {
    }

    @FXML
    private RuleForm ruleFormController;
    @FXML
    private TindakanForm tindakanFormController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ruleFormController.setOnOkFormContract(new SimpanRuleAtauTindakan(METODE.SIMPAN_RULE));
        tindakanFormController.setOnOkFormContract(new SimpanRuleAtauTindakan(METODE.SIMPAN_TINDAKAN));
    }

    private class SimpanRuleAtauTindakan implements OnOkFormContract {
        METODE metode;

        public SimpanRuleAtauTindakan(METODE metode) {
            this.metode = metode;
        }

        @Override
        public void onPositiveButtonClicked(@Nullable Object data) {
            int create = 0;
            switch (metode) {
                case SIMPAN_RULE:
                    try {
                        Rule rule = (Rule) data;
                        create = ruleDao.create(rule);
                        EventBus.getInstance().post(new RuleEvent(rule, OPERATION_TYPE.CREATE));
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Util.showNotif("Error", String.format("Maaf ada kesalahan: %s", e.getMessage()), NotificationType.ERROR);
                    }
                    break;
                case SIMPAN_TINDAKAN:
                    try {
                        Tindakan tindakan = (Tindakan) data;
                        create = tindakanDao.create(tindakan);
                        EventBus.getInstance().post(new TindakanEvent(tindakan, OPERATION_TYPE.CREATE));
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Util.showNotif("Error", String.format("Maaf ada kesalahan: %s", e.getMessage()), NotificationType.ERROR);
                    }
                    break;
            }
            if (create == 1)
                Util.showNotif("Sukses", "Berhasil menanmbahkan data", NotificationType.SUCCESS);
        }
    }
}
