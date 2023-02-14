package digital.fiasco.runtime.repository.local.cache;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.Set;

public class FiascoJavaFileManager extends ForwardingJavaFileManager
{
    /**
     * Creates a new instance of {@code ForwardingJavaFileManager}.
     *
     * @param fileManager delegate to this file manager
     */
    protected FiascoJavaFileManager(JavaFileManager fileManager)
    {
        super(fileManager);
    }

    @Override
    public FileObject getFileForInput(Location location,
                                      String packageName,
                                      String relativeName) throws IOException
    {
        return super.getFileForInput(location, packageName, relativeName);
    }

    /**
     * {@inheritDoc}
     *
     * @param location {@inheritDoc}
     * @param packageName {@inheritDoc}
     * @param set {@inheritDoc}
     * @param recurse {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public Iterable<JavaFileObject> list(Location location,
                                         String packageName,
                                         Set set,
                                         boolean recurse) throws IOException
    {
        return super.list(location, packageName, set, recurse);
    }
}
