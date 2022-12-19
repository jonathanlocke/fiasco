package digital.fiasco.runtime.library;

import digital.fiasco.runtime.library.build.Build;
import digital.fiasco.runtime.library.cloud.Cloud;
import digital.fiasco.runtime.library.data.Data;
import digital.fiasco.runtime.library.data.database.Database;
import digital.fiasco.runtime.library.frameworks.Frameworks;
import digital.fiasco.runtime.library.languages.Languages;
import digital.fiasco.runtime.library.languages.java.Java;
import digital.fiasco.runtime.library.logging.Logging;
import digital.fiasco.runtime.library.parsing.Parsing;
import digital.fiasco.runtime.library.data.search.Search;
import digital.fiasco.runtime.library.testing.Testing;
import digital.fiasco.runtime.library.ui.Ui;
import digital.fiasco.runtime.library.web.Web;

@SuppressWarnings({ "unused" })
public interface Libraries extends
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
        Ui,
        Web

{
}
