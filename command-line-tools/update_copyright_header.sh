#!/bin/bash

find . -type f -name '*.java'  -exec sed -i 's/Copyright 2010-2013,/Copyright 2010-2014,/g' {} \;
find . -type f -name '*.xhtml' -exec sed -i 's/Copyright 2010-2013,/Copyright 2010-2014,/g' {} \;
find . -type f -name '*.xml'   -exec sed -i 's/Copyright 2010-2013,/Copyright 2010-2014,/g' {} \;
