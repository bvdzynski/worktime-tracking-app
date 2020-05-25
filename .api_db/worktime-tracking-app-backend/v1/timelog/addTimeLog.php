<?php

require_once '../../src/DatabaseQueries.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    if (
        isset($_POST['timeLogId'],
        $_POST['status'],
        $_POST['coords'],
        $_POST['workTimeStart'],
        $_POST['workTime'])
    ) {
        $db = new DatabaseQueries();

        $result = $db->addTimeLog(
            $_POST['timeLogId'],
            $_POST['status'],
            $_POST['coords'],
            $_POST['workTimeStart'],
            $_POST['workTime']
        );
        if ($result === true) {
            $response = array('error' => false, 'message' => 'timelog added successfully');
        } else {
            $response = array('error' => true, 'message' => 'error occured');
        }
    } else {
        $response = array('error' => true, 'message' => 'required fields are missing');
    }
} else {
    $response = array('error' => true, 'message' => 'invalid request');
}

echo json_encode($response);
