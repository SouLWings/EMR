<?php
$response = array();
$response["success"] = 0;
if(isset($_POST['patient_ID'])){
	include 'db_con.php';
	$patient_ID=$_POST['patient_ID'];
	$sql=mysql_query("SELECT * from labtest where patient_ID = '$patient_ID'");
	if (mysql_num_rows($sql) > 0) {
		$response["labtest"] = array();
		while ($row = mysql_fetch_array($sql)) {
			$labtest = array();
			$labtest["id"] = $row["id"];
			$labtest["patient_ID"] = $row["patient_ID"];
			$labtest["time"] = $row["time"];
			$labtest["filename"] = $row["filename"];
			$labtest["filedirc"] = $row["filedirc"];
			$labtest["filetype"] = $row["filetype"];
			array_push($response["labtest"], $labtest);
		}
		$response["success"] = 1;
	}
	echo json_encode($response);
}
	
?>