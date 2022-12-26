package digital.fiasco.libraries.cloud;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface Aws extends LibraryGroups
{
    Library aws_jmespath                        = aws_group.library("jmespath-java");
    Library aws_sdk_core                        = aws_group.library("aws-java-sdk-core");
    Library aws_sdk_test_utils                  = aws_group.library("aws-java-sdk-test-utils");
    Library aws_software_sdk_annotations        = aws_software_sdk_group.library("annotations");
    Library aws_software_sdk_apache_client      = aws_software_sdk_group.library("apache-client");
    Library aws_software_sdk_auth               = aws_software_sdk_group.library("auth");
    Library aws_software_sdk_core               = aws_software_sdk_group.library("core");
    Library aws_software_sdk_http_client_spi    = aws_software_sdk_group.library("http-client-spi");
    Library aws_software_sdk_json_protocol      = aws_software_sdk_group.library("aws-json-protocol");
    Library aws_software_sdk_netty_nio_client   = aws_software_sdk_group.library("netty-nio-client");
    Library aws_software_sdk_protocol_core      = aws_software_sdk_group.library("protocol-core");
    Library aws_software_sdk_regions            = aws_software_sdk_group.library("regions");
    Library aws_software_sdk_service_test_utils = aws_software_sdk_group.library("service-test-utils");
    Library aws_software_sdk_utils              = aws_software_sdk_group.library("utils");
}
