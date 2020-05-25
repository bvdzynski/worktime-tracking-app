<?php

require_once '../../src/DatabaseQueries.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    $db = new DatabaseQueries();
    $response = $db->getProjects();
    if ($response == false) {
        $response = array('error' => true, 'message' => 'error occured');
    }
} else {
    $response = array('error' => true, 'message' => 'invalid request');
}

echo json_encode($response);
