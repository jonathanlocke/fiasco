import digital.fiasco.runtime.build.tools.librarian.Library;

import static digital.fiasco.runtime.build.tools.librarian.Library.library;

/**
 * @author jonathan
 */
@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public interface Libraries
{
    Library commons_logging = library("org.apache.commons:logging:1.0");
    Library kryo = library("com.esotericsoftware:kryo:4.3.1");
}
