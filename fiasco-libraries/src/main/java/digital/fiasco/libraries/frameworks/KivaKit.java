package digital.fiasco.libraries.frameworks;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface KivaKit
{
    Library kivakit = library("com.telenav.kivakit:kivakit");
    Library kivakit_application = library("com.telenav.kivakit:kivakit-application");
    Library kivakit_network_core = library("com.telenav.kivakit:kivakit-network-core");
}
