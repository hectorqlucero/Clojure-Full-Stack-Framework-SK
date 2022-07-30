(ns sk.models.builder
  (:require [clojure.java.io :as io]
            [clojure.string :as st]
            [hiccup.core :refer [html]]
            [sk.models.crud :refer
             [build-grid-columns get-table-describe]]))

(defn create-path [path]
  (.mkdir (io/file path)))

;; start grid-skeleton
(def grid-comments
  (str
   "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"))

(def security-comments-1
  "solo <strong>los administradores </strong> pueden accessar esta opción!!!")

(def security-comments-2
  "solo <strong>los administradores nivel sistema </strong> pueden accessar esta opción!!!")

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
        args (:args options)
        root (:root options)
        security (:secure options)
        ns-root (subs (str (st/replace root #"/" ".") folder) 4)]
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
     "\"" grid-comments "\"\n"
     "[{params :params}]\n"
     "(let [table \"" tabla "\"\n"
     "args " args "]\n"
     "(build-grid params table args)))\n\n"
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
        tabla (:table options)
        root (:root options)
        ns-root (subs (str (st/replace root #"/" ".") folder) 4)]
    (str
     "(ns " ns-root ".model\n"
     "(:require [sk.models.crud :refer [Query db]]))\n\n"
     "(defn get-rows [tabla]\n"
     "(Query db [(str \"select * from \" tabla)]))\n\n"
     "(comment\n"
     "(get-rows \"" tabla "\"))")))

(defn grid-col [field]
  (let [field-name (second (st/split field #"\."))
        col-name (st/upper-case field-name)]
    (str "[:th {:data-options \"field:'" field-name "',sortable:true,width:100\"}\"" col-name "\"" "]\n")))

(defn build-grid-field [col]
  (let [field (:field col)
        type (st/lower-case (:type col))]
    (cond
      (= type "text")
      (str
       "(build-field\n"
       "{:id \"" field "\"\n"
       " :name \"" field "\"\n"
       " :class \"easyui-textbox\"\n"
       " :prompt \"xxx\"\n"
       " :data-options \"label:'xxx:',
        labelPosition:'top',
        required:true,
        multiline:true,
        height:120,
        width:'100%'\"})\n")
      (= type "date")
      (str
       "(build-field\n"
       "{:id \"" field "\"\n"
       " :name \"" field "\"\n"
       " :class \"easyui-datebox\"\n"
       " :prompt \"mm/dd/aaaa ex. 02/07/1957 es: Febreo 2 de 1957\"\n"
       " :data-options \"label:'xxx:',
        labelPosition:'top',
        required:true,
        width:'100%'\"})\n")
      (= type "time")
      (str
       "(build-field\n"
       "{:id \"" field "\"\n"
       " :name \"" field "\"\n"
       " :class \"easyui-combobox\"\n"
       " :prompt \"Escojer la hora...\"\n"
       " :data-options \"label:'xxx:',
        labelPosition:'top',
        method:'GET',
        url:'/table_ref/get-time',
        width:'100%'\"})\n")
      :else
      (str
       "(build-field\n"
       "{:id \"" field "\"\n"
       " :name \"" field "\"\n"
       " :class \"easyui-textbox\"\n"
       " :prompt \"xxx\"\n"
       " :data-options \"label:'xxx:',
        labelPosition:'top',
        required:true,
        width:'100%'\"})\n"))))

(defn build-grid-fields [table]
  (let [data (get-table-describe table)
        cols (rest data)]
    (map build-grid-field cols)))

(defn build-grid-view [options]
  (let [folder (:folder options)
        root (:root options)
        url (:link options)
        table (:table options)
        fields (build-grid-fields table)
        cols (map grid-col (build-grid-columns table))
        ns-root (subs (str (st/replace root #"/" ".") folder) 4)]
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
     (html fields)
     "))\n\n"
     "(defn " folder "-view [title]\n"
     "(list\n"
     "(anti-forgery-field)\n"
     "(build-table\n"
     "title\n"
     "\"" url "\"\n"
     "(list\n"
     (html cols)
     "))\n"
     "(build-toolbar)\n"
     "(build-dialog title (dialog-fields))\n"
     "(build-dialog-buttons)))\n\n"
     "(defn " folder "-scripts []\n"
     "(include-js \"/js/grid.js\"))")))

(defn build-grid-skeleton
  "secure: 1=s/a, 2=s, 3=all"
  [options]
  (let [folder (:folder options)
        root (:root options)
        path (str root folder)]
    (create-path path)
    (spit (str path "/handler.clj") (build-grid-handler options))
    (spit (str path "/model.clj") (build-grid-model options))
    (spit (str path "/view.clj") (build-grid-view options))))
;; end grid skeleton

(defn build-pdf-template [row]
  (str
   "[:cell {:align :left} (str $" (:field row) ")]\n"))

(defn build-pdf-headers [row]
  (str "[:paragraph {:align :left} \"" (st/upper-case (:field row)) "\"]\n"))

(defn build-csv-headers [row]
  (str (vec (map #(str (st/upper-case (:field %)) " ") row))))

(defn build-csv-template [row]
  (apply str (map #(str "(str $" (:field %) ") ") row)))

(defn build-skeleton-handler [options]
  (let [folder (:folder options)
        titulo (:title options)
        table (:table options)
        filename (str table ".csv")
        data (get-table-describe table)
        security (:secure options)
        root (:root options)
        ns-root (subs (str (st/replace root #"/" ".") folder) 4)]
    (str
     "(ns " ns-root ".handler\n"
     "(:require \n"
     "[clojure.data.csv :as csv]\n"
     "[clojure.java.io :as java-io]\n"
     "[ring.util.io :refer [piped-input-stream]]\n"
     "[hiccup.core :refer [html]]\n"
     "[pdfkit-clj.core :refer [as-stream gen-pdf]]\n"
     "[clj-pdf.core :refer [pdf template]]\n"
     "[sk.layout :refer [application]]\n"
     "[sk.models.util :refer [get-session-id user-level]]\n"
     "[" ns-root ".model :refer [get-rows]]\n"
     "[" ns-root ".view :refer [" folder "-view " folder "-scripts]]))\n\n"
     "(defn " folder "[_]\n"
     "(let [title \"" titulo "\"\n"
     "ok (get-session-id)\n"
     "js (" folder "-scripts)\n"
     "content (" folder "-view title)]\n"
     (process-security security) "))\n\n"
     "(defn " folder "-reporte [_]\n"
     "(let [title \" " titulo "\"\n"
     "ok (get-session-id)\n"
     "js nil\n"
     "content (html (" folder "-view title))]\n"
     "(if\n"
     "(or\n"
     "(= (user-level) \"U\")\n"
     "(= (user-level) \"A\")\n"
     "(= (user-level) \"S\"))\n"
     "{:status 200\n"
     ":headers {\"Content-Type\" \"application/pdf\"\n"
     "\"Content-Disposition\" \"attachment;filename='" folder ".pdf'\"}\n"
     ":body (as-stream (gen-pdf content))}\n"
     "(application title ok js \"Solo miembros pueden accessar esta opción!!!\"))"
     "))\n\n"
     "(def " table "-pdf-template\n"
     "(template\n"
     "(list\n"
     (apply str (map build-pdf-template (rest data)))
     ")))\n\n"
     "(defn generate-report-header []\n"
     "[{:background-color [233 233 233]}\n"
     (apply str (map build-pdf-headers (rest data)))
     "])\n\n"
     "(defn generate-report-body []\n"
     "(let [rows (get-rows \"" table "\")]\n"
     "(into\n"
     "[:table\n"
     "{:cell-border true\n"
     ":style :normal\n"
     ":size 10\n"
     ":border true\n"
     ":header (generate-report-header)}]\n"
     "(" table "-pdf-template rows))))\n\n"
     "(defn generate-report-header-options [title]\n"
     "{:title title\n"
     ":header {:x 20\n"
     ":y 830\n"
     ":table\n"
     "[:pdf-table\n"
     "{:border false\n"
     ":width-percent 100}\n"
     "[100]\n"
     "[[:pdf-cell {:type :bold :size 16 :align :center} title]]]}\n"
     ":footer \"page\"\n"
     ":left-margin 10\n"
     ":right-margin 10\n"
     ":top-margin 10\n"
     ":bottom-margin 25\n"
     ":size :a4\n"
     ":orientation :portrait\n"
     ":font {:family :helvetica :size 10}\n"
     ":align :center\n"
     ":pages true})\n\n"
     "(defn generate-report [title]\n"
     "(piped-input-stream\n"
     "(fn [output-stream]\n"
     "(pdf\n"
     "[(generate-report-header-options title)\n"
     "(generate-report-body)]\n"
     "output-stream))))\n\n"
     "(defn " table "-pdf [_]\n"
     "(let [title \"" titulo "\"\n"
     "ok (get-session-id)\n"
     "js nil\n"
     "content \"Solo miembros pueden accessar esta opción!!!\"]\n"
     "(if\n"
     "(or\n"
     "(= (user-level) \"U\")\n"
     "(= (user-level) \"A\")\n"
     "(= (user-level) \"S\"))\n"
     "{:status 200\n"
     ":headers {\"Content-Type\" \"application/pdf\"\n"
     "\"Content-Disposition\" \"attachment;filename='" table "'\"}\n"
     ":body (generate-report title)}\n"
     "(application title ok js content))))\n\n"
     "(def " table "-csv-headers\n"
     (build-csv-headers (rest data)) ")\n\n"
     "(def " table "-csv-template\n"
     "(template\n"
     "[" (build-csv-template (rest data)) "]))\n\n"
     "(defn build-csv [filename]\n"
     "(let [rows (get-rows \"" table "\")]\n"
     "(with-open [writer (java-io/writer filename)]\n"
     "(csv/write-csv writer (cons (vec " table "-csv-headers) (" table "-csv-template rows))))))\n\n"
     "(defn " table "-csv [_]\n"
     "(build-csv \"" filename "\")\n"
     "(let [filename \"" filename "\"\n"
     "my-file (slurp filename)]\n"
     "(java-io/delete-file filename)\n"
     "{:status 200\n"
     ":headers {\"Content-Type\" \"text/csv\"\n"
     "\"Content-Disposition\" \"attachment;filename=" filename "\"}\n"
     ":body my-file}"
     "))"
     )))

(defn build-skeleton-model [options]
  (let [folder (:folder options)
        tabla (:table options)
        root (:root options)
        ns-root (subs (str (st/replace root #"/" ".") folder) 4)]
    (str
     "(ns " ns-root ".model\n"
     "(:require [sk.models.crud :refer [Query db]]))\n\n"
     "(defn get-rows [tabla]\n"
     "(Query db [(str \"select * from \" tabla)]))\n\n"
     "(comment\n"
     "(get-rows \"" tabla "\"))")))

(defn grid-row [field]
  (let [field-name (second (st/split field #"\."))]
    (str "[:td (:" field-name " row)]\n")))

(defn build-skeleton-view [options]
  (let [folder (:folder options)
        tabla (:table options)
        cols (map grid-col (build-grid-columns tabla))
        rows (map grid-row (build-grid-columns tabla))
        root (:root options)
        ns-root (subs (str (st/replace root #"/" ".") folder) 4)]
    (str
     "(ns " ns-root ".view\n"
     "(:require "
     "[" ns-root ".model :refer [get-rows]]\n"
     "))\n\n"
     "(defn my-body [row]\n"
     "[:tr\n"
     (html rows)
     "])\n\n"
     "(defn " folder "-view [title]\n"
     "(let [rows (get-rows \"" tabla "\")]\n"
     "[:table.dg {:data-options \"remoteSort:false,fit:true,rownumbers:true,fitColumns:true\" :title title}\n"
     "[:thead\n"
     "[:tr\n"
     (html  cols)
     "]]\n"
     "[:tbody (map my-body rows)]]))\n\n"
     "(defn " folder "-scripts []\n"
     "[:script 
     \"
     var dg = $('.dg');
     $(document).ready(function() {
      dg.datagrid();
      dg.datagrid('enableFilter');
     });
     \"
     ])\n")))

(defn build-skeleton
  "secure: 1=s/a, 2=s, 3=all"
  [options]
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
                        :args "{:sort-extra \"nombre,paterno,materno\"}"
                        :secure 1
                        :link "/admin/contactos"
                        :root "src/sk/handlers/admin/"})
  (build-skeleton {:folder "contactos"
                   :title "Contactos"
                   :table "contactos"
                   :secure 3
                   :link "/contactos"
                   :root "src/sk/handlers/"}))
