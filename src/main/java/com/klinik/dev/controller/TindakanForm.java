package com.klinik.dev.controller;

import com.google.common.eventbus.Subscribe;
import com.j256.ormlite.dao.Dao;
import com.klinik.dev.contract.OnOkFormContract;
import com.klinik.dev.datastructure.ComparableCollections;
import com.klinik.dev.db.model.Rule;
import com.klinik.dev.db.model.Tindakan;
import com.klinik.dev.db.model.TindakanRule;
import com.klinik.dev.enums.OPERATION_TYPE;
import com.klinik.dev.events.EventBus;
import com.klinik.dev.events.RuleEvent;
import com.klinik.dev.util.Log;
import com.klinik.dev.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.Data;
import tray.notification.NotificationType;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by khairulimam on 29/01/17.
 */
@Data
public class TindakanForm implements Initializable {

  private OnOkFormContract onOkFormContract;

  private Dao<Rule, Integer> ruleDao = Rule.getDao();
  private Dao<Tindakan, Integer> tindakanDao = Tindakan.getDao();
  private Dao<TindakanRule, Void> tindakanRuleDao = TindakanRule.getDao();

  private ObservableList<Rule> ruleLists = FXCollections.observableArrayList(ruleDao.queryForAll());

  @FXML
  private TextField tfNamaTindakan;
  @FXML
  private ListView<Rule> lvRules;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    EventBus.getInstance().register(this);
    this.lvRules.setItems(ruleLists);
    this.lvRules.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    EventBus.getInstance().register(this);
  }

  private void updateRulesItem(Rule rule) {
    int indexOfUpdatedRule = ComparableCollections.binarySearch(ruleLists, rule);
    if (indexOfUpdatedRule > -1) {
      this.ruleLists.set(indexOfUpdatedRule, rule);
    }
  }

  private void deleteRulesItem(Rule rule) {
    int index = ComparableCollections.binarySearch(ruleLists, rule);
    if (index > -1) {
      this.ruleLists.remove(index);
    }
  }

  public TindakanForm() throws SQLException {
  }

  @FXML
  private void onOkCreate() {
    if (onOkFormContract != null) {
      onOkFormContract.onPositive();
      return;
    }
    Log.w(getClass(), "Contract ain't implemented yet");
  }

  public Tindakan getTindakan() {
    Tindakan tindakan = new Tindakan();
    tindakan.setNamaTindakan(tfNamaTindakan.getText());
    return tindakan;
  }

  public List<Rule> getSelectedRulesFromLvRules() {
    List<Rule> rules = new ArrayList<>();
    List<Integer> selectedIndices = lvRules.getSelectionModel().getSelectedIndices();
    selectedIndices.forEach(index -> rules.add(ruleLists.get(index)));
    return rules;
  }

  @Subscribe
  public void onRule(RuleEvent ruleEvent) {
    Rule rule = ruleEvent.getRule();
    switch (ruleEvent.getOPERATION_TYPE()) {
      case CREATE:
        this.ruleLists.add(rule);
        break;
      case UPDATE:
        updateRulesItem(rule);
        break;
      case DELETE:
        deleteRulesItem(rule);
        break;
    }
  }

  public void onKeyPressed(KeyEvent keyEvent) throws SQLException {
    if (keyEvent.getCode() == KeyCode.D) {
      Optional<ButtonType> decision = Util.deleteConfirmation().showAndWait();
      boolean isOK = decision.get().getButtonData().equals(ButtonBar.ButtonData.OK_DONE);
      if (isOK) {
        Rule rule = ruleLists.get(lvRules.getSelectionModel().getSelectedIndex());
        int deleted = ruleDao.delete(rule);
        if (deleted == 1) {
          Util.showNotif("Sukses", "Data rule telah dihapus", NotificationType.SUCCESS);
          EventBus.getInstance().post(new RuleEvent(rule, OPERATION_TYPE.DELETE));
        }
      }
    }
  }
}
