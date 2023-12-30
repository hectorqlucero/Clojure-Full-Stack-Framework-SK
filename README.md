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

## Usage
1. Create a database "xxxxxxxxxx" in mySQL or MariaDB.  Note: "xxxxxxxxxx" refers to what you will call your database, so replace with your database name created.
2. Clone the repository
3. Copy resources/private/config_example to config.clj and change all of the "xxxxxxxxxx" to what applies to you setup
  Example of resources/private/config_example.clj
  ```
  {:db-protocol    "mysql"
  :db-name        "//localhost:3306/xxxxxxxxxxx?characterEncoding=UTF-8"
  :db-user        "root"
  :db-pwd         "xxxxxxxxxx"
  :db-class       "com.mysql.cj.jdbc.Driver"
  :email-host     "smtp.gmail.com"
  :email-user     "xxxxxxxxxxx@gmail.com"
  :email-password "xxxxxxxxxx"
  :port           3000
  :tz             "US/Pacific"
  :site-name      "xxxxxxxxxx"
  :base-url       "http://0.0.0.0:3000/"
  :uploads        "./uploads"
  :path           "/uploads/"}
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
- Will update system to accomodate changes for msnu/routes etc...

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
