(ns lakepend.db
	(:require [clojure.java.jdbc :as jdbc]
						[clojure.string :as str]))

(def db-conf {:classname "org.sqlite.JDBC"
							:subprotocol "sqlite"
							:subname "resources/database.db"})

(defn insert-weather-data
	"Input: a seq of map, each keyword correspond with each table column"
	[weather-data]
	(try
		(jdbc/insert-multi! db-conf :weather weather-data)
		(catch Exception e e)))

(defn find-last-datetime []
	(try
		(->
			(jdbc/query db-conf "SELECT recorded_at FROM weather ORDER BY recorded_at DESC LIMIT 1")
			first
			:recorded_at)
		(catch Exception e e)))

(def avg-median-query
	"SELECT AVG(wind_speed) as avg_wind_speed,
	AVG(air_temp) as avg_air_temp,
	AVG(baro) as avg_baro,
	MEDIAN(wind_speed) as med_wind_speed,
	MEDIAN(air_temp) as med_air_temp,
	AVG(baro) as med_baro
	FROM weather
	WHERE recorded_at >= ? AND recorded_at <= ?")

(defn find-data-in-range [start-ms end-ms]
	(try
		(first
			(jdbc/query db-conf [avg-median-query start-ms end-ms]))   ; Parameterized Query
		(catch Exception _ nil)))