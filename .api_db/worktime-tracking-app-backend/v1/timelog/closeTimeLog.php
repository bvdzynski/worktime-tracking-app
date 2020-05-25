<?php

require_once '../../src/DatabaseQueries.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    if (
        isset($_POST['timeLogId'],
        $_POST['status'],
        $_POST['workTimeStop'],
        $_POST['workTime']
            // $_POST['imageUrl'],
        )
    ) {
        $db = new DatabaseQueries();

        $result = $db->closeTimeLog(
            $_POST['timeLogId'],
            $_POST['status'],
            $_POST['workTimeStop'],
            $_POST['workTime']
        );
        if ($result === true) {
            $response = array('error' => false, 'message' => 'timelog closed successfully');
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

// params["status"] = chosenTimeLog.status
//                 params["workTimeStop"] = chosenTimeLog.workTimeStop.toString()
//                 params["workTime"] = chosenTimeLog.workTime.toString()
