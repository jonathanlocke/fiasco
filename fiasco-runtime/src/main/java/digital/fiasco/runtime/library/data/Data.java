package digital.fiasco.runtime.library.data;

import digital.fiasco.runtime.library.data.compression.Compression;
import digital.fiasco.runtime.library.data.database.Database;
import digital.fiasco.runtime.library.data.formats.Formats;

public interface Data extends
        Compression,
        Database,
        Formats
{
}
