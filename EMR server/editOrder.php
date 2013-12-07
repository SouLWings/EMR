<?php
if(isset($_POST['labtest']) && !empty($_POST['labtest']) && isset($_POST['id']) && !empty($_POST['id'])){
	$con = new mysqli("127.0.0.1", "root", "", "emrandroid");
	$id = $_POST['id'];
	$appointmentdate = $_POST['appointmentdate'];
	$labtest = $_POST['labtest'];
	$surgery = $_POST['surgery'];
	$medcine = $_POST['medcine'];
	if($con->query("UPDATE `patient_orders` SET `labtest`='$labtest', `surgery`='$surgery', `medcine`='$medcine', `appointmentdate`='$appointmentdate' WHERE `id` = $id"))
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
