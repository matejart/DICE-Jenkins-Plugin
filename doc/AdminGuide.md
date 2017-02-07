# DICE Continuous Integration installation and administration guide

## Compiling DICE Jenkins plug-in

First, obtain the source code of the DICE Jenkins plug-in:

    $ git clone https://github.com/dice-project/DICE-Jenkins-Plugin.git
    $ cd DICE-Jenkins-Plugin
    $ mvn install

This will produce the `target/dice-qt.jar` file, which is the plug-in.

## Installing the plug-in

Log in into your Jenkins web interface as an administrator user. Then follow
the steps to install the plug-in:

* Click **Manage Jenkins**.
* Click **Manage Plugins**.
* Switch to **Advanced** tab.
* Use the fields in the **Upload Plugin** section to upload the `dice-qt.jar`
  file.
