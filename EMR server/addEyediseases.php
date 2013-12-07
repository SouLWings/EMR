<?php
if(isset($_POST['eyedisease']) && !empty($_POST['eyedisease'])){
	$con = new mysqli("127.0.0.1", "root", "", "emrandroid");
	$doctor_ID = $_POST['doctor_ID'];
	$patient_ID = $_POST['patient_ID'];
	$eyedisease = $_POST['eyedisease'];
	$description = $_POST['etdescription'];
	if($con->query("INSERT INTO eyedisease VALUES(null, $doctor_ID, $patient_ID, CURRENT_TIMESTAMP, null, '$eyedisease','$description')"))
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
