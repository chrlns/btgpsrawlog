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
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

/**
 * Handles events of all Forms.
 * @author Christian Lins
 */
public class EventController implements CommandListener {

	private BluetoothDevList bluetoothDevicesList;
	private LoggerForm loggerForm;
	private MainForm mainForm;
	private SaveLogForm saveLogForm;
	private AboutForm aboutForm;
	private BTGPSRawLogMidlet midlet;

	public EventController(BTGPSRawLogMidlet midlet, BluetoothDevList bluetoothDevicesList,
			LoggerForm loggerForm, MainForm mainForm, SaveLogForm saveLogForm, AboutForm aboutForm) {
		this.midlet = midlet;
		this.bluetoothDevicesList = bluetoothDevicesList;
		this.loggerForm = loggerForm;
		this.mainForm = mainForm;
		this.saveLogForm = saveLogForm;
		this.aboutForm = aboutForm;
	}

	public void commandAction(Command cmd, Displayable disp) {
		if(disp.equals(bluetoothDevicesList)) {
			try {
				if (cmd.equals(BluetoothDevList.BACK)) {
					// TODO: Stop running inquiry
					Display.getDisplay(this.midlet).setCurrent(this.mainForm);
				} else if (cmd.equals(BluetoothDevList.SELECT)) {
					if(bluetoothDevicesList.select()) {
						bluetoothDevicesList.deleteAll();
						bluetoothDevicesList.append("[Please wait...]", null);
					}
				} else if(cmd.equals(BluetoothDevList.SEARCH)) {
					commandAction(MainForm.START, mainForm);
				} else if(cmd.equals(BluetoothDevList.COMPLETED)) {
					ServiceRecord serviceRecord =
								bluetoothDevicesList.getFirstDiscoveredService();
					String url = serviceRecord.getConnectionURL(
								ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
					RawLogger logger = new RawLogger(url,
							serviceRecord.getHostDevice().getFriendlyName(false));
					loggerForm.setLogger(logger);
					Display.getDisplay(this.midlet).setCurrent(this.saveLogForm);
				}
			} catch(Exception ex) {
				bluetoothDevicesList.append(ex.toString(), null);
			}
		} else if(disp.equals(loggerForm)) {
			if (cmd.equals(LoggerForm.START)) {
				try {
					loggerForm.removeCommand(LoggerForm.START);
					loggerForm.getLogger().connect();
					loggerForm.getLogger().start();
					loggerForm.addCommand(LoggerForm.STOP);
				} catch (Exception ex) {
					loggerForm.append(ex.getMessage());
				}
			} else if (cmd.equals(LoggerForm.STOP)) {
				loggerForm.getLogger().disconnect();
				Display.getDisplay(this.midlet).setCurrent(this.mainForm);
			}
		} else if(disp.equals(mainForm)) {
			try {
				if (cmd.equals(MainForm.EXIT)) {
					this.midlet.destroyApp(false);
					this.midlet.notifyDestroyed();
				} else if (cmd.equals(MainForm.START)) {
					bluetoothDevicesList.startInquiry(
							/* 0x1101 = Serial port */
							DiscoveryAgent.GIAC, new UUID[]{new UUID(0x1101)});
					Display.getDisplay(this.midlet).setCurrent(bluetoothDevicesList);
				} else if(cmd.equals(MainForm.ABOUT)) {
					Display.getDisplay(midlet).setCurrent(aboutForm);
				}
			} catch(Exception ex) {
				mainForm.append(ex.getMessage());
			}
		} else if(disp.equals(saveLogForm)) {
			if(cmd.equals(SaveLogForm.OK)) {
				loggerForm.getLogger().setSaveFile(saveLogForm.getPath());
				Display.getDisplay(midlet).setCurrent(loggerForm);
			} else if(cmd.equals(SaveLogForm.BACK)) {
				// Go back to Bluetooth selection
				commandAction(MainForm.START, mainForm);
			}
		} else if(cmd.equals(AboutForm.BACK)) {
			Display.getDisplay(midlet).setCurrent(mainForm);
		}
	}

}
