package digital.fiasco.runtime.dependency.oss.web;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface ApacheVelocity
{
    Library apache_velocity = library("org.apache.velocity:velocity");
}
