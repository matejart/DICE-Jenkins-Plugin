#!/bin/bash

if [ "$BUILD_NUMBER" == "" ]
then
	echo "Please run me from a Jenkins job. Or set a BUILD_NUMBER env var."
	exit -1
fi

# build artifact directory
OUTPUT_DIR=output/
OUTPUT="$OUTPUT_DIR/result.json"

mkdir -p $OUTPUT_DIR

# base for the mock-computed performance metrics
LATENCY_BASE=(3 3 2 2 1.5 8 4 2.3 1.5 1.5)
THROUGHPUT_BASE=(2 2.3 3 3 4 0.2 2 3 5 5)

# pretend to do work for some varying time
SLEEP_TIME=$(( 3 + $RANDOM / 1000 ))
sleep $SLEEP_TIME

# produce the mock performance metric values
NUMBER=$(( $BUILD_NUMBER % 10 ))

LATENCY=$( bc <<< "base = 2; ${LATENCY_BASE[$NUMBER]} + 2*(16383 - $RANDOM)/16383" )
THROUGHPUT=$( bc <<< "base = 2; ${THROUGHPUT_BASE[$NUMBER]} + (16383 - $RANDOM)/16383" )

echo -n "{ 'build_number': $BUILD_NUMBER, " > $OUTPUT
echo -n "'latency': ${LATENCY}, " >> $OUTPUT
echo "'throughput': ${THROUGHPUT} }" >> $OUTPUT