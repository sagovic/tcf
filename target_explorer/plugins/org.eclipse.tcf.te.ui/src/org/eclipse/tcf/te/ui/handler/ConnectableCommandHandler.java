/*******************************************************************************
 * Copyright (c) 2013 Wind River Systems, Inc. and others. All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Wind River Systems - initial API and implementation
 *******************************************************************************/
package org.eclipse.tcf.te.ui.handler;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tcf.te.core.interfaces.IConnectable;
import org.eclipse.tcf.te.core.utils.ConnectStateHelper;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Connectable command handler implementation.
 */
public class ConnectableCommandHandler extends AbstractHandler implements IExecutableExtension {

	protected static final String PARAM_ACTION = "action"; //$NON-NLS-1$

	protected int action = IConnectable.STATE_UNKNOWN;

	/* (non-Javadoc)
	 * @see com.windriver.te.tcf.ui.handler.AbstractAgentCommandHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Assert.isTrue(action >= 0);

		ISelection selection = HandlerUtil.getCurrentSelection(event);

		if (selection instanceof IStructuredSelection && !selection.isEmpty()) {
			Iterator<Object> iterator = ((IStructuredSelection)selection).iterator();
			while (iterator.hasNext()) {
				Object element = iterator.next();
				if (element instanceof IConnectable) {
					IConnectable connectable = (IConnectable)element;
					if (connectable.isConnectStateChangeAllowed(action)) {
						connectable.changeConnectState(action, null, null);
					}
				}
			}
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
	 */
	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		if (data instanceof Map) {
			Map<?,?> dataMap = (Map<?,?>)data;
			if (dataMap.get(PARAM_ACTION) instanceof String) {
				String stateStr = dataMap.get(PARAM_ACTION).toString().trim();
				this.action = ConnectStateHelper.getConnectAction(stateStr);
			}
		}
	}
}
