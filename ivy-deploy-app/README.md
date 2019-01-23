# ivy-deploy-app

This demos shows how you can deploy an application within a container.

Please note that you should mount the application directory as volume.
Because that is an internal state of the engine which is coupled with
the system database.

If you have problems to map this folder as volume. You should first create
it on your local machince and change the owner.

```
mkdir applications
chown 1000:1000 applications
```
