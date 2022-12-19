package digital.fiasco.libraries.testing;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface WireMock
{
    Library wiremock = library("com.github.tomakehurst:wiremock");
}
