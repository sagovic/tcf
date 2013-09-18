/*******************************************************************************
 * Copyright (c) 2011, 2013 Wind River Systems, Inc. and others. All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Wind River Systems - initial API and implementation
 *******************************************************************************/
package org.eclipse.tcf.te.tcf.ui.listeners;

import org.eclipse.tcf.te.ui.views.listeners.AbstractWindowListener;
import org.eclipse.ui.IPartListener2;

/**
 * The window listener implementation. Takes care of the
 * management of the global listeners per workbench window.
 */
public class WorkbenchWindowListener extends AbstractWindowListener {

	/* (non-Javadoc)
	 * @see org.eclipse.tcf.te.ui.views.listeners.AbstractWindowListener#createPartListener()
	 */
	@Override
	protected IPartListener2 createPartListener() {
    	return new WorkbenchPartListener();
	}
}
