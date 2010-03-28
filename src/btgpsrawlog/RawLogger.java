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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Form;

/**
 *
 * @author chris
 */
public class RawLogger extends Thread
{

    private StreamConnection connection = null;
    private FileConnection fileConnection = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
	private Form	outputForm	= null;
	private boolean	running		= false;
	private String	url			= null;

    public RawLogger(String url)
	{
		if(!url.startsWith("btspp://"))
		{
			this.url = "btspp://" + url;
		}
		else
		{
			this.url = url;
		}
    }

    public synchronized void connect()
		throws IOException
	{
        connection = (StreamConnection) Connector.open(url, Connector.READ);
        inputStream = connection.openInputStream();

        fileConnection = (FileConnection)Connector.open(createTimestampFilename());
        if(!fileConnection.exists())
        {
            fileConnection.create();
        }
        outputStream = fileConnection.openOutputStream();
        this.running = true;
    }

	private String createTimestampFilename()
	{
		return "file:///E:/" + System.currentTimeMillis() + ".txt";
	}

	private void dbgmsg(String str)
	{
		if(this.outputForm != null)
		{
			this.outputForm.append(str);
		}
	}

    public synchronized void disconnect()
    {
        this.running = false;
    }

	public String getURL()
	{
		return this.url;
	}

    public void run()
	{
        setPriority(MIN_PRIORITY);
        try
		{

			byte[] buf = new byte[128];
			while(running)
			{
				int read = this.inputStream.read(buf);
				if(read == -1)
				{
					dbgmsg("End of stream.");
					break;
				}
				else
				{
					this.outputStream.write(buf, 0, read);
					this.outputStream.flush();
				}
			}

            connection.close();
            fileConnection.close();
        } 
		catch(Exception ex)
		{
            ex.printStackTrace();
			dbgmsg(ex.getMessage());
        }
		dbgmsg("Logging ended.");
    }

	public void setOutputForm(Form form)
	{
		this.outputForm = form;
	}

}
