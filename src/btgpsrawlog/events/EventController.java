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
package btgpsrawlog.events;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

import btgpsrawlog.BTGPSRawLogMidlet;
import btgpsrawlog.forms.AboutForm;
import btgpsrawlog.forms.LoggerForm;
import btgpsrawlog.forms.MainForm;
import btgpsrawlog.forms.SaveLogForm;

/**
 * Handles events of most forms.
 * 
 * @author Christian Lins
 */
public class EventController implements CommandListener {

    protected BTGPSRawLogMidlet midlet;

    public EventController(BTGPSRawLogMidlet midlet) {
        this.midlet = midlet;
    }

    public void commandAction(Command cmd, Displayable disp) {
        if (disp.equals(this.midlet.getLoggerForm())) {
            if (cmd.equals(LoggerForm.START)) {
                try {
                    LoggerForm loggerForm = this.midlet.getLoggerForm();
                    loggerForm.removeCommand(LoggerForm.START);
                    loggerForm.getLogger().connect();
                    loggerForm.getLogger().start();
                    loggerForm.addCommand(LoggerForm.STOP);
                } catch (Exception ex) {
                    this.midlet.getLoggerForm().append(ex.getMessage());
                }
            } else if (cmd.equals(LoggerForm.STOP)) {
                this.midlet.getLoggerForm().getLogger().disconnect();
                this.midlet.showMainForm();
            }
        } else if (disp.equals(this.midlet.getMainForm())) {
            try {
                if (cmd.equals(MainForm.EXIT)) {
                    this.midlet.destroyApp(false);
                    this.midlet.notifyDestroyed();
                } else if (cmd.equals(MainForm.START)) {
                    this.midlet.showBluetoothDeviceList();
                } else if (cmd.equals(MainForm.ABOUT)) {
                    this.midlet.showAboutForm();
                }
            } catch (Exception ex) {
                this.midlet.getMainForm().append(ex.getMessage());
            }
        } else if (disp.equals(this.midlet.getSaveLogForm())) {
            if (cmd.equals(SaveLogForm.OK)) {
                this.midlet.getLoggerForm().getLogger()
                        .setSaveFile(this.midlet.getSaveLogForm().getPath());
                Display.getDisplay(midlet).setCurrent(this.midlet.getLoggerForm());
            } else if (cmd.equals(SaveLogForm.BACK)) {
                // Go back to Bluetooth selection
                commandAction(MainForm.START, this.midlet.getMainForm());
            }
        } else if (cmd.equals(AboutForm.BACK)) {
            Display.getDisplay(midlet).setCurrent(this.midlet.getMainForm());
        }
    }

}
