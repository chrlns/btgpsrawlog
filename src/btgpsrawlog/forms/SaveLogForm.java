/*
 *   BTGPSRawLog
 *   Copyright (C) 2010-2011 Christian Lins <christian@lins.me>
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package btgpsrawlog.forms;

import java.util.Enumeration;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

/**
 * Allows the user to select the logfile save location.
 * @author Christian Lins
 */
public class SaveLogForm extends Form {

	public static final Command BACK = new Command("Back", Command.BACK, 0);
	public static final Command OK = new Command("Continue", Command.OK, 1);

	private ChoiceGroup choiceGroup = new ChoiceGroup("Roots:", ChoiceGroup.EXCLUSIVE);
	private TextField fileName = new TextField("File name", "nmea.txt", 64, 0);

	public SaveLogForm() {
		super("Choose save location");

		append(fileName);

		Enumeration drives = FileSystemRegistry.listRoots();
		while (drives.hasMoreElements()) {
			String driveString = drives.nextElement().toString();
			choiceGroup.append(driveString, null);
		}

		append(choiceGroup);

		addCommand(BACK);
		addCommand(OK);
	}

	public String getPath() {
		String path = choiceGroup.getString(choiceGroup.getSelectedIndex());
		StringBuffer buf = new StringBuffer();
		buf.append("file://");
		buf.append(path);
		if(!path.endsWith("/")) {
			buf.append('/');
		}
		buf.append(fileName.getString());
		return buf.toString();
	}
}