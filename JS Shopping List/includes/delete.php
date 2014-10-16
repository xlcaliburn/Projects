<?php 
    $item_id = strip_tags( $_POST['id'] );
    require("dbconnect.php");
    mysql_query("DELETE FROM transitlabs WHERE id='$item_id'");
?>