/*
 *   BTGPSRawLog
 *   Copyright (C) 2010-2011 Christian Lins <christian@lins.me>
 *   partly based upon BLUEletUI Copyright (C) 2003 by Ben Hui
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
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class BluetoothDevList extends List {

	private final static int ID_SERVICE_COMPLETED = 1;
	private final static int ID_DEVICE_COMPLETED = 2;
	private final static int ID_DEVICE_SELECTED = 3;

	public static final Command COMPLETED = new Command("COMPLETED", Command.SCREEN, 1);
	public static final Command SEARCH = new Command("Search", Command.SCREEN, 2);
	public static final Command SELECT = new Command("Select", Command.SCREEN, 1);
	public static final Command BACK = new Command("Back", Command.BACK, 1);

	private Vector devices = new Vector();
	private Vector deviceClasses = new Vector();
	private Vector services = new Vector();
	private int selectedDevice = -1;

	// list of UUID to match during service discovery
	private UUID[] serviceUUIDs = null;
	// Bluetooth return code from device inquiry operation
	// see DiscoveryListener
	private int deviceReturnCode;
	// Bluetooth return code from service discovery operation
	// see DiscoveryListener
	private int serviceReturnCode;
	private LocalDevice device;
	private DiscoveryAgent agent;
	private BTGPSRawLogMidlet midlet;
	private CommandListener cmdListener;

	public BluetoothDevList(BTGPSRawLogMidlet midlet) {
		super("Bluetooth devices", List.IMPLICIT);

		this.midlet = midlet;

		addCommand(SELECT);
		addCommand(SEARCH);
		addCommand(BACK);

		setSelectCommand(SELECT);
	}

	public void setCommandListener(CommandListener cmdListener) {
		super.setCommandListener(cmdListener);
		this.cmdListener = cmdListener;
	}

	/**
	 * Set a one-line message to screen.
	 * @param str String
	 */
	public void setMsg(String str) {
		deleteAll();
		append(str, null);
	}

	/**
	 * refresh the list with blutooth devices
	 */
	public void update(Vector devices) {
		deleteAll();

		if (devices.size() > 0) {
			for (int i = 0; i < devices.size(); i++) {
				try {
					RemoteDevice device = (RemoteDevice)devices.elementAt(i);
					String name = device.getFriendlyName(false);
					append(name, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			append("[No device found]", null);
		}
	}

	/**
	 * Get all discovered services from selected remote device.
	 * Your application call this method after your app receive COMPLETED callback
	 * event. This will return all services that match your UUIDs in startInquiry().
	 * @return ServiceRecord[]
	 */
	public ServiceRecord[] getDiscoveredServices() {
		ServiceRecord[] rec = new ServiceRecord[services.size()];
		services.copyInto(rec);
		return rec;
	}

	/**
	 * Get the first discovered service from selected remote device.
	 * Your application call this method after your app receives COMPLETED
	 * callback event. This will return the first service that match your
	 * UUIDs in startInquiry().
	 *
	 * @return ServiceRecord null if no service discovered
	 */
	public ServiceRecord getFirstDiscoveredService() {
		if (services.size() > 0) {
			return (ServiceRecord)services.elementAt(0);
		} else {
			append("getFirstDiscoveredService() = null", null);
			return null;
		}
	}

	/**
	 * Return the Bluetooth result code from device inquiry.
	 * This is the result code obtained in  DiscoveryListener.inquiryCompleted().
	 * Your application cal call this method after a COMPLETED callback event
	 * is received.
	 * @return int
	 */
	public int getDeviceDiscoveryReturnCode() {
		return deviceReturnCode;
	}

	/**
	 * Return the Bluetooth result code from service discovery.
	 * This is the result code obtained in  DiscoveryListener.serviceSearchCompleted().
	 * Your application cal call this method after a COMPLETED callback event
	 * is received.
	 * @return int
	 */
	public int getServiceDiscoveryReturnCode() {
		return serviceReturnCode;
	}

	/**
	 * Return user selected remote device that is used for service discovery.
	 * Your application can call this after your app received SELECTED callback
	 * event.
	 * @return RemoteDevice null if user didn't select anything
	 */
	public RemoteDevice getSelectedDevice() {
		if (selectedDevice != -1) {
			return (RemoteDevice)devices.elementAt(selectedDevice);
		} else {
			return null;
		}
	}

	/**
	 * Start device inquiry. Your application call this method to start inquiry.
	 * @param mode int one of DiscoveryAgent.GIAC or DiscoveryAgent.LIAC
	 * @param serviceUUIDs UUID[]
	 */
	public void startInquiry(int mode, UUID[] serviceUUIDs) 
		throws BluetoothStateException
	{
		//this.discoveryMode = mode;
		this.serviceUUIDs = serviceUUIDs;

		// clear previous values first
		devices.removeAllElements();
		deviceClasses.removeAllElements();

		// initialize the JABWT stack
		device = LocalDevice.getLocalDevice(); // obtain reference to singleton
		device.setDiscoverable(DiscoveryAgent.GIAC); // set Discover Mode
		agent = device.getDiscoveryAgent(); // obtain reference to singleton

		/* boolean result = */ agent.startInquiry(mode, new Listener(this));

		// update screen with "Please Wait" message
		setMsg("[Please wait...]");
	}

	public boolean select() throws BluetoothStateException {
		if (devices.size() > 0) {
			// get selected device
			selectedDevice = getSelectedIndex();
			RemoteDevice remoteDevice = (RemoteDevice)devices.elementAt(selectedDevice);

			// remove all existing record first
			services.removeAllElements();
			agent.searchServices(null, serviceUUIDs, remoteDevice, new Listener(this));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Bluetooth listener object.
	 * Register this listener object to DiscoveryAgent in device inqury and service discovery.
	 */
	class Listener implements DiscoveryListener {

		private Displayable displayable;

		public Listener(Displayable displayable) {
			this.displayable = displayable;
		}

		private void log(String str) {
			System.out.println(str);
		}

		public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
			devices.addElement(remoteDevice);
			deviceClasses.addElement(deviceClass);
		}

		public void inquiryCompleted(int complete) {
			log("device discovery is completed with return code:" + complete);
			log("" + devices.size() + " devices are discovered");

			deviceReturnCode = complete;
			update(devices);

			if (devices.isEmpty()) {
				Alert alert = new Alert("Bluetooth", "No Bluetooth device found", null, AlertType.INFO);
				alert.setTimeout(3000);
				Display.getDisplay(midlet).setCurrent(alert, this.displayable);
			} else {
				Display.getDisplay(midlet).setCurrent(this.displayable);
			}
		}

		public void servicesDiscovered(int transId, ServiceRecord[] records) {
			// note: we do not use transId because we only have one search at a time
			log("Remote Bluetooth services is discovered:");
			for (int i = 0; i < records.length; i++) {
				ServiceRecord record = records[i];
				services.addElement(record);
			}
		}

		public void serviceSearchCompleted(int transId, int complete) {
			// note: we do not use transId because we only have one search at a time
			log("service discovery completed with return code:" + complete);
			log("" + services.size() + " services are discovered");

			serviceReturnCode = complete;

			// we cannot callback in this thread because this is a Bluetooth
			// subsystem thread. we do not want to block it.
			Display.getDisplay(midlet).callSerially(
					new Worker(ID_SERVICE_COMPLETED, displayable));
		}
	}

	/**
	 * Worker thread that invoke callback CommandListener upon Bluetooth event occurs.
	 */
	class Worker implements Runnable {

		private int cmd = 0;
		private Displayable disp;

		public Worker(int cmd, Displayable disp) {
			this.cmd = cmd;
			this.disp = disp;
		}

		public void run() {
			switch (cmd) {
				case ID_SERVICE_COMPLETED: {
					cmdListener.commandAction(COMPLETED, disp);
					break;
				}
				case ID_DEVICE_COMPLETED: {
					cmdListener.commandAction(COMPLETED, disp);
					break;
				}
				case ID_DEVICE_SELECTED: {
					cmdListener.commandAction(SELECT, disp);
					break;
				}
				default:
					break;

			}
		}
	}
}
