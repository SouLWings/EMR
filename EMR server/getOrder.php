<?php
$response = array();
$response["success"] = 0;
if(isset($_POST['id'])){
	include 'db_con.php';
	$id=$_POST['id'];
	$sql=mysql_query("SELECT * from patient_orders where id = '$id'");
	if (mysql_num_rows($sql) > 0) {
		$response["order"] = array();
		while ($row = mysql_fetch_array($sql)) {
			$order = array();
			$order["appointmentdate"] = $row["appointmentdate"];
			$order["labtest"] = $row["labtest"];
			$order["surgery"] = $row["surgery"];
			$order["medcine"] = $row["medcine"];
			array_push($response["order"], $order);
		}
		$response["success"] = 1;
	}
	echo json_encode($response);
}
	
?>