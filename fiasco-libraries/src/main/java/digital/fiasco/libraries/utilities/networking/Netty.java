package digital.fiasco.libraries.utilities.networking;

import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactGroup;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactGroup.group;

@SuppressWarnings("unused")
public interface Netty
{
    ArtifactGroup nettyGroup = group("io.netty");

    Library netty                              = nettyGroup.library("netty").asLibrary();
    Library netty_all                          = nettyGroup.library("netty-all").asLibrary();
    Library netty_buffer                       = nettyGroup.library("netty-buffer").asLibrary();
    Library netty_codec                        = nettyGroup.library("netty-codec").asLibrary();
    Library netty_codec_http                   = nettyGroup.library("netty-codec-http").asLibrary();
    Library netty_codec_http2                  = nettyGroup.library("netty-codec-http2").asLibrary();
    Library netty_codec_socks                  = nettyGroup.library("netty-codec-socks").asLibrary();
    Library netty_codec_transport_native_epoll = nettyGroup.library("netty-transport-native-epoll").asLibrary();
    Library netty_common                       = nettyGroup.library("netty-common").asLibrary();
    Library netty_handler                      = nettyGroup.library("netty-handler").asLibrary();
    Library netty_handler_proxy                = nettyGroup.library("netty-handler-proxy").asLibrary();
    Library netty_native_kqueue                = nettyGroup.library("netty-native-kqueue").asLibrary();
    Library netty_resolver                     = nettyGroup.library("netty-resolver").asLibrary();
    Library netty_resolver_dns                 = nettyGroup.library("netty-resolver-dns").asLibrary();
    Library netty_transport                    = nettyGroup.library("netty-transport").asLibrary();
    Library netty_transport_native_unix_common = nettyGroup.library("netty-transport-native-unix-common").asLibrary();
}
