<?php
$response = array();
$response["success"] = 0;
if(isset($_POST['id'])){
	include 'db_con.php';
	$id=$_POST['id'];
	$sql=mysql_query("SELECT * from eyedisease where id = '$id'");
	if (mysql_num_rows($sql) > 0) {
		$response["eyedisease"] = array();
		while ($row = mysql_fetch_array($sql)) {
			$eyedisease = array();
			$eyedisease["eyedisease"] = $row["eyedisease"];
			$eyedisease["description"] = $row["description"];
			array_push($response["eyedisease"], $eyedisease);
		}
		$response["success"] = 1;
	}
	echo json_encode($response);
}
	
?>