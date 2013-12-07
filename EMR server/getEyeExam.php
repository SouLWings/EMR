<?php
$response = array();
$response["success"] = 0;
if(isset($_POST['id'])){
	include 'db_con.php';
	$id=$_POST['id'];
	$sql=mysql_query("SELECT * FROM `eyetest` WHERE id = '$id'");
	if (mysql_num_rows($sql) > 0) {
		$response["eyeexam"] = array();
		while ($row = mysql_fetch_array($sql)) {
			$eyeexam = array();
			$eyeexam["b1"] = $row["b1"];
			$eyeexam["b2"] = $row["b2"];
			$eyeexam["r1"] = $row["r1"];
			$eyeexam["r2"] = $row["r2"];
			$eyeexam["r3"] = $row["r3"];
			$eyeexam["r4"] = $row["r4"];
			$eyeexam["l1"] = $row["l1"];
			$eyeexam["l2"] = $row["l2"];
			$eyeexam["l3"] = $row["l3"];
			$eyeexam["l4"] = $row["l4"];
			$eyeexam["leye1"] = $row["leye1"];
			$eyeexam["reye1"] = $row["reye1"];
			$eyeexam["bino"] = $row["bino"];
			$eyeexam["type_of_chart"] = $row["type_of_chart"];
			$eyeexam["colorblindness"] = $row["colorblindness"];
			$eyeexam["slitlamp"] = $row["slitlamp"];
			array_push($response["eyeexam"], $eyeexam);
		}
		$response["success"] = 1;
	}
	echo json_encode($response);
}
	
?>
