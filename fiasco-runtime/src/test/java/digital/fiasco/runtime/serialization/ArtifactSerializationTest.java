package digital.fiasco.runtime.serialization;

import digital.fiasco.runtime.FiascoTest;
import org.junit.Test;

import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static digital.fiasco.runtime.build.BuildRepositories.MAVEN_CENTRAL;
import static digital.fiasco.runtime.dependency.artifact.Artifact.artifactFromJson;

public class ArtifactSerializationTest extends FiascoTest
{
    @Test
    public void testArtifactConverter()
    {
        var converter = new ArtifactConverter(throwingListener());
        ensureThrows(() -> converter.convert("wonky/com.telenav.kivakit:kivakit-core:1.0.0"));
        ensureEqual(converter.convert(kivakitCore().name()), kivakitCore());
        ensureEqual(converter.convert(kivakitIcons().name()), kivakitIcons());
        ensureEqual(converter.unconvert(kivakitCore()), kivakitCore().name());
        ensureEqual(converter.unconvert(kivakitIcons()), kivakitIcons().name());
    }

    @Test
    public void testJsonSerialization()
    {
        var application = kivakitApplication()
            .dependsOn(kivakitCore(), kivakitResource())
            .withRepository(MAVEN_CENTRAL)
            .withJar(packageContent())
            .withJavadoc(packageContent());

        var json = application.toJson();
        ensureEqual(application, artifactFromJson(json));
    }
}