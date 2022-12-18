package digital.fiasco.runtime.dependency.oss;

import digital.fiasco.runtime.dependency.oss.build.Build;
import digital.fiasco.runtime.dependency.oss.cloud.Cloud;
import digital.fiasco.runtime.dependency.oss.data.Data;
import digital.fiasco.runtime.dependency.oss.database.Database;
import digital.fiasco.runtime.dependency.oss.frameworks.Frameworks;
import digital.fiasco.runtime.dependency.oss.java.Java;
import digital.fiasco.runtime.dependency.oss.languages.Languages;
import digital.fiasco.runtime.dependency.oss.logging.Logging;
import digital.fiasco.runtime.dependency.oss.parsing.Parsing;
import digital.fiasco.runtime.dependency.oss.search.Search;
import digital.fiasco.runtime.dependency.oss.testing.Testing;
import digital.fiasco.runtime.dependency.oss.web.Web;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "unused" })
public interface OpenSource extends
        Build,
        Cloud,
        Data,
        Database,
        Frameworks,
        Java,
        Languages,
        Logging,
        Parsing,
        Search,
        Testing,
        Web

{
}
