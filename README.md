# SK Full Stack web page generation

### Site created with this tool: [Cicloturismo] (https://ciclismomexicali.org).

# Front End
- bootstrap4
- easyui

# Back End
- MySQL database
- clojure - look at project.clj for libraries used

# Features
- User validation
- Email generation
- PDF report generation
- CRUD Grid generation with filter, search, sort etc...
- DASHBOARD generation with filter, search, sort, pdf, export to csv...
- Migrations with ragtime
- Automated menus generated and stored in the database, with an administration crud grid to administer.
- Automated routes generated and stored in the database, with an administration crud grid to administer.
- lein migrate is a lein alias to create a ragtime migration
- lein rollback is a lein alias to rollback a migration
- lein database is a lein alias to create temporary users
- lein grid 'tablename' is a lein alias to create crud grid and all menus and routes.
- lein dashboard 'tablename' is a lein alias to create dashboard grid and all menus and routes.
- Supports both open and private routes. i.e. open no login and private you must have valid user to login.
- All menus, routes, and private routes are stored in the database. Crud grids available to administer menus and routes.

## Pre-requisites
1. leiningen 2.0.0 or above installed
2. jdk8 or above
3. mySQL or MariaDB
4. wkhtmltopdf *Needed for pdfkit-clj library*

## Usage
1. Create a database "xxxxx" in mySQL or MariaDB.  Note: "xxxxx" refers to what you will call your database, so replace with your database name created.
2. Clone the repository
3. Copy resources/private/config_example to config.clj and change all of the "xxxxx" to what applies to you setup
  Example of resources/private/config_example.clj
  ```
    ;; Replace all "xxxxx" with your configuration
    {:db-protocol  "mysql"
     :db-name      "//localhost:3306/xxxxx?characterEncoding=UTF-8&serverTimezone=America/Los_Angeles" ; Change me
     :database-url "mysql://localhost:3306/xxxxx?user=root&password=xxxxx&serverTimezone=America/Los_Angeles" ; Change me
     :db-user      "xxxxx" ; Change me
     :db-pwd       "xxxxx" ; Change me
     :db-class     "com.mysql.cj.jdbc.Driver"
     :email-host   "xxxxx" ; Optional
     :email-user   "xxxxx" ; Optional
     :email-pwd    "xxxxx" ; Optional
     :port         3000
     :tz           "US/Pacific" ;Change if in different time zone
     :site-name    "xxxxx" ; Change me
     :company-name "xxxxx" ; Change me
     :uploads      "./uploads/xxxxx/" ; Change me
     :base-url     "http://0.0.0.0:3000/"
     :img-url      "https://0.0.0.0/uploads/"
     :path         "/uploads/"}
  ```
## From project directory:
4. lein migrate
5. lein database
6. lein run

## Database migrations
### Tables created:
1. users
2. menus
3. pincludes
4. proutes
5. routes
6. rincludes

## Temporary users
### Users created:
1. user: sistema@gmail.com pass: sistema
2. user: admin@gmail.com   pass: admin
3. user: user@gmail.com    pass: user

## This concludes the installation and you have a fully functional full stack web page application

# DOCUMENTATION

