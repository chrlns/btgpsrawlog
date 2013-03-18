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
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Spacer;

/**
 * Main form of the app.
 * @author Christian Lins
 */
public class MainForm extends Form {

	public static final Command EXIT = new Command("Exit", null, Command.EXIT, 0);
	public static final Command START = new Command("Continue", null, Command.OK, 1);
	public static final Command ABOUT = new Command("About", null, Command.HELP, 0);

	private static final String MSG =
			"Press Continue to select a bluetooth GPS device, " +
			"choose a location to save the NMEA log and " +
			"start walking around to collect data!";

	public MainForm() {
		super("Bluetooth GPS Logger");

		append(new Spacer(getWidth(), 10));
		append(MSG);

		addCommand(EXIT);
		addCommand(START);
		addCommand(ABOUT);
	}
}
