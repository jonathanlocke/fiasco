package digital.fiasco.libraries.data;

import digital.fiasco.libraries.data.compression.Compression;
import digital.fiasco.libraries.data.database.Database;
import digital.fiasco.libraries.data.formats.Formats;

public interface Data extends
        Compression,
        Database,
        Formats
{
}
