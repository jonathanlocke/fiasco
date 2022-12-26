package digital.fiasco.libraries.data;

import digital.fiasco.libraries.data.database.Database;
import digital.fiasco.libraries.data.formats.Formats;
import digital.fiasco.libraries.data.search.Search;

public interface Data extends
    Database,
    Formats,
    Search
{
}
