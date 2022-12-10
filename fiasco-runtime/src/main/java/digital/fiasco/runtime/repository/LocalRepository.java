package digital.fiasco.runtime.repository;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.tools.librarian.Library;

public class LocalRepository extends BaseRepository
{
    @Override
    public ObjectList<Library> dependencies(Library library)
    {
        return null;
    }

    @Override
    public void install(Library library)
    {

    }

    @Override
    public boolean remove(Library library)
    {
        return false;
    }

    @Override
    public boolean removeAll()
    {
        return false;
    }
}
