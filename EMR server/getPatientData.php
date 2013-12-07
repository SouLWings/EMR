<?php
$response = array();
if(isset($_POST['patient_ID']) && !empty($_POST['patient_ID'])){
	include 'db_con.php';
	$patient_ID=$_POST['patient_ID'];
	$sql=mysql_query("select * from patient where ID = '$patient_ID'");
	if (mysql_num_rows($sql) > 0) {
		$response["patientData"] = array();
		while ($row = mysql_fetch_array($sql)) {
			$patient = array();
			$patient["ID"] = $row["ID"];
			$patient["IC_no"] = $row["IC_no"];
			$patient["Name"] = $row["Name"];
			$patient["Gender"] = $row["Gender"];
			$patient["Age"] = $row["Age"];
			$patient["Address"] = $row["Address"];
			$patient["DoB"] = $row["dob"];
			$patient["Phone"] = $row["Phone"];
			array_push($response["patientData"], $patient);
		}
		$response["success"] = 1;
	} else {
		$response["success"] = 0;
	}
	echo json_encode($response);
}
else
{
	$response["success"] = 0;
	echo json_encode($response);
}
?>