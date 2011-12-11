/*******************************************************************************
 * Copyright (c) 2011 Wind River Systems, Inc. and others. All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Wind River Systems - initial API and implementation
 *******************************************************************************/
package org.eclipse.tcf.te.tcf.locator.services;

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.tcf.protocol.Protocol;
import org.eclipse.tcf.te.tcf.locator.interfaces.IModelListener;
import org.eclipse.tcf.te.tcf.locator.interfaces.nodes.ILocatorModel;
import org.eclipse.tcf.te.tcf.locator.interfaces.nodes.IPeerModel;
import org.eclipse.tcf.te.tcf.locator.interfaces.nodes.IPeerModelProperties;
import org.eclipse.tcf.te.tcf.locator.interfaces.services.ILocatorModelUpdateService;


/**
 * Default locator model update service implementation.
 */
public class LocatorModelUpdateService extends AbstractLocatorModelService implements ILocatorModelUpdateService {

	/**
	 * Constructor.
	 *
	 * @param parentModel The parent locator model instance. Must not be <code>null</code>.
	 */
	public LocatorModelUpdateService(ILocatorModel parentModel) {
		super(parentModel);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tcf.te.tcf.locator.core.interfaces.services.ILocatorModelUpdateService#add(org.eclipse.tcf.te.tcf.locator.core.interfaces.nodes.IPeerModel)
	 */
	@Override
	public void add(final IPeerModel peer) {
		Assert.isNotNull(peer);
		Assert.isTrue(Protocol.isDispatchThread());

		Map<String, IPeerModel> peers = (Map<String, IPeerModel>)getLocatorModel().getAdapter(Map.class);
		Assert.isNotNull(peers);
		peers.put(peer.getPeer().getID(), peer);

		final IModelListener[] listeners = getLocatorModel().getListener();
		if (listeners.length > 0) {
			Protocol.invokeLater(new Runnable() {
				@Override
				public void run() {
					for (IModelListener listener : listeners) {
						listener.locatorModelChanged(getLocatorModel(), peer, true);
					}
				}
			});
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tcf.te.tcf.locator.core.interfaces.services.ILocatorModelUpdateService#remove(org.eclipse.tcf.te.tcf.locator.core.interfaces.nodes.IPeerModel)
	 */
	@Override
	public void remove(final IPeerModel peer) {
		Assert.isNotNull(peer);
		Assert.isTrue(Protocol.isDispatchThread());

		Map<String, IPeerModel> peers = (Map<String, IPeerModel>)getLocatorModel().getAdapter(Map.class);
		Assert.isNotNull(peers);
		peers.remove(peer.getPeer().getID());

		final IModelListener[] listeners = getLocatorModel().getListener();
		if (listeners.length > 0) {
			Protocol.invokeLater(new Runnable() {
				@Override
				public void run() {
					for (IModelListener listener : listeners) {
						listener.locatorModelChanged(getLocatorModel(), peer, false);
					}
				}
			});
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tcf.te.tcf.locator.core.interfaces.services.ILocatorModelUpdateService#updatePeerServices(org.eclipse.tcf.te.tcf.locator.core.interfaces.nodes.IPeerModel, java.util.Collection, java.util.Collection)
	 */
	@Override
	public void updatePeerServices(IPeerModel peerNode, Collection<String> localServices, Collection<String> remoteServices) {
		Assert.isNotNull(peerNode);
		Assert.isTrue(Protocol.isDispatchThread());

		peerNode.setProperty(IPeerModelProperties.PROP_LOCAL_SERVICES, localServices != null ? makeString(localServices) : null);
		peerNode.setProperty(IPeerModelProperties.PROP_REMOTE_SERVICES, remoteServices != null ? makeString(remoteServices) : null);
	}

	/**
	 * Transform the given collection into a plain string.
	 *
	 * @param collection The collection. Must not be <code>null</code>.
	 * @return The plain string.
	 */
	protected String makeString(Collection<String> collection) {
		Assert.isNotNull(collection);

		String buffer = collection.toString();
		buffer = buffer.replaceAll("\\[", "").replaceAll("\\]", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		return buffer.trim();
	}
}
