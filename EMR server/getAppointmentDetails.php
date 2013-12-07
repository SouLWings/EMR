<?php
$response = array();
$response["success"] = 0;
if(isset($_POST['patient_ID']) && isset($_POST['date'])){
	include 'db_con.php';
	$patient_ID=$_POST['patient_ID'];
	$date=$_POST['date'];
	$response["eyeexam"] = array();
	$response["eyedisease"] = array();
	$response["orders"] = array();
	$eyetest_resultset=mysql_query("SELECT * from eyetest where patient_ID = '$patient_ID' AND time_created LIKE '$date%' order by time_created desc");
	$eyedisease_resultset=mysql_query("SELECT * from eyedisease where patient_ID = '$patient_ID' AND timecreated LIKE '$date%'  order by timecreated desc");
	$order_resultset=mysql_query("SELECT * from patient_orders where patient_ID = '$patient_ID' AND time_created LIKE '$date%'  order by time_created desc");

	$appointments = array();

	if (mysql_num_rows($eyetest_resultset) > 0) {
		while ($row = mysql_fetch_array($eyetest_resultset)) {
			$jaeger = '';
			if($row['type_of_chart'] == 'Jaeger chart')
			{
				$jaeger = 'J';
			}
			$eyeexam['data'] = "Time: ".$row['time_created']."\nChart used: ".$row['type_of_chart']."\nLeft Eye: $jaeger".$row['leye1']."\nRight Eye: $jaeger".$row['reye1']."\nBinocular: $jaeger".$row['bino']."\nLeft log contrast Sensivity: ".$row['l1']."\nBinocular log contrast Sensivity: ".$row['b1']."\nRight log contrast Sensivity: ".$row['r1']."\nLeft Acuity: ".$row['l2']."\nBinocular Acuity: ".$row['b2']."\nRight Acuity: ".$row['r2']."\nLeft Correction: ".$row['l3']."\nRight Correction: ".$row['r3']."\nLeft Pupil Diameter: ".$row['l4']."mm\nRight Pupil Diameter: ".$row['r4']."mm \nColor Blindness: ".$row['colorblindness']."\nSlitlamp Description: ".$row['slitlamp'];
			$eyeexam['id'] = $row['id'];
			array_push($response["eyeexam"], $eyeexam);
		}
	}
	else{
		array_push($response["eyeexam"], "No data");
	}
	if (mysql_num_rows($eyedisease_resultset) > 0) {
		while ($row = mysql_fetch_array($eyedisease_resultset)) {
			$eyedisease['data'] = "Time: ".$row['timecreated']."\nEye diseases: ".$row['eyedisease']." \nDescription: ".$row['description'];
			$eyedisease['id'] = $row['id'];
			array_push($response["eyedisease"], $eyedisease);
		}
	}
	else{
		array_push($response["eyedisease"], "No data");
	}
	if (mysql_num_rows($order_resultset) > 0) {
		while ($row = mysql_fetch_array($order_resultset)) {
			$order['data'] = "Time: ".$row['time_created']."\nLabtest: ".$row["labtest"]."\nSurgery: ".$row["surgery"]."\nMedcine: ".$row["medcine"]."\nNext appointment date: ".$row["appointmentdate"];
			$order['id'] = $row['id'];
			array_push($response["orders"], $order);
		}
	}
	else{
		array_push($response["orders"], "No data");
	}
	
	
	$response["success"] = 1;
	
	echo json_encode($response);
}
/*	
appointment 
	- date
	- string labtest
	- string surgery
	- string order*/
?>
