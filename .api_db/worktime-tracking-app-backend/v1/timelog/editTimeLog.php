<?php

require_once '../../src/DatabaseQueries.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $response = array();

    //edit timeLog
    if (
        isset($_POST['timeLogId'],
        $_POST['title'],
        $_POST['projectId'],
        $_POST['description'],
        $_POST['locationId'])
    ) {
        $db = new DatabaseQueries();

        $result = $db->editTimeLog(
            $_POST['timeLogId'],
            $_POST['title'],
            $_POST['projectId'],
            $_POST['description'],
            $_POST['locationId']
        );
        if ($result === true) {
            $response['editingTimeLogError'] = false;
            $response['editingTimeLogMessage'] = 'timelog edited successfully';
        } else {
            $response['editingTimeLogError'] = true;
            $response['editingTimeLogMessage'] = 'error occured';
        }
    } else {
        $response['editingTimeLogError'] = true;
        $response['editingTimeLogMessage'] = 'required fields are missing';
    }

    //upload timeLog image
    if (
        isset($_POST['timeLogId'],
        $_POST['encodedImage'])
    ) {
        $db = new DatabaseQueries();
        
        $decodedImage = base64_decode($_POST['encodedImage']);

        $result = $db->uploadTimeLogImage(
            $_POST['timeLogId'],
            $decodedImage
        );
        if ($result === true) {
            $response['uploadingTimeLogError'] = false;
            $response['uploadingTimeLogMessage'] = 'timelog image uploading successfully';
        } else {
            $response['uploadingTimeLogError'] = true;
            $response['uploadingTimeLogMessage'] = 'error occured';
        }
    } else {
        $response['uploadingTimeLogError'] = true;
        $response['uploadingTimeLogMessage'] = 'required fields are missing';
    }
} else {
    $response = array('error' => true, 'message' => 'invalid request');
}

echo json_encode($response);
