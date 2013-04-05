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

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;

/**
 * Shows some information about this app.
 * 
 * @author Christian Lins
 */
public class AboutForm extends AdForm {

    public static final Command BACK = new Command("Back", null, Command.BACK, 0);

    public AboutForm(MIDlet midlet) {
        super("About");

        append(new StringItem("Name", midlet.getAppProperty("MIDlet-Name")));
        append(new StringItem("Version", midlet.getAppProperty("MIDlet-Version")));
        append(new StringItem("Author", midlet.getAppProperty("MIDlet-Vendor")));

        addCommand(BACK);
    }

}
