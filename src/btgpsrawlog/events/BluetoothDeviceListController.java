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

import javax.bluetooth.UUID;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

import btgpsrawlog.BTGPSRawLogMidlet;
import btgpsrawlog.BluetoothDeviceDiscoverer;
import btgpsrawlog.forms.BluetoothDeviceList;

public class BluetoothDeviceListController implements CommandListener {

    protected BluetoothDeviceDiscoverer bluetoothDeviceDiscoverer;
    protected BluetoothDeviceList       bluetoothDeviceList;
    protected BTGPSRawLogMidlet         midlet;

    public BluetoothDeviceListController(BluetoothDeviceList bluetoothDeviceList,
            BTGPSRawLogMidlet midlet) {
        this.bluetoothDeviceList = bluetoothDeviceList;
        this.midlet = midlet;

        this.bluetoothDeviceDiscoverer = new BluetoothDeviceDiscoverer(midlet);
    }

    public void commandAction(Command cmd, Displayable disp) {
        try {
            if (cmd.equals(BluetoothDeviceList.BACK)) {
                this.bluetoothDeviceDiscoverer.cancelInquiry();
                this.midlet.showMainForm();
            } else if (cmd.equals(BluetoothDeviceList.SELECT)) {
                int devIdx = bluetoothDeviceList.getSelectedIndex();
                if (bluetoothDeviceDiscoverer.select(devIdx)) {
                    bluetoothDeviceList.deleteAll();
                    bluetoothDeviceList.append("[Please wait...]", null);
                }
            } else if (cmd.equals(BluetoothDeviceList.SEARCH)) {
                bluetoothDeviceDiscoverer
                        .startInquiry(/* 0x1101 = Serial port */new UUID[] { new UUID(0x1101) });
                Display.getDisplay(this.midlet).setCurrent(bluetoothDeviceList);
            }
        } catch (Exception ex) {
            bluetoothDeviceList.append(ex.toString(), null);
        }
    }

}
