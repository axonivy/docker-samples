# ivy-test-mailserver

This demo creates a local webserver which allows you to test the generated mails like the new task mail of ivy on an engine. Note that the sent mails of this container will most likely end up in your spam folder.

After starting this example you can simply go to http://localhost where you can find a working engine.

## Configuration

We use the docker-mailserver which you can find here: https://github.com/tomav/docker-mailserver

The mailserver operates with the default configurations and the mail address: `noreply@ivyserver.local` and password `admin` on port `25`.
