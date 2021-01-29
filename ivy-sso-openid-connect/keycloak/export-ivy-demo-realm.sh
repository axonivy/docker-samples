# use this script to export a keycloak realm with its users. the ui won't export the susers.
/opt/jboss/keycloak/bin/standalone.sh \
 -Djboss.socket.binding.port-offset=100 \
 -Dkeycloak.migration.action=export \
 -Dkeycloak.migration.provider=singleFile \
 -Dkeycloak.migration.realmName=ivy-demo \
 -Dkeycloak.migration.usersExportStrategy=REALM_FILE \
 -Dkeycloak.migration.file=/shared/realm-ivydemo-import.json
