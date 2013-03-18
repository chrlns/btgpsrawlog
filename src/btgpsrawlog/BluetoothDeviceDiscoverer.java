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
package btgpsrawlog;

import java.io.IOException;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.lcdui.Display;

public class BluetoothDeviceDiscoverer implements DiscoveryListener {

    protected BTGPSRawLogMidlet midlet;

    public BluetoothDeviceDiscoverer(BTGPSRawLogMidlet midlet) {
        this.midlet = midlet;
    }

    public void startInquiry(UUID[] serviceUUIDs) {

    }

    public void cancelInquiry() {

    }

    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        // TODO Auto-generated method stub

    }

    public void inquiryCompleted(int discType) {
        // TODO Auto-generated method stub

    }

    public void serviceSearchCompleted(int transID, int respCode) {
        // TODO Auto-generated method stub

    }

    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        // TODO Auto-generated method stub

    }

    public boolean select(int idx) {
        return false;
    }

    protected ServiceRecord getFirstDiscoveredService() {
        return null;
    }

    protected void serviceDiscoveryCompleted() throws IOException {
        ServiceRecord serviceRecord = getFirstDiscoveredService();
        String url = serviceRecord.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
        RawLogger logger = new RawLogger(url, serviceRecord.getHostDevice().getFriendlyName(false));
        this.midlet.getLoggerForm().setLogger(logger);
        Display.getDisplay(this.midlet).setCurrent(this.midlet.getSaveLogForm());
    }
}
