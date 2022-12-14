package digital.fiasco.runtime.dependency.oss;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public interface Testing
{
    Library assertj_core = library("org.assertj:assertj-core");
    Library easymock = library("org.easymock:easymock");
    Library hamcrest = library("org.hamcrest:hamcrest-all");
    Library hamcrest_library = library("org.hamcrest:hamcrest-library");
    Library junit = library("junit:junit");
    Library junit5_api = library("org.junit.jupiter:junit-jupiter-api");
    Library junit5_engine = library("org.junit.jupiter:junit-jupiter-engine");
    Library mockito_all = library("org.mockito:mockito-all");
    Library mockito_core = library("org.mockito:mockito-core");
    Library powermock_api_mockito = library("org.powermock:powermock-api-mockito");
    Library powermock_module_junit4 = library("org.powermock:powermock-module-junit4");
    Library test_ng = library("org.testng:testng");
    Library wiremock = library("com.github.tomakehurst:wiremock");
}
