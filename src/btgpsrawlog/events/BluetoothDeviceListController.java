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
package btgpsrawlog.events;

import javax.bluetooth.ServiceRecord;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

import btgpsrawlog.BTGPSRawLogMidlet;
import btgpsrawlog.RawLogger;
import btgpsrawlog.forms.BluetoothDeviceList;
import btgpsrawlog.forms.MainForm;

public class BluetoothDeviceListController implements CommandListener {

    protected BluetoothDeviceList bluetoothDeviceList;
    protected BTGPSRawLogMidlet   midlet;

    public BluetoothDeviceListController(BluetoothDeviceList bluetoothDeviceList, BTGPSRawLogMidlet midlet) {
        this.bluetoothDeviceList = bluetoothDeviceList;
        this.midlet = midlet;
    }

    public void commandAction(Command cmd, Displayable disp) {
        try {
            if (cmd.equals(BluetoothDeviceList.BACK)) {
                // TODO: Stop running inquiry
                this.midlet.showMainForm();
            } else if (cmd.equals(BluetoothDeviceList.SELECT)) {
                if (bluetoothDeviceList.select()) {
                    bluetoothDeviceList.deleteAll();
                    bluetoothDeviceList.append("[Please wait...]", null);
                }
            } else if (cmd.equals(BluetoothDeviceList.SEARCH)) {
                commandAction(MainForm.START, mainForm);
            } else if (cmd.equals(BluetoothDeviceList.COMPLETED)) {
                ServiceRecord serviceRecord = bluetoothDeviceList.getFirstDiscoveredService();
                String url = serviceRecord.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                RawLogger logger = new RawLogger(url, serviceRecord.getHostDevice().getFriendlyName(false));
                loggerForm.setLogger(logger);
                Display.getDisplay(this.midlet).setCurrent(this.saveLogForm);
            }
        } catch (Exception ex) {
            bluetoothDeviceList.append(ex.toString(), null);
        }
    }

}
