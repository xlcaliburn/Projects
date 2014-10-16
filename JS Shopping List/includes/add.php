<?php 
    $additem = strip_tags( $_POST['item'] );
    require("dbconnect.php");

    mysql_query("INSERT INTO transitlabs (item,checked) VALUES ('$additem','0')");

    $query = mysql_query("SELECT * FROM transitlabs ORDER BY id DESC");

    $row = mysql_fetch_assoc($query);
    $item_id = $row['id'];
    $item_name=$row['item'];
    
    mysql_close();

   echo '<li>
        <span class="'.$item_id.'">'.$item_name.'</span>
        <img id="'.$item_id.'" class="delete-button" width="15px" src="https://cdn0.iconfinder.com/data/icons/huge-basic-icons-part-2/512/Cancel.png" />
        <img id="'.$item_id.'" class="check-button" width="15px" src="https://cdn2.iconfinder.com/data/icons/flaticons-stroke/16/checkmark-3-512.png" />
    </li>';
?>