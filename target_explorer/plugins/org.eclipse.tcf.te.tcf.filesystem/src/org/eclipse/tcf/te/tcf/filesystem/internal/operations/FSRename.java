/*******************************************************************************
 * Copyright (c) 2011 Wind River Systems, Inc. and others. All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * William Chen (Wind River) - [361324] Add more file operations in the file 
 * 												system of Target Explorer.
 *******************************************************************************/
package org.eclipse.tcf.te.tcf.filesystem.internal.operations;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tcf.te.tcf.filesystem.internal.exceptions.TCFException;
import org.eclipse.tcf.te.tcf.filesystem.internal.exceptions.TCFFileSystemException;
import org.eclipse.tcf.te.tcf.filesystem.internal.handlers.CacheManager;
import org.eclipse.tcf.te.tcf.filesystem.internal.handlers.PersistenceManager;
import org.eclipse.tcf.te.tcf.filesystem.internal.nls.Messages;
import org.eclipse.tcf.te.tcf.filesystem.internal.url.Rendezvous;
import org.eclipse.tcf.te.tcf.filesystem.model.FSModel;
import org.eclipse.tcf.te.tcf.filesystem.model.FSTreeNode;
import org.eclipse.tcf.protocol.IChannel;
import org.eclipse.tcf.protocol.IToken;
import org.eclipse.tcf.services.IFileSystem;
import org.eclipse.tcf.services.IFileSystem.DoneRename;
import org.eclipse.tcf.services.IFileSystem.FileSystemException;
import org.eclipse.ui.PlatformUI;
/**
 * FSRename renames the specified file/folder to a
 * new name.
 *
 */
public class FSRename extends FSOperation {
	// The file/folder node to be renamed.
	FSTreeNode node;
	// The new name the file/folder is renamed to.
	String newName;

	/**
	 * Create a rename operation that renames the node with the new name.
	 * 
	 * @param node The file/folder node to be renamed.
	 * @param newName The new name of this node.
	 */
	public FSRename(FSTreeNode node, String newName) {
		this.node = node;
		this.newName = newName;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.tcf.te.tcf.filesystem.internal.operations.FSOperation#doit()
	 */
	@Override
	public boolean doit() {
		IChannel channel = null;
		try {
			channel = openChannel(node.peerNode.getPeer());
			if (channel != null) {
				IFileSystem service = channel.getRemoteService(IFileSystem.class);
				if (service != null) {
					renameNode(service);
				}
				else {
					String message = NLS.bind(Messages.FSOperation_NoFileSystemError, node.peerNode.getPeer().getID());
					throw new TCFFileSystemException(message);
				}
				return true;
			}
		}
		catch (TCFException e) {
			Shell parent = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openError(parent, Messages.FSRename_RenameFileFolderTitle, e.getLocalizedMessage());
		}
		finally {
			if (channel != null) channel.close();
			FSModel.getInstance().fireNodeStateChanged(node);
		}
		return false;
	}

	/**
	 * Rename the node using the new name.
	 * 
	 * @param service File system service used to rename.
	 * @throws TCFFileSystemException The exception thrown during renaming.
	 */
	private void renameNode(IFileSystem service) throws TCFFileSystemException {
		String src_path = node.getLocation(true);
		String oldName = node.name;
		node.name = newName;
		String dst_path = node.getLocation(true);
		node.name = oldName;
		final TCFFileSystemException[] errors = new TCFFileSystemException[1];
		final Rendezvous rendezvous = new Rendezvous();
		service.rename(src_path, dst_path, new DoneRename() {
			@Override
			public void doneRename(IToken token, FileSystemException error) {
				if (error != null) {
					String message = NLS.bind(Messages.FSRename_CannotRename, node.name, error);
					errors[0] = new TCFFileSystemException(message, error);
				}
				else {
					File file = CacheManager.getInstance().getCacheFile(node);
					if (file.exists()) {
						if (node.isFile()) {
							closeEditor(file);
							PersistenceManager.getInstance().removeBaseTimestamp(node.getLocationURL());
						}
						file.delete();
					}
					node.name = newName;
				}
				rendezvous.arrive();
			}
		});
		try {
			rendezvous.waiting(5000L);
		}
		catch (InterruptedException e) {
			String message = NLS.bind(Messages.FSRename_CannotRename, node.name, Messages.FSOperation_TimedOutWhenOpening);
			errors[0] = new TCFFileSystemException(message);
		}
		if (errors[0] != null) {
			throw errors[0];
		}
	}
}
