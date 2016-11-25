Mock-up for a Quality Testing application
=========================================

The Quality Testing application runs an application, subjecting it to various
inputs. Its result is one or more quality metrics. For example, it measures
the application's throughput or a request's latency.

This folder contains a mock-up for that application. It is a simple Bash
script which, based on the build number, outputs something kind of real
looking for the metrics.

 * `mock-qt.sh` is the script, which mock-runs a quality test and saves
   some quality metrics in the `output/` directory. It expects to be run
   from Jenkins, because it uses `$BUILD_NUMBER` to vary the outputs.
 * `test.sh` just runs the `mock-qt.sh` in command line for testing
   purposes.

The result of `mock-qt.sh` is a JSON file in the `outputs/` subdir, e.g.:

```json
{ 'build_number': 30, 'latency': 3, 'throughput': 2 }
```