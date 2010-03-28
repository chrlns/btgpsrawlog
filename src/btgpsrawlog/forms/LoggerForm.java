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
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

/**
 *
 * @author Christian Lins
 */
public class LoggerForm extends Form implements CommandListener
{

	private RawLogger logger;
	private Command startCommand = new Command("Start", null, Command.OK, 1);
	private Command stopCommand  = new Command("Stop", null, Command.CANCEL, 0);

	public LoggerForm(RawLogger logger)
	{
		super("GPS Status");
		this.logger = logger;
		this.logger.setOutputForm(this);
		append("BTURL: " + logger.getURL());

		addCommand(stopCommand);
		addCommand(startCommand);

		setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable disp)
	{
		if(cmd.equals(this.startCommand))
		{
			try
			{
				this.logger.connect();
				this.logger.start();
				append("Logging started");
			}
			catch(Exception ex)
			{
				append(ex.getMessage());
				ex.printStackTrace();
			}
		}
		else if(cmd.equals(this.stopCommand))
		{
			this.logger.disconnect();
			Display.getDisplay(BTGPSRawLogMidlet.getInstance()).setCurrent(new MainForm());
		}
	}
	
}
