/*******************************************************************************
 * Copyright (c) 2013 Wind River Systems, Inc. and others. All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Wind River Systems - initial API and implementation
 *******************************************************************************/

package org.eclipse.tcf.te.core.utils;

import org.eclipse.tcf.te.core.interfaces.IConnectable;

/**
 * ConnectStateHelper
 */
public final class ConnectStateHelper {

	public static final String UNKNOWN = "unknown"; //$NON-NLS-1$

	public static final String DISCONNECTED = "disconnected"; //$NON-NLS-1$
	public static final String DISCONNECTING = "disconnecting"; //$NON-NLS-1$
	public static final String DISCONNECT_SCHEDULED = "disconnect_scheduled"; //$NON-NLS-1$

	public static final String CONNECTED = "connected"; //$NON-NLS-1$
	public static final String CONNECTING = "connecting"; //$NON-NLS-1$
	public static final String CONNECT_SCHEDULED = "connect_scheduled"; //$NON-NLS-1$

	public static final String DISCONNECT = "disconnect"; //$NON-NLS-1$
	public static final String CONNECT = "connect"; //$NON-NLS-1$

	public static final int getConnectState(String state) {
		if (DISCONNECTED.equalsIgnoreCase(state))
			return IConnectable.STATE_DISCONNECTED;
		if (DISCONNECTING.equalsIgnoreCase(state))
			return IConnectable.STATE_DISCONNECTING;
		if (DISCONNECT_SCHEDULED.equalsIgnoreCase(state))
			return IConnectable.STATE_DISCONNECT_SCHEDULED;
		if (CONNECTED.equalsIgnoreCase(state))
			return IConnectable.STATE_CONNECTED;
		if (CONNECTING.equalsIgnoreCase(state))
			return IConnectable.STATE_CONNECTING;
		if (CONNECT_SCHEDULED.equalsIgnoreCase(state))
			return IConnectable.STATE_CONNECT_SCHEDULED;

		if (CONNECT.equalsIgnoreCase(state))
			return IConnectable.ACTION_CONNECT;
		if (DISCONNECT.equalsIgnoreCase(state))
			return IConnectable.ACTION_DISCONNECT;

		return IConnectable.STATE_UNKNOWN;
	}

	public static final String getConnectState(int state) {
		switch (state) {
		case IConnectable.STATE_DISCONNECTED:
			return DISCONNECTED;
		case IConnectable.STATE_DISCONNECTING:
			return DISCONNECTING;
		case IConnectable.STATE_DISCONNECT_SCHEDULED:
			return DISCONNECT_SCHEDULED;
		case IConnectable.STATE_CONNECTED:
			return CONNECTED;
		case IConnectable.STATE_CONNECTING:
			return CONNECTING;
		case IConnectable.STATE_CONNECT_SCHEDULED:
			return CONNECT_SCHEDULED;
		}

		return UNKNOWN;
	}

	public static final int getConnectAction(String action) {
		if (CONNECT.equalsIgnoreCase(action))
			return IConnectable.ACTION_CONNECT;
		if (DISCONNECT.equalsIgnoreCase(action))
			return IConnectable.ACTION_DISCONNECT;

		return IConnectable.ACTION_UNKNOWN;
	}

	public static final String getConnectAction(int action) {
		switch (action) {
		case IConnectable.ACTION_CONNECT:
			return CONNECT;
		case IConnectable.ACTION_DISCONNECT:
			return DISCONNECT;
		}

		return UNKNOWN;
	}

}
