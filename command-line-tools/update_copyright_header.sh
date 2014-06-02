#!/bin/bash

# include all headers with a single year (4 numbers, e.g. 2010) or with a range of years (4 numbers - 4numbers,
# e.g. 2010-2011) and replace it with a range of years $START-$END.
START=2010
END=2015
TEMPLATE="s/Copyright [0-9]\{4\}\(-[0-9]\{4\}\)\?,/Copyright ${START}-${END},/g"

find . -type f -name '*.java'  -exec sed -i "${TEMPLATE}" {} \;
find . -type f -name '*.xhtml' -exec sed -i "${TEMPLATE}" {} \;
find . -type f -name '*.xml'   -exec sed -i "${TEMPLATE}" {} \;