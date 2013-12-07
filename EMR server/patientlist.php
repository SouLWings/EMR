<?php
if(isset($_POST['search'])){
	include 'db_con.php';
	$srch=$_POST['search'];
	$sql=mysql_query("select ID, Name from patient where LOWER(Name) like LOWER('%$srch%') or ID = '$srch'");
	$response = array();
	if (mysql_num_rows($sql) > 0) {
		$response["patient"] = array();
		while ($row = mysql_fetch_array($sql)) {
			$patient = array();
			$patient["ID"] = $row["ID"];
			$patient["Name"] = $row["Name"];
			array_push($response["patient"], $patient);
		}
		$response["success"] = 1;
	} else {
		$response["success"] = 0;
	}
	echo json_encode($response);
}
?>