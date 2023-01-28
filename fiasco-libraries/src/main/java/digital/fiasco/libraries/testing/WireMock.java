package digital.fiasco.libraries.testing;

import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

import static digital.fiasco.runtime.dependency.artifact.artifacts.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface WireMock
{
    Library wiremock = library("com.github.tomakehurst:wiremock");
}
