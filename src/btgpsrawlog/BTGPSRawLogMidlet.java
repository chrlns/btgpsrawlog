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

package btgpsrawlog;

import btgpsrawlog.forms.MainForm;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

/**
 * @author Christian Lins
 */
public class BTGPSRawLogMidlet extends MIDlet
{

	private static BTGPSRawLogMidlet instance;

	public static BTGPSRawLogMidlet getInstance()
	{
		return instance;
	}

	private Display display;

	public BTGPSRawLogMidlet()
	{
		instance = this;
	}

    public void startApp()
	{
		this.display = Display.getDisplay(this);
		this.display.setCurrent(new MainForm());
    }

    public void pauseApp()
	{
    }

    public void destroyApp(boolean unconditional)
	{
    }

	public static void exit()
	{
		instance.notifyDestroyed();
	}

}
