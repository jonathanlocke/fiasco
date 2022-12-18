package digital.fiasco.runtime.dependency.oss.frameworks;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface Spring
{
    Library spring_beans = library("org.springframework:spring-beans");
    Library spring_boot_autoconfigure = library("org.springframework.boot:spring-boot-autoconfigure");
    Library spring_boot_configuration_processor = library("org.springframework.boot:spring-boot-configuration-processor");
    Library spring_boot_devtools = library("org.springframework.boot:spring-boot-devtools");
    Library spring_boot_starter_actuator = library("org.springframework.boot:spring-boot-starter-actuator");
    Library spring_context = library("org.springframework:spring-context");
    Library spring_core = library("org.springframework:spring-core");
    Library spring_jdbc = library("org.springframework:spring-web");
    Library spring_test = library("org.springframework:spring-test");
    Library spring_tx = library("org.springframework:spring-tx");
    Library spring_web = library("org.springframework:spring-jdbc");
    Library spring_web_mvc = library("org.springframework:spring-webmvc");
    Library spring_boot_starter = library("org.springframework.boot:spring-boot-starter");
    Library spring_boot_starter_data_jpa = library("org.springframework.boot:spring-boot-starter-data-jpa");
    Library spring_boot_starter_test = library("org.springframework.boot:spring-boot-starter-test");
    Library spring_boot_starter_web = library("org.springframework.boot:spring-boot-starter-web");
}
