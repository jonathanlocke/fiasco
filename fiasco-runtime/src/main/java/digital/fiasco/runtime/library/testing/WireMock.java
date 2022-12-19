package digital.fiasco.runtime.library.testing;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface WireMock
{
    Library wiremock = library("com.github.tomakehurst:wiremock");
}
