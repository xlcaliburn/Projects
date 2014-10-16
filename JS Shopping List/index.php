<html>
<head>
    <meta name="viewport" content="width=device-width, user-scalable=no">
    <title>Grocery List</title>
    <link rel="stylesheet" href="style.css">
    

    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>
<body>
    <div class="wrap">
        <div class="title">
            Grocery List by Michael Wong
        </div>
        <div class="item-list">
            <ul>
                <?php
                    require("includes/dbconnect.php");
                    $query = mysql_query("SELECT * FROM transitlabs ORDER BY id ASC");
                    $numrows = mysql_num_rows($query);

                    if($numrows>0){
                        while( $row = mysql_fetch_assoc( $query ) ){

                            $item_id = $row['id'];
                            $item_name = $row['item'];
                            $item_checked = $row['checked'];
                            echo '<li>
                                    <span class="'.$item_id.'">'.$item_name.'</span>
                                    <img id="'.$item_id.'" class="delete-button" width="15px" src="https://cdn0.iconfinder.com/data/icons/huge-basic-icons-part-2/512/Cancel.png" />
                                    <img id="'.$item_id.'" class="check-button" width="15px" src="https://cdn2.iconfinder.com/data/icons/flaticons-stroke/16/checkmark-3-512.png" />
                                    
                                </li>';
                            if ($item_checked == 1) { 
                                echo "
                                    <script>
                                        $('.'+$item_id).toggleClass('stroked'); 
                                        console.log('norma1l');
                                    </script>";
                            }
                        }
                    }
                ?>
            </ul>
        </div>
        <form class="add-new-item" autocomplete="off">
            <input type="text" id="new-item" placeholder="Add a new item..." />
        </form>
    </div>
</body>


<script>
    add_item();
    delete_item();
    check_item();

    function add_item() {
        $('.add-new-item').submit(function(){
        var new_item = $('#new-item').val();
            if(new_item != ''){
                $.post('includes/add.php', { item: new_item }, function( data ) {
                    $('#new-item').val('');
                    $(data).appendTo('.item-list ul').hide().fadeIn();
                    delete_item();
                });
            }
            return false; // Ensure that the form does not submit twice
        });
    }

    function delete_item() {
        $('.delete-button').click(function(){
            var current_element = $(this);
            var item_id = $(this).attr('id');

            $.post('includes/delete.php', { id: item_id }, function() {
                current_element.parent().fadeOut("fast", function() { $(this).remove(); });
            }); 
        });
    }

    function check_item() {
        $(document).on('click', '.check-button', function() {
            var item_id = $(this).attr('id');

            $.post('includes/check.php', { id: item_id }, function() {
               $('.'+item_id).toggleClass('stroked');
               console.log("normal");
            });
            return false;
        });
    }
</script>
</html>