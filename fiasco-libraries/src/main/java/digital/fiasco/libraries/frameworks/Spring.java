package digital.fiasco.libraries.frameworks;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

@SuppressWarnings("unused")
public interface Spring extends LibraryGroups
{
    Library spring_beans                        = spring_framework_group.library("spring-beans").asLibrary();
    Library spring_boot_autoconfigure           = spring_boot_group.library("spring-boot-autoconfigure").asLibrary();
    Library spring_boot_configuration_processor = spring_boot_group.library("spring-boot-configuration-processor").asLibrary();
    Library spring_boot_devtools                = spring_boot_group.library("spring-boot-devtools").asLibrary();
    Library spring_boot_starter                 = spring_boot_group.library("spring-boot-starter").asLibrary();
    Library spring_boot_starter_actuator        = spring_boot_group.library("spring-boot-starter-actuator").asLibrary();
    Library spring_boot_starter_data_jpa        = spring_boot_group.library("spring-boot-starter-data-jpa").asLibrary();
    Library spring_boot_starter_test            = spring_boot_group.library("spring-boot-starter-test").asLibrary();
    Library spring_boot_starter_web             = spring_boot_group.library("spring-boot-starter-web").asLibrary();
    Library spring_context                      = spring_framework_group.library("spring-context").asLibrary();
    Library spring_core                         = spring_framework_group.library("spring-core").asLibrary();
    Library spring_jdbc                         = spring_framework_group.library("spring-web").asLibrary();
    Library spring_test                         = spring_framework_group.library("spring-test").asLibrary();
    Library spring_tx                           = spring_framework_group.library("spring-tx").asLibrary();
    Library spring_web                          = spring_framework_group.library("spring-jdbc").asLibrary();
    Library spring_web_mvc                      = spring_framework_group.library("spring-webmvc").asLibrary();
}
