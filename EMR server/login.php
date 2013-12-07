<?php
if(isset($_POST['username']) && isset($_POST['password'])){
	$con = new mysqli("127.0.0.1", "root", "", "emrandroid");
	$un = $_POST['username'];
	$pw = $_POST['password'];
	if($result = $con->query("select id from user where username = '$un' and password = '$pw'"))
	{
		if($result->num_rows == 1)
		{
			$row = $result->fetch_assoc();
			echo $row['id'];
		}
		else{
			echo 'error';
		}
		$result->free();
	}
	else{
		echo 'error';
	}
	$con->close();
}
else
	echo 'error';
?>
