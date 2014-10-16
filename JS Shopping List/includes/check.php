<?php 
    $item_id = strip_tags( $_POST['id'] );
    require("dbconnect.php");

    $result = mysql_query("SELECT * FROM transitlabs WHERE id = $item_id");
    $row = mysql_fetch_assoc($result);
    $checked = $row['checked'];
    if ($checked == 0) {
        mysql_query("UPDATE transitlabs SET checked = 1 WHERE id = $item_id");
    }
    else {
        mysql_query("UPDATE transitlabs SET checked = 0 WHERE id = $item_id");       
    }


    mysql_close();
?>