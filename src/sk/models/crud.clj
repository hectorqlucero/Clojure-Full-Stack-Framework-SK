(ns sk.models.crud
  (:require [clojure.java.io :as io]
            [clojure.string :as st]
            [clojure.java.jdbc :as j]
            [cheshire.core :refer [generate-string]])
  (:import java.text.SimpleDateFormat))

(defn get-config
  []
  (try
    (binding [*read-eval* false]
      (read-string (str (slurp (io/resource "private/config.clj")))))
    (catch Exception e (.getMessage e))))

(def config (get-config))

(def db {:classname                       (:db-class config)
         :subprotocol                     (:db-protocol config)
         :subname                         (:db-name config)
         :user                            (:db-user config)
         :password                        (:db-pwd config)
         :useSSL                          false
         :useTimezone                     true
         :useLegacyDatetimeCode           false
         :serverTimezone                  "UTC"
         :noTimezoneConversionForTimeType true
         :dumpQueriesOnException          true
         :autoDeserialize                 true
         :useDirectRowUnpack              false
         :cachePrepStmts                  true
         :cacheCallableStmts              true
         :cacheServerConfiguration        true
         :useLocalSessionState            true
         :elideSetAutoCommits             true
         :alwaysSendSetIsolation          false
         :enableQueryTimeouts             false
         :zeroDateTimeBehavior            "CONVERT_TO_NULL"}) ; Database connection

(def SALT "897sdn9j98u98kj")                                ; encryption salt for DB

(def KEY "897sdn9j98u98kjz")

(defn aes-in
  "Encrypt a value MySQL"
  [value & alias]
  (try
    (str "AES_ENCRYPT('" value "','" SALT "')"
         (when (seq alias)
           (str " as " (first alias))))
    (catch Exception e (.getMessage e))))

(defn aes-out
  "Decrypt a value MySQL"
  [value & alias]
  (try
    (str "AES_DECRYPT('" value "','" SALT "')"
         (when (seq alias)
           (str " as " (first alias))))
    (catch Exception e (.getMessage e))))

(defn aes-sel
  "Return field decrypted MySQL"
  [field & alias]
  (try
    (str "AES_DECRYPT(" field ",'" SALT "')"
         (when (seq alias)
           (str " as " (first alias))))
    (catch Exception e (.getMessage e))))

(def phone_mask "(___) ___-____")

(def phone_mask_ext "(___) ___-____ x_____")

(def date_mask "mm/dd/YYYY")

(def n4_mask "____")

(def n5_mask "_____")

(defn cleanup_blanks
  [v]
  (try
    (when-not (clojure.string/blank? v) v)
    (catch Exception e (.getMessage e))))

(defn cleanup_phones
  [v]
  (try
    (when-not (= v phone_mask) v)
    (catch Exception e (.getMessage e))))

(defn cleanup_phones_ext
  [v]
  (try
    (when-not (= v phone_mask_ext) v)
    (catch Exception e (.getMessage e))))

(defn cleanup_dates
  [v]
  (try
    (when-not (= v date_mask) v)
    (catch Exception e (.getMessage e))))

(defn cleanup_n4
  [v]
  (try
    (when-not (= v n4_mask) v)
    (catch Exception e (.getMessage e))))

(defn cleanup_n5
  [v]
  (try
    (when-not (= v n5_mask) v)
    (catch Exception e (.getMessage e))))

(defn cleanup
  "Cleanup row - convert masks or blanks into nil"
  [row]
  (try
    (apply merge
           (for [[k v] row]
             (let [value (and (cleanup_blanks v)
                              (cleanup_phones v)
                              (cleanup_phones_ext v)
                              (cleanup_dates v)
                              (cleanup_n4 v)
                              (cleanup_n5 v))]
               {k value})))
    (catch Exception e (.getMessage e))))

