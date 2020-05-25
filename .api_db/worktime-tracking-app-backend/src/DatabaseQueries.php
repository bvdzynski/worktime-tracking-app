<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

class DatabaseQueries
{
    private $db;

    public function __construct()
    {
        include_once dirname(__FILE__) . '/DatabaseConnection.php';
        $db = new DatabaseConnection();
        $this->db = $db->connect();
    }

    //timelogs 
    function addTimeLog($timeLogId, $status, $coords, $workTimeStart, $workTime)
    {
        $data = array(
            'timeLogId' => $timeLogId,
            'status' => $status,
            'coords' => $coords,
            'workTimeStart' => $workTimeStart,
            'workTime' => $workTime
        );
        $query = $this->db->prepare("INSERT INTO `wt_timelogs`(`timeLogId`, `status`, `coords`, `workTimeStart`, `workTime`) VALUES (:timeLogId, :status, :coords, :workTimeStart, :workTime)");
        return $query->execute($data);
    }

    function editTimeLog($timeLogId, $title, $projectId, $description, $locationId)
    {
        $data = array(
            'timeLogId' => $timeLogId,
            'title' => $title,
            'projectId' => $projectId,
            'description' => $description,
            'locationId' => $locationId
        );

        $query = $this->db->prepare("UPDATE `wt_timelogs` SET `title`=:title, `projectId`=:projectId, `description`=:description, `locationId`=:locationId WHERE `timeLogId`=:timeLogId");

        if ($query->execute($data)) {
            return true;
        } else {
            return false;
        }
    }

    function uploadTimeLogImage($timeLogId, $decodedImage)
    {
        $data = array(
            'timeLogId' => $timeLogId,
            'decodedImage' => $decodedImage
        );

        $query = $this->db->prepare("UPDATE `wt_timelogs` SET `image`=:decodedImage WHERE `timeLogId`=:timeLogId");

        if ($query->execute($data)) {
            return true;
        } else {
            return false;
        }
    }

    function closeTimeLog($timeLogId, $status, $workTimeStop, $workTime)
    {
        $data = array(
            'timeLogId' => $timeLogId,
            'status' => $status,
            'workTimeStop' => $workTimeStop,
            'workTime' => $workTime
        );

        $query = $this->db->prepare("UPDATE `wt_timelogs` SET `status`=:status, `workTimeStop`=:workTimeStop, `workTime`=:workTime WHERE `timeLogId`=:timeLogId");

        if ($query->execute($data)) {
            return true;
        } else {
            return false;
        }
    }

    function deleteTimeLog($timeLogId)
    {
        $data = array(
            'timeLogId' => $timeLogId
        );

        $query = $this->db->prepare("DELETE FROM `wt_timelogs` WHERE `timeLogId`=:timeLogId");

        if ($query->execute($data)) {
            return true;
        } else {
            return false;
        }
    }

    function getTimeLogs()
    {
        $timeLogs = $this->db->query("SELECT * FROM wt_timelogs")->fetchAll();
        $return = array();
        foreach ($timeLogs as $timeLog) {

            $encodedImage = base64_encode($timeLog["image"]);

            array_push($return, array(
                "timeLogId" => $timeLog["timeLogId"],
                "title" => $timeLog["title"],
                "projectId" => $timeLog["projectId"],
                "status" => $timeLog["status"],
                "description" => $timeLog["description"],
                "locationId" => $timeLog["locationId"],
                "coords" => $timeLog["coords"],
                "workTimeStart" => $timeLog["workTimeStart"],
                "workTimeStop" => $timeLog["workTimeStop"],
                "workTime" => $timeLog["workTime"],
                "encodedImage" => $encodedImage,
            ));
        }

        return $return;
    }

    //projects
    function addProject($projectId)
    {
        $data = array(
            'projectId' => $projectId
        );
        $query = $this->db->prepare("INSERT INTO `wt_projects` (`projectId`) VALUES (:projectId)");
        return $query->execute($data);
    }

    function editProject($projectId, $name, $assignedToDeveloper)
    {
        $data = array(
            'projectId' => $projectId,
            'name' => $name,
            'assignedToDeveloper' => $assignedToDeveloper,
        );

        $query = $this->db->prepare("UPDATE `wt_projects` SET `name`=:name, `assignedToDeveloper`=:assignedToDeveloper WHERE `projectId`=:projectId");

        if ($query->execute($data)) {
            return true;
        } else {
            return false;
        }
    }

    function deleteProject($projectId)
    {
        $data = array(
            'projectId' => $projectId
        );

        $query = $this->db->prepare("DELETE FROM `wt_projects` WHERE `projectId`=:projectId");

        if ($query->execute($data)) {
            return true;
        } else {
            return false;
        }
    }

    function getProjects()
    {
        $projects = $this->db->query("SELECT * FROM wt_projects")->fetchAll();
        $return = array();
        foreach ($projects as $project) {
            array_push($return, array(
                "projectId" => $project["projectId"],
                "name" => $project["name"],
                "assignedToDeveloper" => $project["assignedToDeveloper"]
            ));
        }

        return $return;
    }

    //locations
    function addLocation($locationId)
    {
        $data = array(
            'locationId' => $locationId
        );
        $query = $this->db->prepare("INSERT INTO `wt_locations` (`locationId`) VALUES (:locationId)");
        return $query->execute($data);
    }

    function editLocation($locationId, $name, $coordinates)
    {
        $data = array(
            'locationId' => $locationId,
            'name' => $name,
            'coordinates' => $coordinates,
        );

        $query = $this->db->prepare("UPDATE `wt_locations` SET `name`=:name, `coordinates`=:coordinates WHERE `locationId`=:locationId");

        if ($query->execute($data)) {
            return true;
        } else {
            return false;
        }
    }

    function deleteLocation($locationId)
    {
        $data = array(
            'locationId' => $locationId
        );

        $query = $this->db->prepare("DELETE FROM `wt_locations` WHERE `locationId`=:locationId");

        if ($query->execute($data)) {
            return true;
        } else {
            return false;
        }
    }

    function getLocations()
    {
        $locations = $this->db->query("SELECT * FROM wt_locations")->fetchAll();
        $return = array();
        foreach ($locations as $location) {
            array_push($return, array(
                "locationId" => $location["locationId"],
                "name" => $location["name"],
                "coordinates" => $location["coordinates"]
            ));
        }

        return $return;
    }
}
