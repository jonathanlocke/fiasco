package digital.fiasco.runtime.dependency.oss.testing;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface WireMock
{
    Library wiremock = library("com.github.tomakehurst:wiremock");
}
