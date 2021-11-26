(ns sk.models.builder
  (:require [sk.models.util :refer [get-session-id]]))

(defn create-path [path]
  (.mkdir (java.io.File. path)))

(def grid-comments
  (str
   "builds grid. Parameters: params table & args\n"
   "args: {:join 'other-table' :search-extra \"name='pedro'\" :sort-extra 'name,lastname'}"))

(defn build-grid-handler [options]
  (let [folder (:folder options)
        titulo (:title options)
        tabla (:table options)]
    (str
     "(ns sk.handlers." folder ".handler\n"
     "(:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]\n"
     "[sk.models.grid :refer [build-grid]]\n"
     "[sk.layout :refer [application]]\n"
     "[sk.models.util :refer [get-session-id]]\n"
     "[sk.handlers." folder ".view :refer [" folder "-view " folder "-scripts]]))\n\n"
     "(defn " folder "[_]\n"
     "(let [title \"" titulo "\"\n"
     "ok (get-session-id)\n"
     "js (" folder "-scripts)\n"
     "content (" folder "-view title)]\n"
     "(application title ok js content)))\n\n"
     "(defn " folder "-grid [{params :params}]\n"
     \" grid-comments \"
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
  (let [folder (:folder options)]
    (str
     "(ns sk.handlers." folder ".model\n"
     "(:require [sk.models.crud :refer [Query db]]))\n")))

(defn build-grid-view [options]
  (let [folder (:folder options)]
    (str
     "(ns sk.handlers." folder ".view\n"
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
     ":type \"hidden\"})))\n\n"
     "(defn " folder "-view [title]\n"
     "(list\n"
     "(anti-forgery-field)\n"
     "(build-table\n"
     "title\n"
     "/administrar/rodadas es un ejemplo remplanzar por el url correcto\n"
     "(list\n"
     "[:th {:data-options \"field:'titulo',sortable:true,fixed:true\"} \"Titulo\"]\n"
     "[:th {:data-options \"field:'nivel',sortable:true,fixed:true\"} \"Nivel\"]))\n"
     "(build-toolbar)\n"
     "(build-dialog title (dialog-fields))\n"
     "(build-dialog-buttons)))\n\n"
     "(defn " folder "-scripts []\n"
     "(include-js \"/js/grid.js\"))")))

(defn build-skeleton [options]
  (let [folder (:folder options)
        path (str "src/sk/handlers/" folder)]
    (create-path path)
    (spit (str path "/handler.clj") (build-grid-handler options))
    (spit (str path "/model.clj") (build-grid-model options))
    (spit (str path "/view.clj") (build-grid-view options))))

(comment
  (println grid-comments)
  (build-skeleton {:folder "contacts"
                   :title "Contactos"
                   :table "contacts"}))
