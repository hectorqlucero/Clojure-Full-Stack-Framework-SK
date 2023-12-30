# SK Full Stack web page generation

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

## CREATE A CRUD GRID FOR A TABLE
- Create migration files for new table under /resources/migrations.  Look at other migrations to get syntax
- From project directory: execute lein migrate to create the new table on the database
- From project directory: execute lein grid 'yourtablename' this will create the following:
1. A folder /src/sk/handlers/'yourtablename'
2. A file /src/sk/handlers/'yourtablename'/handler.clj
3. A file /src/sk/handlers/'yourtablename'/model.clj
4. A file /src/sk/handlers/'yourtablename'/view.clj
5. Make sure to edit and modify files as needed.
6. Make sure to recompile program with 'lein run' to update software with the new routes.

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