## LETS CREATE A CONTACTS WEB PAGE WITH A GRID TO MAINTAIN CONTACTS AND A DASHBOARD TO DISPLAY, SORT, FILTER, OUTPUT REPORTS AND EXPORT DATA TO CSV
### I am asumming you have your development editor configured to work with Clojure. Vim/Fireplace or Spacemac, vscode/Calba etc...
1. Create an empty MySQL database with any client. Call this database 'demo'
2. From a terminal Clone the repository *git clone https://github.com/hectorqlucero/Clojure-Full-Stack-Framework-SK.git*
3. mv Clojure-Full-Stack-Framework-SK demo *Rename the clone folder demo*
4. cd demo
5. cp resources/private/config_example.clj config.clj
   config.clj will look like this:
   ```
    ;; Replace all "xxxxx" with your configuration
    {:db-protocol  "mysql"
    :db-name      "//localhost:3306/xxxxx?characterEncoding=UTF-8&serverTimezone=America/Los_Angeles" ; Change me
    :database-url "mysql://localhost:3306/xxxxx?user=root&password=xxxxx&serverTimezone=America/Los_Angeles" ; Change me
    :db-user      "root"
    :db-pwd       "xxxxx" ; Change me
    :db-class     "com.mysql.cj.jdbc.Driver"
    :email-host   "xxxxx" ; Optional
    :email-user   "xxxxx" ; Optional
    :email-pwd    "xxxxx" ; Optional
    :port         3000
    :tz           "US/Pacific"
    :site-name    "xxxxx" ; Change me
    :company-name "xxxxx" ; Change me
    :uploads      "./uploads/xxxxx/" ; Change me
    :base-url     "http://0.0.0.0:3000/"
    :img-url      "https://0.0.0.0/uploads/"
    :path         "/uploads/"}
   ```
6. With your editor/IDE of choice open /resources/private/config.clj and modify file to look like this:
   ```
    ;; Replace all "xxxxx" with your configuration
    {:db-protocol  "mysql"
    :db-name      "//localhost:3306/demo?characterEncoding=UTF-8&serverTimezone=America/Los_Angeles" ; Change me
    :database-url "mysql://localhost:3306/demo?user=root&password=your_database_password&serverTimezone=America/Los_Angeles" ; Change me
    :db-user      "root"
    :db-pwd       "your_database_password" ; Change me
    :db-class     "com.mysql.cj.jdbc.Driver"
    :email-host   "xxxxx" ; Optional
    :email-user   "xxxxx" ; Optional
    :email-pwd    "xxxxx" ; Optional
    :port         3000
    :tz           "US/Pacific"
    :site-name    "Contacs" ; Change me
    :company-name "XYZ Company" ; Change me
    :uploads      "./uploads/demo/" ; Change me
    :base-url     "http://0.0.0.0:3000/"
    :img-url      "https://0.0.0.0/uploads/"
    :path         "/uploads/"}
   ```
   *Note:* your_database_password is the password that you gave to MySQL.  Please setup MySQL with a password, don't use a blak password