(defn Query
  "queries database accepts query string"
  [db sql]
  (try
    (j/query db sql {:entities (j/quoted \`)})
    (catch Exception e (.getMessage e))))

(defn Query!
  "queries database accepts query string no return value"
  [db sql]
  (try
    (j/execute! db sql {:entities (j/quoted \`)})
    (catch Exception e (.getMessage e))))

(defn Insert
  "Inserts colums in the specified table"
  [db table row]
  (try
    (j/insert! db table (cleanup row) {:entities (j/quoted \`)})
    (catch Exception e (.getMessage e))))

(defn Insert-multi
  "Inserts multiple rows in specified table"
  [db table rows]
  (try
    (j/with-db-transaction [t-con db]
      (j/insert-multi! t-con table rows))
    (catch Exception e (.getMessage e))))

(defn Update
  "Updates columns in the specified table"
  [db table row where-clause]
  (try
    (j/update! db table (cleanup row) where-clause {:entities (j/quoted \`)})
    (catch Exception e (.getMessage e))))

(defn Delete
  "Deletes columns in a specified table"
  [db table where-clause]
  (try
    (j/delete! db table where-clause {:entities (j/quoted \`)})
    (catch Exception e (.getMessage e))))

(defn Save
  "Updates columns or inserts a new row in the specified table"
  [db table row where-clause]
  (try
    (j/with-db-transaction [t-con db]
      (let [result (j/update! t-con table (cleanup row) where-clause {:entities (j/quoted \`)})]
        (if (zero? (first result))
          (j/insert! t-con table (cleanup row) {:entities (j/quoted \`)})
          result)))
    (catch Exception e (.getMessage e))))

(defn crud-fix-id
  [v]
  (try
    (if (clojure.string/blank? v) nil v)
    (catch Exception e (.getMessage e))))

(defn crud-capitalize-words
  "Capitalize words"
  [s]
  (try
    (->> (clojure.string/split (str s) #"\b")
         (map clojure.string/capitalize)
         (clojure.string/join))
    (catch Exception e (.getMessage e))))

(defn crud-format-date-internal
  "Convert a MM/dd/yyyy format date to yyyy-MM-dd format using a string as a date
   eg. 02/01/1997 -> 1997-02-01"
  [s]
  (if (not-empty s)
    (try
      (.format
       (SimpleDateFormat. "yyyy-MM-dd")
       (.parse
        (SimpleDateFormat. "MM/dd/yyyy") s))
      (catch Exception e (.getMessage e)))
    nil))

(defn get-table-describe
  [table]
  (try
    (Query db (str "DESCRIBE " table))
    (catch Exception e (.getMessage e))))

(defn get-table-columns
  [table]
  (try
    (map #(keyword (:field %)) (get-table-describe table))
    (catch Exception e (.getMessage e))))

(defn get-table-types [table]
  (try
    (map #(keyword (:type %)) (get-table-describe table))
    (catch Exception e (.getMessage e))))

(defn process-field
  [params field field-type]
  (try
    (let [value (str ((keyword field) params))
          field-type (st/lower-case field-type)]
      (cond
        (st/includes? field-type "varchar") (crud-capitalize-words value)
        (st/includes? field-type "char") (st/upper-case value)
        (st/includes? field-type "date") (crud-format-date-internal value)
        :else value))
    (catch Exception e (.getMessage e))))

(defn build-postvars
  "Build post vars for table and process by type"
  [table params]
  (try
    (let [td (get-table-describe table)]
      (into {}
            (map (fn [x]
                   (when ((keyword (:field x)) params)
                     {(keyword (:field x))
                      (process-field params (:field x) (:type x))})) td)))
    (catch Exception e (.getMessage e))))

(defn build-grid-field
  [d]
  (try
    (let [field (:field d)
          field-type (:type d)]
      (cond
        (= field-type "date") (str "DATE_FORMAT(" field "," "'%m/%d/%Y') as " (str field "_formatted"))
        (= field-type "time") (str "TIME_FORMAT(" field "," "'%H:%i') as " (str field "_formatted"))
        (= field-type "decimal(15,2)") (str "concat('$',format(" field ",2)) as " (str field "_formatted"))))
    (catch Exception e (.getMessage e))))

(defn build-form-field
  [d]
  (try
    (let [field (:field d)
          field-type (:type d)]
      (cond
        (= field-type "date") (str "DATE_FORMAT(" field "," "'%m/%d/%Y') as " field)
        (= field-type "time") (str "TIME_FORMAT(" field "," "'%H:%i') as " field)
        :else field))
    (catch Exception e (.getMessage e))))

(defn get-table-key
  [d]
  (try
    (:field (first (filter #(= (:key %) "PRI") d)))
    (catch Exception e (.getMessage e))))

(defn build-grid-columns
  "Builds grid columns ex. ['c1', 'c2'...]"
  [table]
  (try
    (vec
     (flatten
      (map (fn [row]
             (let [type (:type row)]
               (cond
                 (= type "date") [(build-grid-field row) (:field row)]
                 (= type "time") [(build-grid-field row) (:field row)]
                 (= type "decimal(15,2)") [(build-grid-field row) (:field row)]
                 :else (:field row)))) (get-table-describe table))))
    (catch Exception e (.getMessage e))))

(defn build-form-row
  "Builds grid form select"
  [table id]
  (try
    (let [tid (get-table-key (get-table-describe table))
          head "SELECT "
          body (apply str (interpose #"," (map #(build-form-field %) (get-table-describe table))))
          foot (str " FROM " table " WHERE " tid " = ?")
          sql (str head body foot)
          row (Query db [sql id])]
      (generate-string (first row)))
    (catch Exception e (.getMessage e))))

(defn process-regular-form
  "Standard form save ex. (build-for-save params 'eventos')"
  [params table]
  (try
    (let [id (crud-fix-id (:id params))
          postvars (build-postvars table params)
          result (Save db (keyword table) postvars ["id = ?" id])]
      (if (seq result)
        (generate-string {:success "Procesado con éxito!"})
        (generate-string {:error "No se puede procesar!"})))
    (catch Exception e (.getMessge e))))

;; Start upload form
(defn crud-upload-image
  "Uploads image and renames it to the id passed"
  [file id path]
  (let [tempfile   (:tempfile file)
        size       (:size file)
        type       (:content-type file)
        extension  (peek (clojure.string/split type #"\/"))
        extension  (if (= extension "jpeg") "jpg" "jpg")
        image-name (str id "." extension)]
    (when-not (zero? size)
      (io/copy tempfile (io/file (str path image-name))))
    image-name))

(defn get-id [id postvars table]
  (if (nil? id)
    (let [result (Save db (keyword table) postvars ["id = ?" id])]
      (str (:generated_key (first result))))
    id))

(defn process-upload-form
  [params table folder]
  (try
    (let [id (crud-fix-id (:id params))
          file (:file params)
          postvars (dissoc (build-postvars table params) :file)
          the-id (str (get-id id postvars table))
          path (str (:uploads config) folder "/")
          image-name (crud-upload-image file the-id path)
          postvars (assoc postvars :imagen image-name :id the-id)
          result (Update db (keyword table) postvars ["id = ?" the-id])]
      (if (seq result)
        (generate-string {:success "Procesado con éxito!"})
        (generate-string {:error "No se puede procesar!"})))
    (catch Exception e (.getMessage e))))
;; End upload form

(defn build-form-save
  [params table & path]
  (try
    (if-not (nil? (:file params))
      (process-upload-form params table (first path))
      (process-regular-form params table))
    (catch Exception e (.getMessage e))))

(defn build-form-delete
  "Standard form delete ex. (build-form-delete params)"
  [params table]
  (try
    (let [id (:id params nil)
          result (if-not (nil? id)
                   (Delete db (keyword table) ["id = ?" id])
                   nil)]
      (if (seq result)
        (generate-string {:success "Eliminado con éxito!"})
        (generate-string {:error "Incapaz de eliminar!"})))
    (catch Exception e (.getMessage e))))
