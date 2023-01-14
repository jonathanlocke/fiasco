package digital.fiasco.runtime.serialization;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.resource.Uris;
import digital.fiasco.runtime.repository.Repository;

import static com.telenav.kivakit.core.collections.list.StringList.split;
import static com.telenav.kivakit.core.language.Classes.classForName;
import static com.telenav.kivakit.core.language.Classes.newInstance;

public class RepositoryConverter<T extends Repository> extends BaseStringConverter<T>
{
    public RepositoryConverter(Listener listener, Class<T> type)
    {
        super(listener, type);
    }

    @Override
    protected String onToString(T repository)
    {
        return repository.getClass().getName() + ":" + repository.name() + ":" + repository.uri();
    }

    @Override
    protected T onToValue(String text)
    {
        var values = split(text, ":");
        var className = values.first();
        var repositoryName = values.get(1);
        var uri = Uris.uri(values.get(2));

        return newInstance(classForName(className), repositoryName, uri);
    }
}
