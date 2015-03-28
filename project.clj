(defproject overseer "0.1.0-SNAPSHOT"
  :description "Learn clojure by creating simple bank statements parser and analyzer"
  :url "https://github.com/u6f6o/overseer"

  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.csv "0.1.2"]
                 [org.clojure/data.json "0.2.6"]]

  :main ^:skip-aot overseer.core
  :profiles {:dev {:dependencies [[speclj "3.2.0"]]}
             :uberjar {:aot :all}}

  :plugins [[speclj "3.2.0"]]
  :test-paths ["spec"]
  :target-path "target/%s"

)
