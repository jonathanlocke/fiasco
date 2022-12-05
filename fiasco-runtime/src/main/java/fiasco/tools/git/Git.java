//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package fiasco.tools.git;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.conversion.core.time.TimeConverter;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.filesystem.Folder;
import fiasco.BaseBuild;

import static com.telenav.kivakit.core.os.OperatingSystem.operatingSystem;
import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;

/**
 * Runs git
 *
 * @author shibo
 */
@SuppressWarnings("unused")
public class Git extends BaseComponent
{
    private final BaseBuild build;

    public Git(BaseBuild build)
    {
        this.build = build;
    }

    public String commitHash()
    {
        return run(build.rootFolder(), "git", "rev-parse", "HEAD").trim();
    }

    public Time commitTime()
    {
        // Mon Dec 5 03:49:27 2022 -0700
        var time = run(build.rootFolder(), "git", "log", "-1", "--format=%cd");
        return new TimeConverter(this, ISO_ZONED_DATE_TIME).convert(time);
    }

    private String run(Folder folder, String... arguments)
    {
        return operatingSystem().execute(this, folder.asJavaFile(), arguments);
    }
}
