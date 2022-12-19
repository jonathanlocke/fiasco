package digital.fiasco.libraries.ui.mobile;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface ApacheFlex
{
    Library apache_flex_blaze_ds = library("org.apache.flex:blazeds");
    Library apache_flex_compiler = library("org.apache.flex:compiler");
    Library apache_flex_framework = library("org.apache.flex:framework");
    Library apache_flex_javascript = library("org.apache.flex:flexjs");
    Library apache_flex_tool_api = library("org.apache.flex:flex-tool-api");
    Library apache_flex_utilities = library("org.apache.flex:utilities");
}
