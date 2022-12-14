//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.build.builder.tools.tester;

import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.BaseTool;

/**
 * Runs unit tests
 *
 * @author Jonathan Locke
 */
public class Tester extends BaseTool
{
    public Tester(Builder builder)
    {
        super(builder);
    }

    @Override
    public String description()
    {
        return "Runs tests";
    }

    @Override
    public void onRun()
    {
    }
}
