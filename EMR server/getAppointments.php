<?php
$response = array();
$response["success"] = 0;
if(isset($_POST['patient_ID'])){
	include 'db_con.php';
	$patient_ID=$_POST['patient_ID'];
	$response["appointments"] = array();
	$eyetest_resultset=mysql_query("SELECT * from eyetest where patient_ID = '$patient_ID' order by id desc");
	$eyedisease_resultset=mysql_query("SELECT * from eyedisease where patient_ID = '$patient_ID'");
	$order_resultset=mysql_query("SELECT * from patient_orders where patient_ID = '$patient_ID'");
	$unique_date = array();
	if (mysql_num_rows($eyetest_resultset) > 0) {
		while ($row = mysql_fetch_array($eyetest_resultset)) {
			if(!in_array(substr($row['time_created'],0,10),$unique_date))
				$unique_date[]=substr($row['time_created'],0,10);
		}
	}
	if (mysql_num_rows($eyedisease_resultset) > 0) {
		while ($row = mysql_fetch_array($eyedisease_resultset)) {
			if(!in_array(substr($row['timecreated'],0,10),$unique_date))
				$unique_date[]=substr($row['timecreated'],0,10);
		}
	}
	if (mysql_num_rows($order_resultset) > 0) {
		while ($row = mysql_fetch_array($order_resultset)) {
			if(!in_array(substr($row['time_created'],0,10),$unique_date))
				$unique_date[]=substr($row['time_created'],0,10);
		}
	}
	rsort($unique_date);

	$appointments = array();
	
	foreach($unique_date as $date):
		$appointment['date'] = $date;
		array_push($response["appointments"], $appointment);
	endforeach;
	
	
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
