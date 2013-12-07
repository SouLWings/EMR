<?php
$con = new mysqli("127.0.0.1", "root", "", "emrandroid");
if(isset($_POST['leye1']))
{
	$leye1 = $_POST['leye1'];
	$reye1 = $_POST['reye1'];	
	$bino = $_POST['bino'];	
	$l1 = $_POST['l1'];	
	$l2 = $_POST['l2'];	
	$l3 = $_POST['l3'];	
	$l4 = $_POST['l4'];	
	$r1 = $_POST['r1'];	
	$r2 = $_POST['r2'];	
	$r3 = $_POST['r3'];	
	$r4 = $_POST['r4'];	
	$b1 = $_POST['b1'];	
	$b2 = $_POST['b2'];	
	$charttype = $_POST['charttype'];	
	$colorblindness = $_POST['colorblindness'];	
	$slitlamp = $_POST['slitlamp'];	
	$doctor_ID = $_POST['doctor_ID'];
	$patient_ID = $_POST['patient_ID'];
	if($result = $con->query("INSERT INTO eyetest VALUES(null,$doctor_ID,$patient_ID,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'$leye1','$reye1','$bino','$charttype','$l1','$l2','$l3','$l4','$r1','$r2','$r3','$r4','$b1','$b2','$slitlamp','$colorblindness')"))
	{
		if($result > 0)
		{
			echo $result;
		}
		else{
			echo 'error';
		}
	}
	else{
		echo 'eInsert query failed';
	}
}
else if(isset($_POST['labtest']))
{
	$labtest = $_POST['labtest'];
	$appointmentdate = $_POST['appointmentdate'];
	$surgery = $_POST['surgery'];
	$medcine = $_POST['medcine'];
	$doctor_ID = $_POST['doctor_ID'];
	$patient_ID = $_POST['patient_ID'];
	if($result = $con->query("INSERT INTO patient_orders VALUES(null,$patient_ID,$doctor_ID,CURRENT_TIMESTAMP,null,
	'$labtest','$surgery','$medcine','$appointmentdate')"))
	{
		if($result == 1)
		{
			echo $result;
		}
		else{
			echo 'error';
		}
	}
	else{
		echo 'eInsert query failed';
	}
}
else
	echo 'ePost variable not match';
$con->close();
?>
