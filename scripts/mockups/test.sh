#!/bin/bash

for n in $(seq 30); do
	export BUILD_NUMBER=$n
	"./mock-qt.sh"

	cat output/result.json
done
