//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.build.tools;

import com.telenav.kivakit.core.messaging.Repeater;
import digital.fiasco.runtime.build.BaseBuild;
import digital.fiasco.runtime.build.Build;

/**
 * Interface for executable tools. Tools are runnable message repeaters that are associated with a {@link BaseBuild}
 * that can be retrieved with build methods.
 *
 * @author Jonathan locke
 */
public interface Tool extends
        Runnable,
        Repeater,
        Build
{
    String description();
}
