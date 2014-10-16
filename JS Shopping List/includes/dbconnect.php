<?php
    $server = "xlcaliburncom.ipagemysql.com";
    $db_user = "abc";
    $db_pass = "abc123";
    $db_name = "testdb";

    mysql_connect($server, $db_user, $db_pass) or die("Could not connect to server!");
    mysql_select_db($db_name) or die("Could not connect to database!");
?>