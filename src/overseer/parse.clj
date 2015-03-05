(ns overseer.parse
  (:require [clojure.string :as str]))

(defn filter-empty-lines [coll]
  (remove #(.startsWith % ";;;") coll))

(defn headers [line]
  (map keyword (str/split line #";")))

(defn parse-line [headers line]
  (zipmap headers (str/split line #";")))

(defn parse-lines [coll]
  (let [head (headers (first coll))]
    (map (partial parse-line head) (rest coll))))

(defn parse-file [csv-file]
  (->> csv-file
       line-seq
       filter-empty-lines
       parse-lines))










