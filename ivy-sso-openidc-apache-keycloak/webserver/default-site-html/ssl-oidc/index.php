<html>
<head>
	<title>Demo Webserver Home</title>
</head>
<body>
<h1>Demo Webserver Home for OAuth/OIDC secured VHOST</h1>

<ul>
	<li><a href="/ivy/">Axon.ivy Engine Home</a></li>
	<li><a href="/ivy/demo-portal">Axon.ivy Portal Home</a></li>
	<li><a href="oidc-env-dump.php">Dump all OIDC_ Environment Variables</a></li>
	<li><a href="https://jwt.ms/#id_token=<?= urlencode( $_SERVER['OIDC_access_token'] ) ?>">Show access token at jwt.ms</a></li>
	
</ul>	
</body>