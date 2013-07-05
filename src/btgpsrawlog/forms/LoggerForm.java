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
package btgpsrawlog.forms;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Spacer;
import javax.microedition.lcdui.StringItem;

import btgpsrawlog.RawLogger;

/**
 * Logging status form.
 * 
 * @author Christian Lins
 */
public class LoggerForm extends Form {

    public static final Command START = new Command("Start", null, Command.OK, 0);
    public static final Command STOP  = new Command("Stop", null, Command.STOP, 0);

    private static final String MSG   = "\nPress Start to finally start logging GPS data!";
    private static final String MSG2  = "\nGPS device does not respond or is incompatible!\nPlease retry!";

    private RawLogger           logger;
    private final Timer         timer = new Timer();

    public LoggerForm() {
        super("GPS Status");

        init();
    }

    private void init() {
        deleteAll();
        removeCommand(STOP);
        addCommand(START);
    }

    public RawLogger getLogger() {
        return this.logger;
    }

    public void setLogger(RawLogger logger) {
        init();

        this.logger = logger;
        this.logger.setOutputForm(this);

        append(new StringItem(null, "Device: " + logger.getDeviceName()));
        append(new Spacer(10, 5));
        append(MSG);

        this.timer.schedule(new TimerTask() {
            public void run() {
                int bytes = getLogger().bytesRead();
                StringItem item;
                if (bytes == 0) {
                    if (!getLogger().isRunning()) {
                        item = new StringItem(null, MSG);
                    } else {
                        item = new StringItem(null, MSG2);
                    }
                } else if (bytes < 10000) {
                    item = new StringItem(null, "\nData read: " + bytes + " bytes");
                } else {
                    item = new StringItem(null, "\nData read: " + (bytes / 1000) + " kbytes");
                }
                item.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
                set(2, item);
            }
        }, 1000, 500);
    }
}
