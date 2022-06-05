(defproject sk "0.1.0"
  :description "Sitio"
  :url "https://github.com/hectorqlucero/sk"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.0"]
                 [compojure "1.6.2" :exclusions [commons-codec]]
                 [hiccup "1.0.5"]
                 [lib-noir "0.9.9"]
                 [com.draines/postal "2.0.5"]
                 [cheshire "5.10.2"]
                 [clj-pdf "2.5.8" :exclusions [commons-codec]]
                 [ondrs/barcode "0.1.0"]
                 [pdfkit-clj "0.1.7" :exclusions [commons-logging commons-codec]]
                 [cljfmt "0.8.0"]
                 [clj-jwt "0.1.1"]
                 [clj-time "0.15.2"]
                 [date-clj "1.0.1"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [org.clojure/data.codec "0.1.1"]
                 [mysql/mysql-connector-java "8.0.28"]
                 [ragtime "0.8.1"]
                 [ring/ring-core "1.9.5" :exclusions [ring/ring-codec commons-logging commons-codec]]]
  :main ^:skip-aot sk.core
  :aot [sk.core]
  :plugins [[lein-ancient "0.7.0"]
            [lein-pprint "1.3.2"]]
  :uberjar-name "sk.jar"
  :target-path "target/%s"
  :ring {:handler sk.core
         :auto-reload? true
         :auto-refresh? false}
  :resources-paths ["shared" "resources"]
  :aliases {"migrate" ["run" "-m" "sk.user/migrate"]
            "rollback" ["run" "-m" "sk.user/rollback"]}
  :profiles {:uberjar {:aot :all}})
