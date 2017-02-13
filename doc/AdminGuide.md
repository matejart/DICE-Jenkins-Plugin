# DICE Continuous Integration installation and administration guide

## Obtaining the plug-in

The plug-in is available for download at the [GitHub releases][GitHub releases]
page as a `dice-qt-0.2.x.hpi` file.

[GitHub releases]: https://github.com/dice-project/DICE-Jenkins-Plugin/releases

## Compiling DICE Jenkins plug-in

First, obtain the source code of the DICE Jenkins plug-in:

    $ git clone https://github.com/dice-project/DICE-Jenkins-Plugin.git
    $ cd DICE-Jenkins-Plugin

Then use Maven 3 to compile the plug-in:

    $ mvn install

This will produce the `target/dice-qt.hpi` file, which is the plug-in.

## Installing the plug-in

Log in into your Jenkins web interface as an administrator user. Then follow
the steps to install the plug-in:

* Click **Manage Jenkins**.
* Click **Manage Plugins**.
* Switch to **Advanced** tab.
* Use the fields in the **Upload Plugin** section to upload the `.hpi`
  file (i.e., a `dice-qt-0.2.x.hpi` if downloaded from the GitHub releases
  page, or `dice-qt.hpi` if compiled).
