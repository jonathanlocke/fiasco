package digital.fiasco.runtime.dependency.artifact.content.jar;

import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static com.telenav.kivakit.filesystem.Folder.FolderType.CLEAN_UP_ON_EXIT;
import static com.telenav.kivakit.filesystem.Folder.temporaryFolderForProcess;
import static com.telenav.kivakit.resource.WriteMode.OVERWRITE;
import static digital.fiasco.runtime.dependency.artifact.content.jar.JarContent.jarContent;

public class JarContentTest extends FiascoTest
{
    @Test
    public void test()
    {
        var jar = packageResource("test.jar");
        var file = temporaryFolderForProcess(CLEAN_UP_ON_EXIT).file("test.jar");
        jar.copyTo(file, OVERWRITE);
        var content = jarContent(file)
            .withOffset(5);
        var yaml = content.toYaml();
        var deserialized = jarContent(yaml);
        ensureEqual(content, deserialized);
    }
}
