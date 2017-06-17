/*
 * Copyright (C) 2017 FHNW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.fhnw.lernstickwelcome.fxmlcontroller.exam;

import ch.fhnw.lernstickwelcome.util.WelcomeUtil;
import ch.fhnw.lernstickwelcome.view.impl.ToggleSwitch;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author user
 */
public class SystemController implements Initializable {

    @FXML
    private Button btHelp;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfSystemVersion;
    @FXML
    private TextField tfSystemName;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private PasswordField pfPasswordRepeat;
    @FXML
    private ComboBox<Number> cbVisibleFor;
    @FXML
    private ToggleSwitch tsStartWa;
    @FXML
    private ToggleSwitch tsDirectSound;
    @FXML
    private ToggleSwitch tsBlockKde;
    @FXML
    private ToggleSwitch tsAllowFileSystems;
    @FXML
    private TextField tfExchangePartition;
    @FXML
    private ToggleSwitch tsAccessUser;
    @FXML
    private ToggleSwitch tsShowWarning;

    private final Integer[] visibleForValues = new Integer[]{5, 10, 15, 20, 25, 30, 40, 50, 60};

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbVisibleFor.setConverter(new SecondStringConverter(rb));
        cbVisibleFor.setEditable(true);

        tfUsername.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!isChangeUsernameAllowed(newValue)) {
                        tfUsername.setText(oldValue);
                    }
                });

        tfExchangePartition.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == null) {
                        return;
                    }
                    // only allow ASCII input
                    if (!isASCII(newValue)) {
                        tfExchangePartition.setText(oldValue);
                        return;
                    }

                    if (getSpecialLength(newValue) <= 11) {
                        tfExchangePartition.setText(newValue);
                    } else {
                        tfExchangePartition.setText(oldValue);
                        Toolkit.getDefaultToolkit().beep();
                    }
                });

        cbVisibleFor.getItems().addAll(visibleForValues);

        if (!WelcomeUtil.isImageWritable()) {
            cbVisibleFor.setVisible(false);
            tfSystemName.setDisable(true);
            tfSystemVersion.setDisable(true);
        }
    }

    private boolean isChangeUsernameAllowed(String string) {
        if ((string != null) && string.chars().anyMatch(c
                -> (c == ':') || (c == ',') || (c == '='))) {
            Toolkit.getDefaultToolkit().beep();
            return false;
        }
        return true;
    }

    private boolean isASCII(String string) {
        for (int i = 0, length = string.length(); i < length; i++) {
            char character = string.charAt(i);
            if ((character < 0) || (character > 127)) {
                return false;
            }
        }
        return true;
    }

    private int getSpecialLength(String string) {
        // follow special rules for VFAT labels
        int count = 0;
        for (int i = 0, length = string.length(); i < length; i++) {
            char character = string.charAt(i);
            if ((character >= 0) && (character <= 127)) {
                // ASCII
                if ((character == 39) || (character == 96)) {
                    // I have no idea why those both characters take up 3 bytes
                    // but they really do...
                    count += 3;
                } else {
                    count++;
                }
            } else {
                // non ASCII
                count += 2;
            }
        }
        return count;
    }

    private static class SecondStringConverter extends StringConverter<Number> {

        String seconds;
        String second;

        public SecondStringConverter(ResourceBundle rb) {
            if (rb != null) {
                seconds = rb.getString("welcomeApplicationSystem.seconds");
                second = rb.getString("welcomeApplicationSystem.second");
            } else {
                seconds = "";
                second = "";
            }
        }

        @Override
        public String toString(Number t) {
            return t.intValue() + " " + (t.intValue() == 1 ? second : seconds);
        }

        @Override
        public Number fromString(String string) {
            return Integer.valueOf(string.split(" ")[0]);
        }
    }

    public TextField getTfSystemName() {
        return tfSystemName;
    }

    public TextField getTfSystemVersion() {
        return tfSystemVersion;
    }

    public TextField getTfUsername() {
        return tfUsername;
    }

    public TextField getTxt_sys_password() {
        return pfPassword;
    }

    public TextField getTxt_sys_password_repeat() {
        return pfPasswordRepeat;
    }

    public ComboBox<Number> getCbVisibleFor() {
        return cbVisibleFor;
    }

    public ToggleSwitch getTsStartWa() {
        return tsStartWa;
    }

    public ToggleSwitch getTsDirectSound() {
        return tsDirectSound;
    }

    public ToggleSwitch getTsBlockKde() {
        return tsBlockKde;
    }

    public ToggleSwitch getTsAllowFileSystems() {
        return tsAllowFileSystems;
    }

    public TextField getTfExchangePartition() {
        return tfExchangePartition;
    }

    public ToggleSwitch getTsAccessUser() {
        return tsAccessUser;
    }

    public ToggleSwitch getTsShowWarning() {
        return tsShowWarning;
    }

    public Button getBtnSysHelp() {
        return btHelp;
    }
}