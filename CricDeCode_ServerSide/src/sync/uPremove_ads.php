<?php
include_once "conn.php";
include_once "AppConsts.php";
$id = $_POST ['id'];
$json_string = $_POST ['json'];
$json = str_replace ( "\\", "", $json_string );
$json_array = json_decode ( $json, true );
$order_id = $json_array ['orderId'];
$token = $json_array ['Token'];
$sign = $json_array ['Sign'];
$urlToSend = "https://accounts.google.com/o/oauth2/token";
$postdata = http_build_query ( array (
		'grant_type' => 'refresh_token',
		'client_id' => '899334271790-c2h5i9g18ann8b40hbv2pi497ubtu4ps.apps.googleusercontent.com',
		'client_secret' => 'qSaygF9EZZeeZM5rPGh6mKAM',
		'refresh_token' => '1/yBRC1qtHiAcnlGEEssoQe4ApgdA6zqu_1XPv4UI7LX8' 
) );
$opts = array (
		'http' => array (
				'method' => 'POST',
				'header' => 'Content-type: application/x-www-form-urlencoded',
				'content' => $postdata 
		) 
);
$context = stream_context_create ( $opts );
$result = file_get_contents ( $urlToSend, false, $context );
$rply = json_decode ( $result, true );
$url_to_send = "https://www.googleapis.com/androidpublisher/v1.1/applications/" . PACKAGE_NAME . "/inapp/" . PRODUCT_ID_ad_removal . "/purchases/" . $token . "?access_token=" . $rply ['access_token'];
$tuCurl = curl_init ();
curl_setopt ( $tuCurl, CURLOPT_URL, $url_to_send );
curl_setopt ( $tuCurl, CURLOPT_RETURNTRANSFER, 1 );
curl_setopt ( $tuCurl, CURLOPT_HTTPGET, true );
curl_setopt ( $tuCurl, CURLOPT_HTTPHEADER, array (
		'Content-Type: application/json' 
) );
curl_setopt ( $tuCurl, CURLOPT_SSL_VERIFYPEER, false );
curl_setopt ( $tuCurl, CURLOPT_SSL_VERIFYHOST, 2 );
$tuData = curl_exec ( $tuCurl );
echo "<br />".$tuData;
$reply = json_decode ( $tuData, true );
curl_close ( $tuCurl );
$purchase_time = $reply ['purchaseTime'];
$purchaseState = $reply ['purchaseState'];
$consumptionState = $reply ['consumptionState'];
if ($purchaseState == 0 && $consumptionState == 1) {
	$cnt = mysql_query ( "SELECT * FROM remove_ads WHERE order_id = '$order_id' AND is='$id'" );
	if (mysql_num_rows ( $cnt ) == 0) {
		mysql_query ( "INSERT INTO remove_ads VALUES('$id','$order_id','$token','$sign',$purchase_time)" );
	}
}
$ax = array (
		"status" => 1 
);
echo str_replace ( "\\", "", json_encode ( $ax ) );
?>