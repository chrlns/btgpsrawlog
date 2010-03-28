/*
 *   BTGPSRawLog
 *   Copyright (C) 2010 Christian Lins <christian.lins@fh-osnabrueck.de>
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

import btgpsrawlog.BTGPSRawLogMidlet;
import btgpsrawlog.RawLogger;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import net.benhui.btgallery.bluelet.BLUElet;

/**
 *
 * @author chris
 */
public class MainForm extends Form implements CommandListener
{

	private Command exitCommand  = new Command("Exit", null, Command.EXIT, 0);
	private Command startCommand = new Command("Start", null, Command.OK, 1);
	private BLUElet bluelet = null;

	public MainForm()
	{
		super("BT GPS Logger");

		append("Logs raw data of Bluetooth GPS devices");
		append("btgpsrawlogger/0.1");

		addCommand(this.exitCommand);
		addCommand(this.startCommand);

		setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable display)
	{
		if(cmd.equals(this.exitCommand))
		{
			BTGPSRawLogMidlet.exit();
		}
		else if(cmd.equals(this.startCommand))
		{
			// Start BLUElet to discover Bluetooth devices
			bluelet = new BLUElet(BTGPSRawLogMidlet.getInstance(), this);
			bluelet.startApp();
			bluelet.startInquiry(DiscoveryAgent.GIAC, new UUID[]{new UUID(0x1101)});
			Display.getDisplay(BTGPSRawLogMidlet.getInstance()).setCurrent(bluelet.getUI());
		}
		else if(cmd.equals(BLUElet.BACK))
		{
			bluelet.destroyApp(false);
		}
		else if(cmd.equals(BLUElet.COMPLETED))
		{
			RemoteDevice btDev = bluelet.getSelectedDevice();
			ServiceRecord serviceRecord = bluelet.getFirstDiscoveredService();
			String url = serviceRecord.getConnectionURL(
					ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
			RawLogger logger = new RawLogger(url);
			Display.getDisplay(
					BTGPSRawLogMidlet.getInstance()).setCurrent(new LoggerForm(logger));
		}
		else if(cmd.equals(BLUElet.SELECTED))
		{
			
		}
	}
}
