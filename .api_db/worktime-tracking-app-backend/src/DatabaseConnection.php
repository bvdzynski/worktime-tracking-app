<?php

class DatabaseConnection
{
    private $conn;

    public function __construct()
    {
    }

    public function connect()
    {
        include_once dirname(__FILE__) . '/config.php';
        $dsn = "mysql:host=" . DB_HOST . ";dbname=" . DB_NAME . ";charset=utf8mb4;port=3306";
        try {
            $this->conn = new PDO($dsn, DB_USER, DB_PASSWORD);
        } catch (\PDOException $e) {
            throw new \PDOException($e->getMessage(), (int) $e->getCode());
        }

        return $this->conn;
    }
}
