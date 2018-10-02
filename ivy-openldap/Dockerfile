FROM axonivy/axonivy-engine:dev

#
# Download full application zip from artifactory with downloadApp.sh
# Quickly install downloadApp.sh and its dependencies with root user
#
USER root
RUN apt-get update -y && apt-get install curl -y
ADD downloadApp.sh ./downloadApp.sh
RUN chmod ugo+x ./downloadApp.sh
USER ivy

# Download latest full application zip
RUN ./downloadApp.sh ch/ivyteam/ivy/project/demo/ci/deploy/application application zip
