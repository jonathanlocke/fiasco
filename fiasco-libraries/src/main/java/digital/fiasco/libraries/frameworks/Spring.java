package digital.fiasco.libraries.frameworks;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface Spring extends LibraryGroups
{
    Library spring_beans                        = spring_framework_group.library("spring-beans");
    Library spring_boot_autoconfigure           = spring_boot_group.library("spring-boot-autoconfigure");
    Library spring_boot_configuration_processor = spring_boot_group.library("spring-boot-configuration-processor");
    Library spring_boot_devtools                = spring_boot_group.library("spring-boot-devtools");
    Library spring_boot_starter                 = spring_boot_group.library("spring-boot-starter");
    Library spring_boot_starter_actuator        = spring_boot_group.library("spring-boot-starter-actuator");
    Library spring_boot_starter_data_jpa        = spring_boot_group.library("spring-boot-starter-data-jpa");
    Library spring_boot_starter_test            = spring_boot_group.library("spring-boot-starter-test");
    Library spring_boot_starter_web             = spring_boot_group.library("spring-boot-starter-web");
    Library spring_context                      = spring_framework_group.library("spring-context");
    Library spring_core                         = spring_framework_group.library("spring-core");
    Library spring_jdbc                         = spring_framework_group.library("spring-web");
    Library spring_test                         = spring_framework_group.library("spring-test");
    Library spring_tx                           = spring_framework_group.library("spring-tx");
    Library spring_web                          = spring_framework_group.library("spring-jdbc");
    Library spring_web_mvc                      = spring_framework_group.library("spring-webmvc");
}
