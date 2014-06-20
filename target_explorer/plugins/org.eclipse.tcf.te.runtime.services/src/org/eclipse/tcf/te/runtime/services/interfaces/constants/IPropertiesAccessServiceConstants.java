/*******************************************************************************
 * Copyright (c) 2011, 2014 Wind River Systems, Inc. and others. All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Wind River Systems - initial API and implementation
 *******************************************************************************/
package org.eclipse.tcf.te.runtime.services.interfaces.constants;

/**
 * Defines the properties access service constants.
 */
public interface IPropertiesAccessServiceConstants {

	/**
	 * Target name.
	 * <p>
	 * The target name is not meant to be identical with the targets network name. It can
	 * be the targets network name, but it can be any other string identifying the target
	 * to the user as well. The name is for display only, it is not meant to be used for
	 * communicating with the target.
	 */
	public static String PROP_NAME = "name"; //$NON-NLS-1$

	/**
	 * Target transport name.
	 */
	public static String PROP_TRANSPORT_NAME = "transportName"; //$NON-NLS-1$

	/**
	 * Target agent address.
	 * <p>
	 * <i>The value is typically the address an agent running at the target.</i>
	 */
	public static String PROP_ADDRESS = "address"; //$NON-NLS-1$

	/**
	 * Target agent port.
	 * <p>
	 * <i>The value is typically the port an agent running at the target.</i>
	 */
	public static String PROP_PORT = "port"; //$NON-NLS-1$

	/**
	 * <code>true</code> if the port is retrieved automatically.
	 */
	public static String PROP_PORT_IS_AUTO = "autoPort"; //$NON-NLS-1$

	/**
	 * Proxies to connect the target through.
	 */
	public static String PROP_PROXIES = "proxies"; //$NON-NLS-1$
}
