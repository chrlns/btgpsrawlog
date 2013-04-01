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
import java.util.Enumeration;
import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.lcdui.Display;

/**
 * Handles discovery of Bluetooth devices and its services.
 * 
 * @author Christian Lins
 * 
 */
public class BluetoothDeviceDiscoverer implements DiscoveryListener {

    protected DiscoveryAgent    discoveryAgent;
    protected Vector            devices   = new Vector();
    protected Vector            listeners = new Vector();
    protected BTGPSRawLogMidlet midlet;
    protected UUID[]            serviceUUIDs;
    protected Vector            services  = new Vector();

    public BluetoothDeviceDiscoverer(BTGPSRawLogMidlet midlet) {
        this.midlet = midlet;
    }

    public void addDiscoveryListener(DiscoveryListener listener) {
        this.listeners.addElement(listener);
    }

    public boolean hasDevices() {
        return this.devices.size() > 0;
    }

    public void startInquiry(UUID[] serviceUUIDs) throws BluetoothStateException {
        this.serviceUUIDs = serviceUUIDs;
        // TODO: Check if Bluetooth is enabled: localDevice.isPoweredOn()
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        discoveryAgent = localDevice.getDiscoveryAgent();
        discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);
    }

    public void cancelInquiry() {
        if (discoveryAgent != null) {
            this.discoveryAgent.cancelInquiry(this);
        }
    }

    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        if (!this.devices.contains(btDevice)) {
            this.devices.addElement(btDevice);
            fireDeviceDiscoveredEvent(btDevice, cod);
        }
    }

    public void inquiryCompleted(int discType) {
        fireInquiryCompletedEvent(discType);
    }

    public void serviceSearchCompleted(int transID, int respCode) {
        ServiceRecord serviceRecord = getFirstDiscoveredService();
        String url = serviceRecord.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
        RawLogger logger;
        try {
            logger = new RawLogger(url, serviceRecord.getHostDevice().getFriendlyName(false));
            this.midlet.getLoggerForm().setLogger(logger);
        } catch (IOException e) {
            this.midlet.getLoggerForm().append(e.getMessage());
        }
        Display.getDisplay(this.midlet).setCurrent(this.midlet.getSaveLogForm());
    }

    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        for (int n = 0; n < servRecord.length; n++) {
            ServiceRecord record = servRecord[n];
            services.addElement(record);
        }
    }

    protected void fireDeviceDiscoveredEvent(RemoteDevice btDevice, DeviceClass cod) {
        Enumeration enum = listeners.elements();
        while (enum.hasMoreElements()) {
            Object el = enum.nextElement();
            if (el instanceof DiscoveryListener) {
                ((DiscoveryListener) el).deviceDiscovered(btDevice, cod);
            }
        }
    }

    protected void fireInquiryCompletedEvent(int discType) {
        Enumeration enum = listeners.elements();
        while (enum.hasMoreElements()) {
            Object el = enum.nextElement();
            if (el instanceof DiscoveryListener) {
                ((DiscoveryListener) el).inquiryCompleted(discType);
            }
        }
    }

    public boolean select(int idx) throws BluetoothStateException {
        if (idx < this.devices.size()) {
            RemoteDevice dev = (RemoteDevice) this.devices.elementAt(idx);
            this.discoveryAgent.searchServices(null, serviceUUIDs, dev, this);
            return true;
        } else {
            return false;
        }
    }

    protected ServiceRecord getFirstDiscoveredService() {
        return (ServiceRecord) this.services.elementAt(0);
    }
}
