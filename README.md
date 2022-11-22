This directory contains a skeleton for CS61B Project 1.

CONTENTS:

ReadMe                  This file.
        
Makefile                A makefile (for the 'make' program) that will compile
                        your files and run tests.  You must turn in a Makefile,
                        'make' must compile all your files, and 
                        'make check' must perform all your tests.  Currently,
                        this makefile is set up to do just that with our
                        skeleton files.  Be sure to keep it up to date.

staff-version           If we update the skeleton, this file will contain a
                        unique version id indicating what version of the
                        skeleton is currently in use.


db61b                   A subdirectory containing skeletons for the 
                        db61b package:

  Main.java             The main program---entry point to the db61b system.
  Database.java         Abstraction for an entire collection of tables.  
  Table.java            Abstraction for one table.
  Row.java              Abstraction for one row of a table.

testing                 Subdirectory holding files for integration testing:

  Makefile              A makefile containing instructions for performing
                        tests on your project.

  students.db, enrolled.db, courses.db
                        Sample database tables from the project handout.

  test1.in, test2.in    Input files for testing.  The makefile will respond
                        to 'make check' by running these files through your
                        program, filtering the output through 
                        testing/test-filter, and comparing the results with 
                        the corresponding .out files.  You should add more 
                        files to the list in Makefile.
                        REMINDER: These are samples only.  They DON'T 
                        constitute adequate tests.

  test1.out, test2.out  Output that is supposed to result from test1.in
                        and test2.in, with the first line, all prompts,
                        and all blank lines removed (which is what 
                        test-filter does).

  testing.py            A Python 3 module containing a framework for integration
                        testing.   Used by tester.py.

  tester.py             A Python 3 program that tests your project.  It runs
                        your program with each .in file, comparing the output
                        with the corresponding .out file and producing a report
                        of the result.


