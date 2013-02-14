<?php
$username = $_REQUEST['username'];
$password = $_REQUEST['password'];

// T_data_n,  T_time_n, T_data_time

$regdate_mod = $_REQUEST['regdate_mod'];
$regtime_mod = $_REQUEST['regtime_mod'];
$regdatetime_mod = $_REQUEST['regdatetime_mod'];

// T_ceh, T_uch_sozd, T_agr, dposition - D_n_pos

$zeh = $_REQUEST['zeh'];
$sector = $_REQUEST['sector'];
$aggr = $_REQUEST['aggr'];
$posit = $_REQUEST['posit'];

// Смена ?
$smena = $_REQUEST['smena'];

// T_prich_def, T_pKateg_def, T_ppKateg_def

$part = $_REQUEST['part'];
$equip = $_REQUEST['equip'];
$cause = $_REQUEST['cause'];
$descr = $_REQUEST['descr'];

mysql_connect('localhost', $username, $password);
mysql_select_db("udp");

// cyrillic

mysql_query("SET @@local.character_set_client=utf8;");
mysql_query("SET @@local.character_set_results=utf8;");
mysql_query("SET @@local.character_set_connection=utf8;");
mysql_query("SET @@local.character_set_results=utf8;");

//mysql_query("LOCK TABLES test_dmaster WRITE");

mysql_query("INSERT INTO test_dmaster(T_data_n, T_time_n, T_data_time, T_ceh, T_uch_sozd, T_agr, T_smena_sozd, T_prich_def, T_pKateg_def, T_ppKateg_def, T_opis_def) select '$regdate_mod', '$regtime_mod', '$regdatetime_mod', $zeh, $sector, $aggr, null, $part, $equip, $cause, '$descr';");

$id = mysql_insert_id(); 

mysql_query("insert into test_position(T_id_k, T_n_pos) values($id, $posit);");

//mysql_query("UNLOCK TABLES");


//$id = mysql_insert_id(); 



//$r=mysql_query($sql);
//if(!$r)echo "Error in query: ".mysql_error();mysql_close();
?>
