package digital.fiasco.libraries.utilities.math;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface ApacheCommonsMath extends LibraryGroups
{
    Library apache_commons_math3 = apache_commons_group.library("commons-math3");
}