7. Go to the root of your project with your editor of choice and edit *project.clj* the file looks like this:
  ```
  (defproject sk "0.1.0"
  :description "Sitio" ; Change me
  :url "https://github.com/hectorqlucero/sk" ; Change me
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.csv "1.0.1"]
                 [compojure "1.7.0"]
                 [hiccup "1.0.5"]
                 [lib-noir "0.9.9"]
                 [com.draines/postal "2.0.5"]
                 [cheshire "5.12.0"]
                 [clj-pdf "2.6.8"]
                 [ondrs/barcode "0.1.0"]
                 [pdfkit-clj "0.1.7"]
                 [cljfmt "0.9.2"]
                 [clj-jwt "0.1.1"]
                 [clj-time "0.15.2"]
                 [date-clj "1.0.1"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [org.clojure/data.codec "0.1.1"]
                 [mysql/mysql-connector-java "8.0.33"]
                 [ragtime "0.8.1"]
                 [ring/ring-core "1.11.0"]]
  :main ^:skip-aot sk.core
  :aot [sk.core]
  :plugins [[lein-ancient "0.7.0"]
            [lein-pprint "1.3.2"]]
  :uberjar-name "sk.jar" ; Change me
  :target-path "target/%s"
  :ring {:handler sk.core
         :auto-reload? true
         :auto-refresh? false}
  :resources-paths ["shared" "resources"]
  :aliases {"migrate" ["run" "-m" "sk.migrations/migrate"]
            "rollback" ["run" "-m" "sk.migrations/rollback"]
            "database" ["run" "-m" "sk.models.cdb/database"]
            "grid" ["run" "-m" "sk.models.builder/build-grid"]
            "dashboard" ["run" "-m" "sk.models.builder/build-dashboard"]
            "private" ["run" "-m" "sk.models.b-proutes/main-private"]
            "open" ["run" "-m" "sk.models.b-routes/main-open"]}
  :profiles {:uberjar {:aot :all}})
  ```
  Change this file to look like this:
  ```
  (defproject sk "0.1.0"
  :description "Contacts" ; Change me
  :url "https://github.com/hectorqlucero/demo" ; Change me
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.csv "1.0.1"]
                 [compojure "1.7.0"]
                 [hiccup "1.0.5"]
                 [lib-noir "0.9.9"]
                 [com.draines/postal "2.0.5"]
                 [cheshire "5.12.0"]
                 [clj-pdf "2.6.8"]
                 [ondrs/barcode "0.1.0"]
                 [pdfkit-clj "0.1.7"]
                 [cljfmt "0.9.2"]
                 [clj-jwt "0.1.1"]
                 [clj-time "0.15.2"]
                 [date-clj "1.0.1"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [org.clojure/data.codec "0.1.1"]
                 [mysql/mysql-connector-java "8.0.33"]
                 [ragtime "0.8.1"]
                 [ring/ring-core "1.11.0"]]
  :main ^:skip-aot sk.core
  :aot [sk.core]
  :plugins [[lein-ancient "0.7.0"]
            [lein-pprint "1.3.2"]]
  :uberjar-name "demo.jar" ; Change me
  :target-path "target/%s"
  :ring {:handler sk.core
         :auto-reload? true
         :auto-refresh? false}
  :resources-paths ["shared" "resources"]
  :aliases {"migrate" ["run" "-m" "sk.migrations/migrate"]
            "rollback" ["run" "-m" "sk.migrations/rollback"]
            "database" ["run" "-m" "sk.models.cdb/database"]
            "grid" ["run" "-m" "sk.models.builder/build-grid"]
            "dashboard" ["run" "-m" "sk.models.builder/build-dashboard"]
            "private" ["run" "-m" "sk.models.b-proutes/main-private"]
            "open" ["run" "-m" "sk.models.b-routes/main-open"]}
  :profiles {:uberjar {:aot :all}})
  ```
8. Open a terminal at the root of your project and type: *lein run* If everything is ok go to: *http://localhost:3000*  You will get a webpage with two options *Contacts" and *Conectar*
9. Open a terminal at the root of your project and type: *lein repl* if you are using vim/fireplace, otherwise start the rpl with jack-in or whatever your dev setup needs.
10. Open a terminal at the root of your project and type:
    1. *lein migrate* to create the first database migration. You will see on the terminal:
        - Applying 001-users        *users table*
        - Applying 002-menus        *menus table*
        - Applying 003-pincludes    *pincludes table*
        - Applying 004-proutes      *proutes*
        - Applying 005-routes       *routes*
        - Applying 006-rincludes    *rincludes*
11. Open a terminal at the root of your project and type:
    1. *lein database* to create your dummy users to allow you to login to the web app.
        1. *username:* user@gmail.com       *password*  user
        2. *username:* admin@gmail.com      *password*  admin
        3. *username:* sistema@gmail.com    *password:* sistema
12. Login to *http://localhost:3000/home/login* On the form: *Email:* sistema@gmail.com  *Contraseña:* sistema Click the button *Acceder al sitio* - You will now be logged in.
    1. First link on menu *Contacts* lands you on the home page. you will see the logged on user *Usuario:* sistema@gmail.com
    2. Second link is a dropdown menu *Administrar*
        1. First menu option *Menus* This will show you a grid to administer menus.
        2. Second menu option *Open Includes* This will show you a grid to administer the open routes namespace includes. No login required routes.
        3. Third menu option *Open Routes* This will show you a grid to administer the open routes. No login required routes.
        4. Fourth menu option *Private Includes* This will show you a grid to administer the private routes namespace includes. Login required routes.
        5. Fifth menu option *Private Routes* This will show you a grid to administer the Private routes. Login required routes.
        6. Sixth menu option *Usuarios* This wil show you a grid to administer the users.
