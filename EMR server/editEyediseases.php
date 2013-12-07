<?php
if(isset($_POST['eyedisease']) && !empty($_POST['eyedisease']) && isset($_POST['id']) && !empty($_POST['id'])){
	$con = new mysqli("127.0.0.1", "root", "", "emrandroid");
	$id = $_POST['id'];
	$eyedisease = $_POST['eyedisease'];
	$description = $_POST['etdescription'];
	if($con->query("UPDATE `eyedisease` SET `eyedisease`='$eyedisease', `description`='$description' WHERE `id`=$id"))
	{
		echo 'success';
	}
	else{
		echo 'error';
	}
	$con->close();
}
else
	echo 'error';
?>
