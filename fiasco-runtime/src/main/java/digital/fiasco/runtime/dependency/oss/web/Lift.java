package digital.fiasco.runtime.dependency.oss.web;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Lift
{
    Library lift_webkit = library("net.liftweb:lift-webkit");
}
