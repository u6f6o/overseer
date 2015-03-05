(ns overseer.parse
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn filter-empty-lines [coll]
  (remove #(.startsWith % ";;;") coll))

(defn headers [line]
  (map keyword (str/split line #";")))

(defn parse-line [headers line]
  (zipmap headers (str/split line #";")))

(defn parse-lines [coll]
  (let [head (headers (first coll))]
    (map (partial parse-line head) (rest coll))))

(defn file->lines [file-name]
  (with-open [rdr (io/reader file-name)]
    (reduce conj [] (line-seq rdr))))

(defn parse-file [csv-file]
  (->> (file->lines csv-file)
       filter-empty-lines
       parse-lines))










