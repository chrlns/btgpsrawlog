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
package btgpsrawlog;

import btgpsrawlog.forms.AboutForm;
import btgpsrawlog.forms.BluetoothDevList;
import btgpsrawlog.forms.LoggerForm;
import btgpsrawlog.forms.MainForm;
import btgpsrawlog.forms.SaveLogForm;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

/**
 * Main MIDlet.
 * @author Christian Lins
 */
public class BTGPSRawLogMidlet extends MIDlet {

	private Displayable currentDisplay;
	private EventController eventController;
	private RawLogger rawLogger = null;

	public BTGPSRawLogMidlet() {
		BluetoothDevList btDevList = new BluetoothDevList(this);
		LoggerForm loggerForm = new LoggerForm();
		MainForm mainForm = new MainForm();
		SaveLogForm saveLogForm = new SaveLogForm();
		AboutForm aboutForm = new AboutForm(this);

		this.eventController = 
			new EventController(this, btDevList, loggerForm, mainForm, saveLogForm, aboutForm);
		btDevList.setCommandListener(eventController);
		loggerForm.setCommandListener(eventController);
		mainForm.setCommandListener(eventController);
		saveLogForm.setCommandListener(eventController);
		aboutForm.setCommandListener(eventController);

		this.currentDisplay = mainForm;
	}

	public RawLogger getLogger() {
		return this.rawLogger;
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

	/**
	 * Called by the program manager when the app is about to destroyed.
	 * @param unconditional
	 */
	public void destroyApp(boolean unconditional) {
		if(this.rawLogger != null) {
			this.rawLogger.stop();
		}
	}
}
