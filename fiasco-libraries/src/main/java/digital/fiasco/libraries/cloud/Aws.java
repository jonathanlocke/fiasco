package digital.fiasco.libraries.cloud;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings("unused")
public interface Aws extends LibraryGroups
{
    Library aws_jmespath                        = aws_group.library("jmespath-java").asLibrary();
    Library aws_sdk_core                        = aws_group.library("aws-java-sdk-core").asLibrary();
    Library aws_sdk_test_utils                  = aws_group.library("aws-java-sdk-test-utils").asLibrary();
    Library aws_software_sdk_annotations        = aws_software_sdk_group.library("annotations").asLibrary();
    Library aws_software_sdk_apache_client      = aws_software_sdk_group.library("apache-client").asLibrary();
    Library aws_software_sdk_auth               = aws_software_sdk_group.library("auth").asLibrary();
    Library aws_software_sdk_core               = aws_software_sdk_group.library("core").asLibrary();
    Library aws_software_sdk_http_client_spi    = aws_software_sdk_group.library("http-client-spi").asLibrary();
    Library aws_software_sdk_json_protocol      = aws_software_sdk_group.library("aws-json-protocol").asLibrary();
    Library aws_software_sdk_netty_nio_client   = aws_software_sdk_group.library("netty-nio-client").asLibrary();
    Library aws_software_sdk_protocol_core      = aws_software_sdk_group.library("protocol-core").asLibrary();
    Library aws_software_sdk_regions            = aws_software_sdk_group.library("regions").asLibrary();
    Library aws_software_sdk_service_test_utils = aws_software_sdk_group.library("service-test-utils").asLibrary();
    Library aws_software_sdk_utils              = aws_software_sdk_group.library("utils").asLibrary();
}
