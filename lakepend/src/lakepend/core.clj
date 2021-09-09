(ns lakepend.core
  (:require [lakepend.backend :as backend]))


(def usage-string
  "Usage: Must supply two dates in MMM/dd/yyyy format (such as Jan/01/2012) or --sync")

(defn handle-query [star-str end-str]
  (let [invalid? (some nil? (map backend/cal->ms [star-str end-str]))]
    (if invalid?
      (println usage-string)
      (backend/display-query-result star-str end-str))))

(defn -main [& args]
  (condp = (count args)
    0 (println usage-string)
    1 (if (= (first args) "--sync")
        (backend/sync!)
        (println usage-string))
    2 (apply handle-query args)
    (println usage-string)))
