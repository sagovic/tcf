/*******************************************************************************
 * Copyright (c) 2011 Wind River Systems, Inc. and others. All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Wind River Systems - initial API and implementation
 * William Chen (Wind River) - [345387]Open the remote files with a proper editor
 * William Chen (Wind River) - [352302]Opening a file in an editor depending on
 *                             the client's permissions.
 * William Chen (Wind River) - [361324] Add more file operations in the file system
 * 												of Target Explorer.
 *******************************************************************************/
package org.eclipse.tcf.te.tcf.filesystem.internal.handlers;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.runtime.Assert;
import org.eclipse.tcf.te.tcf.filesystem.activator.UIPlugin;
import org.eclipse.tcf.te.tcf.filesystem.internal.operations.FSClipboard;
import org.eclipse.tcf.te.tcf.filesystem.model.CacheState;
import org.eclipse.tcf.te.tcf.filesystem.model.FSModel;
import org.eclipse.tcf.te.tcf.filesystem.model.FSTreeNode;

/**
 * The property tester of an FSTreeNode. The properties include "isFile"
 * if it is a file node, "isDirectory" if it is a directory, "isBinaryFile"
 * if it is a binary file, "isReadable" if it is readable, "isWritable" if
 * it is writable, "isExecutable" if it is executable, "isRoot" if it is a
 * root directory, "isWindows" if it is a windows file node, "isReadOnly"
 * if it is read only, "isHidden" if it is hidden, "getCacheState" to
 * get a node's state, and "isSamePeer" to test if the selected folder is
 * from the same peer with the files in the clip board.
 */
public class FSTreeNodePropertyTester extends PropertyTester {

	/* (non-Javadoc)
	 * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		Assert.isTrue(receiver instanceof FSTreeNode);
		FSTreeNode node = (FSTreeNode) receiver;
		if (property.equals("isFile")) { //$NON-NLS-1$
			return node.isFile();
		} else if (property.equals("isDirectory")) { //$NON-NLS-1$
			return node.isDirectory();
		} else if (property.equals("isBinaryFile")) { //$NON-NLS-1$
			return ContentTypeHelper.getInstance().isBinaryFile(node);
		} else if (property.equals("isReadable")){ //$NON-NLS-1$
			return node.isReadable();
		} else if (property.equals("isWritable")){ //$NON-NLS-1$
			return node.isWritable();
		} else if (property.equals("isExecutable")){ //$NON-NLS-1$
			return node.isExecutable();
		} else if (property.equals("isRoot")) { //$NON-NLS-1$
			return node.isRoot();
		} else if (property.equals("isWindows")) { //$NON-NLS-1$
			return node.isWindowsNode();
		} else if (property.equals("isReadOnly")) { //$NON-NLS-1$
			return node.isReadOnly();
		} else if (property.equals("isHidden")) { //$NON-NLS-1$
			return node.isHidden();
		} else if (property.equals("getCacheState")){ //$NON-NLS-1$
			File file = CacheManager.getInstance().getCacheFile(node);
			if(!file.exists())
				return false;
			CacheState state = StateManager.getInstance().getCacheState(node);
			return state.name().equals(expectedValue);
		} else if (property.equals("isSamePeer")) { //$NON-NLS-1$
			String id = node.peerNode.getPeer().getID();
			FSClipboard cb = UIPlugin.getDefault().getClipboard();
			if (!cb.isEmpty()) {
				List<URL> urls = cb.getFiles();
				for (URL url : urls) {
					FSTreeNode clipped = FSModel.getInstance().getTreeNode(url);
					String cid = clipped.peerNode.getPeer().getID();
					if(!id.equals(cid)){
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
}
