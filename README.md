# Axon Ivy Docker Samples

![Axon Ivy Engine on Docker](axonivy-engine-on-docker.png)

This repository shows you different [docker-compose](https://docs.docker.com/compose/) setups with ivy.

All samples are based on the official [Axon Ivy Engine Docker Image](https://hub.docker.com/r/axonivy/axonivy-engine/).

If you have a GitHub user, you can use [GitHub Codespaces Quickstart](https://github.com/codespaces/new?skip_quickstart=true&machine=standardLinux32gb&repo=138610969&ref=master&devcontainer_path=.devcontainer%2Fdevcontainer.json&geo=EuropeWest) to instantly access and run the examples, [see more](#using-github-codespaces).

## Run

Switch in a directory of your choice

    cd ivy-db-mysql

Start the docker compose setup

    docker-compose up
    
Afterwards the Axon Ivy Engine is available under http://localhost:8080/ivy

## Using GitHub Codespaces

Go to [GitHub Codespaces Quickstart](https://github.com/codespaces/new?skip_quickstart=true&machine=standardLinux32gb&repo=138610969&ref=master&devcontainer_path=.devcontainer%2Fdevcontainer.json&geo=EuropeWest) and create the codespace.

Wait until the codespace is ready and the terminal has been loaded. Run the examples as described above. If the Axon Ivy Engine is running, localhost:8080 is forwarded. Therefore, switch to the ports view where you can access the forwarded address.

## Licence

All these samples are shipped with a limited demo licence. You are able to run the portal application.

## Kubernetes Samples

Also have a look at our [kubernetes samples](https://github.com/axonivy/kubernetes-samples)