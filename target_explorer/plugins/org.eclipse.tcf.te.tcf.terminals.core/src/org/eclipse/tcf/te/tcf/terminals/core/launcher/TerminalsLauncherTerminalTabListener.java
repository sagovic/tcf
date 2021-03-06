/*******************************************************************************
 * Copyright (c) 2011, 2015 Wind River Systems, Inc. and others. All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Wind River Systems - initial API and implementation
 *******************************************************************************/
package org.eclipse.tcf.te.tcf.terminals.core.launcher;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.tm.terminal.view.core.interfaces.ITerminalTabListener;

/**
 * Remote terminals launcher terminal tab listener implementation.
 * <p>
 * <b>Note:</b> The notifications may occur in every thread!
 */
public class TerminalsLauncherTerminalTabListener extends PlatformObject implements ITerminalTabListener {
	// Reference to the parent launcher
	private final TerminalsLauncher parent;

	/**
	 * Constructor.
	 *
	 * @param parent The parent launcher. Must not be <code>null</code>.
	 */
	public TerminalsLauncherTerminalTabListener(TerminalsLauncher parent) {
		super();

		Assert.isNotNull(parent);
		this.parent = parent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tcf.te.core.terminals.interfaces.ITerminalTabListener#terminalTabDisposed(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void terminalTabDisposed(Object source, Object data) {
		Assert.isNotNull(source);

		// The custom data object must be of type TerminalsLauncher and match the parent launcher
		if (data instanceof TerminalsLauncher && parent.equals(data)) {
			// Terminate the remote terminal (leads to the disposal of the parent)
			parent.exit();
		}
	}
}
