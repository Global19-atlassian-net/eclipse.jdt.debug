/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.debug.testplugin;

import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.memory.IMemoryRenderingBindingsProvider;
import org.eclipse.debug.ui.memory.IMemoryRenderingType;

/**
 * Contributed dynamic rendernig bindings.
 * @since 3.1
 */
public class DynamicRenderingBindings implements IMemoryRenderingBindingsProvider {

    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.memory.IMemoryRenderingBindingsProvider#getRenderingTypes(org.eclipse.debug.core.model.IMemoryBlock)
     */
    public IMemoryRenderingType[] getRenderingTypes(IMemoryBlock block) {
        return new IMemoryRenderingType[]{getPrimaryRenderingType(block)};
    }

    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.memory.IMemoryRenderingBindingsProvider#getDefaultRenderingTypes(org.eclipse.debug.core.model.IMemoryBlock)
     */
    public IMemoryRenderingType[] getDefaultRenderingTypes(IMemoryBlock block) {
        return new IMemoryRenderingType[]{getPrimaryRenderingType(block)};
    }

    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.memory.IMemoryRenderingBindingsProvider#getPrimaryRenderingType(org.eclipse.debug.core.model.IMemoryBlock)
     */
    public IMemoryRenderingType getPrimaryRenderingType(IMemoryBlock block) {
        return DebugUITools.getMemoryRenderingManager().getRenderingType("rendering_type_1");
    }

}
