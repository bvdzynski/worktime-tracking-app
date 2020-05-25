<?php

require_once '../../src/DatabaseQueries.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    if (
        isset($_POST['projectId'])
    ) {
        $db = new DatabaseQueries();

        $result = $db->addProject($_POST['projectId']);
        if ($result === true) {
            $response = array('error' => false, 'message' => 'project added successfully');
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
