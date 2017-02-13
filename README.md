# DICE Continuous Integration

## Introduction

The Continuous Integration service provides regular, scheduled or event-based running of tools that are important in the DIA development, testing and deployment lifecycle. In the development process, performance improvements or regressions show as a speed-up or slow-down, measured in performance metrics. Having a way of visualizing performance changes helps assess validity of changes in DIA development.

The DICE Jenkins plug-in adds a project post-build step, which stores the build's performance output that is created as a JSON file by each build. The JSON file may contain any performance metric such as a duration of a batch operation, average or maximum operation latency, etc.

![DICE Jenkins Plug-in](doc/images/DICEProjectStatus.png)

The work is a part of the [DICE] project results. In February 2017, we had the
tools' intermediate release. We accompanied it with the D5.2 DICE delivery
tools - intermediate version project deliverable, which provides the motivation
for the tools and further details.

## Documentation

* [Prerequisites](doc/Prerequisites.md) provides prerequisites and requirements
  for installing and using the DICE Jenkins plug-in.
* [Installation and Administrator guide](doc/AdminGuide.md) provides
  instructions on installation of the DICE Jenkins plug-in.
* [User guide](doc/UserGuide.md) contains instructions for the Jenkins plug-in
  end users (Jenkins project administrators, developers, etc.).
* [Notes for developers](doc/DevelopingJenkinsPlugins.md) provide instructions
  and notes about development of Jenkins plug-ins service.

## Acknowledgment

This project has received funding from the European Union’s
[Horizon 2020] research and
innovation programme under grant agreement No. 644869.

![European Union](doc/images/EUFlag.png)

[DICE]: http://www.dice-h2020.eu/
[Horizon 2020]: http://ec.europa.eu/programmes/horizon2020/
[Prerequisites-wiki]: https://github.com/dice-project/DICE-Jenkins-Plugin/wiki/Prerequisites
[Installation-wiki]: https://github.com/dice-project/DICE-Jenkins-Plugin/wiki/Installation
[Getting-Started-wiki]: https://github.com/dice-project/DICE-Jenkins-Plugin/wiki/Getting-Started
[Links-and-References-wiki]: https://github.com/dice-project/DICE-Jenkins-Plugin/wiki/Links-and-References
[Changelog-wiki]: https://github.com/dice-project/DICE-Jenkins-Plugin/wiki/Changelog
