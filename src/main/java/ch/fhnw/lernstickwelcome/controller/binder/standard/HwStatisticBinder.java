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
package ch.fhnw.lernstickwelcome.controller.binder.standard;

import ch.fhnw.lernstickwelcome.controller.WelcomeController;
import ch.fhnw.lernstickwelcome.controller.exception.ProcessingException;
import ch.fhnw.lernstickwelcome.fxmlcontroller.ErrorController;
import ch.fhnw.lernstickwelcome.fxmlcontroller.standard.HwStatisticController;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Binder class to init binings between view components and backend (model)
 * properties
 *
 * @author sschw
 */
public class HwStatisticBinder {

    private final HwStatisticController hwStatistic;
    private final WelcomeController controller;

    /**
     * Constructor of ExamInformationBinder class
     *
     * @param controller is needed to provide access to the backend properties
     * @param hwStatistic FXML controller which prviedes the view properties
     */
    public HwStatisticBinder(WelcomeController controller,
            HwStatisticController hwStatistic) {
        this.hwStatistic = hwStatistic;
        this.controller = controller;
    }

    /**
     * Method to initialize the handlers for this class.
     *
     * @param errorStage the dialog that should be shown on error.
     * @param error the controller which the error message can be provided.
     */
    public void initHandlers(Stage errorStage, ErrorController error) {
        
        hwStatistic.getYesButton().setOnAction(evt -> {
            controller.getSysconf().allowHwStatistic();
            controller.getSysconf().disableShowHwStatistic();
            
            // Save changes
            controller.getProperties().newTask().run();
            ((Stage) ((Node) evt.getSource()).getScene().getWindow()).close();
        });
        
        // Set flag that it does not show anymore
        hwStatistic.getNoButton().setOnAction(evt -> {
            controller.getSysconf().denyHwStatistic();
            controller.getSysconf().disableShowHwStatistic();
            
            // Save chagnes
            controller.getProperties().newTask().run();
            ((Stage) ((Node) evt.getSource()).getScene().getWindow()).close();
        });
    }
}
