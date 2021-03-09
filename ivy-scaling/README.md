# ivy-scaling

This example shows how to scale Axon Ivy Engine.

![Scaling](scaling.png)

After starting this example you can simply go to http://localhost

## Scale Up

By default one node of Axon Ivy Engine is running. You can scale up Axon Ivy Engine to five nodes with the following command:

`docker-compose up -d --scale ivy=5`

Have a look at the [Cluster view](http://localhost/system/faces/view/engine-cockpit/cluster.xhtml) in the Engine Cockpit to see the current running nodes

![Cluster View](cluster.png)

## Scale Down

You can scale down Axon Ivy Engine to two nodes with the following command:

`docker-compose up -d --scale ivy=2`

