//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.build.builder.tools.test.unit.junit;

import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.BaseTool;

/**
 * Runs unit tests
 *
 * @author Jonathan Locke
 */
public class Tester extends BaseTool<Tester, Void>
{
    public Tester(Builder builder)
    {
        super(builder);
    }

    public Tester(Tester that)
    {
        super(that);
    }

    @Override
    public Tester copy()
    {
        return new Tester(this);
    }

    @Override
    public String description()
    {
        return "Runs tests";
    }

    @Override
    public Void onRun()
    {
        return null;
    }
}
