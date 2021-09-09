(ns lakepend.request
	(:require [clojure.java.io :as io]
						[clj-http.client :as http]))

(def ten-rec
	["2017_01_01 00:02:14\t34.30\t30.50\t26.90\t74.20\t346.40\t11.00\t3.60"
	 "2017_01_01 00:08:29\t34.10\t30.50\t26.50\t73.60\t349.00\t12.00\t8.60"
	 "2017_01_01 00:14:45\t33.90\t30.60\t26.80\t75.00\t217.80\t12.00\t9.20"
	 "2017_01_01 00:21:00\t33.80\t30.60\t27.30\t76.60\t280.80\t12.00\t14.00"
	 "2017_01_01 00:27:16\t33.90\t30.60\t27.40\t77.00\t80.00\t17.00\t9.20"
	 "2017_01_01 00:33:31\t33.80\t30.60\t27.00\t76.00\t11.00\t17.00\t12.20"
	 "2017_01_01 00:39:46\t33.60\t30.60\t27.10\t76.60\t81.00\t15.00\t10.80"
	 "2017_01_01 00:46:01\t33.40\t30.60\t26.40\t75.20\t18.40\t15.00\t11.60"
	 "2017_01_01 00:52:16\t33.40\t30.50\t27.10\t77.40\t20.40\t15.00\t12.40"
	 "2017_01_01 00:58:32\t33.30\t30.50\t27.10\t77.80\t17.20\t15.00\t11.80"])

(defn build-url [year]
	(str "https://lpo.dt.navy.mil/data/DM/Environmental_Data_Deep_Moor_"
		year
		".txt"))

(defn fetch-for-year
	"Return a stream object"
	[year]
	(let [url (build-url year)]
		(try
			(http/get url {:insecure true :as :stream})
			(catch Exception _ nil))))

(defn fetch-seq-for-year
	"Return a lazy seq of string"
	[year]
	(some->> (fetch-for-year year)
		:body
		io/reader
		line-seq
		(drop 1))
	ten-rec)