//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.build.tools;

import com.telenav.kivakit.core.messaging.Repeater;
import digital.fiasco.runtime.build.BuildAssociated;

/**
 * Interface for executable tools.
 */
public interface Tool extends
        Repeater,
        Runnable,
        BuildAssociated
{
}
