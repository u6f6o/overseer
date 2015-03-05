(ns overseer.parse
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn file->lines [file-name]
  (with-open [rdr (io/reader file-name)]
    (reduce conj [] (line-seq rdr))))

(defn filter-empty [lines]
  (remove #( .startsWith % ";;;") lines))

(defn headers [line]
  (map keyword (str/split line #";")))

(defn parse-line [headers line]
  (zipmap headers (str/split line #";")))

(defn lines->sheet [coll]
  (let [head (headers (first coll))]
    (map (partial parse-line head) (rest coll))))

(defn parse-file [csv-file]
  (let [lines (file->lines csv-file)]
    (->> lines
         filter-empty
         lines->sheet)))










