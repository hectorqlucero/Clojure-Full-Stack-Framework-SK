(ns sk.models.builder
  (:require [sk.models.util :refer [user-level]]))

(defn create-path [path]
  (.mkdir (java.io.File. path)))

;; Start grid-skeleton
(def grid-comments
  (str
   "builds grid. Parameters: params table & args\n"
   "args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"))

(def security-comments-1
  "Solo <strong>los administradores </strong> pueden accessar esta opción!!!")

(def security-comments-2
  "Solo <strong>los administradores nivel sistema </strong> pueden accessar esta opción!!!")

(defn process-security [security]
  (cond
    (= security 1) (str "(if\n"
                        "(or\n"
                        "(= (user-level) \"A\")\n"
                        "(= (user-level) \"S\"))\n"
                        "(application title ok js content)\n"
                        "(application title ok nil \"" security-comments-1 "\"))")
    (= security 2) (str "(if (= (user-level) \"S\")\n"
                        "(application title ok js content)\n"
                        "(application title ok nil \"" security-comments-2 "\"))")
    (= security 3) (str "(application title ok js content)")))

(defn build-grid-handler [options]
  (let [folder (:folder options)
        titulo (:title options)
        tabla (:table options)
        root (:root options)
        security (:secure options)
        ns-root (subs (str (clojure.string/replace root #"/" ".") folder) 4)]
    (str
     "(ns " ns-root ".handler\n"
     "(:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]\n"
     "[sk.models.grid :refer [build-grid]]\n"
     "[sk.layout :refer [application]]\n"
     "[sk.models.util :refer [get-session-id user-level]]\n"
     "[" ns-root ".view :refer [" folder "-view " folder "-scripts]]))\n\n"
     "(defn " folder "[_]\n"
     "(let [title \"" titulo "\"\n"
     "ok (get-session-id)\n"
     "js (" folder "-scripts)\n"
     "content (" folder "-view title)]\n"
     (process-security security) "))\n\n"
     "(defn " folder "-grid\n"
     \" grid-comments \"
     "[{params :params}]\n"
     "\n(let [table \"" tabla "\"]\n"
     "(build-grid params table)))\n\n"
     "(defn " folder "-form [id]\n"
     "(let [table \"" tabla "\"]\n"
     "(build-form-row table id)))\n\n"
     "(defn " folder "-save [{params :params}]\n"
     "(let [table \"" tabla "\"]\n"
     "(build-form-save params table)))\n\n"
     "(defn " folder "-delete [{params :params}]\n"
     "(let [table \"" tabla "\"]\n"
     "(build-form-delete params table)))")))

(defn build-grid-model [options]
  (let [folder (:folder options)
        root (:root options)
        ns-root (subs (str (clojure.string/replace root #"/" ".") folder) 4)]
    (str
     "(ns " ns-root ".model\n"
     "(:require [sk.models.crud :refer [Query db]]))\n")))

(defn build-grid-view [options]
  (let [folder (:folder options)
        root (:root options)
        url (:link options)
        ns-root (subs (str (clojure.string/replace root #"/" ".") folder) 4)]
    (str
     "(ns " ns-root ".view\n"
     "(:require\n"
     "[hiccup.page :refer [include-js]]\n"
     "[ring.util.anti-forgery :refer [anti-forgery-field]]\n"
     "[sk.models.util :refer\n"
     "[build-dialog build-dialog-buttons build-field build-table build-toolbar]]))\n\n"
     "(defn dialog-fields []\n"
     "(list\n"
     "(build-field\n"
     "{:id \"id\"\n"
     ":name \"id\"\n"
     ":type \"hidden\"})\n"
     "))\n\n"
     "(defn " folder "-view [title]\n"
     "(list\n"
     "(anti-forgery-field)\n"
     "(build-table\n"
     "title\n"
     "\"" url "\"\n"
     "(list\n"
     "[:th {:data-options \"field:'',sortable:true,fixed:true\"} \"\"]\n"
     "))\n"
     "(build-toolbar)\n"
     "(build-dialog title (dialog-fields))))\n\n"
     "(defn " folder "-scripts []\n"
     "(include-js \"/js/grid.js\"))")))

(defn build-grid-skeleton [options]
  "secure: 1=S/A, 2=S, 3=all"
  (let [folder (:folder options)
        root (:root options)
        path (str root folder)]
    (create-path path)
    (spit (str path "/handler.clj") (build-grid-handler options))
    (spit (str path "/model.clj") (build-grid-model options))
    (spit (str path "/view.clj") (build-grid-view options))))
;; End grid skeleton

(defn build-skeleton-handler [options]
  (let [folder (:folder options)
        titulo (:title options)
        tabla (:table options)
        security (:secure options)
        root (:root options)
        ns-root (subs (str (clojure.string/replace root #"/" ".") folder) 4)]
    (str
     "(ns " ns-root ".handler\n"
     "(:require \n"
     "[sk.layout :refer [application]]\n"
     "[sk.models.util :refer [get-session-id]]\n"
     "[" ns-root ".view :refer [" folder "-view " folder "-scripts]]))\n\n"
     "(defn " folder "[_]\n"
     "(let [title \"" titulo "\"\n"
     "ok (get-session-id)\n"
     "js (" folder "-scripts)\n"
     "content (" folder "-view title)]\n"
     (process-security security) "))\n\n")))

(defn build-skeleton-model [options]
  (let [folder (:folder options)
        tabla (:table options)
        root (:root options)
        ns-root (subs (str (clojure.string/replace root #"/" ".") folder) 4)]
    (str
     "(ns " ns-root ".model\n"
     "(:require [sk.models.crud :refer [Query db]]))\n\n"
     "(defn get-rows [tabla]\n"
     "(Query db [(str \"select * from \" tabla)]\n\n"
     "(comment\n"
     "(get-rows \"" tabla "\"))")))

(defn build-skeleton-view [options]
  (let [folder (:folder options)
        titulo (:title options)
        tabla (:table options)
        root (:root options)
        ns-root (subs (str (clojure.string/replace root #"/" ".") folder) 4)]
    (str
     "(ns " ns-root ".view\n"
     "(:require "
     "[" ns-root ".model :refer [get-rows]]\n"
     "[hiccup.page :refer [include-js]]))\n\n"
     "(def cnt (atom 0))\n\n"
     "(defn my-body [row]\n"
     "[:tr\n"
     "[:td (swap! cnt inc)]\n"
     "[:td (:id row)]])\n\n"
     "(defn " folder "-view [title]\n"
     "(let [rows (get-rows \"" tabla "\")\n"
     "cnt (reset! cnt 0)]\n"
     "[:div.container\n"
     "[:center\n"
     "[:h2 \"" titulo "\"]\n"
     "[:table.table.table-striped.table-hover.table-bordered\n"
     "[:thead.table-primary\n"
     "[:tr\n"
     "[:th \"#\"]\n"
     "[:th \"ID\"]\n"
     "]]\n"
     "[:tbody (map my-body rows)]]]]))\n\n"
     "(defn " folder "-scripts []\n"
     "[:script nil])\n")))

(defn build-skeleton [options]
  "secure: 1=S/A, 2=S, 3=all"
  (let [folder (:folder options)
        root (:root options)
        path (str root folder)]
    (create-path path)
    (spit (str path "/handler.clj") (build-skeleton-handler options))
    (spit (str path "/model.clj") (build-skeleton-model options))
    (spit (str path "/view.clj") (build-skeleton-view options))))

(comment
  (build-grid-skeleton {:folder "contactos"
                        :title "Contactos"
                        :table "contactos"
                        :secure 1
                        :link "/admin/contactos"
                        :root "src/sk/handlers/admin/"})
  (build-skeleton {:folder "contactos"
                   :title "Contactos"
                   :table "contactos"
                   :secure 3
                   :link "/contactos"
                   :root "src/sk/handlers/"}))
