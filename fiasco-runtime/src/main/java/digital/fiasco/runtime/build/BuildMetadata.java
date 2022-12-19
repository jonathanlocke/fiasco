package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.string.FormatProperty;
import digital.fiasco.runtime.build.metadata.Contributor;
import digital.fiasco.runtime.build.metadata.Copyright;
import digital.fiasco.runtime.build.metadata.Issues;
import digital.fiasco.runtime.build.metadata.License;
import digital.fiasco.runtime.build.metadata.Organization;
import digital.fiasco.runtime.build.metadata.SourceControl;
import digital.fiasco.runtime.repository.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.artifact.ArtifactType;

public record BuildMetadata(@FormatProperty ArtifactDescriptor artifact,
                            @FormatProperty ArtifactType artifactType,
                            @FormatProperty Organization organization,
                            @FormatProperty Copyright copyright,
                            @FormatProperty License license,
                            @FormatProperty Issues issues,
                            @FormatProperty SourceControl sources,
                            @FormatProperty ObjectList<Contributor> contributors)
{
}
