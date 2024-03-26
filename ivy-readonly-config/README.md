# Read-only configuration

In a container platform like Kubernetes it is common that configuration
files are provided read-only. This behavior can also be simulated with docker by
adding `:ro` at the and of volume mapping.

To make the Axon Ivy Engine fully functional, the read-only configuration files
will be copied to a temp directory at startup which is writeable at runtime.
So all the feature still work, but all the changes made to the configuration
will be lost after creating a new container of it.

You can also map the hole application configuration as read-only. This will
copy all the files to a writeable space on startup.
