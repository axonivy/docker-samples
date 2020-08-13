<?php
header('Content-Type: text/plain');
?>
List of OIDC_* environment variables:
=======================================

<?php
foreach($_SERVER as $key=>$value) {
  if(substr($key, 0, 5) == 'OIDC_') {
    echo($key . '=' . $value . "\r\n");
  }
}
?>