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
package org.eclipse.tcf.te.tcf.filesystem.internal.dnd;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.tcf.te.tcf.filesystem.model.FSTreeNode;

/**
 * The drag source listener for the file tree of Target Explorer.
 */
public class FSDragSourceListener implements DragSourceListener {
	// The tree viewer in which the DnD gesture happens.
	private TreeViewer viewer;

	/**
	 * Create an FSDragSourceListener using the specified tree viewer.
	 * 
	 * @param viewer The file system tree viewer.
	 */
	public FSDragSourceListener(TreeViewer viewer) {
		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	@Override
	public void dragStart(DragSourceEvent event) {
		IStructuredSelection aSelection = (IStructuredSelection) viewer.getSelection();
		event.doit = isDraggable(aSelection);
		LocalSelectionTransfer.getTransfer().setSelection(aSelection);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	@Override
	public void dragSetData(DragSourceEvent event) {
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.dataType)) {
			event.data = LocalSelectionTransfer.getTransfer().getSelection();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	@Override
	public void dragFinished(DragSourceEvent event) {
	}
	
	/**
	 * If the current selection is draggable.
	 * 
	 * @param selection The currently selected nodes.
	 * @return true if it is draggable.
	 */
	private boolean isDraggable(IStructuredSelection selection) {
		if (selection.isEmpty()) {
			return false;
		}
		Object[] objects = selection.toArray();
		for (Object object : objects) {
			if (!isDraggableObject(object)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * If the specified object is a draggable element.
	 * 
	 * @param object The object to be dragged.
	 * @return true if it is draggable.
	 */
	private boolean isDraggableObject(Object object) {
		if (object instanceof FSTreeNode) {
			FSTreeNode node = (FSTreeNode) object;
			return !node.isRoot() && (node.isWindowsNode() && !node.isReadOnly() || !node.isWindowsNode() && node.isWritable());
		}
		return false;
	}	
}
