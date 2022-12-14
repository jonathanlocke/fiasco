package digital.fiasco.libraries.ui.mobile;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface ApacheFlex extends LibraryGroups
{
    Library apache_flex_blaze_ds   = apache_flex_group.library("blazeds");
    Library apache_flex_compiler   = apache_flex_group.library("compiler");
    Library apache_flex_framework  = apache_flex_group.library("framework");
    Library apache_flex_javascript = apache_flex_group.library("flexjs");
    Library apache_flex_tool_api   = apache_flex_group.library("flex-tool-api");
    Library apache_flex_utilities  = apache_flex_group.library("utilities");
}
