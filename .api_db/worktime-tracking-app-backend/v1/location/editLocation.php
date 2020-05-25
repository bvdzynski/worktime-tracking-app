<?php

require_once '../../src/DatabaseQueries.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    if (
        isset($_POST['locationId'],
        $_POST['name'],
        $_POST['coordinates']
        )
    ) {
        $db = new DatabaseQueries();

        $result = $db->editLocation(
            $_POST['locationId'],
            $_POST['name'],
            $_POST['coordinates']
        );
        if ($result === true) {
            $response = array('error' => false, 'message' => 'location edited successfully');
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
