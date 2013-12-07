<?php
if(isset($_POST['slitlamp']) && isset($_POST['id']) && !empty($_POST['id'])){
	$con = new mysqli("127.0.0.1", "root", "", "emrandroid");
	$id = $_POST['id'];
	$b1 = $_POST['b1'];
	$b2 = $_POST['b2'];
	$r1 = $_POST['r1'];
	$r2 = $_POST['r2'];
	$r3 = $_POST['r3'];
	$r4 = $_POST['r4'];
	$l1 = $_POST['l1'];
	$l2 = $_POST['l2'];
	$l3 = $_POST['l3'];
	$l4 = $_POST['l4'];
	$charttype = $_POST['charttype'];
	$colorblindness = $_POST['colorblindness'];
	$slitlamp = $_POST['slitlamp'];
	$reye1 = $_POST['reye1'];
	$leye1 = $_POST['leye1'];
	$bino = $_POST['bino'];
	if($con->query("UPDATE `eyetest` SET `leye1`='$leye1',`reye1`='$reye1' ,`bino`='$bino' ,`type_of_chart`='$charttype' ,`l1`='$l1',`l2`='$l2',`l3`='$l3',`l4`='$l4',`r1`='$r1',`r2`='$r2',`r3`='$r3',`r4`='$r4',`b1`='$b1',`b2`='$b2',`colorblindness`='$colorblindness',`slitlamp`='$slitlamp' WHERE `id`=$id"))
	{
		echo 'success';
	}
	else{
		echo 'error1';
	}
	$con->close();
}
else
	echo 'error2';
?>