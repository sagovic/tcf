/*******************************************************************************
 * Copyright (c) 2011 Wind River Systems, Inc. and others. All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Wind River Systems - initial API and implementation
 *******************************************************************************/
package org.eclipse.tcf.te.tcf.processes.ui.internal.callbacks;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.core.runtime.Assert;
import org.eclipse.tcf.protocol.IChannel;
import org.eclipse.tcf.protocol.Protocol;
import org.eclipse.tcf.services.ISysMonitor;
import org.eclipse.tcf.te.tcf.core.Tcf;
import org.eclipse.tcf.te.tcf.core.interfaces.IChannelManager;
import org.eclipse.tcf.te.tcf.processes.ui.model.ProcessModel;
import org.eclipse.tcf.te.tcf.processes.ui.model.ProcessTreeNode;

/**
 * The callback handler that handles the event when the channel opens when refreshing.
 */
public class RefreshChildrenDoneOpenChannel implements IChannelManager.DoneOpenChannel {
	// The parent node to be refreshed.
	ProcessTreeNode parentNode;

	/**
	 * Create an instance with the specified field parameters.
	 */
	public RefreshChildrenDoneOpenChannel(ProcessTreeNode parentNode) {
		this.parentNode = parentNode;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.tcf.te.tcf.core.interfaces.IChannelManager.DoneOpenChannel#doneOpenChannel(java.lang.Throwable, org.eclipse.tcf.protocol.IChannel)
	 */
	@Override
	public void doneOpenChannel(Throwable error, final IChannel channel) {
		Assert.isTrue(Protocol.isDispatchThread());
		if (error == null && channel != null) {
			ISysMonitor service = channel.getRemoteService(ISysMonitor.class);
			if (service != null) {
				final ProcessModel model = ProcessModel.getProcessModel(parentNode.peerNode);
				CallbackMonitor monitor = new CallbackMonitor(new Runnable(){
					@Override
                    public void run() {
						Tcf.getChannelManager().closeChannel(channel);
						model.firePropertyChanged(parentNode);
                    }}, getChildrenIds());
				for (ProcessTreeNode child : parentNode.children) {
					if (!child.childrenQueried && !child.childrenQueryRunning) {
						Runnable callback = new RefreshChildrenDoneCallback(child.id, parentNode, monitor, model);
						Queue<ProcessTreeNode> queue = new ConcurrentLinkedQueue<ProcessTreeNode>();
						ISysMonitor.DoneGetChildren done = new RefreshDoneGetChildren(model, callback, queue, channel, service, child);
						service.getChildren(child.id, done);
					}
				}
			}
		}
	}

	/**
     * Create and initialize a status map with all the context ids and completion status
     * set to false.
     */
	private Object[] getChildrenIds() {
        List<Object> ids = new ArrayList<Object>();
        for (ProcessTreeNode child : parentNode.children) {
        	ids.add(child.id);
        }
        return ids.toArray(new Object[ids.size()]);
    }
}
