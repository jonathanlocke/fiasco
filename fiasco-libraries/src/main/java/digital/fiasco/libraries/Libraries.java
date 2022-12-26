package digital.fiasco.libraries;

import digital.fiasco.libraries.build.Build;
import digital.fiasco.libraries.cloud.Cloud;
import digital.fiasco.libraries.data.Data;
import digital.fiasco.libraries.data.search.Search;
import digital.fiasco.libraries.frameworks.Frameworks;
import digital.fiasco.libraries.languages.Languages;
import digital.fiasco.libraries.testing.Testing;
import digital.fiasco.libraries.ui.Ui;
import digital.fiasco.libraries.ui.web.Web;
import digital.fiasco.libraries.utilities.logging.Logging;
import digital.fiasco.libraries.utilities.parsing.Parsing;

@SuppressWarnings({ "unused" })
public interface Libraries extends
    Build,
    Cloud,
    Data,
    Frameworks,
    Languages,
    Logging,
    Parsing,
    Search,
    Testing,
    Ui,
    Web
{
}