13. Now let's create a new migration in resources/migrations:
    1. resources/migrations/007-contacts.down.sql
        ```
        drop table contacts;
        ```
    2. resources/migrations/007-contacts.up.sql
        ```
        create table contacts (
          id int unsigned not null auto_increment primary key,
          firstname varchar(255) default null,
          lastname varchar(255) default null,
          phone varchar(255) default null,
          email varchar(255) default null,
          comments text default null
        ) engine=innodb default charset=utf8;
        ```
14. Open a terminal at the root of your project and type:
    1. *lein migrate* to create the contacts table. You will see on the terminal:
       - Applying 007-contacts *contacts table*
15. Open a terminal at the root of you project and type:
    1. *lein grid contacts* to create a data grid for the table contacts. You will see on the terminal:
      - Codigo generado en: src/sk/handlers/admin/contacts *creates a folder contacts with 3 files inside*:
        ```
        (ns sk.handlers.admin.contacts.handler
        (:require [sk.models.crud :refer [build-form-row build-form-save build-form-delete]]
                    [sk.models.grid :refer [build-grid]]
                    [sk.layout :refer [application]]
                    [sk.models.util :refer [get-session-id user-level]]
                    [sk.handlers.admin.contacts.view :refer [contacts-view contacts-scripts]]))

        (defn contacts [_]
        (let [title "Contacts"
                ok (get-session-id)
                js (contacts-scripts)
                content (contacts-view title)]
            (if
            (or
            (= (user-level) "A")
            (= (user-level) "S"))
            (application title ok js content)
            (application title ok nil "solo <strong>los administradores </strong> pueden accessar esta opción!!!"))))

        (defn contacts-grid
        "builds grid. parameters: params table & args args: {:join 'other-table' :search-extra name='pedro' :sort-extra 'name,lastname'}"
        [{params :params}]
        (let [table "contacts"
                args {:sort-extra "id"}]
            (build-grid params table args)))

        (defn contacts-form [id]
        (let [table "contacts"]
            (build-form-row table id)))

        (defn contacts-save [{params :params}]
        (let [table "contacts"]
            (build-form-save params table)))

        (defn contacts-delete [{params :params}]
        (let [table "contacts"]
            (build-form-delete params table)))

        
        (ns sk.handlers.admin.contacts.model
        (:require [sk.models.crud :refer [Query db]]))

        (defn get-rows [tabla]
        (Query db [(str "select * from " tabla)]))

        (comment
        (get-rows "contacts"))


        (ns sk.handlers.admin.contacts.view
        (:require
        [hiccup.page :refer [include-js]]
        [ring.util.anti-forgery :refer [anti-forgery-field]]
        [sk.models.util :refer
            [build-dialog build-dialog-buttons build-field build-table build-toolbar]]))

        (defn dialog-fields []
        (list
        (build-field
            {:id "id"
            :name "id"
            :type "hidden"})
        (build-field
            {:id "firstname"
            :name "firstname"
            :class "easyui-textbox"
            :prompt "xxx"
            :data-options "label:'xxx:',
                labelPosition:'top',
                required:true,
                width:'100%'"})
        (build-field
            {:id "lastname"
            :name "lastname"
            :class "easyui-textbox"
            :prompt "xxx"
            :data-options "label:'xxx:',
                labelPosition:'top',
                required:true,
                width:'100%'"})
        (build-field
            {:id "phone"
            :name "phone"
            :class "easyui-textbox"
            :prompt "xxx"
            :data-options "label:'xxx:',
                labelPosition:'top',
                required:true,
                width:'100%'"})
        (build-field
            {:id "email"
            :name "email"
            :class "easyui-textbox"
            :prompt "xxx"
            :data-options "label:'xxx:',
                labelPosition:'top',
                required:true,
                width:'100%'"})
        (build-field
            {:id "comments"
            :name "comments"
            :class "easyui-textbox"
            :prompt "xxx"
            :data-options "label:'xxx:',
                labelPosition:'top',
                required:true,
                multiline:true,
                height:120,
                width:'100%'"})))

        (defn contacts-view [title]
        (list
        (anti-forgery-field)
        (build-table
            title
            "/admin/contacts"
            (list
            [:th {:data-options "field:'id',sortable:true,width:100"} "ID"]
            [:th {:data-options "field:'firstname',sortable:true,width:100"} "FIRSTNAME"]
            [:th {:data-options "field:'lastname',sortable:true,width:100"} "LASTNAME"]
            [:th {:data-options "field:'phone',sortable:true,width:100"} "PHONE"]
            [:th {:data-options "field:'email',sortable:true,width:100"} "EMAIL"]
            [:th {:data-options "field:'comments',sortable:true,width:100"} "COMMENTS"]))
        (build-toolbar)
        (build-dialog title (dialog-fields))
        (build-dialog-buttons)))

        (defn contacts-scripts []
        (include-js "/js/grid.js"))
        ``` 

