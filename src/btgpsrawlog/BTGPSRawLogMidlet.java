/*
 *  BTGPSRawLog
 *  Copyright (C) 2010-2013 Christian Lins <christian@lins.me>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package btgpsrawlog;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

import btgpsrawlog.events.BluetoothDeviceListController;
import btgpsrawlog.events.EventController;
import btgpsrawlog.forms.AboutForm;
import btgpsrawlog.forms.AdForm;
import btgpsrawlog.forms.BluetoothDeviceList;
import btgpsrawlog.forms.LoggerForm;
import btgpsrawlog.forms.MainForm;
import btgpsrawlog.forms.SaveLogForm;

/**
 * Main MIDlet.
 * 
 * @author Christian Lins
 */
public class BTGPSRawLogMidlet extends MIDlet {

    public static BTGPSRawLogMidlet instance;
    public static final boolean     isPro = false;

    public static void quitApp() {
        try {
            instance.destroyApp(true);
            instance.notifyDestroyed();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected BluetoothDeviceListController btDevListController;
    protected Displayable                   currentDisplay;
    protected EventController               eventController;
    protected RawLogger                     rawLogger = null;

    protected AboutForm                     aboutForm;
    protected BluetoothDeviceList           btDevList;
    protected LoggerForm                    loggerForm;
    protected MainForm                      mainForm;
    protected SaveLogForm                   saveLogForm;

    public BTGPSRawLogMidlet() {
        setInstance();
        try {
            aboutForm = new AboutForm(this);
            btDevList = new BluetoothDeviceList(this);
            loggerForm = new LoggerForm();
            mainForm = new MainForm();
            saveLogForm = new SaveLogForm();

            this.eventController = new EventController(this);
            this.btDevListController = new BluetoothDeviceListController(btDevList, this);
            btDevList.setCommandListener(this.btDevListController);
            loggerForm.setCommandListener(eventController);
            mainForm.setCommandListener(eventController);
            saveLogForm.setCommandListener(eventController);
            aboutForm.setCommandListener(eventController);

            this.currentDisplay = mainForm;
            AdForm.startLoader();
        } catch (Throwable t) {
            System.gc();
            Alert alert = new Alert("App startup error",
                    "The following error occurred on startup: " + t.getMessage(), null,
                    AlertType.ERROR);
            alert.setTimeout(Alert.FOREVER);
            this.currentDisplay = alert;
        }
    }

    private synchronized void setInstance() {
        BTGPSRawLogMidlet.instance = this;
    }

    public RawLogger getLogger() {
        return this.rawLogger;
    }

    public LoggerForm getLoggerForm() {
        return this.loggerForm;
    }

    public MainForm getMainForm() {
        return this.mainForm;
    }

    public SaveLogForm getSaveLogForm() {
        return this.saveLogForm;
    }

    public void setRawLogger(RawLogger rawLogger) {
        this.rawLogger = rawLogger;
    }

    public void startApp() {
        Display display = Display.getDisplay(this);
        display.setCurrent(currentDisplay);
    }

    public void pauseApp() {
        Display display = Display.getDisplay(this);
        this.currentDisplay = display.getCurrent();
    }

    public void showMainForm() {
        Display.getDisplay(this).setCurrent(this.mainForm);
    }

    public void showAboutForm() {
        Display.getDisplay(this).setCurrent(this.aboutForm);
    }

    public void showBluetoothDeviceList() {
        Display.getDisplay(this).setCurrent(this.btDevList);

        // Start the search for bluetooth device immediately
        this.btDevListController.commandAction(BluetoothDeviceList.SEARCH, this.btDevList);
    }

    /**
     * Called by the program manager when the app is about to destroyed.
     * 
     * @param unconditional
     */
    public void destroyApp(boolean unconditional) {
        if (this.rawLogger != null) {
            this.rawLogger.stop();
        }
    }
}
