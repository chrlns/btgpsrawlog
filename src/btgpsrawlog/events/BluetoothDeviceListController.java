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

import java.io.IOException;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import btgpsrawlog.BTGPSRawLogMidlet;
import btgpsrawlog.BluetoothDeviceDiscoverer;
import btgpsrawlog.forms.BluetoothDeviceList;

/**
 * Handles events of the Bluetooth devices list and reacts to results of the
 * BluetoothDeviceDiscoverer.
 * 
 * @author Christian Lins
 * 
 */
public class BluetoothDeviceListController implements CommandListener, DiscoveryListener {

    protected BluetoothDeviceDiscoverer bluetoothDeviceDiscoverer;
    protected BluetoothDeviceList       bluetoothDeviceList;
    protected BTGPSRawLogMidlet         midlet;

    public BluetoothDeviceListController(BluetoothDeviceList bluetoothDeviceList,
            BTGPSRawLogMidlet midlet) {
        this.bluetoothDeviceList = bluetoothDeviceList;
        this.midlet = midlet;

        this.bluetoothDeviceDiscoverer = new BluetoothDeviceDiscoverer(midlet);
        this.bluetoothDeviceDiscoverer.addDiscoveryListener(this);
    }

    public void commandAction(Command cmd, Displayable disp) {
        try {
            if (cmd.equals(BluetoothDeviceList.BACK)) {
                this.bluetoothDeviceDiscoverer.cancelInquiry();
                this.midlet.showMainForm();
            } else if (cmd.equals(List.SELECT_COMMAND)) {
                int devIdx = bluetoothDeviceList.getSelectedIndex();
                if (bluetoothDeviceDiscoverer.select(devIdx)) {
                    bluetoothDeviceList.deleteAll();
                    bluetoothDeviceList.append("Please wait...", null);
                }
            } else if (cmd.equals(BluetoothDeviceList.SEARCH)) {
                bluetoothDeviceDiscoverer
                        .startInquiry(/* 0x1101 = Serial port */new UUID[] { new UUID(0x1101) });
                Display.getDisplay(this.midlet).setCurrent(bluetoothDeviceList);
                this.bluetoothDeviceList.deleteAll();
                this.bluetoothDeviceList.append("Searching...", null);
            }
        } catch (Exception ex) {
            bluetoothDeviceList.append("Error: " + ex.getMessage(), null);
        }
    }

    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        String deviceName;
        try {
            deviceName = btDevice.getFriendlyName(false);
            this.bluetoothDeviceList.append(deviceName, null);
        } catch (IOException e) {
            e.printStackTrace();
            this.bluetoothDeviceList.append(e.getMessage(), null);
        }
    }

    public void inquiryCompleted(int discType) {
        if (this.bluetoothDeviceDiscoverer.hasDevices()) {
            this.bluetoothDeviceList.delete(0);
        } else {
            this.bluetoothDeviceList.deleteAll();
            this.bluetoothDeviceList.append("No devices found!", null);
        }
    }

    public void serviceSearchCompleted(int transID, int respCode) {
        // TODO Auto-generated method stub

    }

    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        // TODO Auto-generated method stub

    }

}
