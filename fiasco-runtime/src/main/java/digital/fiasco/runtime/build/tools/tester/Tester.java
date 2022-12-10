//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.build.tools.tester;

import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.tools.BaseTool;

/**
 * Runs unit tests
 *
 * @author shibo
 */
public class Tester extends BaseTool
{
    public Tester(Build build)
    {
        super(build);
    }

    @Override
    protected String description()
    {
        return null;
    }

    @Override
    protected void onRun()
    {
    }
}
