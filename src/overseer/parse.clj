(ns overseer.parse
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn file->lines [file-name]
  (with-open [rdr (io/reader file-name)]
    (reduce conj [] (line-seq rdr))))

(defn filter-empty [lines]
  (remove #( .startsWith % ";;;") lines))

(defn extract-header [line]
  (map keyword (str/split line #";")))

(defn extract-content [header line]
  (zipmap header (str/split line #";")))

(defn lines->sheet [coll]
  (let [header (extract-header (first coll))]
    (map (partial extract-content header) (rest coll))))

(defn parse-file [csv-file]
  (let [lines (file->lines csv-file)]
    (->> lines
         filter-empty
         lines->sheet)))