## CREATE A CRUD GRID FOR A TABLE - FROM PROJECT FOLDER COMMAND LINE USING THE LEIN ALIASES
1. Create migration files for new table under /resources/migrations.  Look at other migrations to get syntax
2. From project directory: execute lein migrate to create the new table on the database
3. From project directory: execute lein grid 'yourtablename' this will create the following:
- A folder /src/sk/handlers/admin/'yourtablename'
- A file /src/sk/handlers/admin/'yourtablename'/handler.clj
- A file /src/sk/handlers/admin/'yourtablename'/model.clj
- A file /src/sk/handlers/admin/'yourtablename'/view.clj
- Make sure to edit and modify files as needed.
- Make sure to recompile program with 'lein run' to update software with the new routes.

## CREATE A DASHBOARD FOR A TABLE - FROM PROJECT FOLDER COMMAND LINE USING THE LEIN ALIASES
1. From project directory: execute lein dashboard 'yourtablename' this will create the following:
- A folder /scr/sk/handlers/'yourtablename'
- A file /src/sk/handlers/'yourtablename'/handler.clj
- A file /src/sk/handlers/'yourtablename'/model.clj
- A file /src/sk/handlers/'yourtablename'/view.clj
- Make sure to edit and modify files as needed.
- Make suer to recompile program with 'lein run' to update software with the new routes.

## REBUILD PRIVATE ROUTES IF MANUALLY CREATED - FROM PROJECT FOLDER COMMAND LINE USING THE LEIN ALIASES
1. lein private
- Will update system to accomodate changes for menus/private routes etc...

## REBUILD OPEN ROUTES IF MANUALLY CREATED - FROM PROJECT FOLDER COMMAND LINE USING THE LEIN ALIASES
1. lein open
- Will update system to accomodate changes for menus/routes etc...

## TIPS
1. Look at /resources/templates for examples of different input types ex. combobox, date, email, image etc...
2. Look at /src/sk/handlers/tref/handler.clj for examples of lookups for combobox fields, or other misc routes.
3. Look at /src/sk/models/crud.clj for functions to generate crud sql stmts.
- Query ex: (note on all of the crud examples 'db' is the database connection from /src/sk/models/crud.clj
 ``` 
(Query db "select * from users") 
``` 
- Query! ex: 
``` 
(let [id 1] 
  (Query! db ["delect from users where id = ?" id ])) 
```
- Insert ex: 
``` 
(let [row {:username "Gido" :password "somepwd"}
      table "users"] 
   (Insert db table row)) 
```
- Insert multi ex:
```
(def data
    [
        {:username "Gido"
         :password "gpass"}
        {:username "Mary"
         :password "mpass"}
    ])
(let [table "users"
      rows data]
  (Insert-multi db table rows))
```
- Update ex:
```
(let [table "users"
      row {:username "Gido"
           :password "gpass"}
      where-clause "id = 5"]
  (Update db table row where-clause))
```
- Save ex:  (Note this will create a new item if it does not find and existing item and update an existing item if it exists)
```
(let [table "users"
      id nil
      row {:username "gido"
           :password "gmpass"}
     where-clause (str "id = ?" id)]
  (Save db table row where-clause))
```

## License

Copyright © 2019 LS

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
