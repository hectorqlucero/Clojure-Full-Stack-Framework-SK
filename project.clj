(defproject sk "0.1.0"
  :description "Change me"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [compojure "1.6.2" :exclusions [commons-codec]]
                 [hiccup "1.0.5"]
                 [lib-noir "0.9.9"]
                 [com.draines/postal "2.0.3"]
                 [cheshire "5.10.0"]
                 [clj-pdf "2.4.0" :exclusions [commons-codec]]
                 [ondrs/barcode "0.1.0"]
                 [pdfkit-clj "0.1.7" :exclusions [commons-logging commons-codec]]
                 [clj-jwt "0.1.1"]
                 [clj-time "0.15.2"]
                 [date-clj "1.0.1"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [org.clojure/data.codec "0.1.1"]
                 [mysql/mysql-connector-java "8.0.21"]
                 [ring/ring-core "1.8.1" :exclusions [ring/ring-codec commons-logging commons-codec]]]
  :main ^:skip-aot sk.core
  :aot [sk.core]
  :plugins [[lein-ancient "0.6.10"]
            [lein-pprint "1.1.2"]]
  :uberjar-name "sk.jar"
  :target-path "target/%s"
  :ring {:handler sk.core/app
         :auto-reload? true
         :auto-refresh? false}
  :resources-paths ["shared" "resources"]
  :profiles {:uberjar {:aot :all}})
