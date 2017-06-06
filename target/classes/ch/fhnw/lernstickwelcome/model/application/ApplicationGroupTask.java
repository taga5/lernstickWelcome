/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.fhnw.lernstickwelcome.model.application;

import ch.fhnw.lernstickwelcome.model.Processable;
import ch.fhnw.lernstickwelcome.model.WelcomeConstants;
import ch.fhnw.lernstickwelcome.model.application.proxy.ProxyTask;
import java.util.List;
import java.util.stream.Collectors;
import javafx.concurrent.Task;

/**
 * Representing multiple applications which can be installed.
 *
 * @author sschw
 */
public class ApplicationGroupTask implements Processable<String> {

    private List<ApplicationTask> apps;
    private ProxyTask proxy;
    private String title;

    public ApplicationGroupTask(String title, ProxyTask proxy, List<ApplicationTask> apps) {
        this.title = title;
        this.proxy = proxy;
        this.apps = apps;
    }

    public List<ApplicationTask> getApps() {
        return apps;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public Task<String> newTask() {
        return new InternalTask();
    }

    private class InternalTask extends Task<String> {

        @Override
        protected String call() throws Exception {
            updateTitle(title);
            if (apps != null) {
                // Calculate total work
                final List<ApplicationTask> appsToInstall = apps.stream().
                        filter(a -> !a.installedProperty().get() && a.installingProperty().get()).
                        collect(Collectors.toList());
                final int totalWork = appsToInstall.stream().mapToInt(a -> a.getNoPackages()).sum();
                if(totalWork != 0) {
                    updateProgress(0, totalWork);
                    int previouslyDone = 0;
                    for (ApplicationTask app : appsToInstall) {
                        updateMessage(app.getName());
                        updateValue(WelcomeConstants.ICON_APPLICATION_FOLDER + "/" + app.getIcon());
                        Task<String> appTask = app.newTask();
                        // update this progress on changes of sub-process
                        final int fPreviouslyDone = previouslyDone;
                        appTask.progressProperty().addListener(cl -> updateProgress(fPreviouslyDone + appTask.getWorkDone(), totalWork));

                        app.setProxy(proxy);
                        appTask.run();
                        appTask.get();
                        previouslyDone += app.getNoPackages();
                    }
                }
                updateProgress(1, 1);
            }
            return null;
        }
    }

}