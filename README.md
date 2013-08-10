App Engine Java Guestbook
Copyright (C) 2010-2012 Google Inc.

## Sample guestbook for use with App Engine Java.

Requires [Apache Maven](http://maven.apache.org) 3.0 or greater, and JDK 6+ in order to run.

To build, run

    mvn package

Building will run the tests, but to explicitly run tests you can use the test target

    mvn test

To start the app, use the [App Engine Maven Plugin](http://code.google.com/p/appengine-maven-plugin/) that is already included in this demo.  Just run the command.

cd Documents\GitHub\amudtest
mvn appengine:devserver

For further information, consult the [Java App Engine](https://developers.google.com/appengine/docs/java/overview) documentation.

To see all the available goals for the App Engine plugin, run

    mvn help:describe -Dplugin=appengine
    
http://localhost:8080/GetLocationsByPos?lat=32.0764&lon=34.8562
http://amudtest.appspot.com/GetLocationsByPos?lat=32.0764&lon=34.8562
http://amudtest.appspot.com/GetLocationsByText?txt=ברט

amudtest 

mvn appengine:update

copy /Y C:\Users\Leor\Documents\GitHub\amudtest\src\main\webapp\mapApp.html C:\Users\Leor\Documents\GitHub\amudtest\target\amudtest-1.0-SNAPSHOT

1. When opening the app, show current location in the middle. Show all POIs in the area. Use the terrain map as default. When clicking/touching a TOI, show it's details. Show 7km radius around current location.
2. Allow changing base map: Terrain / 50K / Road / the nice OpenMap one
3. Show details for the closest TOI in a 30% frame. Should scroll if too big. Should show the distance. When touching / clicking, show the details for the TOI closest to the click.


- Fix server side timout using the low-level API

 